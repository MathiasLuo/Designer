package com.mathiasluo.designer.model;

import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.model.IModel.UserModel;
import com.mathiasluo.designer.utils.SPUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by MathiasLuo on 2016/3/5.
 */
public class UserModelImpl implements UserModel {
    @Override
    public Observable<User> getCurrentUser() {
        final String accessToken = SPUtil.getAccessToken(APP.getInstance());
        if (!accessToken.equals(SPUtil.NOACESSTOKEN)) {
            return Observable.just(accessToken)
                    .flatMap(new Func1<String, Observable<User>>() {
                                 @Override
                                 public Observable<User> call(String s) {
                                     Realm mRealm = Realm.getDefaultInstance();
                                     return mRealm.where(User.class)
                                             .equalTo("accessToken", accessToken)
                                             .findAllAsync()
                                             .asObservable()
                                             .flatMap(new Func1<RealmResults<User>, Observable<User>>() {
                                                 @Override
                                                 public Observable<User> call(RealmResults<User> users) {
                                                     return Observable.from(users);
                                                 }
                                             });
                                 }
                             }
                    )
                    ;


        } else
            return null;
    }

    @Override
    public Observable deleteCurrentUser() {
        final String accessToken = SPUtil.getAccessToken(APP.getInstance());
        return null;
    }
}
