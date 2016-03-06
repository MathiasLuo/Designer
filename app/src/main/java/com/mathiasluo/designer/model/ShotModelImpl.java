package com.mathiasluo.designer.model;

import android.content.Intent;

import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.model.IModel.ShotModel;
import com.mathiasluo.designer.service.ShotsIntentService;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public class ShotModelImpl implements ShotModel {

    private Realm mRealm;
    private static ShotModelImpl instance;

    private final static int PAGE = 1;
    private final static int PER_PAGE = 10;

    private int page = 1;
    private int perPage = 10;

    public final static ShotModelImpl getInstance() {
        if (instance == null) {
            synchronized (ShotModelImpl.class) {
                if (instance == null)
                    instance = new ShotModelImpl();
            }
        }
        return instance;
    }

    private ShotModelImpl() {
        mRealm = Realm.getDefaultInstance();
    }


    @Override
    public Observable<List<Shot>> loadShotsWithListener(final RealmChangeListener listener) {

        Observable observable = mRealm.where(Shot.class).findAll().asObservable()
                .filter(new Func1<RealmResults<Shot>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Shot> shots) {
                        shots.addChangeListener(listener);
                        return shots.isLoaded();
                    }
                });
        return observable;
    }

    @Override
    public void startUpdata(int page, int per_page, boolean clear_realm) {

        Intent intentService = new Intent(APP.getInstance(), ShotsIntentService.class);
        intentService.putExtra("page_id", page);
        intentService.putExtra("perPage", per_page);
        intentService.putExtra("clear_realm", clear_realm);
        APP.getInstance().startService(intentService);
    }

    @Override
    public void startUpdata() {
        startUpdata(page, perPage, false);
        page++;
    }

    @Override
    public void requestNewContent() {
        startUpdata(PAGE, PER_PAGE, true);
    }

    @Override
    public void closeSomeThing() {
        mRealm.close();
    }


    @Override
    public Observable<List<Shot>> loadShots() {
        Observable observable = mRealm.where(Shot.class)
                .findAllAsync()
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .filter(shots -> shots.isLoaded())
                .map(new Func1<RealmResults<Shot>, RealmResults<Shot>>() {
                    @Override
                    public RealmResults<Shot> call(RealmResults<Shot> shots) {
                        return shots;
                    }
                });
        return observable;
    }
}
