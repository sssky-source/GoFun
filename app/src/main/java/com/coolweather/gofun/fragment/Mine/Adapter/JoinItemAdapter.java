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
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;


import java.util.List;

public class JoinItemAdapter extends BaseQuickAdapter<PersonActivityItem, BaseViewHolder> {
    public JoinItemAdapter(int layoutResId, @Nullable List<PersonActivityItem> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PersonActivityItem personActivityItem) {
        TextView textView = baseViewHolder.getView(R.id.join_number);
        baseViewHolder.setText(R.id.join_type,"[" + personActivityItem.getType() + "] ");
        baseViewHolder.setText(R.id.join_title,personActivityItem.getTitle());
        baseViewHolder.setText(R.id.join_introduction, personActivityItem.getIntroduction());
        baseViewHolder.setText(R.id.join_startTime,personActivityItem.getStarttime().split("T")[0]
                + " " + personActivityItem.getStarttime().split("T")[1]);

        baseViewHolder.setText(R.id.join_endTime, personActivityItem.getEndtime().split("T")[0]
                + " " + personActivityItem.getEndtime().split("T")[1]);

        baseViewHolder.setText(R.id.join_username,personActivityItem.getUsername());
        baseViewHolder.setText(R.id.join_number,personActivityItem.getCurnumber() + "/" + personActivityItem.getMaxnumber());
        if (personActivityItem.getCurnumber().equals(personActivityItem.getMaxnumber())){
            textView.setTextColor(R.color.red);
        }

        ImageView userPic = baseViewHolder.getView(R.id.join_userImage);
        Glide.with(getContext()).load(personActivityItem.getUserImage()).into(userPic);
    }
}
