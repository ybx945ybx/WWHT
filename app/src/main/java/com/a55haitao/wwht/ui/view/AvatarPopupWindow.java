package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haotao_Fujie on 2016/11/23.
 */

public class AvatarPopupWindow extends PopupWindow {

    @BindView(R.id.centerView)       ImageView   mImgAvatar;
    @BindView(R.id.tv_change_avatar) HaiTextView mTvChangeAvatar;

    private Activity               mActivity;
    private LayoutInflater         mInflater;
    private View                   mContentView;
    private View                   mParentView;
    private OnChangeAvatarListener mOnChangeAvatarListener;

    public AvatarPopupWindow(Activity activity, String imgUrl) {
        this(activity, imgUrl, false, 0);
    }

    public AvatarPopupWindow(Activity activity, String imgUrl, boolean canChagneAvatar) {
        this(activity, imgUrl, canChagneAvatar, 0);
    }

    public AvatarPopupWindow(Activity activity, String imgUrl, boolean canChagneAvatar, int fromRefund) {
        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.avatar_popu_window, null);
        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        mTvChangeAvatar.setVisibility(canChagneAvatar ? View.VISIBLE : View.GONE);
        int screenWidth = DisplayUtils.getScreenWidth(mActivity);
        setWidth(screenWidth);
        setHeight(DisplayUtils.getScreenHeight(mActivity));
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        if (fromRefund == 1) {              //  等于1的时候是退款申请单详情页的 图片设成match_parent
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mImgAvatar.setLayoutParams(lp);
        }
        update();

        Glide.with(mActivity)
                .load(UpyUrlManager.getUrl(imgUrl, screenWidth))
                .placeholder(R.mipmap.ic_default_square_large)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into(mImgAvatar);

        mContentView.setOnClickListener(v -> showOrDismiss(mParentView));
        // 更换头像监听
        mTvChangeAvatar.setOnClickListener(v -> {
            if (mOnChangeAvatarListener != null) {
                mOnChangeAvatarListener.onChange();
            }
        });
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;
        mParentView = parent;
        long duration = 250;
        if (!this.isShowing()) {

            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.00f, 0.1f, 1.00f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            mImgAvatar.startAnimation(scaleAnimation);

        } else {

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            mImgAvatar.startAnimation(scaleAnimation);

            // Dismiss
            new Handler().postDelayed(() -> dismiss(), duration);
        }
    }


    public void setOnChangeAvatarListener(OnChangeAvatarListener onChangeAvatarListener) {
        mOnChangeAvatarListener = onChangeAvatarListener;
    }

    public interface OnChangeAvatarListener {
        void onChange();
    }
}
