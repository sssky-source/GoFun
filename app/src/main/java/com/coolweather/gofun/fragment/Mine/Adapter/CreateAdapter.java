package com.coolweather.gofun.fragment.Mine.Adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.CreateBean;

import java.util.List;

public class CreateAdapter extends BaseQuickAdapter<CreateBean, BaseViewHolder> {
    public CreateAdapter(int layoutResId, @Nullable List<CreateBean> data) {
        super(layoutResId, data);

        addChildClickViewIds(R.id.create_applyMessage);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CreateBean createBean) {
        baseViewHolder.setText(R.id.create_type,"[" + createBean.getType() + "] ")
                .setText(R.id.create_title,createBean.getTitle())
                .setText(R.id.create_introduction,createBean.getIntroduction())
                .setText(R.id.create_startTime,createBean.getStarttime().split("T")[0]
                        + " " + createBean.getStarttime().split("T")[1])
                .setText(R.id.create_endTime,createBean.getEndtime().split("T")[0]
                        + " " + createBean.getEndtime().split("T")[1]);

        baseViewHolder.setText(R.id.create_username,createBean.getUsername())
                .setText(R.id.create_number,createBean.getCurnumber() + "/" + createBean.getMaxnumber());

        ImageView imageView = baseViewHolder.getView(R.id.create_userImage);
        Glide.with(getContext()).load(createBean.getImage()).into(imageView);
    }
}
