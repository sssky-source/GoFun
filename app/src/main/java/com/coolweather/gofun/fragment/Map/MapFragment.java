package com.coolweather.gofun.fragment.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Map.adapter.TypeAdapter;
import com.coolweather.gofun.fragment.Map.bean.TypeItem;
import com.coolweather.gofun.fragment.Map.widget.InfoCard;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.util.BitmapUtils;
import com.coolweather.gofun.util.ToastUtils;
import com.coolweather.gofun.fragment.Map.widget.BottomSelectDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapFragment extends Fragment implements
        AMapLocationListener, LocationSource,
        AMap.OnMapClickListener,AMap.OnMapLongClickListener,
        GeocodeSearch.OnGeocodeSearchListener,View.OnClickListener,
        EditText.OnKeyListener,AMap.OnMarkerClickListener{

    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private MapView mapView;
    //地图控制器
    private AMap aMap = null;
    //位置更改监听
    private OnLocationChangedListener mListener;
    //定位样式
    private MyLocationStyle myLocationStyle = new MyLocationStyle();
    //定义一个UiSettings对象
    private UiSettings mUiSettings;
    //POI查询对象
    private PoiSearch.Query query;
    //POI搜索对象
    private PoiSearch poiSearch;
    //城市码
    private String cityCode = null;
    //浮动按钮
    private FloatingActionButton fabPOI;
    //地理编码搜索
    private GeocodeSearch geocodeSearch;
    //解析成功标识码
    private static final int PARSE_SUCCESS_CODE = 1000;

    private AutoTransition autoTransition;//过渡动画
    private Animation bigShowAnim;//放大显示
    private Animation smallHideAnim;//缩小隐藏
    private int width;//屏幕宽度
    private boolean isOpen = false;//顶部搜索布局的状态


    private ImageView ivSearch;//搜索图标
    private EditText edSearch;//搜索输入框
    private ImageView ivClose;//关闭图标
    private RelativeLayout laySearch;//搜索布局

    //城市
    private String city;

    //浮动按钮  清空地图标点
    private FloatingActionButton fabClearMarker;

    //浮动按钮 筛选类型
    private FloatingActionButton fabSelectType;

    //标点列表
    private List<Marker> markerList = new ArrayList<>();
    private RecyclerView recyclerView;


    private BottomSelectDialog bottomSheetDialog;

    private TypeAdapter typeAdapter;
    private List<TypeItem> titleList = new ArrayList<>();
    private List<ActivityItem> activityItemList = new ArrayList<>();
    private List<ActivityItem> allactivityItemList = new ArrayList<>();
    private NetRequset netRequset = new NetRequset();


    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    titleList = (List<TypeItem>)msg.getData().getSerializable("typelist");
                    titleList.add(0,new TypeItem(R.drawable.head,"全部"));
                    showSheetDialog1();
                    bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
                    bottomSheetDialog.show();
                    Log.d("Hand", String.valueOf(titleList.size()));
                    break;
                case 2:
                    activityItemList = (List<ActivityItem>)msg.getData().getSerializable("typeActivitylist");
                    Log.d("Handd",String.valueOf(activityItemList.size()));
                    initMark(activityItemList);
                    break;
                case 3:
                    // activityItemList = (List<ActivityItem>)msg.getData().getSerializable("allActivitylist");
                    //  initMark(activityItemList);
                    //     Log.d("Handdd0",String.valueOf(allactivityItemList.size()));
                default:

            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取屏幕宽高
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;  //获取屏幕的宽度 像素
        ivSearch = getActivity().findViewById(R.id.iv_search);
        ivClose = getActivity().findViewById(R.id.iv_close);
        edSearch = getActivity().findViewById(R.id.ed_search);
        laySearch = getActivity().findViewById(R.id.lay_search);
        fabClearMarker = getActivity().findViewById(R.id.fab_clear_marker);
        fabSelectType = getActivity().findViewById(R.id.fab_select);
        recyclerView = getActivity().findViewById(R.id.type_recyclerview);
     //   initActivityType();
        ivSearch.setOnClickListener(this::onClick);
        ivClose.setOnClickListener(this::onClick);
        edSearch.setOnKeyListener(this);
        fabClearMarker.setOnClickListener(this::onClick);
        fabSelectType.setOnClickListener(this::onClick);
        Log.d("ss","11111111");
        // toolbar = getActivity().findViewById(R.id.toolbar);
        // ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MapsInitializer.updatePrivacyShow(getContext(),true,true);
        MapsInitializer.updatePrivacyAgree(getContext(),true);
        //初始化定位
        initLocation();
        //初始化地图
        initMap(savedInstanceState);
        Log.d("ss","3333333333");


        checkingAndroidVersion();


    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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
     * 初始化地图
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mapView = getActivity().findViewById(R.id.map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();
        aMap.setMinZoomLevel(20);
        //开启室内地图
        aMap.showIndoorMap(true);
        //实例化UiSettings类对象
        mUiSettings = aMap.getUiSettings();
        //隐藏缩放按钮
        mUiSettings.setZoomControlsEnabled(false);
        //显示比例尺 默认不显示
        mUiSettings.setScaleControlsEnabled(true);

        //设置最小缩放等级为16 ，缩放级别范围为[3, 20]
        aMap.setMinZoomLevel(5);
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色  都为0则透明
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度  0 无宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色  都为0则透明
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        //设置地图的点击事件
        aMap.setOnMapClickListener(this);
        //设置地图的长按事件
        aMap.setOnMapLongClickListener(this);
        //为地图Marker设置点击事件
        aMap.setOnMarkerClickListener(this);
        //构建GeocodeSearch对象
        try {
            geocodeSearch = new GeocodeSearch(getContext());
        } catch (AMapException e) {
            e.printStackTrace();
        }
        //设置监听
        geocodeSearch.setOnGeocodeSearchListener(this);
        Log.d("ss","444444444");
    }

    /**
     * 地图单击事件
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        latlonToAddress(latLng);
        //添加标点
     //   aMap.addMarker(new MarkerOptions().position(latLng).snippet("DefaultMarker"));
        //添加标点
      //  addMarker(latLng,"http://139.224.221.148:1145/user/17/少女前线仲夏夜的精灵 大破.png");
        updateMapCenter(latLng);
     //   showInfoCard();

    }



    /**
     * 地图长按事件
     * @param latLng
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        ToastUtils.show(getContext(),"长按了地图，经度："+latLng.longitude+"，纬度："+latLng.latitude);
    }

    /**
     * 坐标转地址(逆地址编码)
     * @param regeocodeResult
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
       //解析result获取地址描述信息
        if(rCode == PARSE_SUCCESS_CODE){
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            //显示解析后的地址
            ToastUtils.show(getContext(),"地址："+regeocodeAddress.getFormatAddress());
        }else {
            ToastUtils.show(getContext(),"获取地址失败");
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
                LatLng latLng = new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude());
                //显示解析后的坐标
                ToastUtils.show(getContext(),"坐标：" + latLonPoint.getLongitude()+"，"+latLonPoint.getLatitude());
                updateMapCenter(latLng);
            }

        } else {
            ToastUtils.show(getContext(),"获取坐标失败");
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

    /**
     * 检查Android版本
     */
    private void checkingAndroidVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android6.0及以上先获取权限再定位
            requestPermission();
        }else {
            //Android6.0以下直接定位
            mLocationClient.startLocation();
        }
    }

    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(getContext(), permissions)) {
            //true 有权限 开始定位
            ToastUtils.show(getContext(),"已获得权限，可以定位啦！");
            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    /**
     * 请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
                //地址
                String address = aMapLocation.getAddress();
                //纬度
                double latitude = aMapLocation.getLatitude();
                //经度
                double longitude = aMapLocation.getLongitude();
                //城市赋值
                city = aMapLocation.getCity();
                Log.d("Map","地址是" + address);
                mLocationClient.stopLocation();
                if(mListener!=null){
                    mListener.onLocationChanged(aMapLocation);
                }
                fabPOI.show();
                cityCode = aMapLocation.getCityCode();
                Log.d("Map","地址是" + address);
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    /**
     * 添加地图标点
     *
     * @param latLng
     */
    private void addMarker(LatLng latLng,String img,int indentify) {
        //显示浮动按钮
        fabClearMarker.show();
        //添加标点
//        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("标题").snippet("详细信息"));
//        marker.showInfoWindow();
//        //设置标点的绘制动画效果
//        Animation animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
//        long duration = 1000L;
//        animation.setDuration(duration);
//        animation.setInterpolator(new LinearInterpolator());
//        marker.setAnimation(animation);
//        marker.startAnimation();
//        markerList.add(marker);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.marker_layout,null);
        ImageView userPic = view.findViewById(R.id.iv_head);
        Glide.with(getContext()).load(img).into(userPic);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(String.valueOf(indentify));
        markerOptions.position(latLng);
    //    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.urlToBitmap(img)));
        markerOptions.icon(BitmapDescriptorFactory.fromView(view));
        Marker marker = aMap.addMarker(markerOptions);
        markerList.add(marker);
        Log.d("i","00000000000");
    }


    /**
     * 清空地图Marker
     *
     */
    public void clearAllMarker() {
        if (markerList != null && markerList.size()>0){
            for (Marker markerItem : markerList) {
                markerItem.remove();
            }
        }
 //       aMap.clear();
        fabClearMarker.hide();
    }

    /**
     * Marker点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        //showMsg("点击了标点");
        //显示InfoWindow
        if (!marker.isInfoWindowShown()) {

            //显示
       //     marker.showInfoWindow();
            showInfoCard(marker.getTitle());
       //     marker.remove();
        //    showMoneyDialog();
            Log.d("MapView",marker.getTitle());
        } else {
            //隐藏
          //  marker.hideInfoWindow();
        }
        return true;
    }

    /**
     * 改变地图中心位置
     * @param latLng 位置
     */
    private void updateMapCenter(LatLng latLng) {
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        CameraPosition cameraPosition = new CameraPosition(latLng, 16, 30, 0);
        //位置变更
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //改变位置
        aMap.moveCamera(cameraUpdate);
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
        netRequset.initTypeActivity(mHandler,0);
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

    // dp 转成 px
    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    // px 转成 dp
    private int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //过渡动画
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void beginDelayedTransition(ViewGroup view) {
        autoTransition = new AutoTransition();
        autoTransition.setDuration(500);
        TransitionManager.beginDelayedTransition(view,autoTransition);
    }


    /**
     * 展开
     */
    public void initExpand() {
        isOpen = true;
        edSearch.setVisibility(View.VISIBLE);//显示输入框
        ivClose.setVisibility(View.VISIBLE);//显示关闭按钮
        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) laySearch.getLayoutParams();
        LayoutParams.width = dip2px(px2dip(width) - 24);//设置展开的宽度
        LayoutParams.setMargins(dip2px(0), dip2px(0), dip2px(0), dip2px(0));
        laySearch.setPadding(14, 0, 14, 0);
        laySearch.setLayoutParams(LayoutParams);

        //开始动画
        beginDelayedTransition(laySearch);

    }

    /**
     * 收缩
     */
    private void initClose() {
        isOpen = false;
        edSearch.setVisibility(View.GONE);
        edSearch.setText("");
        ivClose.setVisibility(View.GONE);
        Log.d("MainActivity","开始收缩111");
        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) laySearch.getLayoutParams();
        LayoutParams.width = dip2px(48);
        LayoutParams.height = dip2px(48);
        LayoutParams.setMargins(dip2px(0), dip2px(0), dip2px(0), dip2px(0));
        laySearch.setLayoutParams(LayoutParams);

        //隐藏键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

        //开始动画
        beginDelayedTransition(laySearch);
        Log.d("MainActivity","开始收缩");

    }

    /**
     * 键盘点击
     *
     * @param v
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            //获取输入框的值
            String address = edSearch.getText().toString().trim();
            if (address == null || address.isEmpty()) {
                ToastUtils.show(getContext(),"请输入地址");
            }else {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //隐藏软键盘
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                GeocodeQuery query = new GeocodeQuery(address,city);
                geocodeSearch.getFromLocationNameAsyn(query);
            }
            return true;
        }
        return false;
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_search:
                initExpand();
                break;
            case R.id.iv_close:
                initClose();
            case R.id.fab_clear_marker:
                clearAllMarker();
                break;
            case R.id.fab_select:
                titleList.clear();
                netRequset.initActivityType(mHandler);
                break;
            default:
        }

    }



    private void showSheetDialog1() {
        View view = View.inflate(getContext(), R.layout.dialog_bottom_select, null);
        recyclerView = view.findViewById(R.id.type_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        typeAdapter = new TypeAdapter(R.layout.item_type,titleList);
        recyclerView.setAdapter(typeAdapter);
        bottomSheetDialog = new BottomSelectDialog(getContext(),getActivity());
        bottomSheetDialog.setContentView(view);
        typeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Log.d("position", String.valueOf(position));
                netRequset.initTypeActivity(mHandler,position);
                bottomSheetDialog.dismiss();
            }
        });
    }




    private void showInfoCard(String indntify) {
        Log.d("list", String.valueOf(activityItemList.size()));
        InfoCard infoCard = new InfoCard(getContext(),getActivity(),activityItemList.get(Integer.parseInt(indntify)));
        // infoCard.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        //此处设置位置窗体大小，
        infoCard.getWindow().setLayout(800,1000);
        infoCard.show();
    }

    private void initMark(List<ActivityItem> itemList){
        clearAllMarker();
        Log.d("ii","2222222222");
        for(int i = 0; i < itemList.size(); i++){
            ActivityItem activityItem = itemList.get(i);
            LatLng latLng = new LatLng(activityItem.getY(),activityItem.getX());
            String img = activityItem.getImage();
            Log.d("iii","纬度：" + latLng.latitude + "经度" + latLng.longitude);
            addMarker(latLng,img,i);
        }
    }


}
