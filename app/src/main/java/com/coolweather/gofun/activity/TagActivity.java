package com.coolweather.gofun.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.TagChange;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.net.RecommendService;
import com.coolweather.gofun.util.ToastUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagActivity extends AppCompatActivity implements View.OnClickListener{

    private int userId;
    private RecyclerView recyclerView;
    private TagAdapter tagAdapter;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        recyclerView = findViewById(R.id.tag_recyclerview);
        enter = findViewById(R.id.tag_enter);
        enter.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TagActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        PersonService personService = HttpRequest.create(PersonService.class);
        personService.getAllTag("Bearer " + GoFunApplication.token).enqueue(new Callback<List<TagChange>>() {
            @Override
            public void onResponse(Call<List<TagChange>> call, Response<List<TagChange>> response) {
                List<TagChange> list = response.body();
                tagAdapter = new TagAdapter(R.layout.activity_tag_item,list,userId);
                recyclerView.setAdapter(tagAdapter);
            }

            @Override
            public void onFailure(Call<List<TagChange>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tag_enter:
                Intent intent = new Intent(TagActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}