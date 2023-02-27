package com.coolweather.gofun.fragment.Mine.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TagFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;
    private ArrayList<String> tabName;

    public TagFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, ArrayList<String>
            tabName) {
        super(fm);
        this.fragmentList = fragmentList;
        this.tabName = tabName;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabName.get(position);
    }
}
