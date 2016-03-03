package com.mathiasluo.designer.presenter;

import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.model.IModel.ShotModel;
import com.mathiasluo.designer.model.ShotModelImpl;
import com.mathiasluo.designer.view.ShotsActivity;

import java.util.List;

import io.realm.RealmChangeListener;
import rx.functions.Action1;

/**
 * Created by MathiasLuo on 2016/3/3.
 */
public class ShotsPresenter extends BasePresenter<ShotsActivity> {

    private ShotModel shotModel;

    @Override
    public void OnViewResume() {
        super.OnViewResume();
        shotModel = ShotModelImpl.getInstance();
        shotModel.startUpdata(1, 10);

        shotModel
                .loadShotsWithListener(new RealmChangeListener() {
                    @Override
                    public void onChange() {
                        onDataChange();
                    }
                })
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        getView().showShots(shots);
                    }
                });
    }

    public void onDataChange() {
        shotModel
                .loadShots()
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        getView().showShots(shots);
                    }
                });
    }

}
