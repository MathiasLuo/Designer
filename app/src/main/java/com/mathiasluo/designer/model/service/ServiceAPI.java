package com.mathiasluo.designer.model.service;

import com.mathiasluo.designer.bean.AccessToken;
import com.mathiasluo.designer.bean.AuthBody;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.bean.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public interface ServiceAPI {

    String API = "https://api.dribbble.com/v1/";


    @GET("shots")
    Observable<Shot[]> getShots(@Query("list") String list,
                                @Query("timeframe") String timeframe,
                                @Query("date") String date,
                                @Query("sort") String sort,
                                @Query("page") int page,
                                @Query("per_page") int per_page);

    @GET("shots")
    Observable<Shot[]> getShots(@Query("page") int page,
                                @Query("per_page") int per_page);


    @POST("https://dribbble.com/oauth/token")
    Observable<AccessToken> getAccessToken(@Body AuthBody body);


    @GET("user")
    Observable<User> getUserWithAccessToken(@Query("access_token") String accessToken);


    @GET("users/{username}")
    Observable<User> getUserInfo(@Path("username") String username);


    @GET("shots/{shot_id}/likes")
    Observable<User> getShotLikes(@Path("shot_id") String shot_id);

    @GET("shots/{shot_id}/likes")
    Observable cheakIsLikeShot(@Path("shot_id") String shot_id);

    @POST("shots/{shot_id}/likes")
    Observable likeShot(@Path("shot_id") String shot_id);



}
