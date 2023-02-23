package com.coolweather.gofun.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.apply.PersonActivity;
import com.coolweather.gofun.fragment.Mine.collect.CollectActivity;
import com.coolweather.gofun.fragment.Mine.create.CreateActivity;
import com.coolweather.gofun.fragment.Mine.join.JoinActivity;

public class NewMineInformationCard2 extends CardView implements View.OnClickListener{

    private RelativeLayout join,apply,create,star;

    public NewMineInformationCard2(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_newmine_card_2,this);

        join = findViewById(R.id.relative_join);
        join.setOnClickListener(this);

        apply = findViewById(R.id.relative_apply);
        apply.setOnClickListener(this);

        create = findViewById(R.id.relative_create);
        create.setOnClickListener(this);

        star = findViewById(R.id.relative_star);
        star.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

                //加入的
            case R.id.relative_join:
                Intent join = new Intent(getContext(), JoinActivity.class);
                getContext().startActivity(join);
                break;

                //申请的
            case R.id.relative_apply:
                Intent apply = new Intent(getContext(), PersonActivity.class);
                getContext().startActivity(apply);
                break;

                //创建的
            case R.id.relative_create:
                Intent create = new Intent(getContext(), CreateActivity.class);
                getContext().startActivity(create);
                break;

                //收藏的
            case R.id.relative_star:
                Intent collect = new Intent(getContext(), CollectActivity.class);
                getContext().startActivity(collect);
                break;
        }
    }
}
