package com.mathiasluo.designer.adpter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mathiasluo.designer.R;
import com.mathiasluo.designer.app.APP;
import com.mathiasluo.designer.bean.Shot;
import com.mathiasluo.designer.model.ImageModelImpl;
import com.mathiasluo.designer.utils.DensityUtil;
import com.mathiasluo.designer.view.activity.ShotActivity;
import com.mathiasluo.designer.view.widget.CircleImageView;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MathiasLuo on 2016/3/3.
 */
public class ShotAdapter extends RecyclerView.Adapter<ShotAdapter.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;

    public static List<Shot> mDataList;

    private int pre_page = 1;

    private Activity activity;


    public ShotAdapter(List<Shot> mDataList, Activity activity) {
        this.mDataList = mDataList;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.recycle_shot_item, parent, false);
            view.setTag(TYPE_ITEM);
        } else {
            view = inflater.inflate(R.layout.recycle_foot_view, parent, false);
            view.setTag(TYPE_FOOTER);
        }
        return new ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (position < mDataList.size()) {
            Shot shot = mDataList.get(position);
            holder.commentsCountText.setText(shot.getCommentsCount().toString());
            holder.likeCountText.setText(shot.getLikesCount().toString() + " 人喜欢这张照片。");
            holder.usernameText.setText(shot.getUser().getUsername().toString());
            // holder.viewsCountText.setText(shot.getViewsCount().toString());
            holder.mFavoriteImg.setOnClickListener(v -> {
                        if (!holder.ISLIKE) {
                            holder
                                    .mFavoriteImg
                                    .setImageDrawable(new IconicsDrawable(APP.getInstance(), "gmi_favorite")
                                            .color(Color.parseColor("#FF4081"))
                                    );
                            holder.ISLIKE = true;
                        } else {
                            holder
                                    .mFavoriteImg
                                    .setImageDrawable(new IconicsDrawable(APP.getInstance(), "gmi_favorite_outline")
                                            .color(Color.parseColor("#FF4081"))
                                    );
                            holder.ISLIKE = false;
                        }

                    }
            );

            ViewGroup.LayoutParams params = holder.shotImage.getLayoutParams();
            params.height = DensityUtil.dip2px(APP.getInstance(), shot.getHeight());
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.shotImage.setLayoutParams(params);


            ImageModelImpl.getInstance().loadImage(shot.getImages().getNormal(), holder.shotImage);
            ImageModelImpl.getInstance().loadImageWithTargetView(shot.getUser().getAvatarUrl(), new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                    holder.avatarImage.setImageBitmap(bitmap);
                }
            });

            holder.shotImage.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ShotActivity.class);
                    intent.putExtra("position", position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions
                                .makeSceneTransitionAnimation(activity, holder.shotImage, "shotImage");
                        activity.startActivity(intent, options.toBundle());
                    } else {
                        activity.startActivity(intent);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size() == 0 ? 0 : mDataList.size() + 1;
    }


    public void addMoreData(List<Shot> datas, int current_page) {

        if (current_page == pre_page) {
            mDataList = datas;
        } else
            mDataList.addAll(datas);

        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();

    }

    public static List<Shot> removeDuplicateDataInOrder(List<Shot> newList, List<Shot> oldList) {
        for (int i = 0; i < 10; i++) {
            oldList.add(i, newList.get(i));
        }
        return oldList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        boolean ISLIKE = false;

        @Bind(R.id.shot_image)
        ImageView shotImage;
        @Bind(R.id.avatar_image)
        CircleImageView avatarImage;
        @Bind(R.id.username_text)
        TextView usernameText;
        @Bind(R.id.commentsCount_text)
        TextView commentsCountText;
        @Bind(R.id.likeCount_text)
        TextView likeCountText;
        @Bind(R.id.shot_favorite)
        ImageView mFavoriteImg;

        ViewHolder(View view) {
            super(view);
            if ((int) view.getTag() == TYPE_ITEM)
                ButterKnife.bind(this, view);

        }
    }


}
