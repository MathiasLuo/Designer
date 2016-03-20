package com.mathiasluo.designer.view.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.adpter.ViewPagerAdapter;
import com.mikepenz.iconics.context.IconicsContextWrapper;

public class ShotActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shot);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mCurrentPosition = getIntent().getIntExtra("position", 1);
        init();
    }

    private void init() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mViewPager);
        mAdapter.adddatasAndPosition( mCurrentPosition);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

}
