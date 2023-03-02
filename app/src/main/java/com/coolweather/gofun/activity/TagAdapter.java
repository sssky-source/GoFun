package com.coolweather.gofun.activity;

import android.util.Log;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.Tag;
import com.coolweather.gofun.fragment.Mine.bean.TagChange;
import com.coolweather.gofun.fragment.Mine.bean.UserTag;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.ToastUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagAdapter extends BaseQuickAdapter<TagChange, BaseViewHolder> {

    private int userId;
    private List<UserTag> userTagList;
    private int tagId;

    public TagAdapter(int layoutResId, @Nullable List<TagChange> data,int userId) {
        super(layoutResId, data);
        this.userId = userId;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TagChange tagChange) {
        PersonService personService = HttpRequest.create(PersonService.class);

        personService.getUserTag("Bearer " + GoFunApplication.token,userId).enqueue(new Callback<List<UserTag>>() {
            @Override
            public void onResponse(Call<List<UserTag>> call, Response<List<UserTag>> response) {
                userTagList = response.body();
            }

            @Override
            public void onFailure(Call<List<UserTag>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        
        baseViewHolder.setText(R.id.tag_type,tagChange.getName());
        ChipGroup chipGroup = baseViewHolder.getView(R.id.tag_chipGroup);
        List<Tag> list = tagChange.getTag();
        for (Tag tag : list){
            Chip chip = new Chip(getContext());
            chip.setCheckable(true);
            chip.setChipIconVisible(true);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setChipStrokeWidth(2);
            chip.setChipStrokeColorResource(R.color.grey);
            chip.setText(tag.getName());

            chipGroup.addView(chip);

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        
                        //添加标签 传ID数组
                        int addTag[];
                        addTag = new int[]{tag.getId()};
                        personService.AddUserTag("Bearer " + GoFunApplication.token,addTag).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.d("register_tag","code:" + response.code());
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("register_tag","cause:" + t.getCause());
                            }
                        });
                    }
                    if (!b){
                        /**
                         * 删除标签是从用户个人标签中删，需要其中的tagId
                         * 则需要从全部标签中找到用户个人的标签
                         */
                        
                        for (UserTag userTag : userTagList){
                            if (chip.getText().equals(userTag.getName())){
                                tagId = userTag.getTagId();
                            }
                        }


                        personService.delUserTag("Bearer " + GoFunApplication.token, tagId).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        }
    }
}
