package com.mathiasluo.designer.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.bean.User;
import com.mathiasluo.designer.presenter.MainActivityPresenter;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.view.IView.IMainActivity;
import com.mathiasluo.designer.view.fragment.ShotsFragment;
import com.mathiasluo.designer.view.widget.CircleImageView;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainActivity, MainActivityPresenter> implements IMainActivity {

    @Bind(R.id.main_frameLayout)
    FrameLayout mFrameLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    public final static int REQUTECODE = 0001;

    CircleImageView mUserAvater;
    TextView mUserName;
    TextView mUserDesgcribe;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        mPresenter.loadUserInfoFromRealm();
        setShotsFragment(new ShotsFragment());
    }

    private void setShotsFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_frameLayout, fragment);
        transaction.commit();
    }


    public void init() {
        mUserAvater = (CircleImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_img);
        mUserName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
        mUserDesgcribe = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_describe);
        mNavigationView.setNavigationItemSelectedListener(
                item -> {
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
        //头像登录
        mUserAvater.setOnClickListener(v -> {
            if (getPresenter().getCurrentUser() == null)
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUTECODE);
                //不关闭抽屉，便于登陆后直观看到效果
                // mDrawerLayout.closeDrawers();
            else {
                //如果已经登陆，就打开个人主页
            }
        });
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            LogUtils.d("用户登录成功");
            //加载设置用户信息
            mPresenter.loadUserInfoFromRealm();
        }
    }

    @Override
    protected MainActivityPresenter creatPresenter() {
        return new MainActivityPresenter();
    }

    @Override
    public void loadUerInfo(User user) {
        //设置用户头像
        mPresenter.loadImageWithurl(user.getAvatarUrl(), mUserAvater);
        //设置用户其他信息...
        mUserName.setText(user.getUsername());
        mUserDesgcribe.setText(user.getHtmlUrl());
    }

    @Override
    public void addDrawerListener(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).sizeDp(16).icon("gmi_menu").color(Color.WHITE));
        mDrawerLayout.addDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close));
    }


}
