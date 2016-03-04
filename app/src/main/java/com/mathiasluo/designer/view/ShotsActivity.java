package com.mathiasluo.designer.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mathiasluo.designer.R;
import com.mathiasluo.designer.adpter.ShotAdapter;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.presenter.ShotsPresenter;
import com.mathiasluo.designer.view.IView.IShotsActivity;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShotsActivity extends BaseActivity<ShotsActivity, ShotsPresenter> implements IShotsActivity<Shot> {


    @Bind(R.id.content_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.menu_item)
    FloatingActionButton menuItem;
    @Bind(R.id.menu)
    FloatingActionMenu menu;
    @Bind(R.id.content_swipe)
    SwipeRefreshLayout mRefreshLayout;


    private RecyclerView.LayoutManager mLayoutManager;
    private List<Shot> dataList;
    private ShotAdapter mShotAdapter;
    private int lastVisableItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shots);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mRecyclerView.setLayoutManager(mLayoutManager = new LinearLayoutManager(this));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(new IconicsDrawable(this).sizeDp(16).icon("gmi_menu").color(Color.WHITE));

        setListener();
    }

    private void setListener() {

        mDrawerLayout.addDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close));

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestNewDate();
                mRefreshLayout.setRefreshing(true);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisableItem = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisableItem + 1 == mShotAdapter.getItemCount()) {
                    mPresenter.requestDate();
                }

            }
        });

    }

    @Override
    protected ShotsPresenter creatPresenter() {
        return new ShotsPresenter();
    }

    @Override
    public void showShots(List<Shot> list) {

        if (dataList == null) {
            dataList = list;
            mShotAdapter = new ShotAdapter(list);
            mRecyclerView.setAdapter(mShotAdapter);
        } else {
            closeProgress();
            dataList = list;
            mShotAdapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_shots_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }
}
