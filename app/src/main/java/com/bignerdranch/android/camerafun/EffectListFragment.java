package com.bignerdranch.android.camerafun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EffectListFragment extends Fragment {

    private static final String EXTRA_EFFECT_NUM = "com.bignerdranch.android.camerafun.effect_num";

    private RecyclerView mEffectRecyclerView;
    private EffectAdapter mAdapter;

    public static EffectListFragment newInstance() {
        EffectListFragment fragment = new EffectListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_effect_list, container, false);

        mEffectRecyclerView = view.findViewById(R.id.effect_recycler_view);
        mEffectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        EffectLab effectLab = EffectLab.get();
        List<Effect> effects = effectLab.getEffects();

        mAdapter = new EffectAdapter(effects);
        mEffectRecyclerView.setAdapter(mAdapter);
    }

    private class EffectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private Effect mEffect;

        public EffectHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_effect, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.effect_title);
        }

        public void bind(Effect effect) {
            mEffect = effect;
            mTitleTextView.setText("Effect " + mEffect.getNumber());
        }

        @Override
        public void onClick(View v) {
            // Apply effect to the photo
            returnResult(mEffect.getId());
            // Go back to previous Activity
            getActivity().finish();
        }
    }

    private class EffectAdapter extends RecyclerView.Adapter<EffectHolder> {

        private List<Effect> mEffects;

        public EffectAdapter(List<Effect> effects) {
            mEffects = effects;
        }

        @NonNull
        @Override
        public EffectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EffectHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EffectHolder holder, int position) {
            Effect effect = mEffects.get(position);
            holder.bind(effect);
        }

        @Override
        public int getItemCount() {
            return mEffects.size();
        }
    }

    private void returnResult(UUID id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EFFECT_NUM, id.toString());
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    public static String getEffectUUID(Intent result) {
        return result.getStringExtra(EXTRA_EFFECT_NUM);
    }
}
