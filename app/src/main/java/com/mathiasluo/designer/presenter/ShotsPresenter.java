package com.mathiasluo.designer.presenter;

import android.graphics.Bitmap;
import android.os.Handler;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.model.IModel.ShotModel;
import com.mathiasluo.designer.model.IModel.UserModel;
import com.mathiasluo.designer.model.ImageModelImpl;
import com.mathiasluo.designer.model.ShotModelImpl;
import com.mathiasluo.designer.model.UserModelImpl;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.utils.SPUtil;
import com.mathiasluo.designer.utils.ToastUtil;
import com.mathiasluo.designer.utils.UserUtil;
import com.mathiasluo.designer.view.ShotsActivity;
import com.mathiasluo.designer.view.widget.CircleImageView;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MathiasLuo on 2016/3/3.
 */
public class ShotsPresenter extends BasePresenter<ShotsActivity> {

    private ShotModel shotModel;
    private UserModel userModel;
    private static User mCurrentUser;
    int page = 3;
    int per_page = 10;

    Shot mShot;

    public void loadDataFromReaml() {
        userModel = UserModelImpl.getInstance();
        shotModel = ShotModelImpl.getInstance();
        //开始应用就加载用户信息
        showUserInfo();
        loadShotsFromRealm();
        requestNewDate();

    }

    public void loadShotsFromServer(int page, int per_page, boolean isShow) {
        shotModel.getShotsFromServer(page, per_page)
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        getView().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (isShow) showProgress();
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        closeProgress();
                        if (mShot != null && (mShot.getId().equals(shots.get(0).getId()) && isShow)) {
                            ToastUtil.Toast("已经是最新的数据了");
                        } else if (mShot != null && !(mShot.getId().equals(shots.get(0).getId()) && isShow)) {
                            shotModel.clearShotsToRealm();
                            shotModel.saveShotsToRealm(shots);
                            getView().showShots(shots, page);
                        } else {
                            getView().showShots(shots, page);
                            LogUtils.d("在这里展示了数据" + shots.size());
                        }
                        mShot = shots.get(0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        closeProgress();
                        ToastUtil.Toast("网络刷新出现了错误");
                        LogUtils.e("从server中加载数据出错----->>>>" + throwable.toString());
                    }
                });
    }

    public void loadShotsFromRealm() {
        shotModel.loadShots()
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        getView().showShots(shots, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e("从Realm中加载数据出错----->>>>" + throwable.toString());
                    }
                });
    }

    @Override
    public void OnViewResume() {
        super.OnViewResume();
    }

    @Override
    public void onViewDestroy() {
        super.onViewDestroy();
        shotModel.closeSomeThing();
    }

    public void updataUserInfo() {
        userModel.getUseWithAccessToken(SPUtil.getAccessToken(APP.getInstance()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        if (!UserUtil.enqual(user, mCurrentUser)) {
                            user.setAccessToken(mCurrentUser.getAccessToken());
                            userModel.saveUserToReaml(user);
                            return user;
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        if (user != null) {
                            mCurrentUser = user;
                            LogUtils.d("从网络上更新用户成功啦");
                            getView().uploadUserInfo(mCurrentUser);
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


    public void requestDate() {
        loadShotsFromServer(page, per_page, false);
        page++;
    }

    public void requestNewDate() {
        loadShotsFromServer(ShotModelImpl.PAGE, ShotModelImpl.PER_PAGE, true);
    }

    public void showUserInfo() {
        userModel.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            if (user != null) {
                                mCurrentUser = user;
                                getView().uploadUserInfo(mCurrentUser);
                                //从网络更新用户消息
                                updataUserInfo();
                            } else ToastUtil.Toast("你还没登录哟\n登陆后会更加精彩哟");
                        }
                        , throwable -> {
                            LogUtils.e("为什么又在这里出现问题了" + throwable.toString());
                            ToastUtil.Toast("出现了不可预计的错误...");
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

    private void showProgress() {
        getView().showProgress();
    }

    private void closeProgress() {
        getView().closeProgress();
    }
}
