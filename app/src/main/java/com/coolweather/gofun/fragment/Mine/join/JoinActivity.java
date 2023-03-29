package com.coolweather.gofun.fragment.Mine.join;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.JoinItemAdapter;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;
import com.coolweather.gofun.fragment.Recommend.Adapter.RecommendItemAdapter;
import com.coolweather.gofun.fragment.Recommend.RecommendActivityDetail;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.net.RecommendService;
import com.coolweather.gofun.util.DialogUtils;
import com.coolweather.gofun.util.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    我加入的
 */
public class JoinActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private JoinItemAdapter joinItemAdapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        PersonService personService = HttpRequest.create(PersonService.class);
        recyclerView = findViewById(R.id.Join_RecyclerView);
        title = findViewById(R.id.Join_title);
        title.setText("我加入的");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JoinActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = findViewById(R.id.Join_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request(personService);
                joinItemAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        request(personService);

    }

    private void request(PersonService personService) {
        Log.d("test","test22" + GoFunApplication.token);
        personService.getJoinActivity("Bearer " + GoFunApplication.token).enqueue(new Callback<List<PersonActivityItem>>() {
            @Override
            public void onResponse(Call<List<PersonActivityItem>> call, Response<List<PersonActivityItem>> response) {
                List<PersonActivityItem> list = response.body();
                joinItemAdapter = new JoinItemAdapter(R.layout.activity_join_item,list);
                recyclerView.setAdapter(joinItemAdapter);
                swipeRefreshLayout.setRefreshing(false);

                joinItemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                        switch(view.getId()){
                            case R.id.join_register:
                                Toast.makeText(JoinActivity.this,"未至签到范围",Toast.LENGTH_SHORT).show();
                                break;
//                            case R.id.card:
//                                Intent detail = new Intent(JoinActivity.this, RecommendActivityDetail.class);
//                                //传入活动ID
//                                int activityId = list.get(position).getId();
//                                detail.putExtra("activityId", activityId);
//                                startActivity(detail);
//                                break;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<PersonActivityItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}