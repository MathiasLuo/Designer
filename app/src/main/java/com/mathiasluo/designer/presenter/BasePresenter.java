package com.mathiasluo.designer.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by MathiasLuo on 2016/3/2.
 */
public abstract class BasePresenter<T> {

    protected Reference<T> mViewRef;


    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }


    protected T getView() {
        if (mViewRef.get() != null)
            return mViewRef.get();
        return null;
    }


    public boolean isAttachedView() {
        return mViewRef.get() == null ? false : true;
    }

    public void detachView() {
        if (mViewRef.get() != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public void OnViewResume() {

    }

    public void OnViewCreate() {

    }

    public void onViewDestroy() {

    }


}
