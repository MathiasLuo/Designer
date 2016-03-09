package com.mathiasluo.designer.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mathiasluo.designer.R;
import com.mathiasluo.designer.adpter.ShotAdapter;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.model.UserModelImpl;
import com.mathiasluo.designer.presenter.ShotsPresenter;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.view.IView.IShotsActivity;
import com.mathiasluo.designer.view.widget.CircleImageView;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

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

    private final static int REQUTECODE = 0001;

    CircleImageView mUserAvater;
    TextView mUserName;
    TextView mUserDesgcribe;

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
        mPresenter.loadDataFromReaml();
    }

    private void init() {

        mUserAvater = (CircleImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_img);
        mUserName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
        mUserDesgcribe = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_describe);
        mRecyclerView.setLayoutManager(mLayoutManager = new LinearLayoutManager(this));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(new IconicsDrawable(this).sizeDp(16).icon("gmi_menu").color(Color.WHITE));
        mUserAvater.setImageBitmap(new IconicsDrawable(this).sizeDp(66).icon("gmi_account").color(Color.WHITE).toBitmap());
        setListener();
    }

    private void setListener() {

        mDrawerLayout.addDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close));

        mNavigationView.setNavigationItemSelectedListener(
                item -> {
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisableItem + 1 == mShotAdapter.getItemCount()) {
                    mPresenter.requestDate();
                }

            }
        });
        //头像登录
        mUserAvater.setOnClickListener(v -> {
            startActivityForResult(new Intent(ShotsActivity.this, LoginActivity.class), REQUTECODE);
            //不关闭抽屉，便于登陆后直观看到效果
            // mDrawerLayout.closeDrawers();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            LogUtils.d("用户登录成功");
            //加载设置用户信息
            //..............
            mPresenter.showUserInfo();
        }
    }

    @Override
    protected ShotsPresenter creatPresenter() {
        return new ShotsPresenter();
    }

    @Override
    public void showShots(List<Shot> list, int current_page) {
        if (dataList == null) {
            dataList = list;
            mShotAdapter = new ShotAdapter(list);
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
        //设置用户头像
        mPresenter.loadImageWithurl(user.getAvatarUrl(), mUserAvater);
        //设置用户其他信息
        //........
        mUserName.setText(user.getUsername());
        mUserDesgcribe.setText(user.getHtmlUrl());
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

    @Override
    public Handler getHandler() {
        return super.getHandler();
    }
}
