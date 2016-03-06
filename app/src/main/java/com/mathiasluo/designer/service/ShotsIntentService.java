package com.mathiasluo.designer.service;

import android.app.IntentService;
import android.content.Intent;

import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.bean.Team;
import com.mathiasluo.designer.model.service.ServiceAPI;
import com.mathiasluo.designer.model.service.ServiceAPIModel;
import com.mathiasluo.designer.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.functions.Action1;

/**
 * Created by MathiasLuo on 2016/3/2.
 */
public class ShotsIntentService extends IntentService {

    private final static String TAG = "ShotsIntentService";

    private ServiceAPI mServiceAPI;
    private Realm mRealm;
    private boolean clear_realm;
    private static List<Shot> mShots;


    public ShotsIntentService() {
        super(TAG);
        mServiceAPI = ServiceAPIModel.provideServiceAPI(ServiceAPIModel.provideOkHttpClient());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        clear_realm = intent.getBooleanExtra("clear_realm", false);
        int page_id = intent.getIntExtra("page_id", 1);
        int perPage = intent.getIntExtra("perPage", 10);
        mServiceAPI.getShots(page_id, perPage)
                .subscribe(shots -> {
                            mRealm = Realm.getDefaultInstance();
                            List<Shot> shotList = new ArrayList<>();
                            for (int i = 0; i < shots.length; i++) {
                                Shot val = shots[i];
                                if (val.getTeam() == null)
                                    val.setTeam(new Team(new Integer(12345)));
                                shotList.add(val);
                            }
                            saveShots(shotList);
                        },
                        throwable -> {
                            LogUtils.e("出现问题了哟========>>>>>>" + throwable.getMessage());
                        }

                );
    }

    private void saveShots(List<Shot> shots) {

        if (clear_realm && shots.get(0).equals(mShots.get(0))) {
            // 清除数据库，但是数据没变
        } else if (clear_realm && !shots.get(0).equals(mShots.get(0))) {
            mRealm.clear(Shot.class);
            saveShotsToRealm(shots);
        } else if (!clear_realm) {
            saveShotsToRealm(shots);
        }
        mShots = shots;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void saveShotsToRealm(List<Shot> shots) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(shots);
        mRealm.commitTransaction();
        mRealm.close();
    }
}
