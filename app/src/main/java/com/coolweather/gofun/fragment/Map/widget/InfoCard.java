package com.coolweather.gofun.fragment.Map.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.coolweather.gofun.R;
import com.coolweather.gofun.activity.DetailInfoActivity;
import com.coolweather.gofun.bean.CommentItem;
import com.coolweather.gofun.fragment.Map.NetRequset;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.util.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoCard extends Dialog implements View.OnClickListener {

    private Context context;
    private LinearLayout bt_detail;
    private ActivityItem activityItem = new ActivityItem();
    private TextView tv_type,tv_location,tv_creator;
    private CircleImageView join_imageone,join_imagetwo;
    private TextView join_nameone,join_nametwo;
    private TextView join_commitone,join_committwo;
    private TextView join_dateone,join_datetwo;
    private LinearLayout linearLayout_commit,comment_one,comment_two;
    private TextView textView_commitnone;
    private List<CommentItem> commentItemList = new ArrayList<>();
    private String url = "http://139.224.221.148:1145/user/16/default.jpg";
    private Activity activity;

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    commentItemList = (List<CommentItem>)msg.getData().getSerializable("commentlist");
                    Log.d("Hand", String.valueOf(commentItemList.size()));
                    if(commentItemList == null || commentItemList.size() == 0){
                        linearLayout_commit.setVisibility(View.INVISIBLE);
                    }else if(commentItemList.size() == 1){
                        textView_commitnone.setVisibility(View.INVISIBLE);
                        comment_two.setVisibility(View.INVISIBLE);
                    }else{
                        Log.d("img", "size" + String.valueOf(commentItemList.size()));
                        textView_commitnone.setVisibility(View.INVISIBLE);
                        CommentItem commentItem1 = commentItemList.get(0);
                        CommentItem commentItem2 = commentItemList.get(1);
                        Glide.with(context).load(commentItem1.getImage()).into(join_imageone);
                        Log.d("img","username" + commentItem1.getUsername());
                        join_nameone.setText(commentItem1.getUsername());
                        join_commitone.setText(commentItem1.getComment());
                        join_dateone.setText(commentItem1.getCreatetime().split("T")[0]
                                + " " + commentItem1.getCreatetime().split("T")[1]);

                        Glide.with(context).load(commentItem2.getImage()).into(join_imagetwo);
                        Log.d("img","username" + commentItem2.getUsername());
                        join_nametwo.setText(commentItem2.getUsername());
                        join_committwo.setText(commentItem2.getComment());
                        join_datetwo.setText(commentItem2.getCreatetime().split("T")[0]
                                + " " + commentItem2.getCreatetime().split("T")[1]);
                    }
                    break;
                case 2:

                    break;

                default:

            }
        }
    };

    public InfoCard(@NonNull Context context,Activity activity, ActivityItem activityItem) {
        super(context, R.style.dialog);
        this.context = context;
        this.activity = activity;
        this.activityItem = activityItem;
        initView();
        initDatas();
      //  bindViews();
    }

    private void initDatas() {
        NetRequset netRequset = new NetRequset();
        netRequset.initActivityComment(mHandler,activityItem.getId());
    }


    private void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.info_dialog,null);
        Window window = this.getWindow();
        if (window!=null){
            window.setGravity(Gravity.CENTER);
        }
        bt_detail = view.findViewById(R.id.bt_detail);
        tv_type = view.findViewById(R.id.tv_type);
        tv_location = view.findViewById(R.id.tv_location);
        tv_creator = view.findViewById(R.id.tv_creator);
        bt_detail.setOnClickListener(this);
        join_imageone = view.findViewById(R.id.join_imageone);
        join_imagetwo = view.findViewById(R.id.join_imagetwo);
        join_nameone = view.findViewById(R.id.join_nameone);
        join_nametwo = view.findViewById(R.id.join_nametwo);
        join_commitone = view.findViewById(R.id.join_commitone);
        join_committwo = view.findViewById(R.id.join_committwo);
        join_dateone = view.findViewById(R.id.join_dateone);
        join_datetwo = view.findViewById(R.id.join_datetwo);
        linearLayout_commit = view.findViewById(R.id.line_commit);
        textView_commitnone = view.findViewById(R.id.commit_none);
        comment_one = view.findViewById(R.id.comment_one);
        comment_two = view.findViewById(R.id.comment_two);
        tv_type.setText(activityItem.getType());
        tv_location.setText(activityItem.getLocation());
        tv_creator.setText(activityItem.getUsername());
        Log.d("ac", String.valueOf(activityItem.getId()));
        setContentView(view);
    }

    private void bindViews() {
        bt_detail = findViewById(R.id.bt_detail);
        tv_type = findViewById(R.id.tv_type);
        tv_location = findViewById(R.id.tv_location);
        tv_creator = findViewById(R.id.tv_creator);
        bt_detail.setOnClickListener(this);
        join_imageone = findViewById(R.id.join_imageone);
        join_imagetwo = findViewById(R.id.join_imagetwo);
        join_commitone = findViewById(R.id.join_commitone);
        join_committwo = findViewById(R.id.join_committwo);
        join_dateone = findViewById(R.id.join_dateone);
        join_datetwo = findViewById(R.id.join_datetwo);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, DetailInfoActivity.class);
        context.startActivity(intent);
        activity.finish();
    }

}