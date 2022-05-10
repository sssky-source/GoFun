package com.coolweather.gofun.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coolweather.gofun.R;
import com.coolweather.gofun.activity.MainActivity;
import com.coolweather.gofun.fragment.Mine.PersonActivity;
import com.coolweather.gofun.util.ToastUtils;

public class PersonInformationCard extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout sel1, sel2, sel3;

    public PersonInformationCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_mine_top_1, this);
        initView();
    }

    private void initView() {
        sel1 = findViewById(R.id.sel1);
        sel2 = findViewById(R.id.sel2);
        sel3 = findViewById(R.id.sel3);
        sel1.setOnClickListener(this);
        sel2.setOnClickListener(this);
        sel3.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sel1:
                break;
            case R.id.sel2:
                Intent intent = new Intent(getContext(),PersonActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.sel3:
                ToastUtils.show(getContext(), "跳转");
                break;
        }
    }
}

