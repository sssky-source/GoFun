package com.coolweather.gofun.Animation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coolweather.gofun.R;
import com.coolweather.gofun.activity.LoginActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SgfSplash6Activity extends AppCompatActivity {
    @BindView(R.id.lin)
    LinearLayout lin;
    //@BindView(R.id.tv_sbs)
    //TextView tv_sbs;
    //@BindView(R.id.tv_search)
    //TextView tv_search;
    //@BindView(R.id.tv_course)
    //TextView tv_course;
    @BindView(R.id.logo)
    ImageView logo;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sgf_splash6);

        ButterKnife.bind(this);

        ArrayList<TextView> textViews = new ArrayList<>();
        //textViews.add(tv_sbs);
        //textViews.add(tv_search);
        //textViews.add(tv_course);
//        showAnimation(textViews);
        showAnimation();

    }

    private void showAnimation() {
        //tv_sbs.setVisibility(View.INVISIBLE);
        //tv_search.setVisibility(View.INVISIBLE);
        //tv_course.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(SgfSplash6Activity.this, R.anim.my_anim_scale2);
        logo.startAnimation(animation);
        logo.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(SgfSplash6Activity.this, R.anim.my_anim_scale2);
                //tv_sbs.startAnimation(animation);
                //tv_sbs.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        //动画效果
                        Animation animation = AnimationUtils.loadAnimation(SgfSplash6Activity.this, R.anim.my_anim_scale2);
                        //tv_search.startAnimation(animation);
                        //tv_search.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                //动画效果
                                Animation animation = AnimationUtils.loadAnimation(SgfSplash6Activity.this, R.anim.my_anim_scale2);
                                //tv_course.startAnimation(animation);
                                //tv_course.setVisibility(View.VISIBLE);
                                mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        startActivity(new Intent(SgfSplash6Activity.this, LoginActivity.class));
                                        finish();
                                    }
                                }, 500);
                            }
                        }, 500);
                    }
                }, 500);
            }
        }, 500);
    }

    private void showAnimation(ArrayList<TextView> textViews) {
        try {
            //菜单项弹出动画
            for (int i = 0; i < textViews.size(); i++) {
//                textViews.get(i).setOnClickListener(this);
                textViews.get(i).setVisibility(View.INVISIBLE);
                int finalI = i;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textViews.get(finalI).setVisibility(View.VISIBLE);
                        //动画效果1
//                        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(textViews.get(finalI), "translationY", 600, 0);
//                        fadeAnim.setDuration(2000);
//                        KickBackAnimator kickAnimator = new KickBackAnimator();
//                        kickAnimator.setDuration(1500);
//                        fadeAnim.setEvaluator(kickAnimator);
//                        fadeAnim.start();
                        //动画效果2
//                        Animation animation = AnimationUtils.loadAnimation(SgfSplash6Activity.this, R.anim.anim_top_in);
//                        textViews.get(finalI).startAnimation(animation);
                        //动画效果3
//                        Animation animation = AnimationUtils.loadAnimation(SgfSplash6Activity.this, R.anim.my_anim_scale);
                        Animation animation = AnimationUtils.loadAnimation(SgfSplash6Activity.this, R.anim.my_anim_scale2);
                        textViews.get(finalI).startAnimation(animation);
                    }
                }, i * 50 + 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}