package com.coolweather.gofun.net;

import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**

* @Author : LWD

* @Time : On 2022/4/28 9:41

*/

//定义相对路径和方法
public interface RecommendService {

    //获取推荐活动类型
    @GET("Activity/getActivityType")
    Call<List<Activity>> getActivityType(@Header("Authorization") String token);

    //根据活动类型ID获取活动条目
    @GET("Activity/getActivityByType")
    Call<List<ActivityItem>> getActivityItem(@Header("Authorization") String token, @Query("typeId") int id);

    //申请加入活动
    @POST("Activity/applyActivity")
    Call<ResponseBody> applyActivity(@Header("Authorization") String token,@Query("activityId") int id);

}
