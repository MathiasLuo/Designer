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


    public ShotsIntentService() {
        super(TAG);
        mServiceAPI = ServiceAPIModel.provideServiceAPI(ServiceAPIModel.provideOkHttpClient());

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int page_id = intent.getIntExtra("page_id", 1);
        int perPage = intent.getIntExtra("perPage", 10);
        mServiceAPI.getShots(page_id, perPage)
                .subscribe(new Action1<Shot[]>() {
                               @Override
                               public void call(Shot[] shots) {
                                   mRealm = Realm.getDefaultInstance();
                                   List<Shot> shotList = new ArrayList<Shot>();
                                   for (int i = 0; i < shots.length; i++) {
                                       Shot val = shots[i];
                                       if (val.getTeam() == null)
                                           val.setTeam(new Team(new Integer(12345)));
                                       shotList.add(val);
                                   }
                                   saveShots(shotList);
                               }
                           }

                        , new Action1<Throwable>()

                        {
                            @Override
                            public void call(Throwable throwable) {
                               LogUtils.e("出现问题了哟========>>>>>>" + throwable.getMessage());
                            }
                        }

                );
    }

    private void saveShots(List<Shot> shots) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(shots);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
