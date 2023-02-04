package com.coolweather.gofun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.coolweather.gofun.R;

public class NewMineInformationCard1 extends CardView implements View.OnClickListener {


    public NewMineInformationCard1(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_newmine_card_1,this);


    }

    @Override
    public void onClick(View view) {

    }
}
