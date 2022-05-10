package com.coolweather.gofun.fragment.Map.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Map.bean.TypeItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TypeAdapter extends BaseQuickAdapter<TypeItem, BaseViewHolder> {


    public TypeAdapter(int layoutResId, @Nullable List<TypeItem> data) {
        super(layoutResId, data);
        //添加子点击事件
        addChildClickViewIds(R.id.item_select);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TypeItem typeItem) {
        baseViewHolder.setImageResource(R.id.img_type,typeItem.getImage());
        baseViewHolder.setText(R.id.tv_type,typeItem.getType());
        Log.d("Select","11111111111111");
        Log.d("Select",typeItem.getType());

    }

}
