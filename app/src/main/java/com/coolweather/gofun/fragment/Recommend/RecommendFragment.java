package com.coolweather.gofun.fragment.Recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.coolweather.gofun.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**

* @Author : LWD

* @Time : On 2022/4/22 15:52

*/
public class RecommendFragment extends Fragment {

    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    final ArrayList<String> tabName = new ArrayList<>();
    final ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recommand, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.recommend_Toolbar);
        toolbar.setTitle("");
        tabLayout = view.findViewById(R.id.recommend_TabLayout);
        viewPager2 = view.findViewById(R.id.recommend_ViewPager2);

    }
}
