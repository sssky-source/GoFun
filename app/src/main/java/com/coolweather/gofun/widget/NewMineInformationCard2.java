package com.coolweather.gofun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.coolweather.gofun.R;

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

    }
}
