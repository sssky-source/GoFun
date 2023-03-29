package com.coolweather.gofun.fragment.Mine.about;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        Toolbar toolbar = findViewById(R.id.about_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void web(View view){
        Intent intent = new Intent(AboutActivity.this,WebActivity.class);
        startActivity(intent);
    }

    public void service(View view) {
        Intent intent = new Intent(AboutActivity.this,Service.class);
        startActivity(intent);
    }

    public void secret(View view) {
        Intent intent = new Intent(AboutActivity.this,Service.class);
        startActivity(intent);
    }

    public void person(View view) {
        //Toast.makeText(AboutActivity.this,"hhhhh",Toast.LENGTH_SHORT).show();
        AlertDialog alertDialog = new AlertDialog.Builder(AboutActivity.this)
                //标题
                .setTitle("开发人员")
                //内容
                .setMessage("        You&I是由中南民族大学新思路Android组开发运营，旨在打造一个线上线下陌生人交友平台，提高当代年轻人的社交主动性。")
                //图标
                .setIcon(R.drawable.logo)
                .setPositiveButton("确认", null)
                .create();
        alertDialog.show();
    }
}