package com.mathiasluo.designer.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.model.ShotModelImpl;
import com.mathiasluo.designer.presenter.ShotsPresenter;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.view.fragment.SingleShotFragment;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ViewPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
    private List<SingleShotFragment> fragments = new ArrayList<>();
    public static int mCurrentPosition = -1;
    private ViewPager mViewPager;

    public ViewPagerAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(10);
    }

    public void addFragment(SingleShotFragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    public void addDatasAndPosition(int position) {
        this.mCurrentPosition = position;
        for (Shot shot : ShotAdapter.mDataList) {
            fragments.add(new SingleShotFragment(shot));
        }
        notifyDataSetChanged();
    }

    public void addDatasAndPosition(int position, List<Shot> datas) {
        this.mCurrentPosition = position;
        int index = fragments.size() - 1;
        for (int i = datas.size(); i > 0; i--) {
            fragments.get(index).setData(datas.get(i - 1));
            index--;
        }
    }

    public void addDatasAndPosition(int position, int count) {
        this.mCurrentPosition = position;
        for (int i = 0; i < count; i++) {
            fragments.add(new SingleShotFragment());
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        if (position == fragments.size() - 1) {
            //这里更新数据了
            addDatasAndPosition(position, 10);
            ShotModelImpl.getInstance()
                    .getShotsFromServer(ShotsPresenter.page, 10)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Shot>>() {
                        @Override
                        public void call(List<Shot> shots) {
                            addDatasAndPosition(position, shots);
                            ShotAdapter.mDataList.addAll(shots);
                            ++ShotsPresenter.page;
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            LogUtils.e(throwable.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
}