package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.a55haitao.wwht.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 去应用市场评论弹窗
 * Created by a55 on 2017/8/7.
 */

public class GoCommentPopupWindow extends PopupWindow implements View.OnClickListener {

    @BindView(R.id.tv_go_like)  HaiTextView tvGoLike;
    @BindView(R.id.tv_go_vomit) HaiTextView tvGoVomit;
    @BindView(R.id.tv_dismiss)  HaiTextView tvDismiss;

    @BindView(R.id.centerView) LinearLayout centerView;

    private View mContentView;
    private View mParentView;

    private Activity       mActivity;
    private LayoutInflater mInflater;

    public GoCommentPopupWindow(Activity activity) {
        mActivity = activity;

        mInflater = LayoutInflater.from(activity);
        mContentView = mInflater.inflate(R.layout.go_comment_popup_window, null);
        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        // 渲染UI
        renderUI();
    }

    private void renderUI() {
        tvGoLike.setOnClickListener(this);
        tvGoVomit.setOnClickListener(this);
        tvDismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_like:
            case R.id.tv_go_vomit:
                try {
                    Uri    uri           = Uri.parse("market://details?id=" + mActivity.getPackageName());
                    Intent commentIntent = new Intent(Intent.ACTION_VIEW, uri);
                    mActivity.startActivity(commentIntent);
                } catch (Exception e) {
                    Toast.makeText(mActivity, "没有可以打开的应用", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                showOrDismiss(mParentView);
                break;
            case R.id.tv_dismiss:
                showOrDismiss(mParentView);
                break;

        }
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;

        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }

        mParentView = parent;

        long duration = 300;

        if (!this.isShowing()) {

            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
            new Handler().postDelayed(() -> dismiss(), duration);
        }
    }
}
