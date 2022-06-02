package com.coolweather.gofun.fragment.Message.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;

import java.util.List;

public class JoinActivityAdapter extends BaseQuickAdapter<PersonActivityItem, BaseViewHolder> {

    public JoinActivityAdapter(int layoutResId, @Nullable List<PersonActivityItem> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.message_CardView);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PersonActivityItem personActivityItem) {
        baseViewHolder.setText(R.id.message_ActivityType,"[" + personActivityItem.getType() + "] ")
                .setText(R.id.message_ActivityTitle,personActivityItem.getTitle());
    }
}
