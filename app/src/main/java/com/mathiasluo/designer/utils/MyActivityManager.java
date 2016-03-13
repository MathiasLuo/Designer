package com.mathiasluo.designer.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * Created by MathiasLuo on 2016/3/6.
 */
public class MyActivityManager {

    private WeakReference<Activity> sCurrentActivity;

    private MyActivityManager() {
    }

    public final void init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    public final static MyActivityManager getInstance() {
        return ActivityManangerHiolder.instance;
    }

    private final static class ActivityManangerHiolder {
        private final static MyActivityManager instance = new MyActivityManager();
    }



    public Activity getCurentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivity != null) {
            currentActivity = sCurrentActivity.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivity = new WeakReference<Activity>(activity);
    }

}
