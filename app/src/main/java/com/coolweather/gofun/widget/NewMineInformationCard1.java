package com.coolweather.gofun.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.PersonLitePal;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Message.ActivityChartMessage;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Mine.bean.UserTag;
import com.coolweather.gofun.fragment.Mine.introduce.Introduce;
import com.coolweather.gofun.fragment.Mine.tag.MineTagActivity;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.ToastUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMineInformationCard1 extends CardView implements View.OnClickListener {

    private PersonService personService;
    private PersonLitePal personLitePal;
    //头像和性别
    private ShapeableImageView userImage,sex;

    private int result = 0;

    //名字
    private TextView username;

    //标签
    private ChipGroup chipGroup;
    private LinearLayout layout;


    public NewMineInformationCard1(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_newmine_card_1,this);
        personLitePal = LitePal.findFirst(PersonLitePal.class);
        personService = HttpRequest.create(PersonService.class);

        initView();

        //请求标签
        requestTag();

        //第一个时间：多长时间后会调用onFinish方法，第二个时间：多长时间会调用onTick方法
        CountTime countTime = new CountTime(30000,3000);
//        countTime.start();
    }

    private void initView() {

        chipGroup = findViewById(R.id.mine_tag);
        layout = findViewById(R.id.mine_tagChange);
        username = findViewById(R.id.mine_username);
        username.setText(personLitePal.getUsername());

        userImage = findViewById(R.id.mine_userImage);
        Glide.with(getContext()).load(personLitePal.getImage()).into(userImage);

        sex = findViewById(R.id.mine_sex);
        if (personLitePal.getSex().compareTo("男") == 0){
            Log.d("999999","男");
        }else if (personLitePal.getSex().compareTo("女") == 0){
            Log.d("999999","女");
        }

        layout.setOnClickListener(this);
        username.setOnClickListener(this);
        userImage.setOnClickListener(this);
        sex.setOnClickListener(this);
    }

    //请求标签
    private void requestTag() {
        personService.getUserTag("Bearer " + GoFunApplication.getToken(),personLitePal.getUserID()).enqueue(new Callback<List<UserTag>>() {
            @Override
            public void onResponse(Call<List<UserTag>> call, Response<List<UserTag>> response) {
                List<UserTag> list = response.body();

                for (UserTag userTag : list){
                    Chip chip = new Chip(getContext());
                    chip.setText(userTag.getName());
                    chip.setCheckable(false);
                    chip.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent tagChange = new Intent(getContext(), MineTagActivity.class);
                            tagChange.putExtra("userID",personLitePal.getUserID());
                            getContext().startActivity(tagChange);
                        }
                    });
                    chipGroup.addView(chip);
                }
            }
            @Override
            public void onFailure(Call<List<UserTag>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //修改个人资料
            case R.id.mine_username:
            case R.id.mine_userImage:
            case R.id.mine_sex:
                Intent introduce = new Intent(getContext(), Introduce.class);
                getContext().startActivity(introduce);
                break;

                //修改标签
            case R.id.mine_tagChange:
            case R.id.mine_tag:
                Intent tagChange = new Intent(getContext(), MineTagActivity.class);
                //传入userID用于在修改标签页面 确定已经是用户的标签
                tagChange.putExtra("userID",personLitePal.getUserID());
                getContext().startActivity(tagChange);
                break;
            default:
                break;
        }

    }

    class CountTime extends CountDownTimer {

        public CountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            requestTag();
        }

        @Override
        public void onFinish() {
        }
    }

}
