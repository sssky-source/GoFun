package com.coolweather.gofun.activity;

import android.content.Intent;
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
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.LitPalUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.LocalDb.PersonLitePal;
import com.coolweather.gofun.bean.User;
import com.coolweather.gofun.config.Config;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.OkhttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText accountLoginName;
    private EditText accountLoginPassword;
    private Button loginBtn, viewpagerbutton,guidance;
    private TextView registerAccountBtn;
    private ProgressBar progressBar;
    private LinearLayout llLogin;
    private CheckBox checkBox;


    private CircleImageView QQ_Login,V_Login;
    SQLiteDatabase db;
    private PersonService personService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    private void initView() {
        personService = HttpRequest.create(PersonService.class);
        accountLoginName = (EditText) findViewById(R.id.i8_accountLogin_name);
        accountLoginPassword = (EditText) findViewById(R.id.i8_accountLogin_password);
        loginBtn = (Button) findViewById(R.id.i8_accountLogin_toLogin);
        viewpagerbutton = (Button) findViewById(R.id.viewpager);
        guidance = findViewById(R.id.guidance);
        registerAccountBtn = (TextView) findViewById(R.id.register_account_btn);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        llLogin = (LinearLayout) findViewById(R.id.ll_login);
        checkBox = (CheckBox) findViewById(R.id.login_check);
        QQ_Login = findViewById(R.id.QQ_Login);
        V_Login = findViewById(R.id.V_Login);
        registerAccountBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        registerAccountBtn.getPaint().setAntiAlias(true);//抗锯齿
        accountLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        checkBox.setBackgroundResource(R.drawable.icon_bukejian);
        Log.d("TOKEN222", GoFunApplication.getToken());
        if(searchUser()!=null){
            accountLoginName.setText(searchUser().getEmail());
            accountLoginPassword.setText(searchUser().getPassword());
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
        QQ_Login.setOnClickListener(this);
        V_Login.setOnClickListener(this);
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
                //startActivityForResult(intent,1);
                startActivity(intent);
                break;
            case R.id.viewpager:
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.guidance:
                Intent guidance = new Intent(LoginActivity.this, SgfSplash6Activity.class);
                startActivity(guidance);
                break;
            case R.id.QQ_Login:
            case R.id.V_Login:
              //  ToastUtils.show(this,"敬请期待");
                break;
            default:
                break;
        }
    }

    private void uerAccountLogin() {
        final String accountName = accountLoginName.getText().toString().trim();//账号
        final String accountPassword = accountLoginPassword.getText().toString().trim();//密码

        if (TextUtils.isEmpty(accountName)) {
          //  ToastUtils.show(this, "用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(accountPassword)) {
           // ToastUtils.show(this, "密码不能为空");
            return;
        }

        final String url = Config.BASE_URL + "User/login";

        User user = new User();
        user.setEmail(accountName);
        user.setPassword(accountPassword);

        String value = JSON.toJSONString(user);

        OkhttpUtil.requestpostone(url, value, new Callback() {

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                boolean flag = false;
                Log.d("kwwl", "获取数据成功了");
                Log.d("kwwl", "response.code()==" + response.code());
                Log.d("kwwl", "response ==" + response);

                String token = response.body().string().trim();
                Log.d("tokenn", "login中的token" + token );
                if (response.code() == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //请求个人信息
                            GoFunApplication.token = token;
                            requestPersonInfo(personService,token,accountPassword);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("token",token);
                            startActivity(intent);
                            finish();
                        }

                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    User user = (User)data.getSerializableExtra("user");
//                    accountLoginName.setText(user.getUsername());
//                    accountLoginPassword.setText(user.getPassword());
//                }
//                break;
//            default:
//        }
//    }


    private PersonLitePal searchUser(){
        PersonLitePal personLitePal = null;
        if(LitPalUtil.getPersonInfo() != null){
            personLitePal  = LitPalUtil.getPersonInfo();
        }else {
            Log.d("login","1111111");
        }
        return personLitePal;
    }

    /*
    登陆成功的话获取用户的全部信息，存入数据库
    */
    private void requestPersonInfo(PersonService personService,String token,String password) {
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

    public void Start(View view){
        Toast.makeText(view.getContext(),"暂不支持此功能",Toast.LENGTH_SHORT).show();
    }

}