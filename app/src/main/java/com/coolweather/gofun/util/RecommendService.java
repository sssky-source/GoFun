package com.coolweather.gofun.util;

import com.coolweather.gofun.fragment.Recommend.bean.Activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**

* @Author : LWD

* @Time : On 2022/4/28 9:41

*/

//定义相对路径和方法
public interface RecommendService {
    /*
    * 实例
    * @GET("Good/getGoodsByType?page=1&size=6&type=1")
    * Call<List<Good>> getGoodData();
    *
    * @POST("User/Login")
    * Call<ResponseBody> createData(@Body UserSign userSign);
    * */
    @GET("Activity/getActivityType")
    Call<List<Activity>> getActivityType(@Header("Authorization") String token);


}
