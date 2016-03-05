package com.mathiasluo.designer.utils;


import com.orhanobut.logger.Logger;

public class LogUtils {

    private LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;

    public static void i(String msg, Object... args) {
        if (isDebug) {
            Logger.i(msg, args);
        }
    }

    public static void d(String msg, Object... args) {
        if (isDebug) {
            Logger.d(msg, args);
        }
    }

    public static void e(String msg, Object... args) {
        if (isDebug) {
            Logger.e(msg, args);
        }
    }

    public static void v(String msg, Object... args) {
        if (isDebug) {
            Logger.v(msg, args);
        }
    }

}
