package com.coolweather.gofun.fragment.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.bean.CommentItem;
import com.coolweather.gofun.fragment.Map.bean.TypeItem;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.MapService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetRequset {

    private MapService mapService;

    public NetRequset(){
        mapService = HttpRequest.create(MapService.class);
    }


    public void initActivityType(final Handler mHandler) {
        List<TypeItem> typeItems = new ArrayList<>();
   //     MapService mapService = HttpRequest.create(MapService.class);
        mapService.getActivityType("Bearer" + GoFunApplication.token).enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                List<Activity> list = response.body();
                Log.d("Bottom1",list.toString());
                Log.d("Botton4", String.valueOf(list.size()));
                for(int i = 0; i < list.size(); i++){
                    TypeItem typeItem = new TypeItem(R.drawable.head,list.get(i).getType1());
                    Log.d("Bottom","type" + typeItem.getType());
                    typeItems.add(typeItem);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("typelist",(Serializable)typeItems);
                Message message = new Message();
                message.what = 1;
                message.setData(bundle);
                mHandler.sendMessage(message);

            }
            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {

            }
        });
    }

    public void initTypeActivity(final Handler mHandler,int id){
        MapService mapService = HttpRequest.create(MapService.class);
        mapService.getActivityItem("Bearer " + GoFunApplication.token, id).enqueue(new Callback<List<ActivityItem>>() {
            @Override
            public void onResponse(Call<List<ActivityItem>> call, Response<List<ActivityItem>> response) {
                List<ActivityItem> list = response.body();
                Bundle bundle = new Bundle();
                bundle.putSerializable("typeActivitylist",(Serializable)list);
                Log.d("NetRe", String.valueOf(list.size()));
                Log.d("ss","222222222");
                Message message = new Message();
                message.what = 2;
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<ActivityItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void initAllActivity(final Handler mHandler){
        mapService.getAllActivity("Bearer " + GoFunApplication.token).enqueue(new Callback<List<ActivityItem>>() {
            @Override
            public void onResponse(Call<List<ActivityItem>> call, Response<List<ActivityItem>> response) {
                List<ActivityItem> list = response.body();
                Log.d("111","1111111");
                Log.d("11",String.valueOf(list.size()));
                Bundle bundle = new Bundle();
                bundle.putSerializable("allActivitylist",(Serializable)list);
                Message message = new Message();
                message.what = 3;
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<ActivityItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void initActivityComment(final Handler mHandler,int id){
        MapService mapService = HttpRequest.create(MapService.class);
        mapService.getCommentItem("Bearer " + GoFunApplication.token,id).enqueue(new Callback<List<CommentItem>>() {
            @Override
            public void onResponse(Call<List<CommentItem>> call, Response<List<CommentItem>> response) {
                List<CommentItem> list = response.body();
                Bundle bundle = new Bundle();
                bundle.putSerializable("commentlist",(Serializable)list);
                Message message = new Message();
                message.what = 4;
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<CommentItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
