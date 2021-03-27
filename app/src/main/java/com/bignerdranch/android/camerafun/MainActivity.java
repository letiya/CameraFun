package com.bignerdranch.android.camerafun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String PHOTO_PATH = "photo_path";

    private static final int REQUEST_PHOTO = 0;

    private Button mSelectButton;
    private ImageView mPhotoView;
    private ImageView mEffect1View;
    private ImageView mEffect2View;
    private ImageView mEffect3View;
    private ImageView mEffect4View;
    private ImageView mEffect5View;
    private ImageView[] mEffectViews;

    private String mPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectButton = findViewById(R.id.photo_select_btn);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PHOTO);
            }
        });

        mPhotoView = findViewById(R.id.photo_view);
        if (savedInstanceState != null) {
            mPhotoPath = savedInstanceState.getString(PHOTO_PATH);
            updatePhotoView(mPhotoPath);
        }

        mEffect1View = findViewById(R.id.effect1_view);
        mEffect2View = findViewById(R.id.effect2_view);
        mEffect3View = findViewById(R.id.effect3_view);
        mEffect4View = findViewById(R.id.effect4_view);
        mEffect5View = findViewById(R.id.effect5_view);

        mEffectViews = new ImageView[] {mEffect1View, mEffect2View, mEffect3View, mEffect4View, mEffect5View};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHOTO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    // Method 1)
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    // Method 2)
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("image/*");

                    startActivityForResult(intent, REQUEST_PHOTO);
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast toast = Toast.makeText(this, R.string.permission_warning, Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO && data != null) {
            Uri photoSelectUri = data.getData();
            // Method 1)
            // Specify which fields you want your query to return values for
            String[] queryFields = new String[]{MediaStore.Images.Media.DATA};
            // Perform your query - the contactUri is like a "where" clause here
            Cursor c = getContentResolver().query(photoSelectUri, queryFields, null, null, null);
            try {
                // Double check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }
                // Pull out the first column of the first row of data - that is your suspect's name
                c.moveToFirst();
                mPhotoPath = c.getString(0);
                updatePhotoView(mPhotoPath);
            } finally {
                c.close();
            }
//            // Method 2)
//            mPhotoView.setImageURI(photoSelectUri);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(PHOTO_PATH, mPhotoPath);
    }

    private void updatePhotoView(String path) {
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (mPhotoView == null || !file.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mPhotoView.setImageBitmap(bitmap);
            mEffect1View.setImageBitmap(bitmap);
            mEffect2View.setImageBitmap(bitmap);
            mEffect3View.setImageBitmap(bitmap);
            mEffect4View.setImageBitmap(bitmap);
            mEffect5View.setImageBitmap(bitmap);
            updateEffectPreviews();
        }
    }

    private class applyEffectTask extends AsyncTask<Object, Void, Bitmap> {
        private ImageView imageView;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String photoPath = (String) params[0];
            Effect effect = (Effect) params[1];
            EffectLab effectLab = (EffectLab) params[2];
            imageView = (ImageView) params[3];
            Log.i(TAG, "Applying effect " + effect.getNumber());
            return effectLab.applyEffect(photoPath, effect);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private void updateEffectPreviews() {
        // Update effects to effect imageviews
        EffectLab effectLab = EffectLab.get();
        List<Effect> effects = effectLab.getEffects();
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            ImageView imageView = mEffectViews[i];
            new applyEffectTask().execute(mPhotoPath, effect, effectLab, imageView);
            imageView.setImageResource(R.drawable.msg);
        }
    }
}