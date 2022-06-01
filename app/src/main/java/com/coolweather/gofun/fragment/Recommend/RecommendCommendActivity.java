package com.coolweather.gofun.fragment.Recommend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Recommend.Adapter.CommendAdapter;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.fragment.Recommend.bean.PersonAddComment;
import com.coolweather.gofun.fragment.Recommend.bean.PersonComment;
import com.coolweather.gofun.net.CommentService;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    查看全部评论
 */
public class RecommendCommendActivity extends AppCompatActivity {

    private CommentService commentService;
    private List<PersonComment> list;
    private RecyclerView recyclerView;
    private CommendAdapter commendAdapter;
    private TextView commendNumber;
    private FloatingActionButton floatingActionButton;
    private Person person_LitePal;
    //活动详情信息列表
    private ActivityItem item;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_commend);

        person_LitePal = LitePal.findFirst(Person.class);
        commentService = HttpRequest.create(CommentService.class);
        //接受评论list
        Intent intent = getIntent();
        list = (List<PersonComment>) intent.getSerializableExtra("commendList");
        item = (ActivityItem) intent.getSerializableExtra("ActivityItem");

        recyclerView = findViewById(R.id.commend_history);
        commendNumber = findViewById(R.id.commend_number);
        floatingActionButton = findViewById(R.id.floating_addCommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecommendCommendActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commendAdapter = new CommendAdapter(R.layout.activity_commend_item,list);
        recyclerView.setAdapter(commendAdapter);
        commendNumber.setText(getResources().getString(R.string.commend) + "(" + list.size() + ")");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RecommendCommendActivity.this,R.style.dialog);
                View dialogView = getLayoutInflater().inflate(R.layout.commend_bottomsheet,null,false);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet);
                bottomSheetDialog.show();
                //dialog中的控件
                CircleImageView sheetUserImage = dialogView.findViewById(R.id.sheetUserImage);
                Glide.with(RecommendCommendActivity.this).load(person_LitePal.getImage()).into(sheetUserImage);
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
                        personAddComment.setActivity_id(item.getId());
                        commentService.addComment("Bearer " + GoFunApplication.token,personAddComment).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ToastUtils.show(RecommendCommendActivity.this,"成功发表评论");
                                bottomSheetDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                });
            }
        });

    }
}