package com.coolweather.gofun.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.TagChange;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.net.RecommendService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        recyclerView = findViewById(R.id.tag_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TagActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        PersonService personService = HttpRequest.create(PersonService.class);
        personService.getAllTag("Bearer " + GoFunApplication.token).enqueue(new Callback<List<TagChange>>() {
            @Override
            public void onResponse(Call<List<TagChange>> call, Response<List<TagChange>> response) {
                List<TagChange> list = response.body();
                tagAdapter = new TagAdapter(R.layout.activity_tag_item,list);
                recyclerView.setAdapter(tagAdapter);
            }

            @Override
            public void onFailure(Call<List<TagChange>> call, Throwable t) {
                t.printStackTrace();
            }
        });
//        recommendService.getActivityType("Bearer " + GoFunApplication.getToken()).enqueue(new Callback<List<Activity>>() {
//            @Override
//            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
//                List<Activity> list = response.body();
//                for (Activity activity : list){
//                    //动态创建标签
//                    Chip chip = new Chip(TagActivity.this);
//                    chip.setCheckable(true);
//                    chip.setChecked(true);
//                    chip.setChipIconVisible(true);
//                    //chip.setCheckedIconVisible(true);
//                    chip.setChipIcon(getResources().getDrawable(R.mipmap.dynamics));
//                    chip.setChipBackgroundColorResource(R.color.chip);
//                    chip.setText(activity.getType1());
//                    chipGroup.addView(chip);
//                    chip.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (chip.isChecked()){
//                                chip.setChipBackgroundColorResource(R.color.chip_pressed);
//                            }else{
//                                chip.setChecked(false);
//                                chip.setChipBackgroundColorResource(R.color.chip);
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Activity>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }
}