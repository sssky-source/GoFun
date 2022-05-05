package com.coolweather.gofun.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.coolweather.gofun.R;

public class InfoCard extends Dialog implements View.OnClickListener {

    private Context context;
    private EditText money_text;
    private Button sure_button;
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
        View view = inflater.inflate(R.layout.money_dialog,null);
        Window window = this.getWindow();
        if (window!=null){
            window.setGravity(Gravity.CENTER);
        }
        setContentView(view);
    }

    private void bindViews() {
        money_text = findViewById(R.id.name_editText);
        sure_button = findViewById(R.id.sure_btn);
        sure_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String money = money_text.getText().toString();
        if (money.length()>0){
            lisenter.sureOnlcik(Float.valueOf(money));
            dismiss();
        }else {
            Toast.makeText(context,"请输入金额",Toast.LENGTH_SHORT).show();
        }

    }

    public interface SureOnlickLisenter{
        void sureOnlcik(float money);
    }
}