package com.coolweather.gofun.activity;

import androidx.annotation.NonNull;
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
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.LitPalUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.bean.User;
import com.coolweather.gofun.config.Config;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Mine.bean.UserTag;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.OkhttpUtil;
import com.coolweather.gofun.util.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.text.InputType.*;

/**

 * @Author : CYT

 * @Time : On 2022/4/29

 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText register_name,register_password,register_email;
    private Button registerBtn;
    private CheckBox checkBox;
    private PersonService personService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        personService = HttpRequest.create(PersonService.class);
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
                break;

        }
    }

    private void uerAccountRegister() {
        final String username = register_name.getText().toString();
        final String password = register_password.getText().toString();
        final String email = register_email.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, "用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "邮箱不能为空");
            return;
        }

        if (!isMail(email)){
            ToastUtils.show(this,"邮箱格式错误");
            return;
        }

        final String url = Config.BASE_URL + "User/userSignUp";

        User user = new User(username,password,email);
        String value = JSON.toJSONString(user);
        OkhttpUtil.requestpostone(url, value, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("RegisterActiivty","获取数据失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("kwwl", "获取数据成功了");
                Log.d("kwwl", "response.code()==" + response.code());
                Log.d("kwwl", "response ==" + response);
                Log.d("kwwl", "response.url==" + response.request().url());
                Log.d("kwwl", "response.body().string()==" + response.body().string());
                if(response.code() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
//                            User user = new User(username,password,email);
//                            Intent intent = new Intent();
//                            intent.putExtra("user",user);
//                            setResult(RESULT_OK,intent);
//                            finish();

                            final String url = Config.BASE_URL + "User/login";

                            User login = new User();
                            Log.d("test","email:" + email);
                            Log.d("test","password:" + password);
                            login.setPassword(password);
                            login.setEmail(email);
                            //String value = JSON.toJSONString(login);
                            //Log.d("test","value:" + value);

//                            OkhttpUtil.requestpostone(url, value, new Callback() {
//                                @Override
//                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                    Log.d("test", "获取数据失败了");
//                                }
//
//                                @Override
//                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//
//                                    Log.d("test", "1" + response);
//                                    Log.d("test", "2" + response.headers());
//                                    Log.d("test", "3" + response.request());
//                                    Log.d("test", "4" + response.networkResponse());
//                                    Log.d("test", "code:" + response.code());
//                                    Log.d("test","" + response.request().body());
//
//                                }
//                            });
                            /**
                             * okhttp3的方式post 需要转成json格式
                             * retrofit2方式post 不需要转成json格式？？？
                             */
                            personService.login(login).enqueue(new retrofit2.Callback<ResponseBody>() {
                                @Override
                                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    String token = null;
                                    Log.d("test",":" + response);
                                    Log.d("test","body:" + response.body());
                                    Log.d("test","code: " + response.code());
                                    Log.d("test","message:" + response.message());
                                    Log.d("test","message:" + response.errorBody());
                                    Log.d("test","call.request:" + call.request());
                                    try {
                                        token = response.body().string().trim();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    GoFunApplication.token = token;
                                    //GoFunApplication.token = response.body().string().trim();
                                    requestPersonInfo(personService,token,password);
                                    Intent intent = new Intent(RegisterActivity.this,TagActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                                    Log.d("test","t.getCause:" + t.getCause());
                                    t.printStackTrace();
                                }
                            });
                        }
                    });
                }else if(response.code() == 400){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"用户名已经存在",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void requestPersonInfo(PersonService personService, String token, String password) {

        personService.getUserInfo("Bearer " + token).enqueue(new retrofit2.Callback<Person>() {
            @Override
            public void onResponse(retrofit2.Call<Person> call, retrofit2.Response<Person> response) {
                Log.d("111",token);
                Person person = response.body();

                //要保证已经保存完成在查询数据库
                //因为LitePal的原因 会将ID 默认作为主键，导致用户id不能保存下来
                //所以另外建立了一个Bean id作为主键 用户ID写为userID
                /*
                    LitePal不支持自定义主键，默认的主键为id,不管一个实体类对象有没有设置id字段，
                    数据库的表中都会创建一个id的主键，而这个id的值会在新记录插入时被自动置为表中的Id，
                    也即是唯一值。如果你里面定义了个String id，运行会报错的。
                 */
                if (person != null){
                    LitPalUtil.setUserInfo(person,token,password);
                }else {
                    Log.d("111","not request");
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 邮箱格式校验
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
