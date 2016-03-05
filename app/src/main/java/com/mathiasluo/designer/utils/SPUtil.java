package com.mathiasluo.designer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MathiasLuo on 2016/3/5.
 */
public class SPUtil {

    private final static String DATA = "ACCESSTOKEN";
    public final static String NOACESSTOKEN = "0001";

    public final static void putAccesToken(Context context, String accessToken) {
        SharedPreferences share = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(DATA, accessToken);
        editor.commit();
    }

    public final static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(DATA, NOACESSTOKEN);
        return accessToken;
    }


    public final static void removeAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences share = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(DATA, NOACESSTOKEN);
        editor.commit();
    }

}
