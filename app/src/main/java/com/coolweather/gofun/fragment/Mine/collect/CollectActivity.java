package com.coolweather.gofun.fragment.Mine.collect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.CollectItemAdapter;
import com.coolweather.gofun.fragment.Mine.bean.CollectBean;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CollectItemAdapter collectItemAdapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        PersonService personService = HttpRequest.create(PersonService.class);
        recyclerView = findViewById(R.id.Collect_RecyclerView);
        title = findViewById(R.id.Collect_title);
        title.setText("我的收藏");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CollectActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = findViewById(R.id.Collect_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                requestStar(personService);
                collectItemAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(true);

        requestStar(personService);

    }

    /**
     * 不运行函数？
     * 解决：onFailure中 使用t.getCause()方法找到原因
     * 发现是类型错误 经纬度x和y应该是double类型 而使用了GsonFormat之后转换为int类型 所以导致失败
     * @param personService
     */
    private void requestStar(PersonService personService) {
        personService.getStarActivity("Bearer " + GoFunApplication.token).enqueue(new Callback<List<CollectBean>>() {
            @Override
            public void onResponse(Call<List<CollectBean>> call, Response<List<CollectBean>> response) {
                List<CollectBean> list = response.body();
                collectItemAdapter = new CollectItemAdapter(R.layout.activity_collect_item,list);
                recyclerView.setAdapter(collectItemAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<CollectBean>> call, Throwable t) {
                Log.d("test","test3:" + t.getCause());
                t.printStackTrace();
            }
        });

    }
}