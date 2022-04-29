package com.coolweather.gofun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.graphics.Paint;
import android.widget.ProgressBar;

import com.coolweather.gofun.R;

import static android.text.InputType.*;

/**

 * @Author : CYT

 * @Time : On 2022/4/29

 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText register_name,register_password,register_email;
    private Button registerBtn;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListener();
    }

    private void initViews() {
        register_name = findViewById(R.id.accountRegister_name);
        register_password = findViewById(R.id.accountRegister_password);
        register_email = findViewById(R.id.accountRegister_email);
        registerBtn = findViewById(R.id.accountRegistern_toRegister);
        checkBox = findViewById(R.id.register_check);
        register_password.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
        checkBox.setBackgroundResource(R.drawable.icon_bukejian);
    }

    private void initListener() {
        registerBtn.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //CheckBox选中，显示明文
                    register_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    checkBox.setBackgroundResource(R.drawable.icon_kejian);
                } else {
                    //CheckBox取消选中，显示暗文
                    register_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    checkBox.setBackgroundResource(R.drawable.icon_bukejian);
                }
                //光标移至最末端
                register_password.setSelection(register_password.getText().toString().length());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.accountRegistern_toRegister:
                uerAccountRegister();//注册
        }
    }

    private void uerAccountRegister() {

    }
}
