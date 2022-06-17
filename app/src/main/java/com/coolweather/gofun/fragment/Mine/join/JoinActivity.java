package com.coolweather.gofun.fragment.Mine.join;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.JoinItemAdapter;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        PersonService personService = HttpRequest.create(PersonService.class);
        recyclerView = findViewById(R.id.Join_RecyclerView);
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
        personService.getJoinActivity("Bearer " + GoFunApplication.token).enqueue(new Callback<List<PersonActivityItem>>() {
            @Override
            public void onResponse(Call<List<PersonActivityItem>> call, Response<List<PersonActivityItem>> response) {
                List<PersonActivityItem> list = response.body();
                joinItemAdapter = new JoinItemAdapter(R.layout.activity_join_item,list);
                recyclerView.setAdapter(joinItemAdapter);
                swipeRefreshLayout.setRefreshing(false);
                Log.d("22222222","stopJoin");
            }

            @Override
            public void onFailure(Call<List<PersonActivityItem>> call, Throwable t) {
                t.printStackTrace();
                Log.d("22222222","stopJoin111");
            }
        });
    }
}