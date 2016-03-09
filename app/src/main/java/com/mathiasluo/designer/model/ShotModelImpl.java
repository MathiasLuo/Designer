package com.mathiasluo.designer.model;

import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.bean.Team;
import com.mathiasluo.designer.model.IModel.ShotModel;
import com.mathiasluo.designer.model.service.ServiceAPI;
import com.mathiasluo.designer.model.service.ServiceAPIModel;
import com.mathiasluo.designer.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public class ShotModelImpl implements ShotModel {

    private Realm mRealm;
    private static ShotModelImpl instance;
    private static ServiceAPI mServiceAPI;

    public final static int PAGE = 1;
    public final static int PER_PAGE = 10;


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

        mServiceAPI = ServiceAPIModel.provideServiceAPI(ServiceAPIModel.provideOkHttpClient());
    }


    @Override
    public Observable<List<Shot>> getShotsFromServer(int page, int per_page) {
        return mServiceAPI.getShots(page, per_page)
                .map(new Func1<Shot[], List<Shot>>() {
                    @Override
                    public List<Shot> call(Shot[] shots) {
                        List<Shot> shotList = new ArrayList<>();
                        for (int i = 0; i < shots.length; i++) {
                            Shot val = shots[i];
                            if (val.getTeam() == null)
                                val.setTeam(new Team(new Integer(12345)));
                            shotList.add(val);
                        }
                        return shotList;
                    }
                });
    }

    @Override
    public void saveShotsToRealm(List<Shot> shots) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(shots);
        mRealm.commitTransaction();
        mRealm.close();
        LogUtils.d("保存了新来的Shot");
    }

    @Override
    public void clearShotsToRealm() {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.clear(Shot.class);
        mRealm.commitTransaction();
        mRealm.close();
        LogUtils.d("清除了所有Shot");
    }

    @Override
    public void closeSomeThing() {
        mRealm.close();
    }


    @Override
    public Observable<List<Shot>> loadShots() {
        mRealm = Realm.getDefaultInstance();
        Observable observable = mRealm.where(Shot.class)
                .findAllAsync()
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<RealmResults<Shot>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Shot> shots) {
                        return shots.isLoaded();
                    }
                })
                .map(new Func1<RealmResults<Shot>, List<Shot>>() {
                    @Override
                    public List<Shot> call(RealmResults<Shot> shots) {
                        ArrayList<Shot> LIST = new ArrayList<Shot>();
                        for (Shot shot : shots) LIST.add(shot);

                        return LIST;
                    }
                });
        return observable;
    }


}
