package com.coolweather.gofun.fragment.Recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.coolweather.gofun.activity.MainActivity;
import com.coolweather.gofun.fragment.Recommend.Adapter.FragmentAdapter;
import com.coolweather.gofun.LocalDb.SqliteUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.RecommendService;
import com.coolweather.gofun.util.ToastUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author : LWD
 * @Time : On 2022/4/22 15:52
 */
public class RecommendFragment extends Fragment {

    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String token;

    final ArrayList<String> tabName = new ArrayList<>();
    final ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommand, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabName.clear();
        fragmentList.clear();
        //指出fragment愿意添加item到选项菜单
        setHasOptionsMenu(true);
        //本地数据库查找token
        SqliteUtil sqliteUtil = new SqliteUtil(getActivity());
        token = sqliteUtil.getToken();
        toolbar = view.findViewById(R.id.recommend_Toolbar);
        toolbar.setTitle("");
        //将Toolbar的实例传入
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        tabLayout = view.findViewById(R.id.recommend_TabLayout);
        viewPager2 = view.findViewById(R.id.recommend_ViewPager2);

        RecommendService recommendService = HttpRequest.create(RecommendService.class);
        recommendService.getActivityType("Bearer " + token).enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                List<Activity> list = response.body();
                Collections.reverse(list);
                for (Activity activity : list) {
                    tabName.add(activity.getType1());
                    fragmentList.add(new RecommendItemFragment(token,activity.getId()));
                }

                viewPager2.setAdapter(new FragmentAdapter(getActivity().getSupportFragmentManager(), getLifecycle(), fragmentList));
                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabName.get(position));
                    }
                });
                tabLayoutMediator.attach();
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.invite,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.QR_Code:
                ToastUtils.show(getActivity(),"扫一扫");
                break;
            case R.id.invitation:
                ToastUtils.show(getActivity(),"邀请码");
                break;
        }
        return true;
    }
}
