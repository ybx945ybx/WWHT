package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.ui.activity.easyopt.EasyOptDetailActivity;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haotao_Fujie on 2016/11/14.
 */

public class ToastPwAddToEasyopt extends PopupWindow {

    @BindView(R.id.img_logo)        ImageView   mImgLogo;       // Logo
    @BindView(R.id.tv_easyopt_name) HaiTextView mTvEasyoptName; // 草单名

    private Activity       mActivity;
    private LayoutInflater mInflater;
    private View           mContentView;
    private View           mParentView;

    public static ToastPwAddToEasyopt makeText(Activity activity, int easyoptId, String logo, String name) {
        ToastPwAddToEasyopt toastPopuWindow = new ToastPwAddToEasyopt(activity, easyoptId, logo, name);
        return toastPopuWindow;
    }

    // 积分提示
    public ToastPwAddToEasyopt(Activity activity, int easyoptId, String logo, String name) {
        mActivity = activity;
        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.dlg_toast_add_to_easyopt, null);
        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        this.setWidth(DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 32));
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //        this.setHeight(DisplayUtils.dp2px(mActivity, 46));
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.update();

        // 草单图片
        Glide.with(mActivity)
                .load(logo)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImgLogo);
        // 草单名
        mTvEasyoptName.setText(name);

        mContentView.setOnClickListener(v -> {
            EasyOptDetailActivity.toThisActivity(mActivity, easyoptId);
        });
    }

    public ToastPwAddToEasyopt parentView(View parentView) {
        mParentView = parentView;
        return this;
    }

    public void show() {
        show(null);
    }

    private void show(View parent) {

        if (parent == null) {
            if (mParentView != null) {
                parent = mParentView;
            } else {
                //                parent = mActivity.getCurrentFocus();
                parent = mActivity.getWindow().getDecorView();
            }
        }

        if (!this.isShowing()) {
            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, DisplayUtils.dp2px(mActivity, 85) + DisplayUtils.getNavigationBarHeight(mActivity));

            dismissMySelf(3000);
        }
    }

    private void dismissMySelf(long delayMillis) {
        // 消失
        new Handler().postDelayed(this::dismiss, delayMillis);
    }

}
