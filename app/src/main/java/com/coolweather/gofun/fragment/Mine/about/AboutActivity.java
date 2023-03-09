package com.coolweather.gofun.fragment.Mine.about;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.coolweather.gofun.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void web(View view){
        Intent intent = new Intent(AboutActivity.this,WebActivity.class);
        startActivity(intent);
    }
}