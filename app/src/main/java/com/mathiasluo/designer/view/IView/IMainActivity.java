package com.mathiasluo.designer.view.IView;

import android.os.Handler;
import android.support.v7.widget.Toolbar;

import com.mathiasluo.designer.bean.User;

/**
 * Created by MathiasLuo on 2016/3/10.
 */
public interface IMainActivity {

    void loadUerInfo(User user);

    Handler getHandler();

    void addDrawerListener(Toolbar toolbar);
}
