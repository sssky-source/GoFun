package com.coolweather.gofun.fragment.Mine.apply;

import android.annotation.SuppressLint;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.StateItemAdapter;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.net.PersonService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    我申请的活动详情
 */
public class StateFragment extends Fragment {

    private int id;
    private PersonService personService;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StateItemAdapter stateItemAdapter;

    public StateFragment(Integer id,PersonService personService) {
        this.id = id;
        this.personService = personService;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_state,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.activityState_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = view.findViewById(R.id.activityState_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                request();
                stateItemAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        request();
    }

    private void request() {
        personService.getApplyStatusDetail("Bearer " + GoFunApplication.token,id).enqueue(new Callback<List<PersonActivityItem>>() {
            @Override
            public void onResponse(Call<List<PersonActivityItem>> call, Response<List<PersonActivityItem>> response) {
                List<PersonActivityItem> list = response.body();
                stateItemAdapter = new StateItemAdapter(R.layout.activity_state_item,list);
                recyclerView.setAdapter(stateItemAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<PersonActivityItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
