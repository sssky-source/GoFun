package com.coolweather.gofun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.RecommendService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagActivity extends AppCompatActivity {

    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        chipGroup = findViewById(R.id.chipGroup);

        RecommendService recommendService = HttpRequest.create(RecommendService.class);
        recommendService.getActivityType("Bearer " + GoFunApplication.getToken()).enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                List<Activity> list = response.body();
                for (Activity activity : list){
                    //动态创建标签
                    Chip chip = new Chip(TagActivity.this);
                    chip.setCheckable(true);
                    chip.setChecked(true);
                    chip.setChipIconVisible(true);
                    //chip.setCheckedIconVisible(true);
                    chip.setChipIcon(getResources().getDrawable(R.mipmap.dynamics));
                    chip.setChipBackgroundColorResource(R.color.chip);
                    chip.setText(activity.getType1());
                    chipGroup.addView(chip);
                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (chip.isChecked()){
                                chip.setChipBackgroundColorResource(R.color.chip_pressed);
                            }else{
                                chip.setChecked(false);
                                chip.setChipBackgroundColorResource(R.color.chip);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}