package com.coolweather.gofun.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.coolweather.gofun.MainActivity;
import com.coolweather.gofun.fragment.Mapfragment;
import com.coolweather.gofun.fragment.Messagefragment;
import com.coolweather.gofun.fragment.Minefragment;
import com.coolweather.gofun.fragment.Recommandfragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;

    private Mapfragment mapfragment;
    private Recommandfragment recommandfragment;
    private Messagefragment messagefragment;
    private Minefragment minefragment;

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mapfragment = new Mapfragment();
        recommandfragment = new Recommandfragment();
        messagefragment = new Messagefragment();
        minefragment = new Minefragment();
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
                fragment = recommandfragment;
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
