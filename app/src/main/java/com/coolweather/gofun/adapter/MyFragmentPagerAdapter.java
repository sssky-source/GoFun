package com.coolweather.gofun.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.coolweather.gofun.activity.MainActivity;
import com.coolweather.gofun.fragment.Map.MapFragment;
import com.coolweather.gofun.fragment.Message.MessageFragment;
import com.coolweather.gofun.fragment.Mine.MineFragment;
import com.coolweather.gofun.fragment.Recommend.RecommendFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;

    private MapFragment mapfragment;
    private RecommendFragment recommendFragment;
    private MessageFragment messagefragment;
    private MineFragment minefragment;

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mapfragment = new MapFragment();
        recommendFragment = new RecommendFragment();
        messagefragment = new MessageFragment();
        minefragment = new MineFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = mapfragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = recommendFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = messagefragment;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = minefragment;
                break;
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
