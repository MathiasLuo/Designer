package com.mathiasluo.designer.view.IView;

import android.os.Handler;

import com.mathiasluo.designer.bean.User;

import java.util.List;

/**
 * Created by MathiasLuo on 2016/3/3.
 */
public interface IShotsActivity<T> {

    void showShots(List<T> list, int current_page);

    void showProgress();

    void closeProgress();

    void uploadUserInfo(User user);

    Handler getHandler();
}
