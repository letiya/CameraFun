package com.bignerdranch.android.camerafun;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EffectLab {

    private static EffectLab sEffectLab;

    private List<Effect> mEffects;

    public static EffectLab get() {
        if (sEffectLab == null) {
            sEffectLab = new EffectLab();
        }
        return sEffectLab;
    }

    private EffectLab() {
        mEffects = new ArrayList<>();
        // Add default effects
        mEffects.add(new Effect(1, 5, 5, 6, 0));
        mEffects.add(new Effect(2, 5, 5, 0, 10));
        mEffects.add(new Effect(3, 5, 0, 10, 0));
        mEffects.add(new Effect(4, 15, 5, 0, 10));
        mEffects.add(new Effect(5, 5, 10, 0, 0));
    }

    public List<Effect> getEffects() {
        return mEffects;
    }

    public Effect getEffect(UUID id) {
        for (Effect effect : mEffects) {
            if (effect.getId().equals(id)) {
                return effect;
            }
        }
        return null;
    }

    public Effect getEffect(String id) {
        for (Effect effect : mEffects) {
            if (effect.getId().toString().equals(id)) {
                return effect;
            }
        }
        return null;
    }

    public Bitmap applyEffect(Bitmap src, Bitmap resizedSrc, Effect effect) {
        int newWidth = resizedSrc.getWidth();
        int newHeight = resizedSrc.getHeight();

        int scaleWidth = src.getWidth() / newWidth;
        int scaleHeight = src.getHeight() / newHeight;

        int depth = effect.getDepth();
        double red = effect.getRed();
        double green = effect.getGreen();
        double blue = effect.getBlue();

        Bitmap result = Bitmap.createBitmap(newWidth, newHeight, resizedSrc.getConfig());

        for (int i = 0; i < scaleWidth * newWidth; i += scaleWidth) {
            for (int j = 0; j < scaleHeight * newHeight; j += scaleHeight) {
                int pixel = src.getPixel(i, j);
                int pixelAlpha = Color.alpha(pixel);
                int pixelRed = Color.red(pixel);
                int pixelGreen = Color.green(pixel);
                int pixelBlue = Color.blue(pixel);

                pixelRed += depth * red;
                pixelRed = Math.min(pixelRed, 255);

                pixelGreen += depth * green;
                pixelGreen = Math.min(pixelGreen, 255);

                pixelBlue += depth * blue;
                pixelBlue = Math.min(pixelBlue, 255);

                result.setPixel(i / scaleWidth, j / scaleHeight, Color.argb(pixelAlpha, pixelRed, pixelGreen, pixelBlue));
            }
        }
        return result;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
        return resizedBitmap;
    }
}
