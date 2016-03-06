package com.mathiasluo.designer.presenter;

import android.graphics.Bitmap;
import android.widget.ImageView;

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

import io.realm.RealmChangeListener;
import rx.android.schedulers.AndroidSchedulers;
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
    private List<Shot> mCurrentShots;


    public void loadDataFromReaml() {
        userModel = UserModelImpl.getInstance();
        shotModel = ShotModelImpl.getInstance();
        //开始应用就加载用户信息
        showUserInfo();
        shotModel
                .loadShotsWithListener(new RealmChangeListener() {
                                           @Override
                                           public void onChange() {
                                               onDataChange();
                                           }
                                       }
                )
                .subscribe(shots -> {
                    mCurrentShots = shots;
                    getView().showShots(shots);
                });
        shotModel.startUpdata();
        getView().showProgress();
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
                        LogUtils.d("和服务器上的数据一样不用更新");
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

    public void onDataChange() {
        shotModel
                .loadShots()
                .subscribe(shots -> {
                    if (shots.get(0).equals(mCurrentShots.get(0))) {
                     // ToastUtil.Toast("已经是最新的数据了");
                    } else
                        getView().showShots(shots);
                    getView().closeProgress();
                    mCurrentShots = shots;
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e("为什么从网络上更新Shot失败呀------>>>>" + throwable.toString());
                    }
                });
    }

    public void requestDate() {
        shotModel.startUpdata();
    }

    public void requestNewDate() {
        shotModel.requestNewContent();
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


}
