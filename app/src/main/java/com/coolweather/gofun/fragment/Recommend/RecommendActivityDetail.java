package com.coolweather.gofun.fragment.Recommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
    ??????????????????
 */

public class RecommendActivityDetail extends AppCompatActivity implements View.OnClickListener, AMapLocationListener, LocationSource{

    private RecommendService recommendService;
    private CommentService commentService;
    //????????????????????????
    private ActivityItem item;
    //??????????????????
    private CircleImageView creatorImage, userImage;  //???????????????
    //???????????????????????????????????????????????????????????????????????????
    private TextView userName, type, title, introduction, startTime, endTime;
    //????????????
    private Button apply, checkCommend;
    private TextView addCommend;
    private RecyclerView recyclerView;
    private CommendAdapter commendAdapter;
    private List<PersonComment> temp = new ArrayList<>();
    private List<PersonComment> list;
    private PersonLitePal person_LitePal;
    private MapView mapView = null;
    //???????????????
    private AMap aMap = null;
    //??????????????????
    private OnLocationChangedListener mListener;


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
        //???????????????list
        Intent intent = getIntent();
        item = (ActivityItem) intent.getSerializableExtra("detail_item");

        initial();

        requestCommend();
        addMark();
    }

    private void requestCommend() {
        commentService.getComment("Bearer " + GoFunApplication.token, item.getId()).enqueue(new Callback<List<PersonComment>>() {
            @Override
            public void onResponse(@NonNull Call<List<PersonComment>> call, @NonNull Response<List<PersonComment>> response) {
                list = response.body();
                //??????list
                if (list != null) {
                    Collections.reverse(list);
                    if (list.size() != 0){
                        //??????list??????????????????
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
        startTime = findViewById(R.id.ActivityDetail_startTime);
        endTime = findViewById(R.id.ActivityDetail_endTime);
        apply = findViewById(R.id.ActivityDetail_Apply);
        checkCommend = findViewById(R.id.ActivityDetail_CheckCommend);
        addCommend = findViewById(R.id.ActivityDetail_AddCommendWays);
        recyclerView = findViewById(R.id.ActivityDetail_Commend);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecommendActivityDetail.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        addCommend.setOnClickListener(this);
        checkCommend.setOnClickListener(this);
        apply.setOnClickListener(this);

        Glide.with(RecommendActivityDetail.this).load(item.getImage()).into(creatorImage);
        Glide.with(RecommendActivityDetail.this).load(person_LitePal.getImage()).into(userImage);
        userName.setText(item.getUsername());
        type.setText("[" + item.getType() + "]");
        title.setText(item.getTitle());
        introduction.setText(item.getIntroduction());
        startTime.setText(item.getStarttime());
        endTime.setText(item.getEndtime());
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ActivityDetail_Apply:
                DialogUtils.getInstance().showDialog(RecommendActivityDetail.this, "?????????????????????", new DialogUtils.DialogCallBack() {
                    @Override
                    public void OkEvent() {
                        applyRequest(recommendService, item.getId());
                    }
                });
                break;
            case R.id.ActivityDetail_CheckCommend:
                Intent intent = new Intent(RecommendActivityDetail.this, RecommendCommendActivity.class);
                Bundle bundle = new Bundle();
                //????????????list
                bundle.putSerializable("commendList", (Serializable) list);
                bundle.putSerializable("ActivityItem",item);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ActivityDetail_AddCommendWays:
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.dialog);
                View dialogView = getLayoutInflater().inflate(R.layout.commend_bottomsheet,null,false);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet);
                bottomSheetDialog.show();
                //dialog????????????
                CircleImageView sheetUserImage = dialogView.findViewById(R.id.sheetUserImage);
                Glide.with(RecommendActivityDetail.this).load(person_LitePal.getImage()).into(sheetUserImage);
                //?????????????????????????????????????????????View?????????
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
                        personAddComment.setActivity_id(item.getId());
                        commentService.addComment("Bearer " + GoFunApplication.token,personAddComment).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ToastUtils.show(RecommendActivityDetail.this,"??????????????????");
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
        }
    }



    private void applyRequest(RecommendService recommendService, int id) {
        recommendService.applyActivity("Bearer " + GoFunApplication.token, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                finish();
                ToastUtils.show(RecommendActivityDetail.this, "????????????????????????????????????");
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
        //????????????
       aMap.addMarker(new MarkerOptions().position(latLng).snippet("????????????"));
       updateMapCenter(latLng);

    }

    /**
     * ????????????????????????
     * @param latLng ??????
     */
    private void updateMapCenter(LatLng latLng) {
        // CameraPosition ?????????????????? ????????????????????????????????????????????????
        // CameraPosition ?????????????????? ?????????????????????????????????
        // CameraPosition ?????????????????? ??????????????????????????????????????????????????????
        // CameraPosition ?????????????????? ??????????????????????????????????????????????????????????????????????????????????????????0??????360???
        CameraPosition cameraPosition = new CameraPosition(latLng, 16, 30, 0);
        //????????????
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //????????????
        aMap.moveCamera(cameraUpdate);
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
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}