package com.coolweather.gofun.net;

import com.coolweather.gofun.fragment.Mine.bean.ActivityStatus;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;
import com.coolweather.gofun.fragment.Mine.bean.PersonChange;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PersonService {

    //个人信息
    @GET("User/getUserInfo")
    Call<Person> getUserInfo(@Header("Authorization") String token);

    //修改个人信息
    @POST("User/editUserInfo")
    Call<ResponseBody> editUserInfo(@Header("Authorization") String token, @Body PersonChange personChange);

    //上传头像
    @POST("User/uploadHeadImg")
    Call<ResponseBody> uploadHeadImg(@Header("Authorization") String token, @Body String image);

    //我申请的
    @GET("Activity/getApplyStatus")
    Call<List<ActivityStatus>> getApplyStatus(@Header("Authorization") String token);

    //我申请的活动详情
    @GET("Activity/getApplyActivityOfApplicant")
    Call<List<PersonActivityItem>> getApplyStatusDetail(@Header("Authorization") String token, @Query("applyState") int id);

    //我加入的
    @GET("Activity/getGoingActivityOfApplicant")
    Call<List<PersonActivityItem>> getJoinActivity(@Header("Authorization") String token);
}
