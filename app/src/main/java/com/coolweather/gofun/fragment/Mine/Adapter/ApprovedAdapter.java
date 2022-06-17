package com.coolweather.gofun.fragment.Mine.Adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.GetApplyID;

import java.util.List;

public class ApprovedAdapter extends BaseQuickAdapter<GetApplyID, BaseViewHolder> {

    private int state;

    public ApprovedAdapter(int layoutResId, @Nullable List<GetApplyID> data, int state) {
        super(layoutResId, data);
        this.state = state;
        addChildClickViewIds(R.id.apply_agree); //同意申请
        addChildClickViewIds(R.id.apply_refuse); //拒绝申请
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, GetApplyID getApplyID) {
        baseViewHolder.setText(R.id.apply_userName, getApplyID.getUsername())
                .setText(R.id.apply_time, getApplyID.getApplytime().split("T")[0]
                        + " " + getApplyID.getApplytime().split("T")[1]);

        ImageView imageView = baseViewHolder.getView(R.id.apply_userImage);
        Glide.with(getContext()).load(getApplyID.getUserImage()).into(imageView);

        if (state != 1) {
            baseViewHolder.setVisible(R.id.apply_agree, false)
                    .setVisible(R.id.apply_refuse, false);
        }
    }
}
