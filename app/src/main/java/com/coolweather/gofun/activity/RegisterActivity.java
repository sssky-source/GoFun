package com.coolweather.gofun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.graphics.Paint;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.coolweather.gofun.R;
import com.coolweather.gofun.bean.User;
import com.coolweather.gofun.config.Config;
import com.coolweather.gofun.util.OkhttpUtil;
import com.coolweather.gofun.util.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
                    //CheckBox?????????????????????
                    register_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    checkBox.setBackgroundResource(R.drawable.icon_kejian);
                } else {
                    //CheckBox???????????????????????????
                    register_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    checkBox.setBackgroundResource(R.drawable.icon_bukejian);
                }
                //?????????????????????
                register_password.setSelection(register_password.getText().toString().length());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.accountRegistern_toRegister:
                uerAccountRegister();//??????
                break;

        }
    }

    private void uerAccountRegister() {
        final String username = register_name.getText().toString();
        final String password = register_password.getText().toString();
        final String email = register_email.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, "?????????????????????");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "??????????????????");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "??????????????????");
            return;
        }

        if (!isMail(email)){
            ToastUtils.show(this,"??????????????????");
            return;
        }

        final String url = Config.BASE_URL + "User/userSignUp";

        User user = new User(username,password,email);
        String value = JSON.toJSONString(user);
        OkhttpUtil.requestpostone(url, value, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("RegisterActiivty","??????????????????");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("kwwl", "?????????????????????");
                Log.d("kwwl", "response.code()==" + response.code());
                Log.d("kwwl", "response ==" + response);
                Log.d("kwwl", "response.url==" + response.request().url());
                Log.d("kwwl", "response.body().string()==" + response.body().string());
                if(response.code() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                            User user = new User(username,password,email);
                            Intent intent = new Intent();
                            intent.putExtra("user",user);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                }else if(response.code() == 400){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * ??????????????????
     */
    public static boolean isMail(String str) {
        boolean flag = false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(str);
        if(m.matches())
            flag = true;
        else
            flag = false;
        return flag;
    }

}
