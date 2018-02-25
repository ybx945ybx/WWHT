package com.a55haitao.wwht.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlertViewType_Error;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlertViewType_OK;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlertViewType_Warning;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Add_Cart;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Pray;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Comment;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Follow;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Like;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_None;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Pray;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_SelectPost;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_SendPost;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Share;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_UploadAvatar;

/**
 * Created by Haotao_Fujie on 2016/11/14.
 */

public class ToastPopuWindow extends PopupWindow {

    @BindView(R.id.toastImage)   ImageView    toastImage;
    @BindView(R.id.toastMessage) HaiTextView  toastMessage;
    @BindView(R.id.toastPoint)   HaiTextView  toastPoint;
    @BindView(R.id.centerView)   LinearLayout centerView;

    private Activity       mActivity;
    private LayoutInflater mInflater;
    private View           mContentView;
    private View           mParentView;

    public static ToastPopuWindow makeText(Activity activity, String message, int type) {
        return new ToastPopuWindow(activity, type, message);
    }

    public static ToastPopuWindow makeText(Activity activity, int point, int type) {
        return new ToastPopuWindow(activity, type, point);
    }

    // 积分提示
    public ToastPopuWindow(Activity activity, int type, int point) {
        if (activity.isFinishing())
            return;
        init(activity);
        renderUI_Point(type, point);
    }

    // 普通Toast
    public ToastPopuWindow(Activity activity, int type, String message) {
        if (activity.isFinishing())
            return;
        init(activity);
        renderUI_Common(type, message);
    }

    /**
     * 初始化
     *
     * @param activity Activity
     */
    private void init(Activity activity) {
        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.alert_toast_view, null);
        ButterKnife.bind(this, mContentView);
        setContentView(mContentView);
        setWidth(DisplayUtils.getScreenWidth(mActivity));
        setHeight(DisplayUtils.getScreenHeight(mActivity));
        setFocusable(true);
        setOutsideTouchable(true);
        update();
    }

    public ToastPopuWindow parentView(View parentView) {
        mParentView = parentView;
        return this;
    }

    public void show() {
        show(null);
    }

    private void renderUI_Point(int type, int point) {

        int    resId   = -1;
        String message = null;
        switch (type) {
            case AlterPointViewType_None: {
                // Nothing
                break;
            }
            case AlterPointViewType_Share: {
                message = "分享成功";
                resId = R.mipmap.icon_toast_share;
                break;
            }
            case AlterPointViewType_Follow: {
                message = "已关注成功";
                resId = R.mipmap.icon_toast_follow;
                break;
            }
            case AlterPointViewType_Comment: {
                message = "评论成功";
                resId = R.mipmap.icon_toast_comment;
                break;
            }
            case AlterPointViewType_SendPost: {
                message = "新笔记发布";
                resId = R.mipmap.icon_toast_sendpost;
                break;
            }
            case AlterPointViewType_SelectPost: {
                message = "笔记加精";
                resId = R.mipmap.icon_toast_selectpost;
                break;
            }
            case AlterPointViewType_UploadAvatar: {
                message = "头像上传";
                resId = R.mipmap.icon_toast_uploadavatar;
                break;
            }
            case AlterPointViewType_Like: {
                message = "点赞成功 ";
                resId = R.mipmap.icon_toast_like;
                break;
            }

            case AlterPointViewType_Pray: {
                message = "已加入心愿单";
                resId = R.mipmap.icon_toast_like;
                break;
            }
            default:
                return;
        }

        if (resId == -1) {
            toastImage.setVisibility(View.GONE);
        } else {
            toastImage.setVisibility(View.VISIBLE);
            toastImage.setImageResource(resId);
        }

        toastMessage.setText(message);
        if (point <= 0) {
            toastPoint.setVisibility(View.GONE);
        } else {
            toastPoint.setVisibility(View.VISIBLE);
            toastPoint.setText(String.format("积分+%d", point));
        }

    }

    private void renderUI_Common(int type, String message) {
        int resId = -1;
        switch (type) {
            case AlertViewType_OK: {
                resId = R.mipmap.icon_toast_ok;
                break;
            }
            case AlertViewType_Warning: {
                resId = R.mipmap.icon_toast_warning;
                break;
            }
            case AlertViewType_Error: {
                resId = R.mipmap.icon_toast_error;
                break;
            }
            case AlterViewType_Pray: {
                resId = R.mipmap.icon_toast_like;
                break;
            }
            case AlterViewType_Add_Cart: {
                resId = R.mipmap.ic_add_cart_error;
                break;
            }
        }

        if (resId == -1) return;

        toastImage.setImageResource(resId);
        toastMessage.setText(message);
        toastPoint.setVisibility(View.GONE);
    }

    private void show(View parent) {

        if (parent == null) {
            if (mParentView != null) {
                parent = mParentView;
            } else {
                parent = mActivity.getWindow().getDecorView();
            }
        }

        if (!this.isShowing()) {
            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            dismissMySelf(1000);
        }
    }

    private void dismissMySelf(long delayMillis) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(centerView, "scaleX", 1, 0.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(centerView, "scaleY", 1, 0.8f);
        ObjectAnimator alpha  = ObjectAnimator.ofFloat(centerView, "alpha", 1, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(delayMillis);
        animatorSet.setDuration(300);
        animatorSet.play(scaleX).with(scaleY).before(alpha);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isShowing()) {
                    dismiss();
                }
            }
        });


    }

}
