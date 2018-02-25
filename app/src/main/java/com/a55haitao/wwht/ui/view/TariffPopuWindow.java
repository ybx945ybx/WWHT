package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haotao_Fujie on 2016/11/9.
 */

public class TariffPopuWindow extends PopupWindow {


    @Nullable @BindView(R.id.tariffRateTxt)
    HaiTextView tariffRateTxt;
    @Nullable @BindView(R.id.seeMoreTariffBtn)
    HaiTextView seeMoreTariffBtn;
    @Nullable @BindView(R.id.subsidyTxt)
    TextView subsidyTxt;
    @Nullable @BindView(R.id.centerView)
    LinearLayout centerView;
    @Nullable @BindView(R.id.subsidyIcon)
    HaiTextView subsidyIcon;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private View mContentView;
    private View mParentView;
    private TariffPopuWindowInterface mInterface;

    public TariffPopuWindow(Activity activity, String tariffRate, String detailedMsg, int tariffValue) {
        detailedMsg = init(activity, detailedMsg,R.layout.product_tariff_popu_window);

        // 设置动画
        // this.setAnimationStyle(R.style.AnimationSpecPopuWindow);

        // 渲染UI
        renderUI(tariffRate, detailedMsg, tariffValue);
    }

    public TariffPopuWindow(Activity activity, String detailedMsg){
        detailedMsg = init(activity, detailedMsg,R.layout.order_tariff_popu_window);

        renderUI(detailedMsg);
    }

    //初始化
    private String init(Activity activity, String detailedMsg,int resId) {
        mActivity = activity;

        while (!TextUtils.isEmpty(detailedMsg) && detailedMsg.contains("￥")){
            detailedMsg = detailedMsg.replace("￥", ApiUrl.RMB_UNICODE);
        }

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(resId, null);
        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        this.setWidth(DisplayUtils.getScreenWidth(mActivity));
        this.setHeight(DisplayUtils.getScreenHeight(mActivity));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        return detailedMsg;
    }

    public void setTariffPopuWindowInterface(TariffPopuWindowInterface mInterface) {
        this.mInterface = mInterface;
    }

    private void renderUI(String tariffRate, String detailedMsg, int tariffValue) {

        centerView.setOnClickListener(v -> {return;});

        tariffRateTxt.setText(tariffRate);
        if (tariffValue == 0 ) {
            subsidyIcon.setVisibility(View.GONE);
        }else {
            subsidyIcon.setVisibility(View.VISIBLE);
        }
        subsidyTxt.setText(detailedMsg);
        seeMoreTariffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterface != null) {
                    showOrDismiss(mParentView);
                    mInterface.seeMoreTariffRule();
                }
            }
        });

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrDismiss(mParentView);
            }
        });
    }

    private void renderUI(String detailedMsg) {
        subsidyTxt.setText(detailedMsg);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrDismiss(mParentView);
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

    public interface TariffPopuWindowInterface {
        public void seeMoreTariffRule();
    }

}
