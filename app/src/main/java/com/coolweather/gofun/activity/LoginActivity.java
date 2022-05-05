package com.coolweather.gofun.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.gofun.BaseActivity;
import com.coolweather.gofun.Guidance.GuidanceActivity;
import com.coolweather.gofun.R;
import com.coolweather.gofun.config.Config;
import com.coolweather.gofun.util.ToastUtils;
import com.coolweather.gofun.util.OkhttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText accountLoginName;
    private EditText accountLoginPassword;
    private Button loginBtn, viewpagerbutton,guidance;
    private TextView registerAccountBtn;
    private ProgressBar progressBar;
    private LinearLayout llLogin;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        accountLoginName = (EditText) findViewById(R.id.i8_accountLogin_name);
        accountLoginPassword = (EditText) findViewById(R.id.i8_accountLogin_password);
        loginBtn = (Button) findViewById(R.id.i8_accountLogin_toLogin);
        viewpagerbutton = (Button) findViewById(R.id.viewpager);
        registerAccountBtn = (TextView) findViewById(R.id.register_account_btn);
        guidance = findViewById(R.id.guidance);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        llLogin = (LinearLayout) findViewById(R.id.ll_login);
        checkBox = (CheckBox) findViewById(R.id.login_check);
        registerAccountBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        registerAccountBtn.getPaint().setAntiAlias(true);//抗锯齿
        accountLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        checkBox.setBackgroundResource(R.drawable.icon_bukejian);
    }

    private void initData() {

    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        llLogin.setVisibility(View.GONE);
        viewpagerbutton.setVisibility(View.GONE);
    }

    private void hiddenProgressBar() {
        progressBar.setVisibility(View.GONE);
        llLogin.setVisibility(View.VISIBLE);
        viewpagerbutton.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        loginBtn.setOnClickListener(this);
        registerAccountBtn.setOnClickListener(this);
        viewpagerbutton.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //CheckBox选中，显示明文
                    accountLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    checkBox.setBackgroundResource(R.drawable.icon_kejian);
                } else {
                    //CheckBox取消选中，显示暗文
                    accountLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    checkBox.setBackgroundResource(R.drawable.icon_bukejian);
                }
                //光标移至最末端
                accountLoginPassword.setSelection(accountLoginPassword.getText().toString().length());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.i8_accountLogin_toLogin:
                uerAccountLogin();//登录
                break;
            case R.id.register_account_btn:
                //跳转到注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.viewpager:
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.guidance:
                Intent guidance = new Intent(LoginActivity.this, GuidanceActivity.class);
                startActivity(guidance);
                break;
            default:
                break;
        }
    }

    private void uerAccountLogin() {
        final String accountName = accountLoginName.getText().toString().trim();//账号
        final String accountPassword = accountLoginPassword.getText().toString().trim();//密码

        if (TextUtils.isEmpty(accountName)) {
            ToastUtils.show(this, "用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(accountPassword)) {
            ToastUtils.show(this, "密码不能为空");
            return;
        }

        final String url = Config.BASE_URL + "login1";
        Map<String,String> map = new HashMap<>();
        map.put("username",accountName);
        map.put("password",accountPassword);
        OkhttpUtil.requestpostone(url,map,new Callback(){
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull final Response response) throws IOException {
                boolean flag = false;
                Log.d("kwwl", "获取数据成功了");
                Log.d("kwwl", "response.code()==" + response.code());
                Log.d("kwwl", "response ==" + response);
                Log.d("kwwl", "response.url==" + response.request().url());
                Log.d("kwwl", "response.body().string()==" + response.body().string());
                if(response.code() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Headers headers = response.headers();
                            List<String> cookies = headers.values("Set-Cookie");

                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("kwwl", "获取数据失败了");
            }
        });
    }

}