package com.mathiasluo.designer.model.IModel;

import com.mathiasluo.designer.bean.User;

import rx.Observable;

/**
 * Created by MathiasLuo on 2016/3/5.
 */
public interface UserModel {

    Observable<User> getCurrentUser();

    Observable deleteCurrentUser();

}
