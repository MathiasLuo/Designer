package com.mathiasluo.designer.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.adpter.ShotAdapter;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.databinding.ActivityShotActivtyBinding;
import com.mathiasluo.designer.model.ImageModelImpl;
import com.mathiasluo.designer.presenter.ShotsPresenter;
import com.mathiasluo.designer.view.ShotActivityModel;
import com.mikepenz.iconics.context.IconicsContextWrapper;


public class ShotActivty extends AppCompatActivity {

    private ActivityShotActivtyBinding binding;
    private int mCurrentPosition;
    ShotActivityModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mCurrentPosition = getIntent().getIntExtra("position", 1);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shot_activty);
        viewModel = new ShotActivityModel(binding, mCurrentPosition);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
