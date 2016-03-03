package com.mathiasluo.designer.model.service;

import com.mathiasluo.designer.bean.Shot;

import retrofit2.http.GET;
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


}
