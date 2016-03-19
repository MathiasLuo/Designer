package com.mathiasluo.designer.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.view.fragment.SingleShotFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private List<Shot> mDatas;
    private int mCurrentPosition;
    private ViewPager mViewPager;

    public ViewPagerAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        this.mViewPager = viewPager;
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public void adddatasAndPosition(List<Shot> mDatas, int position) {
        this.mDatas = mDatas;
        this.mCurrentPosition = position;
        for (Shot shot : mDatas) {
            fragments.add(new SingleShotFragment(shot));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}