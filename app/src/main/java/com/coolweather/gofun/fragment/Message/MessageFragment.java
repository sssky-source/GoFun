package com.coolweather.gofun.fragment.Message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amap.api.maps.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Message.Adapter.JoinActivityAdapter;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;
import com.coolweather.gofun.net.ChartMessage;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

* @Author : LWD

* @Time : On 2022/6/2 0:14

*/

/*
    消息碎片
 */
public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;
    private JoinActivityAdapter joinActivityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    //空布局
    private LinearLayout empty;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PersonService personService = HttpRequest.create(PersonService.class);
        ChartMessage chartMessage = HttpRequest.create(ChartMessage.class);
        title = view.findViewById(R.id.title);
        title.setText("我的消息");
        recyclerView = view.findViewById(R.id.message_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = view.findViewById(R.id.message_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink);
        empty = view.findViewById(R.id.message_empty);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestGoingActivity(personService);
                joinActivityAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        requestGoingActivity(personService);
    }

    private void requestGoingActivity(PersonService personService) {
        personService.getJoinActivity("Bearer " + GoFunApplication.token).enqueue(new Callback<List<PersonActivityItem>>() {
            @Override
            public void onResponse(Call<List<PersonActivityItem>> call, Response<List<PersonActivityItem>> response) {
                List<PersonActivityItem> list = response.body();
                joinActivityAdapter = new JoinActivityAdapter(R.layout.activity_message_item,list);
                recyclerView.setAdapter(joinActivityAdapter);
                swipeRefreshLayout.setRefreshing(false);

                if (list.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }

                joinActivityAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        switch (view.getId()){
                            case R.id.message_CardView:
                                String groupName = list.get(position).getTitle();
                                int number = list.get(position).getCurnumber();
                                int id = list.get(position).getActivity_id();
                                Intent chartMessage = new Intent(getActivity(),ActivityChartMessage.class);
                                chartMessage.putExtra("groupName",groupName);
                                chartMessage.putExtra("number",number);
                                chartMessage.putExtra("id",id);
                                startActivity(chartMessage);
                                break;
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
