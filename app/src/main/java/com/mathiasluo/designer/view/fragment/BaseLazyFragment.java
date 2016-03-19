package com.mathiasluo.designer.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.utils.LogUtils;


abstract class BaseLazyFragment extends Fragment {
    private static final String TAG = BaseLazyFragment.class.getSimpleName();
    private boolean isPrepared;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    public void showProgress() {
        LogUtils.e("在showProgress中哟");
    }

    public void closeProgress() {
        LogUtils.e("在closeProgress中哟");
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    abstract void onFirstUserVisible();

    /**
     * fragment可见（切换回来或者onResume）
     */
    abstract void onUserVisible();

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    abstract void onFirstUserInvisible();

    /**
     * fragment不可见（切换掉或者onPause）
     */
    abstract void onUserInvisible();

    abstract void setData();

    public abstract void setData(Shot shot);
}