package com.coolweather.gofun.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.coolweather.gofun.R;
import com.coolweather.gofun.util.ToastUtils;

public class InfoCard extends Dialog implements View.OnClickListener {

    private Context context;
    private LinearLayout bt_detail;
    private SureOnlickLisenter lisenter;

    public InfoCard(@NonNull Context context) {
        super(context, R.style.dialog);
        this.context = context;
        initView();
        bindViews();
    }

    public void setLisenter(SureOnlickLisenter lisenter) {
        this.lisenter = lisenter;
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
        bt_detail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ToastUtils.show(context,"进入详细信息页面");
    }

    public interface SureOnlickLisenter{
        void sureOnlcik();
    }
}