package com.coolweather.gofun.fragment.Recommend;

import static com.coolweather.gofun.GoFunApplication.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.SqliteUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Recommend.Adapter.CommendAdapter;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.fragment.Recommend.bean.PersonComment;
import com.coolweather.gofun.net.CommentService;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.RecommendService;
import com.coolweather.gofun.util.DialogUtils;
import com.coolweather.gofun.util.ToastUtils;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivityDetail extends AppCompatActivity implements View.OnClickListener {

    private RecommendService recommendService;
    private CommentService commentService;
    //活动详情信息列表
    private ActivityItem item;
    //活动信息详情
    private CircleImageView creatorImage, userImage;  //创建者头像
    //创建者名字，活动类型，活动标题，介绍，开始结束时间
    private TextView userName, type, title, introduction, startTime, endTime;
    //申请按钮
    private Button apply, checkCommend;
    private RecyclerView recyclerView;
    private CommendAdapter commendAdapter;
    private List<PersonComment> temp = new ArrayList<>();
    private List<PersonComment> list;
    private SqliteUtil sqliteUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);

        sqliteUtil = new SqliteUtil(RecommendActivityDetail.this);
        recommendService = HttpRequest.create(RecommendService.class);
        commentService = HttpRequest.create(CommentService.class);

        //接受传递的list
        Intent intent = getIntent();
        item = (ActivityItem) intent.getSerializableExtra("detail_item");

        initial();

        requestCommend();
    }

    private void requestCommend() {
        commentService.getComment("Bearer " + GoFunApplication.token, item.getId()).enqueue(new Callback<List<PersonComment>>() {
            @Override
            public void onResponse(@NonNull Call<List<PersonComment>> call, @NonNull Response<List<PersonComment>> response) {
                list = response.body();
                //倒转list
                if (list != null) {
                    Collections.reverse(list);
                }
                //临时list保存最新评论
                temp.add(list.get(0));
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
        Person person_LitePal = LitePal.findFirst(Person.class);
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
        recyclerView = findViewById(R.id.ActivityDetail_Commend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecommendActivityDetail.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        checkCommend.setOnClickListener(this);
        apply.setOnClickListener(this);

        Glide.with(RecommendActivityDetail.this).load(item.getImage()).into(creatorImage);
        Log.d("22222", sqliteUtil.getToken("token"));
        Glide.with(RecommendActivityDetail.this).load(person_LitePal.getImage()).into(userImage);
        userName.setText(item.getUsername());
        type.setText("[" + item.getType() + "]");
        title.setText(item.getTitle());
        introduction.setText(item.getIntroduction());
        startTime.setText(item.getStarttime());
        endTime.setText(item.getEndtime());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ActivityDetail_Apply:
                DialogUtils.getInstance().showDialog(RecommendActivityDetail.this, "确认加入活动？", new DialogUtils.DialogCallBack() {
                    @Override
                    public void OkEvent() {
                        applyRequest(recommendService, item.getId());

                    }
                });
                break;
            case R.id.ActivityDetail_CheckCommend:
                Intent intent = new Intent(RecommendActivityDetail.this, RecommendCommendActivity.class);
                Bundle bundle = new Bundle();
                //传递评论list
                bundle.putSerializable("commendList", (Serializable) list);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

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

}