package com.mathiasluo.designer.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.adpter.ShotAdapter;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.presenter.ShotsPresenter;
import com.mathiasluo.designer.view.IView.IShotsActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShotsActivity extends BaseActivity<ShotsActivity, ShotsPresenter> implements IShotsActivity<Shot> {


    @Bind(R.id.content_recyclerView)
    RecyclerView contentRecyclerView;

    private List<Shot> dataList;
    private ShotAdapter mShotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shots);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            contentRecyclerView.setAdapter(mShotAdapter);
        } else {
            dataList = list;
            mShotAdapter.notifyDataSetChanged();
        }
    }


}
