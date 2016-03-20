package com.mathiasluo.designer.model.service;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public class ServiceAPIModel {

    public final static ServiceAPI provideServiceAPI(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceAPI.API)
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(ServiceAPI.class);
    }

    public final static OkHttpClient.Builder initOhHttp() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor);
    }

    public final static OkHttpClient provideOkHttpClient() {
        return initOhHttp().addInterceptor(new HeaderInterceptors()).build();
    }

    public final static OkHttpClient provideOkHttpClient(String Authorization) {
        return initOhHttp().addInterceptor(new HeaderInterceptors(Authorization)).build();
    }


    public final static Gson provideGson() {
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getDeclaringClass().equals(RealmObject.class);
            }
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        return gson;

    }


}
