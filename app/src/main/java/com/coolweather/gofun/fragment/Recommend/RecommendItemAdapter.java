package com.coolweather.gofun.fragment.Recommend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class RecommendItemAdapter extends BaseQuickAdapter<Activity, BaseViewHolder> {
    public RecommendItemAdapter(int layoutResId, @Nullable List<Activity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Activity activity) {

    }
}