package com.coolweather.gofun.fragment.Mine.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;

import java.util.List;

public class StateItemAdapter extends BaseQuickAdapter<PersonActivityItem, BaseViewHolder> {
    public StateItemAdapter(int layoutResId, @Nullable List<PersonActivityItem> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PersonActivityItem activityItem) {
        TextView textView = baseViewHolder.getView(R.id.state_number);
        baseViewHolder.setText(R.id.state_type,"[" + activityItem.getType() + "] ");
        baseViewHolder.setText(R.id.state_title,activityItem.getTitle());
        baseViewHolder.setText(R.id.state_introduction, activityItem.getIntroduction());
        baseViewHolder.setText(R.id.state_startTime,activityItem.getStarttime().split("T")[0]
                + " " + activityItem.getStarttime().split("T")[1]);

        baseViewHolder.setText(R.id.state_endTime, activityItem.getEndtime().split("T")[0]
                + " " + activityItem.getEndtime().split("T")[1]);

        baseViewHolder.setText(R.id.state_username,activityItem.getUsername());
        baseViewHolder.setText(R.id.state_number,activityItem.getCurnumber() + "/" + activityItem.getMaxnumber());
        if (activityItem.getCurnumber().equals(activityItem.getMaxnumber())){
            textView.setTextColor(R.color.red);
        }

        ImageView userPic = baseViewHolder.getView(R.id.state_userImage);
        Glide.with(getContext()).load(activityItem.getUserImage()).into(userPic);
    }
}
