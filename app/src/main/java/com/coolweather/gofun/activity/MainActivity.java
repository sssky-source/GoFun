package com.coolweather.gofun.activity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.coolweather.gofun.BaseActivity;
import com.coolweather.gofun.R;
import com.coolweather.gofun.adapter.MyFragmentPagerAdapter;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener{

    private RadioGroup rg_all;
    private RadioButton rb_map;
    private RadioButton rb_recommand;
    private RadioButton rb_message;
    private RadioButton rb_mine;
    private ViewPager vpager;
    private ImageView imageView;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    //代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        initView();
        rb_map.setChecked(true);
    }

    private void initView() {
        vpager = findViewById(R.id.fragment_container);
        rg_all = findViewById(R.id.radioGroup);
        rb_map = findViewById(R.id.rb_location);
        rb_recommand = findViewById(R.id.rb_recommand);
        rb_message = findViewById(R.id.rb_message);
        rb_mine = findViewById(R.id.rb_mine);
        imageView = findViewById(R.id.rbAdd);
        rg_all.setOnCheckedChangeListener(this);
        vpager.setAdapter(myFragmentPagerAdapter);
        vpager.addOnPageChangeListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        switch (checkedId) {
            case R.id.rb_location:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_recommand:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_mine:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
            default:
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int state) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

        switch (vpager.getCurrentItem()) {
            case PAGE_ONE:
                rb_map.setChecked(true);
                break;
            case PAGE_TWO:
                rb_recommand.setChecked(true);
                break;
            case PAGE_THREE:
                rb_message.setChecked(true);
                break;
            case PAGE_FOUR:
                rb_mine.setChecked(true);
                break;
        }

    }
}
