package com.coolweather.gofun.net;

import com.coolweather.gofun.fragment.Recommend.bean.PersonAddComment;
import com.coolweather.gofun.fragment.Recommend.bean.PersonComment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentService {

    //根据活动ID获取活动评论
    @GET("Comment/showComment")
    Call<List<PersonComment>> getComment(@Header("Authorization") String token, @Query("activtiyId") int id);

    //添加评论
    @POST("Comment/addComment")
    Call<ResponseBody> addComment(@Header("Authorization") String token, @Body PersonAddComment personAddComment);
}
