package com.coolweather.gofun.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.Tag;
import com.coolweather.gofun.fragment.Mine.bean.TagChange;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class TagAdapter extends BaseQuickAdapter<TagChange, BaseViewHolder> {

    public TagAdapter(int layoutResId, @Nullable List<TagChange> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TagChange tagChange) {
        baseViewHolder.setText(R.id.tag_type,tagChange.getName());
        ChipGroup chipGroup = baseViewHolder.getView(R.id.tag_chipGroup);
        List<Tag> list = tagChange.getTag();
        for (Tag tag : list){
            Chip chip = new Chip(getContext());
            chip.setCheckable(true);
            chip.setChipIconVisible(true);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setChipStrokeWidth(2);
            chip.setChipStrokeColorResource(R.color.grey);
            chip.setText(tag.getName());

            chipGroup.addView(chip);
        }
    }
}
