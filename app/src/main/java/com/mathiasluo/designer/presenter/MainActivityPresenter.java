package com.mathiasluo.designer.presenter;

import android.graphics.Bitmap;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.model.IModel.UserModel;
import com.mathiasluo.designer.model.ImageModelImpl;
import com.mathiasluo.designer.model.UserModelImpl;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.utils.SPUtil;
import com.mathiasluo.designer.utils.ToastUtil;
import com.mathiasluo.designer.utils.UserUtil;
import com.mathiasluo.designer.view.activity.MainActivity;
import com.mathiasluo.designer.view.widget.CircleImageView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MathiasLuo on 2016/3/10.
 */
public class MainActivityPresenter extends BasePresenter<MainActivity> {
    private UserModel mUserModel;

    private User mCurrentUser;

    public MainActivityPresenter() {
        mUserModel = UserModelImpl.getInstance();
    }

    public void loadUserInfoFromRealm() {
        mUserModel.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            if (user != null) {
                                mCurrentUser = user;
                                getView().loadUerInfo(mCurrentUser);
                                //从网络更新用户消息
                                loadUserInfoFromNet();
                            } else ToastUtil.Toast("你还没登录哟\n登陆后会更加精彩哟");
                        }
                        , throwable -> {
                            LogUtils.e("为什么又在这里出现问题了" + throwable.toString());
                            ToastUtil.Toast("出现了不可预计的错误...");
                        });
    }

    public void loadUserInfoFromNet() {
        mUserModel.getUseWithAccessToken(SPUtil.getAccessToken(APP.getInstance()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        if (!UserUtil.enqual(user, mCurrentUser)) {
                            user.setAccessToken(mCurrentUser.getAccessToken());
                            LogUtils.d("更新USER数据成功");
                            mUserModel.saveUserToReaml(user);
                            return user;
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        if (user != null) {
                            mCurrentUser = user;
                            getView().loadUerInfo(mCurrentUser);
                        }
                        return;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e("为什么从网络上更新用户消息失败呀------>>>>" + throwable.toString());
                    }
                });
    }

   public void loadImageWithurl(String url, CircleImageView imageView) {
        ImageModelImpl.getInstance().loadImageWithTargetView(url, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageView.setImageBitmap(resource);
            }
        });
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }
}
