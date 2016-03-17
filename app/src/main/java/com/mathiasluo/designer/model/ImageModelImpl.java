package com.mathiasluo.designer.model;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mathiasluo.designer.R;
import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.model.IModel.ImageModel;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public class ImageModelImpl implements ImageModel {

    private static ImageModelImpl instance;

    private ImageModelImpl() {

    }

    public static ImageModelImpl getInstance() {

        if (instance == null) {
            synchronized (ImageModelImpl.class) {
                if (instance == null)
                    instance = new ImageModelImpl();
            }
        }
        return instance;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(APP.getInstance())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.img_on_laoding)
                .error(R.drawable.img_on_laoding)
                .crossFade()
                .into(imageView);
    }

    @Override
    public void loadImageWithTargetView(String url, SimpleTarget simpleTarget) {
        Glide.with(APP.getInstance())
                .load(url)
                .asBitmap()
              /*  .placeholder(R.drawable.img_on_laoding)
                .error(R.drawable.img_on_laoding)*/
                .into(simpleTarget);
    }
}
