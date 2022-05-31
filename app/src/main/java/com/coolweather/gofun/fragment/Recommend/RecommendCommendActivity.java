package com.coolweather.gofun.fragment.Recommend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Recommend.Adapter.CommendAdapter;
import com.coolweather.gofun.fragment.Recommend.bean.PersonComment;

import java.util.List;

public class RecommendCommendActivity extends AppCompatActivity {

    private List<PersonComment> list;
    private RecyclerView recyclerView;
    private CommendAdapter commendAdapter;
    private TextView commendNumber;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_commend);

        //接受评论list
        Intent intent = getIntent();
        list = (List<PersonComment>) intent.getSerializableExtra("commendList");

        recyclerView = findViewById(R.id.commend_history);
        commendNumber = findViewById(R.id.commend_number);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecommendCommendActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commendAdapter = new CommendAdapter(R.layout.activity_commend_item,list);
        recyclerView.setAdapter(commendAdapter);
        commendNumber.setText(getResources().getString(R.string.commend) + "(" + list.size() + ")");
    }
}