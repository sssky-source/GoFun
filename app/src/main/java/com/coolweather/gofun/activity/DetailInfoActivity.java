package com.coolweather.gofun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;

public class DetailInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        Log.d("TOK", GoFunApplication.getToken());
    }
}
