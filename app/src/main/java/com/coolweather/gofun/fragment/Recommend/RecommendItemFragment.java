package com.coolweather.gofun.fragment.Recommend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Recommend.Adapter.RecommendItemAdapter;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.util.HttpRequest;
import com.coolweather.gofun.util.RecommendService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendItemFragment extends Fragment {

    private int id;
    private String token;
    private RecyclerView recyclerView;
    private RecommendItemAdapter recommendItemAdapter;

    public RecommendItemFragment(String token,int id) {
        this.token = token;
        this.id = id;
    }

    public RecommendItemFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend_item,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recommend_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        RecommendService recommendService = HttpRequest.create(RecommendService.class);
        recommendService.getActivityItem("Bearer " + token,id).enqueue(new Callback<List<ActivityItem>>() {
            @Override
            public void onResponse(Call<List<ActivityItem>> call, Response<List<ActivityItem>> response) {
                List<ActivityItem> list = response.body();

                recommendItemAdapter = new RecommendItemAdapter(R.layout.activity_recommend_item_detail,list);
                recyclerView.setAdapter(recommendItemAdapter);

            }

            @Override
            public void onFailure(Call<List<ActivityItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
