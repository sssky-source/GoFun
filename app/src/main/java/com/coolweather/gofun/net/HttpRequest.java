package com.coolweather.gofun.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**

* @Author : LWD

* @Time : On 2022/4/28 9:41

*/

//请求封装
public class HttpRequest {
    //根地址
    private static final String BASE_URI ="http://121.4.109.235:666/api/";
    //http://192.168.0.11:8000/swagger/index.html
    //"http://139.224.221.148:666/api/"

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URI)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T create(Class<T> serviceClass){
        return retrofit.create(serviceClass);
    }
}
