package com.coolweather.gofun.fragment.Recommend.Adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Recommend.bean.PersonComment;

import java.util.List;

public class CommendAdapter extends BaseQuickAdapter<PersonComment, BaseViewHolder> {

    public CommendAdapter(int layoutResId, @Nullable List<PersonComment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PersonComment personComment) {
        baseViewHolder.setText(R.id.commentItemUserName,personComment.getUsername())
                .setText(R.id.commentItemTime,personComment.getCreatetime().split("T")[0])
                .setText(R.id.commentItemContent,personComment.getComment());

        ImageView userImage =baseViewHolder.getView(R.id.commentItemUserImage);
        Glide.with(getContext()).load(personComment.getImage()).into(userImage);
    }
}
