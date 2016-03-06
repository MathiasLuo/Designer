package com.mathiasluo.designer.view.IView;

import com.mathiasluo.designer.bean.User;

import java.util.List;

/**
 * Created by MathiasLuo on 2016/3/3.
 */
public interface IShotsActivity<T> {

    void showShots(List<T> list);

    void showProgress();

    void closeProgress();

    void uploadUserInfo(User user);
}
