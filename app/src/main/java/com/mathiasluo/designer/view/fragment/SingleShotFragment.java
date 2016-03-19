package com.mathiasluo.designer.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.databinding.ActivityShotActivtyBinding;
import com.mathiasluo.designer.model.ImageModelImpl;
import com.mathiasluo.designer.utils.LogUtils;

/**
 * Created by MathiasLuo on 2016/3/18.
 */
public class SingleShotFragment extends BaseLazyFragment {


    private Shot shot;
    private ActivityShotActivtyBinding binding;
    private boolean isVisible;

    public SingleShotFragment(Shot shot) {
        this.shot = shot;
    }

    public SingleShotFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shot_activty, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    void onFirstUserVisible() {

        if (shot != null)
            setData();
    }


    @Override
    public void setData() {
        if (!isVisible) {
            showProgress();
            bindData();
        }
    }

    private void bindData() {
        binding.setShot(shot);
        ImageModelImpl.getInstance().loadImage(shot.getImages().getNormal(), binding.image);
        isVisible = true;
        closeProgress();
    }


    @Override
    public void setData(Shot shot) {
        this.shot = shot;
        LogUtils.e("我就想知道这里的binding是不是空的----->>>>" + (binding == null));
        if (binding != null) {
            bindData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isVisible = false;
    }


    @Override
    void onUserVisible() {
    }


    @Override
    void onFirstUserInvisible() {

    }

    @Override
    void onUserInvisible() {

    }

    @Override
    public void showProgress() {
        super.showProgress();
    }

    @Override
    public void closeProgress() {
        super.closeProgress();
    }
}
