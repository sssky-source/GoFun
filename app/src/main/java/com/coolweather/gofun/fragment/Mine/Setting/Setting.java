package com.coolweather.gofun.fragment.Mine.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.coolweather.gofun.R;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void erase(View view) {
        Toast.makeText(Setting.this,"清除成功",Toast.LENGTH_SHORT).show();
        finish();
    }
}