package com.coolweather.gofun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.coolweather.gofun.R;

public class NewMineInformationCard3 extends CardView implements View.OnClickListener{
    public NewMineInformationCard3(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_newmine_card_3,this);
    }

    @Override
    public void onClick(View view) {

    }
}
