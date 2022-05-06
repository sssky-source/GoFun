package com.coolweather.gofun.util;

import com.coolweather.gofun.bean.User;

import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    /*
    * 实例
    * @GET("Good/getGoodsByType?page=1&size=6&type=1")
    * Call<List<Good>> getGoodData();
    *
    * @POST("User/Login")
    * Call<ResponseBody> createData(@Body UserSign userSign);
    * */
    @GET("/TheActivity/login1")
    Call<ResponseBody> createData(@Query("username") String username ,@Query("password") String password);
    @GET("")
    Call<Object> getType(@Header("Authorization") String token);


}
