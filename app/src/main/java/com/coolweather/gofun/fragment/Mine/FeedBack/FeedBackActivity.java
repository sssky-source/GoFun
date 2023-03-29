package com.coolweather.gofun.fragment.Mine.FeedBack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.MineFragment;

public class FeedBackActivity extends AppCompatActivity {

    EditText help_feedback=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        Toolbar toolbar = findViewById(R.id.feedback_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton button = findViewById(R.id.image_button);
        help_feedback = findViewById(R.id.help_feedback);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Context = help_feedback.getText().toString();
                Toast.makeText(FeedBackActivity.this, "感谢您的反馈,我们会尽快处理您的意见。", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}