package com.coolweather.gofun.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.coolweather.gofun.activity.MainActivity;
import com.coolweather.gofun.fragment.Dynamics.DynamicsFragment;
import com.coolweather.gofun.fragment.Map.MapFragment;
import com.coolweather.gofun.fragment.Message.MessageFragment;
import com.coolweather.gofun.fragment.Mine.MineFragment;
import com.coolweather.gofun.fragment.Recommend.RecommendFragment;
import com.coolweather.gofun.fragment.TestFragment;
import com.coolweather.gofun.fragment.TestFragment1;
import com.coolweather.gofun.fragment.TestFragment2;
import com.coolweather.gofun.fragment.TestFragment4;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 5;

    private MapFragment mapfragment;
    private RecommendFragment recommendFragment;
    private MessageFragment messagefragment;
    private MineFragment minefragment;
    private TestFragment testFragment;
    private TestFragment1 testFragment1;
    private TestFragment2 testFragment2;
    private TestFragment4 testFragment4;

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mapfragment = new MapFragment();
        recommendFragment = new RecommendFragment();
        messagefragment = new MessageFragment();
        minefragment = new MineFragment();

        testFragment = new TestFragment();
        testFragment1 = new TestFragment1();
        testFragment2 = new TestFragment2();
        testFragment4 = new TestFragment4();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = mapfragment;
                //  fragment = testFragment;
                break;
            case MainActivity.PAGE_TWO:
                //    fragment = testFragment2;
                fragment = recommendFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = messagefragment;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = minefragment;
                break;
            default:
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
