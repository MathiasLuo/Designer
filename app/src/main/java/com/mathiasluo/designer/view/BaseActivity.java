package com.mathiasluo.designer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mathiasluo.designer.presenter.BasePresenter;

/**
 * Created by MathiasLuo on 2016/3/2.
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = creatPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.OnViewResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mPresenter.onViewDestroy();
    }


    protected abstract T creatPresenter();
}
