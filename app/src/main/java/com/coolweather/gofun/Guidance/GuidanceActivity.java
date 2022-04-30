package com.coolweather.gofun.Guidance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.coolweather.gofun.R;

import java.util.ArrayList;

/**

* @Author : LWD

* @Time : On 2022/4/30 0:34

*/
//引导页
public class GuidanceActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private final ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);

        viewPager2 = findViewById(R.id.guideViewPager2);

        fragmentList.add(new FragmentOne());
        fragmentList.add(new FragmentTwo());

        viewPager2.setAdapter(new FragmentAdapter(getSupportFragmentManager(),getLifecycle(),fragmentList));
    }
}