package com.coolweather.gofun.fragment.Mine.create;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.ApprovedAdapter;
import com.coolweather.gofun.fragment.Mine.bean.GetApplyID;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.ToastUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovedFragment extends Fragment {

    private PersonService service;
    private int activityID, state;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ApprovedAdapter approvedAdapter;

    public ApprovedFragment(int activityID, int s) {
        this.activityID = activityID;
        state = s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_approved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        service = HttpRequest.create(PersonService.class);
        recyclerView = view.findViewById(R.id.approvedFrag_RecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.approvedFrag_SwipeRefreshLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                requestApplyID();
                approvedAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        requestApplyID();
    }

    //获取申请ID，用于通过或者拒绝申请
    private void requestApplyID() {
        service.getApplyID("Bearer " + GoFunApplication.token, activityID, state).enqueue(new Callback<List<GetApplyID>>() {
            @Override
            public void onResponse(Call<List<GetApplyID>> call, Response<List<GetApplyID>> response) {
                List<GetApplyID> list = response.body();
                approvedAdapter = new ApprovedAdapter(R.layout.fragment_approved_item, list, state);
                recyclerView.setAdapter(approvedAdapter);
                swipeRefreshLayout.setRefreshing(false);

                approvedAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                        int id = list.get(position).getId();
                        switch (view.getId()) {
                            case R.id.apply_agree:
                                passApply(id);
                                break;
                            case R.id.apply_refuse:
                                unPassApply(id);
                                break;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<GetApplyID>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //拒绝申请
    private void unPassApply(int id) {
        service.unPassApply("Bearer " + GoFunApplication.token,id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ToastUtils.show(getContext(),"拒绝该成员加入活动");
                swipeRefreshLayout.setRefreshing(true);
                requestApplyID();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //通过申请
    private void passApply(int id) {
        service.passApply("Bearer " + GoFunApplication.token,id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ToastUtils.show(getContext(),"已同意加入活动");
                swipeRefreshLayout.setRefreshing(true);
                requestApplyID();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
