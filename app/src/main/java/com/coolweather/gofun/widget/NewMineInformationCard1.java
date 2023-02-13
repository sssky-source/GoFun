package com.coolweather.gofun.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.PersonLitePal;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.UserTag;
import com.coolweather.gofun.fragment.Mine.introduce.Introduce;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import org.litepal.LitePal;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMineInformationCard1 extends CardView implements View.OnClickListener {

    private PersonService personService;
    private PersonLitePal personLitePal;
    //头像和性别
    private ShapeableImageView userImage,sex;

    //名字
    private TextView username;

    //标签
    private ChipGroup chipGroup;

    public NewMineInformationCard1(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_newmine_card_1,this);
        personLitePal  = LitePal.findFirst(PersonLitePal.class);
        personService = HttpRequest.create(PersonService.class);

        initView();

        //请求标签
        requestTag();
    }

    private void initView() {

        chipGroup = findViewById(R.id.mine_tag);
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
            case R.id.mine_username:
            case R.id.mine_userImage:
            case R.id.mine_sex:
                Intent introduce = new Intent(getContext(), Introduce.class);
                getContext().startActivity(introduce);
                break;

        }

    }
}
