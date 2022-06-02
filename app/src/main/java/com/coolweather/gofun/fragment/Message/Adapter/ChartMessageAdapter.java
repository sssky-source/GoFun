package com.coolweather.gofun.fragment.Message.Adapter;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Message.bean.ChartMessageBean;

import java.util.List;

public class ChartMessageAdapter extends BaseMultiItemQuickAdapter<ChartMessageBean, BaseViewHolder> {


    public ChartMessageAdapter(@Nullable List<ChartMessageBean> data) {
        super(data);
        addItemType(1, R.layout.chart_item_one);
        addItemType(2,R.layout.chart_item_two);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ChartMessageBean chartMessageBean) {
        switch (chartMessageBean.getItemType()){
            case 1:
                baseViewHolder.setText(R.id.chart_Name1,chartMessageBean.getUsername())
                        .setText(R.id.chart_Message1,chartMessageBean.getMessage());
                Log.d("000" , ":" + chartMessageBean.getItemType());
                ImageView imageView = baseViewHolder.getView(R.id.chart_Image1);
                Glide.with(getContext()).load(chartMessageBean.getImage()).into(imageView);
                break;
            case 2:
                baseViewHolder.setText(R.id.chart_Name,chartMessageBean.getUsername())
                        .setText(R.id.chart_Message,chartMessageBean.getMessage());
                Log.d("000" , ":" + chartMessageBean.getItemType());
                ImageView imageView1 = baseViewHolder.getView(R.id.chart_Image);
                Glide.with(getContext()).load(chartMessageBean.getImage()).into(imageView1);
        }
    }
}
