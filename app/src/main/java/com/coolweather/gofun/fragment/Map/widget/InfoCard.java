package com.coolweather.gofun.fragment.Map.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.coolweather.gofun.R;
import com.coolweather.gofun.activity.DetailInfoActivity;
import com.coolweather.gofun.util.ToastUtils;

public class InfoCard extends Dialog implements View.OnClickListener {

    private Context context;
    private LinearLayout bt_detail;
    private TextView tv_type,tv_location,tv_creator;
    private ImageView join_imageone,join_imagetwo;
    private TextView join_nameone,join_nametwo;
    private TextView join_commitone,join_committwo;
    private TextView join_dateone,join_datetwo;

    public InfoCard(@NonNull Context context) {
        super(context, R.style.dialog);
        this.context = context;
        initView();
        bindViews();
    }


    private void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.info_dialog,null);
        Window window = this.getWindow();
        if (window!=null){
            window.setGravity(Gravity.CENTER);
        }
        setContentView(view);
    }

    private void bindViews() {
        bt_detail = findViewById(R.id.bt_detail);
        tv_type = findViewById(R.id.tv_type);
        tv_location = findViewById(R.id.tv_location);
        tv_creator = findViewById(R.id.tv_creator);
        bt_detail.setOnClickListener(this);
        join_imageone = findViewById(R.id.join_imageone);
        join_imagetwo = findViewById(R.id.join_imagetwo);
        join_commitone = findViewById(R.id.join_commitone);
        join_committwo = findViewById(R.id.join_committwo);
        join_dateone = findViewById(R.id.join_dateone);
        join_datetwo = findViewById(R.id.join_datetwo);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, DetailInfoActivity.class);
        context.startActivity(intent);
    }

}