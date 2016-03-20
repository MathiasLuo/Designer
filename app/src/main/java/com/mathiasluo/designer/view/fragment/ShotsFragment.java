package com.mathiasluo.designer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.adpter.ShotAdapter;
import com.mathiasluo.designer.adpter.ViewPagerAdapter;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.presenter.ShotsPresenter;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.view.IView.IMainActivity;
import com.mathiasluo.designer.view.IView.IShotsFragment;
import com.mathiasluo.designer.view.activity.MainActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MathiasLuo on 2016/3/10.
 */
public class ShotsFragment extends BaseFragment<ShotsFragment, ShotsPresenter> implements IShotsFragment<Shot> {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.content_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.content_swipe)
    SwipeRefreshLayout mRefreshLayout;


    private int lastVisableItem;

    private RecyclerView.LayoutManager mLayoutManager;

    private ShotAdapter mShotAdapter;

    private List<Shot> dataList;

    @Override
    protected ShotsPresenter creatPresenter() {
        return new ShotsPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_content_shots, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        mPresenter.loadDataFromReaml();
    }


    public void init() {
        ((IMainActivity) getActivity()).addDrawerListener(mToolbar);
        mRecyclerView.setLayoutManager(mLayoutManager = new LinearLayoutManager(getActivity()));
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.requestNewDate();
            mPresenter.updataUserInfo();
            showProgress();
        });
        //上拉加载
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisableItem = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtils.e("lastVisableItem=" + lastVisableItem + "\n" + "mShotAdapter.mDataList.size()=" + mShotAdapter.mDataList.size());
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisableItem == mShotAdapter.mDataList.size()) {
                    mPresenter.requestDate();
                }
            }
        });
    }

    @Override
    public void showShots(List<Shot> list, int current_page) {
        if (dataList == null) {
            dataList = list;
            mShotAdapter = new ShotAdapter(list, getActivity());
            mRecyclerView.setAdapter(mShotAdapter);
        } else {
            mShotAdapter.addMoreData(list, current_page);
        }
    }

    @Override
    public void showProgress() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void closeProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void uploadUserInfo(User user) {
        ((MainActivity) getActivity()).loadUerInfo(user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (ShotAdapter.mDataList != null && ViewPagerAdapter.mCurrentPosition != -1) {
            mShotAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(ViewPagerAdapter.mCurrentPosition);
        }

    }
}
