package com.mathiasluo.designer.utils;

import android.widget.Toast;

/**
 * Created by MathiasLuo on 2016/3/6.
 */
public class ToastUtil {

    public final static void Toast(String string) {
        toast(string, Toast.LENGTH_SHORT);
    }

    public final static void toast(String string, int dur) {
        Toast.makeText(MyActivityManager.getInstance().getCurentActivity(), string, dur).show();
    }
}

