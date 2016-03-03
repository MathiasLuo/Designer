package com.mathiasluo.designer.model.service;

import android.text.TextUtils;
import android.util.Log;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.app.APP;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public class HeaderInterceptors implements Interceptor {
    public static String Authorization = APP.getInstance().getString(R.string.Client_Access_Token);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!TextUtils.isEmpty(Authorization))
            request = request.newBuilder()
                    .header("Authorization", Authorization)
                    .build();
        return chain.proceed(request);
    }
}
