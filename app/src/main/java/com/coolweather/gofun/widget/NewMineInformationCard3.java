package com.coolweather.gofun.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.coolweather.gofun.R;
import com.coolweather.gofun.activity.LoginActivity;
import com.coolweather.gofun.fragment.Mine.FeedBack.FeedBackActivity;
import com.coolweather.gofun.fragment.Mine.about.AboutActivity;

public class NewMineInformationCard3 extends CardView implements View.OnClickListener{

    private RelativeLayout help,about,out,set;

    public NewMineInformationCard3(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_newmine_card_3,this);

        help = findViewById(R.id.mine_feedback);
        about = findViewById(R.id.mine_about);
        out = findViewById(R.id.mine_out);
        set = findViewById(R.id.mine_set);

        help.setOnClickListener(this);;
        about.setOnClickListener(this);
        out.setOnClickListener(this);
        set.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_feedback:
                Intent feedback = new Intent(getContext(), FeedBackActivity.class);
                getContext().startActivity(feedback);
                break;
            case R.id.mine_about:
                Intent about = new Intent(getContext(), AboutActivity.class);
                getContext().startActivity(about);
                break;
            case R.id.mine_out:
                Toast.makeText(getContext(),"退出成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.mine_set:
        }
    }
}
