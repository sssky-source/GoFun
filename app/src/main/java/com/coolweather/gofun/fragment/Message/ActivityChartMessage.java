package com.coolweather.gofun.fragment.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.SqliteUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.bean.PersonLitePal;
import com.coolweather.gofun.fragment.Message.Adapter.ChartMessageAdapter;
import com.coolweather.gofun.fragment.Message.bean.ChartMessageBean;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Recommend.RecommendActivityDetail;
import com.coolweather.gofun.net.ChartMessage;
import com.coolweather.gofun.net.HttpRequest;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChartMessage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChartMessageAdapter chartMessageAdapter;
    private TextView groupName;
    private int activityID;
    private PersonLitePal person_LitePal;
    private List<ChartMessageBean> list = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_message);

        ChartMessage chartMessage = HttpRequest.create(ChartMessage.class);

        person_LitePal = LitePal.findFirst(PersonLitePal.class);
        Log.d("000","LitePal id111:" + person_LitePal.getUserID());
        Log.d("000","LitePal id111:" + person_LitePal.getUsername());
        Log.d("000","LitePal id111:" + person_LitePal.getImage());
        Intent intent = getIntent();
        String name = intent.getStringExtra("groupName");
        int number = intent.getIntExtra("number",0);
        activityID = intent.getIntExtra("id",0);
        recyclerView = findViewById(R.id.chart_RecyclerView);
        groupName = findViewById(R.id.chart_GroupName);
        groupName.setText(name + "(" + number + ")");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestChartMessage(chartMessage);
    }

    private void requestChartMessage(ChartMessage chartMessage) {
        chartMessage.getChartMessage("Bearer " + GoFunApplication.token,activityID).enqueue(new Callback<List<ChartMessageBean>>() {
            @Override
            public void onResponse(Call<List<ChartMessageBean>> call, Response<List<ChartMessageBean>> response) {
                List<ChartMessageBean> chartMessageBeans = response.body();
                for (ChartMessageBean bean : chartMessageBeans){
                    Log.d("000","id:" + bean.getUserid());
                    Log.d("000","LitePal id:" + person_LitePal.getUserID());
                    if (person_LitePal.getUserID() == bean.getUserid()){
                        list.add(new ChartMessageBean(2,bean.getMessage(),bean.getUsername(),bean.getImage()));

                    }else {
                        list.add(new ChartMessageBean(1,bean.getMessage(),bean.getUsername(),bean.getImage()));
                        Log.d("000" , "66666666666" );
                    }
                }

                chartMessageAdapter = new ChartMessageAdapter(list);
                recyclerView.setAdapter(chartMessageAdapter);
            }

            @Override
            public void onFailure(Call<List<ChartMessageBean>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}