package com.coolweather.gofun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.coolweather.gofun.R;
import com.coolweather.gofun.datepicker.CustomDatePicker;
import com.coolweather.gofun.datepicker.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTvSelectedDate, mTvSelectedTime,mTvSelectedTimeend;
    private EditText numofpeople,theme;
    private CustomDatePicker mDatePicker, mTimerPicker,mTimerPickerone;

    private int snumofpeople;
    private String stheme;
    private String  starttime = String.valueOf(System.currentTimeMillis());
    private String entime = String.valueOf(System.currentTimeMillis());
    private Spinner spinner;
    private List<String> data_list = new ArrayList<>();
    private ArrayAdapter<String> arr_adapter;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initSpinner();
        findViewById(R.id.ll_date).setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date);
        mTvSelectedDate.setClickable(false);
        initDatePicker();
        findViewById(R.id.ll_starttime).setOnClickListener(this);
        findViewById(R.id.ll_endtime).setOnClickListener(this);
        findViewById(R.id.bt_order).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        mTvSelectedTimeend = findViewById(R.id.tv_selected_endtime);
        numofpeople = findViewById(R.id.ed_number);
        theme = findViewById(R.id.ed_theme);

        initTimerPicker();
    }

    private void initSpinner() {
        spinner=(Spinner)findViewById(R.id.ed_spinner);
        str=(String)spinner.getSelectedItem();
        data_list.add("任意");
        data_list.add("圆形");
        data_list.add("教室型");
        data_list.add("方框型");
        data_list.add("主席台U型");
        arr_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //拿到被选择项的值
                str = (String) spinner.getSelectedItem();
                TextView tv = (TextView)view;
                tv.setTextColor(R.color.selected_time_text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                // 日期格式为yyyy-MM-dd
                //    mDatePicker.show(mTvSelectedDate.getText().toString());

                break;

            case R.id.ll_starttime:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
            case R.id.ll_endtime:
                mTimerPickerone.show(mTvSelectedTimeend.getText().toString());
                break;
            case R.id.bt_order:
                if(Long.parseLong(starttime) >= Long.parseLong(entime)){
                    Toast.makeText(LaunchActivity.this,"开始时间不能大于等于截止时间",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(numofpeople.getText().toString().length() == 0 ||theme.getText().toString().length() == 0){
                    Toast.makeText(LaunchActivity.this,"主题和人数不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    snumofpeople = Integer.parseInt(numofpeople.getText().toString().trim());
                    stheme = theme.getText().toString().trim();
//                    Intent intent = new Intent(LaunchActivity.this,FreeroomActivity.class);
//                    Order order = new Order();
//                    order.setIdofpeople(BmobUser.getCurrentUser(BmobUser.class).getUsername());
//                    order.setNumofpeople(snumofpeople);
//                    order.setTstart(starttime);
//                    order.setTend(entime);
//                    order.setTheme(stheme);
//                    Log.d("Frees",str);
//                    order.setState(0);
//                    intent.putExtra("typeofroom",str);
//                    intent.putExtra("order",order);
//                    startActivity(intent);
                }
                break;

            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mTvSelectedDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(true);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        mTvSelectedTime.setText(endTime);
        mTvSelectedTimeend.setText(endTime);
        Log.d("SubscribeActivity",endTime);
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                starttime = String.valueOf(timestamp);
                Log.d("Freeeee",starttime);
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPickerone = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                entime = String.valueOf(timestamp);
                mTvSelectedTimeend.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        mTimerPickerone.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        mTimerPickerone.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        mTimerPickerone.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
        mTimerPickerone.setCanShowAnim(true);
    }


}
