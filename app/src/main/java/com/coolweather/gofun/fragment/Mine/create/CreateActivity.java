package com.coolweather.gofun.fragment.Mine.create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.CreateAdapter;
import com.coolweather.gofun.fragment.Mine.bean.CreateBean;
import com.coolweather.gofun.fragment.Mine.bean.GetApplyID;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.ToastUtils;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CreateAdapter adapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        PersonService service = HttpRequest.create(PersonService.class);
        textView = findViewById(R.id.Create_title);
        textView.setText("我创建的");
        recyclerView = findViewById(R.id.Create_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreateActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = findViewById(R.id.Create_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                request(service);
                adapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        request(service);
    }

    private void request(PersonService service) {
        service.getCreateActivity("Bearer " + GoFunApplication.token).enqueue(new Callback<List<CreateBean>>() {
            @Override
            public void onResponse(Call<List<CreateBean>> call, Response<List<CreateBean>> response) {
                List<CreateBean> list = response.body();
                Collections.reverse(list);
                adapter = new CreateAdapter(R.layout.activity_create_item,list);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

                adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                        switch (view.getId()){
                            case R.id.create_applyMessage:
                                //弹窗显示申请人
                                Intent applyBody = new Intent(CreateActivity.this,ApprovedActivity.class);
                                applyBody.putExtra("activityID",list.get(position).getId());
                                applyBody.putExtra("title",list.get(position).getTitle());
                                startActivity(applyBody);
                                break;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<CreateBean>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}