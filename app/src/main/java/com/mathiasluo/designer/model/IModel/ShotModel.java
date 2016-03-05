package com.mathiasluo.designer.model.IModel;

import com.mathiasluo.designer.bean.Shot;

import java.util.List;

import io.realm.RealmChangeListener;
import rx.Observable;

/**
 * Created by MathiasLuo on 2016/3/2.
 */
public interface ShotModel {

    Observable<List<Shot>> loadShots();

    Observable<List<Shot>> loadShotsWithListener(RealmChangeListener listener);

    void startUpdata(int page, int per_page);

    void startUpdata();

    void requestNewContent();

    void closeSomeThing();

}
