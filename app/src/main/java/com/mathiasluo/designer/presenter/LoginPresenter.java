package com.mathiasluo.designer.presenter;

import android.app.Activity;

import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.AccessToken;
import com.mathiasluo.designer.bean.AuthBody;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.model.service.ServiceAPI;
import com.mathiasluo.designer.model.service.ServiceAPIModel;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.utils.SPUtil;
import com.mathiasluo.designer.view.LoginActivity;

import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MathiasLuo on 2016/3/4.
 */
public class LoginPresenter extends BasePresenter<LoginActivity> {

    ServiceAPI serviceAPI;
    private String accessToken;
    private Realm mRealm;


    @Override
    public void OnViewResume() {
        super.OnViewResume();
        serviceAPI = ServiceAPIModel.provideServiceAPI(ServiceAPIModel.provideOkHttpClient());
    }


    public void getAccesToken(final String athuCode) {
        getView().showLoading();
        serviceAPI
                .getAccessToken(new AuthBody(athuCode))
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<AccessToken, Observable<User>>() {
                    @Override
                    public Observable<User> call(AccessToken Token) {
                        accessToken = Token.getAccess_token();
                        SPUtil.putAccesToken(APP.getInstance(), accessToken);
                        return serviceAPI.getUserWithAccessToken(accessToken);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        closeLoading();
                        return user;
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        user.setAccessToken(athuCode);
                        mRealm = Realm.getDefaultInstance();
                        mRealm.beginTransaction();
                        mRealm.copyToRealmOrUpdate(user);
                        mRealm.commitTransaction();
                        mRealm.close();
                        getView().setResult(Activity.RESULT_OK);
                        getView().finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e("又是在这里出现了问题呀----->" + throwable.toString());
                    }
                });

    }


    public void showLoading() {
        getView().showLoading();
    }

    public void closeLoading() {
        getView().closeLoading();
    }

    @Override
    public void onViewDestroy() {
        super.onViewDestroy();

    }
}
