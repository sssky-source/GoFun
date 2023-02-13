package com.coolweather.gofun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.coolweather.gofun.R;

public class NewMineInformationCard3 extends CardView implements View.OnClickListener{

    private RelativeLayout help,about,out,set;

    public NewMineInformationCard3(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_newmine_card_3,this);

        help = findViewById(R.id.mine_help);
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
            case R.id.mine_help:
                //intent跳转
                break;
            case R.id.mine_about:

            case R.id.mine_out:

            case R.id.mine_set:
        }
    }
}
