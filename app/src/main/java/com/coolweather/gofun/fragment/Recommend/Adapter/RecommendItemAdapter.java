package com.coolweather.gofun.fragment.Recommend.Adapter;

import android.annotation.SuppressLint;
import android.text.TextPaint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;

import java.util.List;

public class RecommendItemAdapter extends BaseQuickAdapter<ActivityItem, BaseViewHolder> {
    public RecommendItemAdapter(int layoutResId, @Nullable List<ActivityItem> data) {
        super(layoutResId, data);
        //点击事件添加
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ActivityItem activity) {
        TextView textView = baseViewHolder.getView(R.id.activity_number);
        baseViewHolder.setText(R.id.activity_type,"[" + activity.getType() + "] ");
        baseViewHolder.setText(R.id.activity_title,activity.getTitle());
        baseViewHolder.setText(R.id.activity_introduction, activity.getIntroduction());
        baseViewHolder.setText(R.id.activity_startTime,activity.getStarttime().split("T")[0]
                        + " " + activity.getStarttime().split("T")[1]);

        baseViewHolder.setText(R.id.activity_endTime, activity.getEndtime().split("T")[0]
                + " " + activity.getEndtime().split("T")[1]);

        baseViewHolder.setText(R.id.activity_username,activity.getUsername());
        baseViewHolder.setText(R.id.activity_number,activity.getCurnumber() + "/" + activity.getMaxnumber());
        if (activity.getCurnumber().equals(activity.getMaxnumber())){
            textView.setTextColor(R.color.red);
        }

        ImageView userPic = baseViewHolder.getView(R.id.activity_userImage);
        Glide.with(getContext()).load(activity.getImage()).into(userPic);
        Log.d("Select","Recommand");
    }
}
