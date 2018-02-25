package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haotao_Fujie on 2016/11/9.
 */

public class ShippingFeePopuWindow extends PopupWindow {

    @BindView(R.id.centerView)
    LinearLayout centerView;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private View mContentView;
    private View mParentView;

    public ShippingFeePopuWindow(Activity activity) {

        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.product_shipping_fee_popu_window, null);
        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        this.setWidth(DisplayUtils.getScreenWidth(mActivity));
        this.setHeight(DisplayUtils.getScreenHeight(mActivity));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        // 设置动画
        // this.setAnimationStyle(R.style.AnimationSpecPopuWindow);

        mContentView.setOnClickListener(v -> showOrDismiss(mParentView));
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

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.05f, 0.1f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            centerView.startAnimation(scaleAnimation);

        } else {

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            centerView.startAnimation(scaleAnimation);

            // Dismiss
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, duration);
        }
    }

}
