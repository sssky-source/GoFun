package com.coolweather.gofun.net;

import com.coolweather.gofun.fragment.Message.bean.AddChartMessage;
import com.coolweather.gofun.fragment.Message.bean.ChartMessageBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChartMessage {

    //获取聊天内容
    @GET("ChartMessage/showChartMessage")
    Call<List<ChartMessageBean>> getChartMessage(@Header("Authorization") String token, @Query("activtiyId") int id);

    //添加聊天信息
    @POST("ChartMessage/addChartMessage")
    Call<ResponseBody> addChartMessage(@Header("Authorization") String token, @Body AddChartMessage addChartMessage);

}
