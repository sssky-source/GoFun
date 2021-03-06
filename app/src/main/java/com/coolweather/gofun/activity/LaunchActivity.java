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
    //??????AMapLocationClient?????????
    public AMapLocationClient mLocationClient = null;
    //??????AMapLocationClientOption??????
    public AMapLocationClientOption mLocationOption = null;
    //???????????????
    private AMap aMap = null;
    //??????????????????
    private OnLocationChangedListener mListener;
    //???????????????
    private static final int REQUEST_PERMISSIONS = 9527;
    //??????????????????
    private GeocodeSearch geocodeSearch;
    //?????????????????????
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
                    //???????????????????????????
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    //???????????????????????????
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

                    // name??????????????????????????????????????????????????????????????????????????????citycode???adcode
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
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// ????????????
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
        //???????????????
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //????????????????????????
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
                // ???????????????yyyy-MM-dd
                //    mDatePicker.show(mTvSelectedDate.getText().toString());

                break;

            case R.id.ll_starttime:
                // ???????????????yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
            case R.id.ll_endtime:
                mTimerPickerone.show(mTvSelectedTimeend.getText().toString());
                break;
            case R.id.bt_order:
                String address = autoCompleteTextView.getText().toString();
                if(address.length() == 0){
                    Log.d("autoCompleteTextView11","autoCompleteTextView" + autoCompleteTextView.getText().toString());
                    ToastUtils.show(LaunchActivity.this,"??????????????????");
                    break;
                }
                if(Long.parseLong(starttime) >= Long.parseLong(entime)){
                    ToastUtils.show(LaunchActivity.this,"??????????????????????????????????????????");
                    break;
                }
                if(introduce.getText().length() == 0){
                    ToastUtils.show(LaunchActivity.this,"????????????????????????");
                    break;
                }
                if(numofpeople.getText().toString().length() == 0 ||theme.getText().toString().length() == 0){
                    ToastUtils.show(LaunchActivity.this,"???????????????????????????");
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
    ????????????
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
                            ToastUtils.show(LaunchActivity.this,"??????????????????");
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(LaunchActivity.this,"??????????????????");
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
        //???activity??????onResume?????????mMapView.onResume ()???????????????????????????
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView.onPause ()????????????????????????
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)??????????????????????????????
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
        //?????????????????????????????????????????????????????????
        mLocationClient.onDestroy();
        mapView.onDestroy();
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mTvSelectedDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // ?????????????????????????????????????????????
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
                Log.d("time",DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // ?????????????????????????????????????????????
        mDatePicker.setCancelable(false);
        // ??????????????????
        mDatePicker.setCanShowPreciseTime(false);
        // ?????????????????????
        mDatePicker.setScrollLoop(false);
        // ?????????????????????
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        mTvSelectedTime.setText(endTime);
        mTvSelectedTimeend.setText(endTime);
        addActivityItem.setStarttime(beginTime.replace(" ","T"));
        addActivityItem.setEndtime(endTime.replace(" ","T"));
        // ??????????????????????????????????????????????????????yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                starttime = String.valueOf(timestamp);
                addActivityItem.setStarttime(DateFormatUtils.long2Str(timestamp, true).replace(" ","T"));
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // ??????????????????????????????????????????????????????yyyy-MM-dd HH:mm
        mTimerPickerone = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                entime = String.valueOf(timestamp);
                addActivityItem.setEndtime(DateFormatUtils.long2Str(timestamp, true).replace(" ","T"));
                mTvSelectedTimeend.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // ??????????????????????????????????????????
        mTimerPicker.setCancelable(true);
        mTimerPickerone.setCancelable(true);
        // ???????????????
        mTimerPicker.setCanShowPreciseTime(true);
        mTimerPickerone.setCanShowPreciseTime(true);
        // ??????????????????
        mTimerPicker.setScrollLoop(true);
        mTimerPickerone.setScrollLoop(true);
        // ??????????????????
        mTimerPicker.setCanShowAnim(true);
        mTimerPickerone.setCanShowAnim(true);
    }

    /**
     * ???????????????
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map_view);
        //???activity??????onCreate?????????mMapView.onCreate(savedInstanceState)???????????????
        mapView.onCreate(savedInstanceState);
        //??????????????????????????????
        aMap = mapView.getMap();

        // ??????????????????
        aMap.setLocationSource(this);
        // ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
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
     * ????????????
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//????????????
        }
    }

    /**
     * ????????????
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
     * ?????????????????????????????????
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
                //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }



    /**
     * ???????????????
     */
    private void initLocation() {
        //???????????????
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //????????????????????????
        mLocationClient.setLocationListener(this);
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        //?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //????????????3s???????????????????????????????????????
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        //????????????????????????????????????????????????????????????
        mLocationOption.setNeedAddress(true);
        //?????????????????????????????????????????????????????????30000???????????????????????????????????????8000?????????
        mLocationOption.setHttpTimeOut(20000);
        //??????????????????????????????????????????????????????
        mLocationOption.setLocationCacheEnable(false);
        //??????????????????????????????????????????
        mLocationClient.setLocationOption(mLocationOption);

    }

    /**
     * ??????????????????
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        //showMsg("???????????????????????????"+latLng.longitude+"????????????"+latLng.latitude);
        latlonToAddress(latLng);
    }


    /**
     * ???????????????
     * @param regeocodeResult
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        //??????result????????????????????????
        if(rCode == PARSE_SUCCESS_CODE){
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            //????????????????????????
            ToastUtils.show(this,"?????????"+regeocodeAddress.getFormatAddress());
        }else {
         //   showMsg("??????????????????");
        }

    }


    /**
     * ???????????????
     * @param geocodeResult
     * @param rCode
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == PARSE_SUCCESS_CODE) {
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            if(geocodeAddressList!=null && geocodeAddressList.size()>0){
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                //????????????????????????
                Log.d("add","?????????" + latLonPoint.getLongitude()+"???"+latLonPoint.getLatitude());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addActivityItem.setX(latLonPoint.getLongitude());
                        addActivityItem.setY(latLonPoint.getLatitude());
                    }
                });
            }

        } else {
            ToastUtils.show(LaunchActivity.this,"??????????????????");
        }
    }


    /**
     * ???????????????????????????
     * @param latLng
     */
    private void latlonToAddress(LatLng latLng) {
        //?????????  ???????????????????????????
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        //???????????????  ???????????????????????????Latlng????????????????????????????????????????????????????????????????????????????????????GPS???????????????
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);
        //????????????????????????
        geocodeSearch.getFromLocationAsyn(query);
    }


}
