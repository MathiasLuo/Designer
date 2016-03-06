package com.mathiasluo.designer.model;

import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.AccessToken;
import com.mathiasluo.designer.bean.AuthBody;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.model.IModel.UserModel;
import com.mathiasluo.designer.model.service.ServiceAPI;
import com.mathiasluo.designer.model.service.ServiceAPIModel;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.utils.SPUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MathiasLuo on 2016/3/5.
 */
public class UserModelImpl implements UserModel {

    ServiceAPI mServiceAPI;

    private UserModelImpl() {
        mServiceAPI = ServiceAPIModel.provideServiceAPI(ServiceAPIModel.provideOkHttpClient());
    }

    public final static UserModel getInstance() {
        return UserModelHolder.instance;
    }

    private final static class UserModelHolder {
        private final static UserModel instance = new UserModelImpl();
    }

    @Override
    public Observable<User> getCurrentUser() {
        final String accessToken = SPUtil.getAccessToken(APP.getInstance());
        if (!accessToken.equals(SPUtil.NOACESSTOKEN)) {
            return queryUserFromReaml(accessToken);
        } else
            return Observable.just(null);
    }

    @Override
    public Observable deleteCurrentUser() {
        final String accessToken = SPUtil.getAccessToken(APP.getInstance());
        if (!accessToken.equals(SPUtil.NOACESSTOKEN)) {
            return queryUserFromReaml(accessToken)
                    .map(user -> {
                        Realm mReam = Realm.getDefaultInstance();
                        mReam.beginTransaction();
                        user.removeFromRealm();
                        mReam.commitTransaction();
                        return user;
                    });
        } else
            return null;
    }

    @Override
    public Observable<User> queryUserFromReaml(String accessToken) {

        LogUtils.e("accessToken" + accessToken);
        return Observable.just(accessToken)
                .flatMap(s -> {
                    Realm mRealm = Realm.getDefaultInstance();
                    return mRealm.where(User.class)
                            .equalTo("accessToken", s)
                            .findAllAsync()
                            .asObservable()
                            .flatMap(users ->
                            {
                                LogUtils.e("这里出现了问题吗" + users.toString());
                                return Observable.from(users);
                            });
                });

    }

    @Override
    public Observable<String> Login2GetAccessToken(String athuCode) {
        return mServiceAPI
                .getAccessToken(new AuthBody(athuCode))
                .flatMap(new Func1<AccessToken, Observable<String>>() {
                    @Override
                    public Observable<String> call(AccessToken accessToken) {
                        return Observable.just(accessToken.getAccess_token());
                    }
                });
    }

    @Override
    public Observable<User> getUseWithAccessToken(String accessToken) {
        return mServiceAPI.getUserWithAccessToken(accessToken);
    }

    @Override
    public Observable<User> setCurrentUser(User user) {

        return Observable.just(user)
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        saveUserToReaml(user);
                        return user;
                    }
                });

    }

    @Override
    public void saveUserToReaml(User user) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
        mRealm.close();
    }


}
