package com.mathiasluo.designer.presenter;

import android.app.Activity;

import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.AccessToken;
import com.mathiasluo.designer.bean.AuthBody;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.model.IModel.UserModel;
import com.mathiasluo.designer.model.UserModelImpl;
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
    private UserModel mUserModel;

    @Override
    public void OnViewResume() {
        super.OnViewResume();
        serviceAPI = ServiceAPIModel.provideServiceAPI(ServiceAPIModel.provideOkHttpClient());
        mUserModel = UserModelImpl.getInstance();
    }

    public void getAccesToken(final String athuCode) {
        mUserModel.Login2GetAccessToken(athuCode)
                .flatMap(new Func1<String, Observable<User>>() {
                    @Override
                    public Observable<User> call(String s) {
                        accessToken = s;
                        SPUtil.putAccesToken(APP.getInstance(), s);
                        return mUserModel.getUseWithAccessToken(s);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        user.setAccessToken(accessToken);
                        mUserModel.saveUserToReaml(user);
                        return user;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
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
}
