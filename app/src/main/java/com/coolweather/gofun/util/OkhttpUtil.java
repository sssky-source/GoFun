package com.coolweather.gofun.util;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpUtil {
    public final static String TAG = "OkHttpUtil";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    // 后台数据接口基础路径
    public final static String BASE_URL="http://120.26.164.64:8080/TheActivity/";


    public static void requestpost(String url,String username,String password,Callback callback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("username",username)
                .add("password",password);//传递键值对参数
        final Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    /*
    登录注册的post请求
    登录：/username=name&password=number
    注册：/username=name&password=number&icon_email=icon_email
     */
    public static void requestpostone(String url, String value, Callback callback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8"); //设置格式
        RequestBody requestBody = RequestBody.create(mediaType, value);
        final Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(requestBody)//传递请求体
                .build();
        //回调接口，在网络请求时回调实现
        client.newCall(request).enqueue(callback);
    }




}
