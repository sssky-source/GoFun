package com.coolweather.gofun.net;

import com.coolweather.gofun.fragment.Mine.bean.ActivityStatus;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PersonService {

    //我申请的
    @GET("Activity/getApplyStatus")
    Call<List<ActivityStatus>> getApplyStatus(@Header("Authorization") String token);

    //我申请的活动详情
    @GET("Activity/getApplyActivityOfApplicant")
    Call<List<ActivityItem>> getApplyStatusDetail(@Header("Authorization") String token, @Query("applyState") int id);

    //我加入的
    @GET("Activity/getApplyActivityOfApplicant?applyState=2")
    Call<List<ActivityItem>> getJoinActivity(@Header("Authorization") String token);
}
