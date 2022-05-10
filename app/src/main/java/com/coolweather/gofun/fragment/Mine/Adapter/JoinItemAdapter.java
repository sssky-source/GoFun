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
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;

import java.util.List;

public class JoinItemAdapter extends BaseQuickAdapter<ActivityItem, BaseViewHolder> {
    public JoinItemAdapter(int layoutResId, @Nullable List<ActivityItem> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ActivityItem activityItem) {
        TextView textView = baseViewHolder.getView(R.id.join_number);
        baseViewHolder.setText(R.id.join_type,"[" + activityItem.getType() + "] ");
        baseViewHolder.setText(R.id.join_title,activityItem.getTitle());
        baseViewHolder.setText(R.id.join_introduction, activityItem.getIntroduction());
        baseViewHolder.setText(R.id.join_startTime,activityItem.getStarttime().split("T")[0]
                + " " + activityItem.getStarttime().split("T")[1]);

        baseViewHolder.setText(R.id.join_endTime, activityItem.getEndtime().split("T")[0]
                + " " + activityItem.getEndtime().split("T")[1]);

        baseViewHolder.setText(R.id.join_username,activityItem.getUsername());
        baseViewHolder.setText(R.id.join_number,activityItem.getCurnumber() + "/" + activityItem.getMaxnumber());
        if (activityItem.getCurnumber().equals(activityItem.getMaxnumber())){
            textView.setTextColor(R.color.red);
        }

        ImageView userPic = baseViewHolder.getView(R.id.join_userImage);
        Glide.with(getContext()).load(activityItem.getImage()).into(userPic);
    }
}
