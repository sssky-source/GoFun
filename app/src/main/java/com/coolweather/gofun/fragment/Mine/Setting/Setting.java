package com.coolweather.gofun.fragment.Mine.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.introduce.Introduce;
import com.coolweather.gofun.fragment.Mine.tag.MineTagActivity;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void erase(View view) {
        Toast.makeText(Setting.this,"清除成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void intentToIntroduce(View view) {
        Intent introduce = new Intent(Setting.this, Introduce.class);
        startActivity(introduce);
    }

    public void intentToMineTag(View view) {
        Intent tag = new Intent(Setting.this, MineTagActivity.class);
        startActivity(tag);
    }
}