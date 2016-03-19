package com.mathiasluo.designer.model.IModel;

import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by MathiasLuo on 2016/3/2.
 */
public interface ImageModel {

    void loadImage(String url, ImageView imageView);

    void loadImageWithTargetView(String url, SimpleTarget simpleTarget);

    void loadImageWithListener(String url, SimpleTarget simpleTarget, RequestListener listener);
}
