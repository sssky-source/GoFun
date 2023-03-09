package com.coolweather.gofun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.coolweather.gofun.BaseActivity;
import com.coolweather.gofun.CustomViewPager;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.adapter.MyFragmentPagerAdapter;
import com.coolweather.gofun.LocalDb.PersonLitePal;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.ToastUtils;

import org.litepal.LitePal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, CustomViewPager.OnPageChangeListener {

    private RadioGroup rg_all;
    private RadioButton rb_map;
    private RadioButton rb_recommand;
    private RadioButton rb_message;
    private RadioButton rb_mine;
    private CustomViewPager vpager;
    private ImageView imageView;
   // private String token;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    //代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("111","create");
        //获取数据库
        LitePal.getDatabase();
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
                Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                startActivity(intent);
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

    //返回监听 两次返回直接退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                ToastUtils.show(MainActivity.this,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
