package com.mathiasluo.designer.view;

import com.mathiasluo.designer.adpter.ShotAdapter;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.databinding.ActivityShotActivtyBinding;
import com.mathiasluo.designer.model.IModel.ImageModel;
import com.mathiasluo.designer.model.IModel.ShotModel;
import com.mathiasluo.designer.model.ImageModelImpl;
import com.mathiasluo.designer.model.ShotModelImpl;

import java.util.List;

/**
 * Created by MathiasLuo on 2016/3/17.
 */
public class ShotActivityModel {
    ActivityShotActivtyBinding binding;
    ShotModel shotModel;
    ImageModel imageModel;
    int mCurrentPosition;

    public ShotActivityModel(ActivityShotActivtyBinding binding, int mCurrentPosition) {
        shotModel = ShotModelImpl.getInstance();
        imageModel = ImageModelImpl.getInstance();

        this.binding = binding;
        this.mCurrentPosition = mCurrentPosition;
        init();
    }

    private void init() {
        imageModel.loadImage(ShotAdapter.mDataList.get(mCurrentPosition).getImages().getNormal(), binding.image);
        binding.setShot(ShotAdapter.mDataList.get(mCurrentPosition));
    }


}
