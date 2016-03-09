package com.mathiasluo.designer.model.IModel;

import com.mathiasluo.designer.bean.Shot;

import java.util.List;

import rx.Observable;

/**
 * Created by MathiasLuo on 2016/3/2.
 */
public interface ShotModel {

    Observable<List<Shot>> loadShots();

    Observable<List<Shot>> getShotsFromServer(int page, int per_page);

    void saveShotsToRealm(List<Shot> shots);

    void closeSomeThing();

    void clearShotsToRealm();


}
