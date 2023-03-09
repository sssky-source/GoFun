package com.coolweather.gofun.fragment.Recommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.bumptech.glide.Glide;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.LitPalUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.LocalDb.PersonLitePal;
import com.coolweather.gofun.activity.RouteActivity;
import com.coolweather.gofun.fragment.Recommend.Adapter.CommendAdapter;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.fragment.Recommend.bean.PersonAddComment;
import com.coolweather.gofun.fragment.Recommend.bean.PersonComment;
import com.coolweather.gofun.net.CommentService;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.RecommendService;
import com.coolweather.gofun.util.DialogUtils;
import com.coolweather.gofun.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    活动介绍界面
 */

public class RecommendActivityDetail extends AppCompatActivity implements View.OnClickListener, AMapLocationListener, LocationSource{

    private RecommendService recommendService;
    private CommentService commentService;
    //活动详情信息列表
    private ActivityItem item;
    private int activityId;
    //活动信息详情
    private CircleImageView creatorImage, userImage;  //创建者头像
    //创建者名字，活动类型，活动标题，介绍，开始结束时间
    private TextView userName, type, title, introduction, startTime, endTime,money,number;
    //申请按钮
    private Button apply, checkCommend;
    private TextView addCommend;
    private RecyclerView recyclerView;
    private CommendAdapter commendAdapter;
    private List<PersonComment> temp = new ArrayList<>();
    private List<PersonComment> list;
    private PersonLitePal person_LitePal;
    private MapView mapView = null;
    //地图控制器
    private AMap aMap = null;
    //位置更改监听
    private OnLocationChangedListener mListener;
    //收藏
    private ImageView collect,navigate;
    private TextView collectNum,item_name;
    private String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);
        MapsInitializer.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
        recommendService = HttpRequest.create(RecommendService.class);
        commentService = HttpRequest.create(CommentService.class);
        mapView = findViewById(R.id.ActivityDetail_Map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        //接受传递的list
        Intent intent = getIntent();

        //item = (ActivityItem) intent.getSerializableExtra("detail_item");
        activityId = intent.getIntExtra("activityId",0);
        //Log.d("111020","star：" + item.getStar());

        initial();

        //根据传入的活动id获取活动详细信息
        requestActivityById();

        //评论
        requestCommend();


        //获取收藏人数
        requestStarActivityNum();
    }

    //根据传入的活动id获取活动详细信息
    private void requestActivityById() {
        recommendService.getActivityItemById("Bearer " + GoFunApplication.token,activityId).enqueue(new Callback<ActivityItem>() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onResponse(Call<ActivityItem> call, Response<ActivityItem> response) {
                item = response.body();
                Glide.with(RecommendActivityDetail.this).load(item.getImage()).into(creatorImage);
                Glide.with(RecommendActivityDetail.this).load(person_LitePal.getImage()).into(userImage);
                userName.setText(item.getUsername());
                type.setText("[" + item.getType() + "]");
                title.setText(item.getTitle());
                item_name.setText(item.getTitle());
                introduction.setText(item.getIntroduction());
                number.setText("人数：" + item.getCurnumber() + "/" + item.getMaxnumber());
                startTime.setText(item.getStarttime());
                endTime.setText(item.getEndtime());
                location = item.getLocation();
                if (item.getPayment() != null){
                    Log.d("item","type" + item.getPayment().getType());
                    if (item.getPayment().getType() == 0){
                        //方法弃用：'getColor(int)' is deprecated as of API 23: Android 6.0 (Marshmallow)
                        //money.setTextColor(getResources().getColor(R.color.red));
                        //下为6.0之后使用的方法
                        money.setTextColor(ContextCompat.getColor(RecommendActivityDetail.this,R.color.blue));
                    }else if (item.getPayment().getType() == 1){
                        money.setTextColor(ContextCompat.getColor(RecommendActivityDetail.this,R.color.red));
                    }
                    money.setText("￥:" + item.getPayment().getPayment1());
                }


                addMark();

                /**
                 * 怎么判断用户是否已经收藏活动了呢？
                 */
                //判断isStar 是否已经收藏
                if (item.getStar()){
                    //已经收藏则图标改位收藏
                    collect.setImageDrawable(getDrawable(R.drawable.collect_press));
                    Log.d("111020","isStar");
                }

                if (item.getPayment() == null){
                    money.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ActivityItem> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //获取收藏人数
    private void requestStarActivityNum() {
        recommendService.getStarActivityNum("Bearer " + GoFunApplication.token,activityId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                collectNum.setText(response.body().toString());
                Log.d("111020","111:"+ response.body());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //评论
    private void requestCommend() {
        commentService.getComment("Bearer " + GoFunApplication.token, activityId).enqueue(new Callback<List<PersonComment>>() {
            @Override
            public void onResponse(@NonNull Call<List<PersonComment>> call, @NonNull Response<List<PersonComment>> response) {
                list = response.body();
                //倒转list
                if (list != null) {
                    Collections.reverse(list);
                    if (list.size() != 0){
                        //临时list保存最新评论
                        temp.add(list.get(0));
                    }
                }
                commendAdapter = new CommendAdapter(R.layout.activity_commend_item, temp);
                recyclerView.setAdapter(commendAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<PersonComment>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void initial() {
        person_LitePal = LitPalUtil.getPersonInfo();
        creatorImage = findViewById(R.id.ActivityDetail_activityCreateImage);
        userImage = findViewById(R.id.ActivityDetail_PersonUserImage);
        userName = findViewById(R.id.ActivityDetail_username);
        type = findViewById(R.id.ActivityDetail_type);
        title = findViewById(R.id.ActivityDetail_title);
        introduction = findViewById(R.id.ActivityDetail_introduction);
        number = findViewById(R.id.ActivityDetail_number);
        startTime = findViewById(R.id.ActivityDetail_startTime);
        endTime = findViewById(R.id.ActivityDetail_endTime);
        apply = findViewById(R.id.ActivityDetail_Apply);
        checkCommend = findViewById(R.id.ActivityDetail_CheckCommend);
        addCommend = findViewById(R.id.ActivityDetail_AddCommendWays);
        recyclerView = findViewById(R.id.ActivityDetail_Commend);
        collect = findViewById(R.id.collect);
        navigate = findViewById(R.id.navigate);
        collectNum = findViewById(R.id.collect_Num);
        money = findViewById(R.id.ActivityDetail_money);
        item_name = findViewById(R.id.item_name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecommendActivityDetail.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        addCommend.setOnClickListener(this);
        checkCommend.setOnClickListener(this);
        apply.setOnClickListener(this);
        collect.setOnClickListener(this);
        navigate.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //活动申请
            case R.id.ActivityDetail_Apply:
                DialogUtils.getInstance().showDialog(RecommendActivityDetail.this, "确认加入活动？", new DialogUtils.DialogCallBack() {
                    @Override
                    public void OkEvent() {
                        applyRequest(recommendService, activityId);
                    }
                });
                break;

                //评论
            case R.id.ActivityDetail_CheckCommend:
                Intent intent = new Intent(RecommendActivityDetail.this, RecommendCommendActivity.class);
                Bundle bundle = new Bundle();
                //传递评论list
                bundle.putSerializable("commendList", (Serializable) list);
                bundle.putSerializable("ActivityItem",item);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

                //在此进行评论
            case R.id.ActivityDetail_AddCommendWays:
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.dialog);
                View dialogView = getLayoutInflater().inflate(R.layout.commend_bottomsheet,null,false);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet);
                bottomSheetDialog.show();
                //dialog中的控件
                CircleImageView sheetUserImage = dialogView.findViewById(R.id.sheetUserImage);
                Glide.with(RecommendActivityDetail.this).load(person_LitePal.getImage()).into(sheetUserImage);
                //设置点击事件找到实例使用上面的View来寻找
                TextView textView = dialogView.findViewById(R.id.sheetActivity);
                textView.setText("[" + item.getType() + "] " + item.getTitle());

                EditText editText = dialogView.findViewById(R.id.sheetEditText);
                Button button = dialogView.findViewById(R.id.sheetButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCommendPost(editText.getText().toString());
                    }

                    private void addCommendPost(String commend) {
                        PersonAddComment personAddComment = new PersonAddComment();
                        personAddComment.setCommentInfo(commend);
                        personAddComment.setActivity_id(activityId);
                        commentService.addComment("Bearer " + GoFunApplication.token,personAddComment).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ToastUtils.show(RecommendActivityDetail.this,"成功发表评论");
                                bottomSheetDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                });
                break;

            //收藏
            case R.id.collect:
                //更改收藏和取消收藏图标
                //原本未收藏
                if (!item.getStar()){
                    recommendService.starActivity("Bearer " + GoFunApplication.token, activityId).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ToastUtils.show(RecommendActivityDetail.this,"收藏成功");
                            Log.d("111020","afterStar：" + item.getStar());
                            Log.d("111020","code:" + response.code());
                            //isStar状态没有改变
                            collect.setImageDrawable(getDrawable(R.drawable.collect_press));
                            //collectNum.setText((Integer.parseInt(collectNum.toString())+1));
                            collectNum.setText(String.valueOf(Integer.parseInt(collectNum.getText().toString())+1));
                            //setText()方法不能传int类型参数 会被认为是资源的id int类型的数据需要转为string类型
                            item.setStar(true);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }else if (item.getStar()){
                    //取消收藏
                    //collect.setImageDrawable(getDrawable(R.drawable.collect));
                    recommendService.unStarActivity("Bearer " + GoFunApplication.token, activityId).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ToastUtils.show(RecommendActivityDetail.this,"取消收藏");
                            collect.setImageDrawable(getDrawable(R.drawable.collect));
                            collectNum.setText(String.valueOf(Integer.parseInt(collectNum.getText().toString())-1));
                            item.setStar(false);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
                break;
            case R.id.navigate:
                Intent navigate = new Intent(this, RouteActivity.class);
                navigate.putExtra("address",location);
                startActivity(navigate);
                break;
            default:
                break;
        }
    }


    //申请加入活动
    private void applyRequest(RecommendService recommendService, int id) {
        recommendService.applyActivity("Bearer " + GoFunApplication.token, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                finish();
                ToastUtils.show(RecommendActivityDetail.this, "已提交申请，等待加入活动");
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void addMark(){
        LatLng latLng = new LatLng(item.getY(),item.getX());
        Log.d("item",item.getImage() + item.getX());
        String img = item.getImage();
        //添加标点
       aMap.addMarker(new MarkerOptions().position(latLng).snippet("活动地点"));
       updateMapCenter(latLng);

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
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}