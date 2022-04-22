package com.coolweather.gofun.widget;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coolweather.gofun.R;

public class PersonImformationCard extends RelativeLayout {


    public PersonImformationCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_mine_top_1,this);
    }

}

