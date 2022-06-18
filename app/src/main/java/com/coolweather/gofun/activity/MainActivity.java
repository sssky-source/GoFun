package com.coolweather.gofun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.coolweather.gofun.BaseActivity;
import com.coolweather.gofun.CustomViewPager;
import com.coolweather.gofun.R;
import com.coolweather.gofun.adapter.MyFragmentPagerAdapter;
import com.coolweather.gofun.LocalDb.PersonLitePal;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;

import org.litepal.LitePal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, CustomViewPager.OnPageChangeListener {

    private RadioGroup rg_all;
    private RadioButton rb_map;
    private RadioButton rb_recommand;
    private RadioButton rb_message;
    private RadioButton rb_mine;
    private CustomViewPager vpager;
    private ImageView imageView;
    private String token;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    //代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Log.d("111","create");

        //获取数据库
        LitePal.getDatabase();
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        //在主活动获取用户个人信息
        PersonService personService = HttpRequest.create(PersonService.class);
        initView();
        //请求个人信息
        requestPersonInfo(personService);
        rb_map.setChecked(true);
    }

    private void requestPersonInfo(PersonService personService) {
        personService.getUserInfo("Bearer " + token).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                Log.d("111",token);
                Person person = response.body();
                Log.d("111","response" + response);
                Log.d("111","response" + response.code());
                Log.d("111","response" + response.body());
                PersonLitePal person_LitePal = new PersonLitePal();

                //要保证已经保存完成在查询数据库
                //因为LitePal的原因 会将ID 默认作为主键，导致用户id不能保存下来
                //所以另外建立了一个Bean id作为主键 用户ID写为userID
                /*
                    LitePal不支持自定义主键，默认的主键为id,不管一个实体类对象有没有设置id字段，
                    数据库的表中都会创建一个id的主键，而这个id的值会在新记录插入时被自动置为表中的Id，
                    也即是唯一值。如果你里面定义了个String id，运行会报错的。
                 */
                if (person != null){
                    person_LitePal.setUserID(person.getId());
                    person_LitePal.setEmail(person.getEmail());
                    person_LitePal.setUsername(person.getUsername());
                    person_LitePal.setImage(person.getImage());
                    person_LitePal.setSex(person.getSex());
                    person_LitePal.setAge(person.getAge());
                    person_LitePal.setBrief(person.getBrief());
                    person_LitePal.setX(person.getX());
                    person_LitePal.setY(person.getY());
                    person_LitePal.setHobby(person.getHobby());
                    person_LitePal.setLocation(person.getLocation());
                    person_LitePal.setToken(token);
                    Log.d("111","Ok request");
                    //存入数据库
                    person_LitePal.save();
                }else {
                    Log.d("111","not request");
                }
            }
            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initView() {
        vpager = findViewById(R.id.fragment_container);
        rg_all = findViewById(R.id.radioGroup);
        rb_map = findViewById(R.id.rb_location);
        rb_recommand = findViewById(R.id.rb_recommand);
        rb_message = findViewById(R.id.rb_message);
        rb_mine = findViewById(R.id.rb_mine);
        imageView = findViewById(R.id.rbAdd);
        rg_all.setOnCheckedChangeListener(this);
        vpager.setAdapter(myFragmentPagerAdapter);
        vpager.addOnPageChangeListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_location:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_recommand:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_mine:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
            default:
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int state) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        switch (vpager.getCurrentItem()) {
            case PAGE_ONE:
                rb_map.setChecked(true);
                break;
            case PAGE_TWO:
                rb_recommand.setChecked(true);
                break;
            case PAGE_THREE:
                rb_message.setChecked(true);
                break;
            case PAGE_FOUR:
                rb_mine.setChecked(true);
                break;
        }
    }
}
