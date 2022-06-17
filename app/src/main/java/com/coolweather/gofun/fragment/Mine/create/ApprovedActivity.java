package com.coolweather.gofun.fragment.Mine.create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.ActivityAdapter;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ApprovedActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TextView title;
    final String[] titleArray = new String[]{"审核中", "已通过", "未通过"};
    final int[] state = new int[]{1,2,3};
    final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved);

        Intent intent = getIntent();
        int activityID = intent.getIntExtra("activityID", 0);
        String activityTitle = intent.getStringExtra("title");
        title = findViewById(R.id.approved_title);
        title.setText(activityTitle);
        tabLayout = findViewById(R.id.approved_TabLayout);
        viewPager2 = findViewById(R.id.approved_ViewPager2);

        for (int i = 0 ; i< titleArray.length ; i++){
            fragmentArrayList.add(new ApprovedFragment(activityID,state[i]));
        }

        viewPager2.setAdapter(new ActivityAdapter(getSupportFragmentManager(),getLifecycle(),fragmentArrayList));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titleArray[position]);
            }
        }).attach();
    }

}