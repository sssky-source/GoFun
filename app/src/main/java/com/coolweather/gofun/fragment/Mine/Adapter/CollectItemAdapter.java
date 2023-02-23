package com.coolweather.gofun.fragment.Mine.Adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.CollectBean;

import java.util.List;

public class CollectItemAdapter extends BaseQuickAdapter<CollectBean, BaseViewHolder> {
    public CollectItemAdapter(int layoutResId, @Nullable List<CollectBean> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CollectBean collectBean) {
        TextView textView = baseViewHolder.getView(R.id.collect_number);

        baseViewHolder.setText(R.id.collect_type,"[" + collectBean.getType() + "] ")
                .setText(R.id.collect_title,collectBean.getTitle())
                .setText(R.id.collect_introduction,collectBean.getIntroduction())
                .setText(R.id.collect_startTime,collectBean.getStarttime().split("T")[0]
                        + " " + collectBean.getStarttime().split("T")[1])
                .setText(R.id.collect_endTime,collectBean.getEndtime().split("T")[0]
                        + " " + collectBean.getEndtime().split("T")[1])
                .setText(R.id.collect_createUserName,collectBean.getCreateUserName())
                .setText(R.id.collect_number,collectBean.getCurnumber() + "/" + collectBean.getMaxnumber());
        if (collectBean.getCurnumber().equals(collectBean.getMaxnumber())){
            textView.setTextColor(R.color.red);
        }

        ImageView userPic = baseViewHolder.getView(R.id.collect_userImage);
        Glide.with(getContext()).load(collectBean.getImage()).into(userPic);
    }
}
