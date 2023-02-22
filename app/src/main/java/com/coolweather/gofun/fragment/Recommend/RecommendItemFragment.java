package com.coolweather.gofun.fragment.Recommend;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.listener.OnUpFetchListener;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Recommend.Adapter.RecommendItemAdapter;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.RecommendService;
import com.coolweather.gofun.util.DialogUtils;
import com.coolweather.gofun.util.ToastUtils;

import java.io.Serializable;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendItemFragment extends Fragment {

    private int id;
    private String token;
    private RecyclerView recyclerView;
    private RecommendItemAdapter recommendItemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ActivityItem> list;
    private int lastId = 0;

    public RecommendItemFragment(String token, int id) {
        this.token = token;
        this.id = id;
    }

    public RecommendItemFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecommendService recommendService = HttpRequest.create(RecommendService.class);

        recyclerView = view.findViewById(R.id.recommend_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = view.findViewById(R.id.recommend_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink);
        //监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                request(recommendService);
                recommendItemAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        request(recommendService);

    }

    //根据活动类型ID获取活动条目
    private void request(RecommendService recommendService) {
        recommendService.getActivityItemByType("Bearer " + token, id, 0).enqueue(new Callback<List<ActivityItem>>() {
            @Override
            public void onResponse(Call<List<ActivityItem>> call, Response<List<ActivityItem>> response) {
                list = response.body();
                recommendItemAdapter = new RecommendItemAdapter(R.layout.activity_recommend_item_detail, list);
                recyclerView.setAdapter(recommendItemAdapter);
                swipeRefreshLayout.setRefreshing(false);
                Log.d("size", "size:" + list.size());
                lastId = list.get(list.size()-1).getId();

                //lastId = list.get(9).getId();
                Log.d("size", "lastid:" + lastId);


                //封装一个确认和取消的提示框
                recommendItemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                        switch (view.getId()) {
                            case R.id.activity_apply:
                                DialogUtils.getInstance().showDialog(getContext(), "确认加入活动？", new DialogUtils.DialogCallBack() {
                                    @Override
                                    public void OkEvent() {
                                        ActivityItem activityItem = list.get(position);
                                        applyRequest(recommendService, activityItem.getId());
                                        ToastUtils.show(getContext(), "已提交申请，等待加入活动");
                                    }
                                });
                                break;
                            case R.id.activity_detailCard:

                                /**
                                 * 将ActivityItem 整个活动信息传入
                                 * ActivityItem activityItem = list.get(position);
                                 * Intent detail = new Intent(getActivity(), RecommendActivityDetail.class);
                                 * Bundle bundle = new Bundle();
                                 * bundle.putSerializable("detail_item",activityItem);
                                 * detail.putExtras(bundle);
                                 */

                                Intent detail = new Intent(getActivity(), RecommendActivityDetail.class);
                                //传入活动ID
                                int activityId = list.get(position).getId();
                                detail.putExtra("activityId", activityId);
                                startActivity(detail);
                                break;
                        }
                    }
                });

                /**
                 * BaseRecyclerViewAdapterHelper3.x
                 * 上拉加载
                 * 与2.x版本不一样
                 * list改变之后 界面没有更新
                 * ！改进：需要双向绑定
                 */
                recommendItemAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        moreRequest(recommendService);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ActivityItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void moreRequest(RecommendService recommendService){
        recommendService.getActivityItemByType("Bearer " + token, id, lastId).enqueue(new Callback<List<ActivityItem>>() {
            @Override
            public void onResponse(Call<List<ActivityItem>> call, Response<List<ActivityItem>> response) {
                List<ActivityItem> itemList = response.body();
                for (ActivityItem activityItem : itemList) {
                    list.add(activityItem);
                }

                /**
                 * 重新覆盖list会导致 新列表没有向上加载 和 点击事件的监听
                 * recommendItemAdapter = new RecommendItemAdapter(R.layout.activity_recommend_item_detail, list);
                 * recyclerView.setAdapter(recommendItemAdapter);
                 */

                //是否打开自动加载更多
                recommendItemAdapter.getLoadMoreModule().setAutoLoadMore(false);
                //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多
                recommendItemAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
                Log.d("size", "After size:" + list.size());
                lastId = list.get(list.size()-1).getId();
                Log.d("size", "lastId:" + lastId);
                recommendItemAdapter.addData(list);
                recommendItemAdapter.getLoadMoreModule().loadMoreComplete();
            }

            @Override
            public void onFailure(Call<List<ActivityItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //申请加入活动
    private void applyRequest(RecommendService recommendService, int id) {
        recommendService.applyActivity("Bearer " + GoFunApplication.token, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                swipeRefreshLayout.setRefreshing(true);
                request(recommendService);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
