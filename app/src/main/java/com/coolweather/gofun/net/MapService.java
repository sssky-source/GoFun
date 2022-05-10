package com.coolweather.gofun.net;

import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MapService {
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

    @GET("Activity/getActivityByType")
    Call<List<ActivityItem>> getActivityItem(@Header("Authorization") String token, @Query("typeId") int id);

    @GET("Activity/getAllActivity")
    Call<List<ActivityItem>> getAllActivity(@Header("Authorization") String token);
}
