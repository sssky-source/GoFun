package com.coolweather.gofun.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**

* @Author : LWD

* @Time : On 2022/4/28 9:41

*/

//请求封装
public class HttpRequest {
    //根地址
    //private static final String BASE_URI ="http://120.26.164.64:8080/";
    private static final String BASE_URI ="http://192.168.0.11:8080/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URI)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T create(Class<T> serviceClass){
        return retrofit.create(serviceClass);
    }
}
