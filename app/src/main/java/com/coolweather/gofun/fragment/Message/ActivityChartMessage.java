package com.coolweather.gofun.fragment.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.SqliteUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.bean.PersonLitePal;
import com.coolweather.gofun.fragment.Message.Adapter.ChartMessageAdapter;
import com.coolweather.gofun.fragment.Message.bean.AddChartMessage;
import com.coolweather.gofun.fragment.Message.bean.ChartMessageBean;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Recommend.RecommendActivityDetail;
import com.coolweather.gofun.net.ChartMessage;
import com.coolweather.gofun.net.HttpRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChartMessage extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ChartMessageAdapter chartMessageAdapter;
    private TextView groupName;
    private int activityID;
    private PersonLitePal person_LitePal;
    private List<ChartMessageBean> list = new ArrayList<>();
    private ChartMessage chartMessage;
    private TextInputEditText editText;
    private Button sendButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_message);

        chartMessage = HttpRequest.create(ChartMessage.class);

        person_LitePal = LitePal.findFirst(PersonLitePal.class);
        Log.d("000", "LitePal id111:" + person_LitePal.getUserID());
        Log.d("000", "LitePal id111:" + person_LitePal.getUsername());
        Log.d("000", "LitePal id111:" + person_LitePal.getImage());
        Intent intent = getIntent();
        String name = intent.getStringExtra("groupName");
        int number = intent.getIntExtra("number", 0);
        activityID = intent.getIntExtra("id", 0);
        Log.d("888", "activityID" + activityID);
        recyclerView = findViewById(R.id.chart_RecyclerView);
        groupName = findViewById(R.id.chart_GroupName);
        sendButton = findViewById(R.id.chart_SendMessageButton);
        sendButton.setOnClickListener(this);
        editText = findViewById(R.id.chart_SendMessageContent);
        groupName.setText(name + "(" + number + ")");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestChartMessage(chartMessage);
        //第一个时间：多长时间后会调用onFinish方法，第二个时间：多长时间会调用onTick方法
        CountTime countTime = new CountTime(30000,3000);
        countTime.start();
    }

    //获取聊天历史记录
    private void requestChartMessage(ChartMessage chartMessage) {
        chartMessage.getChartMessage("Bearer " + GoFunApplication.token, activityID).enqueue(new Callback<List<ChartMessageBean>>() {
            @Override
            public void onResponse(Call<List<ChartMessageBean>> call, Response<List<ChartMessageBean>> response) {
                List<ChartMessageBean> chartMessageBeans = response.body();
                list.clear();
                if (chartMessageBeans != null) {
                    for (ChartMessageBean bean : chartMessageBeans) {
                        if (person_LitePal.getUserID() == bean.getUserid()) {
                            list.add(new ChartMessageBean(2, bean.getMessage(), bean.getUsername(), bean.getImage()));
                        } else {
                            list.add(new ChartMessageBean(1, bean.getMessage(), bean.getUsername(), bean.getImage()));
                        }
                    }
                    chartMessageAdapter = new ChartMessageAdapter(list);
                    recyclerView.setAdapter(chartMessageAdapter);
                    recyclerView.scrollToPosition(list.size()-1);
                }
            }

            @Override
            public void onFailure(Call<List<ChartMessageBean>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_SendMessageButton:
                String message = editText.getText().toString();
                addMessagePost(message);

                break;
        }
    }

    //发送新的聊天消息
    private void addMessagePost(String message) {
        AddChartMessage addChartMessage = new AddChartMessage();
        addChartMessage.setActivity_id(activityID);
        addChartMessage.setChartMessageInfo(message);
        chartMessage.addChartMessage("Bearer " + GoFunApplication.token, addChartMessage).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //清空对话框
                editText.setText("");
                //静态添加聊天内容
                ChartMessageBean temp = new ChartMessageBean(2, message, person_LitePal.getUsername(), person_LitePal.getImage());
                chartMessageAdapter.addData(temp);
                //通知适配器数据改变
                chartMessageAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(chartMessageAdapter);
                recyclerView.scrollToPosition(list.size()-1);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //用于聊天记录定时刷新的内部类
    class CountTime extends CountDownTimer {

        public CountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            requestChartMessage(chartMessage);
        }

        @Override
        public void onFinish() {
        }
    }
}