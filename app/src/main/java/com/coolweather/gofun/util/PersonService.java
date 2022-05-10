package com.coolweather.gofun.util;

import com.coolweather.gofun.fragment.Mine.bean.ActivityStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PersonService {

    @GET("Activity/getApplyStatus")
    Call<List<ActivityStatus>> getActivityStatus(@Header("Authorization") String token);

}
