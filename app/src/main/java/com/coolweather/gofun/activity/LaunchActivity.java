package com.coolweather.gofun.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.LitPalUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.bean.AddActivityItem;
import com.coolweather.gofun.datepicker.CustomDatePicker;
import com.coolweather.gofun.datepicker.DateFormatUtils;
import com.coolweather.gofun.fragment.Recommend.Adapter.FragmentAdapter;
import com.coolweather.gofun.fragment.Recommend.RecommendItemFragment;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.RecommendService;
import com.coolweather.gofun.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener, AMapLocationListener,
        LocationSource,AMap.OnMapClickListener,GeocodeSearch.OnGeocodeSearchListener{

    private TextView mTvSelectedDate, mTvSelectedTime,mTvSelectedTimeend;
    private EditText numofpeople,theme,introduce;
    private AutoCompleteTextView autoCompleteTextView;
    private CustomDatePicker mDatePicker, mTimerPicker,mTimerPickerone;
    private MapView mapView;
    private int snumofpeople;
    private String stheme;
    private String  starttime = String.valueOf(System.currentTimeMillis());
    private String entime = String.valueOf(System.currentTimeMillis());
    private Spinner spinner;
    private List<String> data_list = new ArrayList<>();
    private ArrayAdapter<String> arr_adapter;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //地图控制器
    private AMap aMap = null;
    //位置更改监听
    private OnLocationChangedListener mListener;
    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //地理编码搜索
    private GeocodeSearch geocodeSearch;
    //解析成功标识码
    private static final int PARSE_SUCCESS_CODE = 1000;
    private String city;
    private String str;
    private List<Tip> autoTips;
    private boolean isfirstinput = true;
    private AddActivityItem addActivityItem = new AddActivityItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this,true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getActivityType();
        findViewById(R.id.ll_date).setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date);
        mTvSelectedDate.setClickable(false);
        initDatePicker();
        findViewById(R.id.ll_starttime).setOnClickListener(this);
        findViewById(R.id.ll_endtime).setOnClickListener(this);
        findViewById(R.id.bt_order).setOnClickListener(this);
        autoCompleteTextView = findViewById(R.id.tv_selected_position);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        mTvSelectedTimeend = findViewById(R.id.tv_selected_endtime);
        numofpeople = findViewById(R.id.ed_number);
        theme = findViewById(R.id.ed_theme);
        introduce = findViewById(R.id.ed_introduce);
        introduce.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    //通知父控件不要干扰
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    //通知父控件不要干扰
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        initTimerPicker();
        initLocation();
        initMap(savedInstanceState);
        mLocationClient.startLocation();
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {

                    // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                    GeocodeQuery query = new GeocodeQuery(newText,city);
                    geocodeSearch.getFromLocationNameAsyn(query);
                    InputtipsQuery inputquery = new InputtipsQuery(newText, city);
                    Inputtips inputTips = new Inputtips(LaunchActivity.this, inputquery);
                    inputquery.setCityLimit(true);
                    inputTips.setInputtipsListener(inputtipsListener);
                    inputTips.requestInputtipsAsyn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    Inputtips.InputtipsListener inputtipsListener = new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> list, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
                autoTips = list;
                List<String> listString = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    listString.add(list.get(i).getName());
                }
                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.route_inputs, listString);
                autoCompleteTextView.setAdapter(aAdapter);
                aAdapter.notifyDataSetChanged();
                if (isfirstinput) {
                    isfirstinput = false;
                    autoCompleteTextView.showDropDown();
                }
            } else {
                Toast.makeText(LaunchActivity.this, "erroCode " + rCode , Toast.LENGTH_SHORT).show();
            }
        }
    };



    private void initSpinner(List<Activity> list) {
        spinner=(Spinner)findViewById(R.id.ed_spinner);
        str=(String)spinner.getSelectedItem();
        for (int i = 0; i < list.size(); i++){
            data_list.add(list.get(i).getType1());
        }
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
                addActivityItem.setType(position);
                Log.d("item",str + position);
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
                String address = autoCompleteTextView.getText().toString();
                if(address.length() == 0){
                    Log.d("autoCompleteTextView11","autoCompleteTextView" + autoCompleteTextView.getText().toString());
                    ToastUtils.show(LaunchActivity.this,"地址不能为空");
                    break;
                }
                if(Long.parseLong(starttime) >= Long.parseLong(entime)){
                    ToastUtils.show(LaunchActivity.this,"开始时间不能大于等于截止时间");
                    break;
                }
                if(introduce.getText().length() == 0){
                    ToastUtils.show(LaunchActivity.this,"活动简介不能为空");
                    break;
                }
                if(numofpeople.getText().toString().length() == 0 ||theme.getText().toString().length() == 0){
                    ToastUtils.show(LaunchActivity.this,"主题和人数不能为空");
                }
                else {
                    snumofpeople = Integer.parseInt(numofpeople.getText().toString().trim());
                    stheme = theme.getText().toString().trim();

                    addActivityItem.setLocation(address);
                    addActivityItem.setTitle(stheme);
                    addActivityItem.setMaxnumber(snumofpeople);
                    addActivityItem.setIntroduction(introduce.getText().toString().trim());
                    Log.d("item",addActivityItem.getEndtime());
                    Log.d("item",addActivityItem.getType().toString());
                    Log.d("item",String.valueOf(addActivityItem.getX()));
                    addActivity(addActivityItem);
                    finish();
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

    private List<Activity> getActivityType(){
        RecommendService recommendService = HttpRequest.create(RecommendService.class);
        recommendService.getActivityType("Bearer " + GoFunApplication.token).enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(@NonNull Call<List<Activity>> call, @NonNull Response<List<Activity>> response) {
                List<Activity> list = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initSpinner(list);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Activity>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return null;
    }

    /*
    创建活动
    */
    private void addActivity(AddActivityItem activityItem) {
        RecommendService recommendService = HttpRequest.create(RecommendService.class);
        Log.d("tokenn22",GoFunApplication.token);
        Log.d("tokenn11",LitPalUtil.getPersonInfo().getToken());
        recommendService.addActivity("Bearer " + GoFunApplication.token,activityItem).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("item","response " + response.code());
                Log.d("item","response " + response.body().toString());
                if(response.code() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(LaunchActivity.this,"发起活动成功");
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(LaunchActivity.this,"发起活动失败");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy();
        mapView.onDestroy();
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
                Log.d("time",DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        mTvSelectedTime.setText(endTime);
        mTvSelectedTimeend.setText(endTime);
        addActivityItem.setStarttime(beginTime.replace(" ","T"));
        addActivityItem.setEndtime(endTime.replace(" ","T"));
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                starttime = String.valueOf(timestamp);
                addActivityItem.setStarttime(DateFormatUtils.long2Str(timestamp, true).replace(" ","T"));
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPickerone = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                entime = String.valueOf(timestamp);
                addActivityItem.setEndtime(DateFormatUtils.long2Str(timestamp, true).replace(" ","T"));
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

    /**
     * 初始化地图
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();

        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapClickListener(this);
        try {
            geocodeSearch = new GeocodeSearch(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                city = aMapLocation.getCity();
                Log.d("Map","Map" + city);
                mLocationClient.stopLocation();
                if(mListener != null){
                    mListener.onLocationChanged(aMapLocation);
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }



    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制，高精度定位会产生缓存。
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

    }

    /**
     * 地图单击事件
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        //showMsg("点击了地图，经度："+latLng.longitude+"，纬度："+latLng.latitude);
        latlonToAddress(latLng);
    }


    /**
     * 坐标转地址
     * @param regeocodeResult
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        //解析result获取地址描述信息
        if(rCode == PARSE_SUCCESS_CODE){
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            //显示解析后的地址
            ToastUtils.show(this,"地址："+regeocodeAddress.getFormatAddress());
        }else {
         //   showMsg("获取地址失败");
        }

    }


    /**
     * 地址转坐标
     * @param geocodeResult
     * @param rCode
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == PARSE_SUCCESS_CODE) {
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            if(geocodeAddressList!=null && geocodeAddressList.size()>0){
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                //显示解析后的坐标
                Log.d("add","坐标：" + latLonPoint.getLongitude()+"，"+latLonPoint.getLatitude());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addActivityItem.setX(latLonPoint.getLongitude());
                        addActivityItem.setY(latLonPoint.getLatitude());
                    }
                });
            }

        } else {
            ToastUtils.show(LaunchActivity.this,"地址获取失败");
        }
    }


    /**
     * 通过经纬度获取地址
     * @param latLng
     */
    private void latlonToAddress(LatLng latLng) {
        //位置点  通过经纬度进行构建
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        //逆编码查询  第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);
        //异步获取地址信息
        geocodeSearch.getFromLocationAsyn(query);
    }


}
