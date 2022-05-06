package com.coolweather.gofun.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.alibaba.fastjson.JSON;
import com.coolweather.gofun.Animation.SgfSplash6Activity;
import com.coolweather.gofun.BaseActivity;
import com.coolweather.gofun.LocalDb.MyDatabaseHelper;
import com.coolweather.gofun.R;
import com.coolweather.gofun.bean.User;
import com.coolweather.gofun.config.Config;
import com.coolweather.gofun.util.ToastUtils;
import com.coolweather.gofun.util.OkhttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText accountLoginName;
    private EditText accountLoginPassword;
    private Button loginBtn, viewpagerbutton, guidance;
    private TextView registerAccountBtn;
    private ProgressBar progressBar;
    private LinearLayout llLogin;
    private CheckBox checkBox;
    private MyDatabaseHelper dbhelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    private void initView() {
        accountLoginName = (EditText) findViewById(R.id.i8_accountLogin_name);
        accountLoginPassword = (EditText) findViewById(R.id.i8_accountLogin_password);
        loginBtn = (Button) findViewById(R.id.i8_accountLogin_toLogin);
        viewpagerbutton = (Button) findViewById(R.id.viewpager);
        guidance = findViewById(R.id.guidance);
        registerAccountBtn = (TextView) findViewById(R.id.register_account_btn);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        llLogin = (LinearLayout) findViewById(R.id.ll_login);
        checkBox = (CheckBox) findViewById(R.id.login_check);
        registerAccountBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        registerAccountBtn.getPaint().setAntiAlias(true);//抗锯齿
        accountLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        checkBox.setBackgroundResource(R.drawable.icon_bukejian);
        //通过构造函数将参数将数据库名指定为UserList.db，版本为1
        dbhelper = new MyDatabaseHelper(this, "UserList.db", null, 1);
        //进入活动运行到这一步后检测到没有数据库就会创建
        db = dbhelper.getWritableDatabase();
        if (searchUser() != null) {
            User user = searchUser();
            accountLoginName.setText(user.getUsername());
            accountLoginPassword.setText(user.getPassword());
        }
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
        guidance.setOnClickListener(this);
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
                startActivityForResult(intent, 1);
                break;
            case R.id.viewpager:
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.guidance:
                Intent guidance = new Intent(LoginActivity.this, SgfSplash6Activity.class);
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

        final String url = Config.BASE_URL + "User/login";
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8"); //设置格式
//        Map<String,String> map = new HashMap<>();
//        map.put("username",accountName);
//        map.put("password",accountPassword);
        User user = new User();
        user.setEmail(accountName);
        user.setPassword(accountPassword);

        String value = JSON.toJSONString(user);
        RequestBody requestBody = RequestBody.create(mediaType, value);

        OkhttpUtil.requestpostone(url, requestBody, new Callback() {

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                boolean flag = false;
                Log.d("kwwl", "获取数据成功了");
                Log.d("kwwl", "response.code()==" + response.code());
                Log.d("kwwl", "response ==" + response);
                Log.d("kwwl","token:" + response.body().string());

                String token = response.body().string();
                if (response.code() == 200) {
//                    Headers headers = response.headers();
//                    Log.d("kww1", "headers" + headers);
//                    List<String> cookies = headers.values("set-Cookies");
//                    String session = cookies.get(0);
//                    Log.d("kww1", "onResponse-size" + cookies);
//                    String s = session.substring(0, session.indexOf(";"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                            rememberUser(accountName,accountPassword,token);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    User user = (User) data.getSerializableExtra("user");
                    accountLoginName.setText(user.getUsername());
                    accountLoginPassword.setText(user.getPassword());
//                    Log.d("LoginActivity", user.getEmail());
                }
                break;
            default:
        }
    }

    private void rememberUser(String username, String password,String token) {
        db = dbhelper.getWritableDatabase();
        db.execSQL("delete from UserTable");
        ContentValues values = new ContentValues();
        //put(属性名，属性值) put("username","小明")；
        values.put("username", username);
        values.put("password", password);
        values.put("token",token);
        db.insert("UserTable", null, values);  //将数据插入数据库
        values.clear();
        db.close();
    }

    private User searchUser() {
      /*Cursor是相当于一个游标，便利表中没个条目，然后获取不同属性，找到与自己查找相符合
          的属性就将此条提取出来
      */
        User user = null;
        Cursor cursor = db.query("UserTable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Integer ID = Integer.valueOf(cursor.getString(cursor.getColumnIndex("id")));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                user = new User(username, password);

            } while (cursor.moveToNext());
        }
        cursor.close();
        //  db.close();
        return user;
    }

}