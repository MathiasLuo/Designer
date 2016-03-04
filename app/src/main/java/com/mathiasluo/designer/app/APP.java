package com.mathiasluo.designer.app;

import android.app.Application;
import android.support.v4.view.LayoutInflaterCompat;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public class APP extends Application {
    public static APP instance;

    public final static APP getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);
    }
}
