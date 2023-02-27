package com.coolweather.gofun.fragment.Mine.tag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.Tag;
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

public class TabFragment extends Fragment {

    private ChipGroup chipGroup;

    //全部标签列表
    private List<Tag> tag;
    //用户个人标签列表
    private int userId;
    private int tagId;
    private List<UserTag> userTagList;

    public TabFragment(List<Tag> tag, int userId) {
        this.tag = tag;
        this.userId = userId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tag, container, false);
        chipGroup = view.findViewById(R.id.TagChangeGroup);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PersonService personService = HttpRequest.create(PersonService.class);
        //用户个人标签获取
        getUserTagRequest(personService, userId);

    }

    //用户个人标签获取
    private void getUserTagRequest(PersonService personService, int userId) {
        personService.getUserTag("Bearer " + GoFunApplication.token, userId).enqueue(new Callback<List<UserTag>>() {
            @Override
            public void onResponse(Call<List<UserTag>> call, Response<List<UserTag>> response) {
                userTagList = response.body();
                //全部标签生成
                for (Tag tagItem : tag) {
                    Chip chip = new Chip(getActivity());
                    chip.setCheckable(true);
                    //背景色
                    chip.setChipBackgroundColorResource(R.color.white);
                    //描边宽度和颜色
                    chip.setChipStrokeWidth(2);
                    chip.setChipStrokeColorResource(R.color.grey);
                    chip.setText(tagItem.getName());

                    for (UserTag userTag : userTagList) {
                        if (tagItem.getName().equals(userTag.getName())) {
                            chip.setChecked(true);
                        }
                    }
                    chipGroup.addView(chip);

                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {

                                int addTag[];
                                addTag = new int[]{tagItem.getId()};
                                personService.AddUserTag("Bearer " + GoFunApplication.token, addTag).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    }
                                });
                            }
                            if (!b) {

                                /**
                                 * 删除标签是从用户个人标签中删，需要其中的tagId
                                 * 则需要从全部标签中找到用户个人的标签
                                 */
                                for (UserTag userTag : userTagList) {
                                    if (chip.getText().equals(userTag.getName())) {
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

            @Override
            public void onFailure(Call<List<UserTag>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
