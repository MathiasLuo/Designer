package com.mathiasluo.designer.view.activity;

import android.os.Bundle;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.mathiasluo.designer.presenter.BasePresenter;
import com.mathiasluo.designer.view.activity.BaseActivity;

/**
 * Created by MathiasLuo on 2016/3/4.
 */
public abstract class SwipBaseActivity<V, T extends BasePresenter<V>> extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeRelateEnable(true)
                .setSwipeSensitivity(1);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

}
