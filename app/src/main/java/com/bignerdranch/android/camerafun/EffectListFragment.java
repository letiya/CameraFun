package com.bignerdranch.android.camerafun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EffectListFragment extends Fragment {

    private RecyclerView mEffectRecyclerView;
    private EffectAdapter mAdapter;

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
}
