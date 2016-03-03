package com.mathiasluo.designer.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mathiasluo.designer.R;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.model.ImageModelImpl;
import com.mathiasluo.designer.view.widget.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MathiasLuo on 2016/3/3.
 */
public class ShotAdapter extends RecyclerView.Adapter<ShotAdapter.ViewHolder> {

    private List<Shot> mDataList;

    public ShotAdapter(List<Shot> mDataList) {
        this.mDataList = mDataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shot_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Shot shot = mDataList.get(position);

        holder.commentsCountText.setText(shot.getCommentsCount().toString());
        holder.likeCountText.setText(shot.getLikesCount().toString());
        holder.usernameText.setText(shot.getUser().getUsername().toString());
        holder.viewsCountText.setText(shot.getViewsCount().toString());

        ImageModelImpl.getInstance().loadImage(shot.getImages().getNormal(), holder.shotImage);
        ImageModelImpl.getInstance().loadImageWithTargetView(shot.getUser().getAvatarUrl(), new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                holder.avatarImage.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.shot_image)
        ImageView shotImage;
        @Bind(R.id.avatar_image)
        CircleImageView avatarImage;
        @Bind(R.id.username_text)
        TextView usernameText;
        @Bind(R.id.viewsCount_text)
        TextView viewsCountText;
        @Bind(R.id.commentsCount_text)
        TextView commentsCountText;
        @Bind(R.id.likeCount_text)
        TextView likeCountText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
