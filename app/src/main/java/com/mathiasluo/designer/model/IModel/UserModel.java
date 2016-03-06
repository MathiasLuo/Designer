package com.mathiasluo.designer.model.IModel;

import com.mathiasluo.designer.bean.User;

import rx.Observable;

/**
 * Created by MathiasLuo on 2016/3/5.
 */
public interface UserModel {

    Observable<User> getCurrentUser();

    Observable deleteCurrentUser();

    Observable<User> queryUserFromReaml(String accessToken);

    Observable<String> Login2GetAccessToken(String oauthCode);

    Observable<User> getUseWithAccessToken(String accessToken);

    Observable<User> setCurrentUser(User user);

    void saveUserToReaml(User user);
}
