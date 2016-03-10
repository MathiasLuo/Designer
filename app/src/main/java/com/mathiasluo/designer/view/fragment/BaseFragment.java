package com.mathiasluo.designer.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.mathiasluo.designer.presenter.BasePresenter;

/**
 * Created by MathiasLuo on 2016/3/2.
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {
    protected T mPresenter;
    protected Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = creatPresenter();
        mHandler = new Handler();
        mPresenter.attachView((V) this);
        mPresenter.OnViewCreate();
    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.OnViewResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mPresenter.onViewDestroy();
    }
    @Override
    public void onStop() {
        super.onStop();
        mPresenter.OnViewStop();
    }

    protected T getPresenter() {
        return mPresenter;
    }

    protected abstract T creatPresenter();

    public Handler getHandler() {
        return mHandler;
    }


}
