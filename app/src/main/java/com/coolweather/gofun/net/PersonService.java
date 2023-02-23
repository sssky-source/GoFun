package com.coolweather.gofun.net;

import com.coolweather.gofun.fragment.Mine.bean.ActivityStatus;
import com.coolweather.gofun.fragment.Mine.bean.CollectBean;
import com.coolweather.gofun.fragment.Mine.bean.CreateBean;
import com.coolweather.gofun.fragment.Mine.bean.GetApplyID;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Mine.bean.PersonActivityItem;
import com.coolweather.gofun.fragment.Mine.bean.PersonChange;
import com.coolweather.gofun.fragment.Mine.bean.UserTag;
import com.coolweather.gofun.fragment.Recommend.bean.ActivityItem;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PersonService {

    //个人信息
    @GET("User/getUserInfo")
    Call<Person> getUserInfo(@Header("Authorization") String token);

    //修改个人信息
    @POST("User/editUserInfo")
    Call<ResponseBody> editUserInfo(@Header("Authorization") String token, @Body PersonChange personChange);

    //上传头像
    @Multipart
    @POST("User/uploadHeadImg")
    Call<ResponseBody> uploadHeadImg(@Header("Authorization") String token, @Part MultipartBody.Part file);

    //我申请的
    @GET("Activity/getApplyStatus")
    Call<List<ActivityStatus>> getApplyStatus(@Header("Authorization") String token);

    //我申请的活动详情
    @GET("Activity/getApplyActivityOfApplicant")
    Call<List<PersonActivityItem>> getApplyStatusDetail(@Header("Authorization") String token, @Query("applyState") int id);

    //我加入的
    @GET("Activity/getGoingActivityOfApplicant")
    Call<List<PersonActivityItem>> getJoinActivity(@Header("Authorization") String token);

    //我创建的
    @GET("Activity/getActivityByCreator")
    Call<List<CreateBean>> getCreateActivity(@Header("Authorization") String token);

    //我收藏的
    @GET("Activity/getStarActivities")
    Call<List<CollectBean>> getStarActivity(@Header("Authorization") String token);

    //获取申请id
    @GET("Activity/getApplyActivityOfCreator")
    Call<List<GetApplyID>> getApplyID(@Header("Authorization") String token, @Query("activityId") int id, @Query("applyState") int state);

    //通过申请
    @PUT("Activity/passApply")
    Call<ResponseBody> passApply(@Header("Authorization") String token,@Query("applyId") int id);

    //不通过申请
    @PUT("Activity/unpassApply")
    Call<ResponseBody> unPassApply(@Header("Authorization") String token,@Query("applyId") int id);

    //个人标签
    @GET("User/getUserTag")
    Call<List<UserTag>> getUserTag(@Header("Authorization") String token,@Query("userId") int id);
}
