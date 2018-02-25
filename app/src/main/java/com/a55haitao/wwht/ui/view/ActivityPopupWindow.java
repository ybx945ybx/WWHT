package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ActivityBean;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页活动弹窗
 * Created by Haotao_Fujie on 2016/11/9.
 */

public class ActivityPopupWindow extends PopupWindow {

    @BindView(R.id.activityImage)    ImageView    activityImage;
    @BindView(R.id.activityCloseBtn) HaiTextView  activityCloseBtn;
    @BindView(R.id.centerView)       LinearLayout centerView;

    private Activity                    mActivity;
    private LayoutInflater              mInflater;
    private View                        mContentView;
    private View                        mParentView;
    private ActivityPopuWindowInterface mInterface;

    public ActivityPopupWindow(Activity activity, ActivityBean data) {

        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (!TextUtils.isEmpty(data.activity.desc)) {
            mContentView = mInflater.inflate(R.layout.mall_activity_popu_window_txt, null);
        } else {
            mContentView = mInflater.inflate(R.layout.mall_activity_popu_window, null);
        }

        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        // 设置动画
        // this.setAnimationStyle(R.style.AnimationSpecPopuWindow);

        // 渲染UI
        renderUI(data, !TextUtils.isEmpty(data.activity.desc));
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) activityImage.getLayoutParams();
                    lp.width = msg.arg1;
                    lp.height = msg.arg2;

                    Logger.d("lp Height == " + lp.height + "lp Width == " + lp.width);

                    activityImage.setLayoutParams(lp);
                    break;
            }

        }

    };

    public void setActivityPopuWindowInterface(ActivityPopuWindowInterface mInterface) {
        this.mInterface = mInterface;
    }

    private void renderUI(ActivityBean data, boolean hasText) {
        // 获取活动图取得宽高比
        new Thread(() -> {
            // 显示网络上的图片
            Bitmap bitmap = null;
            try {

                BitmapFactory.Options options = new BitmapFactory.Options();

                /**
                 * 最关键在此，把options.inJustDecodeBounds = true;
                 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
                 */
                options.inJustDecodeBounds = true;
                /**
                 *options.outHeight为原始图片的高
                 */

                URL myFileUrl = new URL(data.activity.image);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is, null, options);
                is.close();

                int     width   = DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 60);
                int     height  = width * options.outHeight / options.outWidth;
                Message message = new Message();
                message.arg1 = width;
                message.arg2 = height;
                message.what = 0;
                mHander.sendMessage(message);   //   在主线程中更新ui操作

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // 活动图
        Glide.with(mActivity)
                .load(UPaiYunLoadManager.formatImageUrl(data.activity.image, UpaiPictureLevel.SINGLE))
                .transform(new GlideRoundTransform(mActivity, 4))
                .placeholder(R.mipmap.ic_default_400_240)
                .dontAnimate()
                .diskCacheStrategy(UPaiYunLoadManager.sDiskCacheStrategy)
                .into(activityImage);
        activityImage.setOnClickListener(v -> {
            showOrDismiss(mParentView);
            if (mInterface != null) {
                mInterface.enterActivity(data.activity.uri);
            }
        });

        // 关闭按钮
        activityCloseBtn.setOnClickListener(v -> showOrDismiss(mParentView));

        // 带文字的活动弹窗用
        if (hasText){
            // 带文字的活动弹窗用
            HaiTextView activityTxt = (HaiTextView)mContentView.findViewById(R.id.activityTxt);
            HaiTextView activityEnterBtn = (HaiTextView)mContentView.findViewById(R.id.activityEnterBtn);

            activityTxt.setText(data.activity.desc);
            activityEnterBtn.setOnClickListener(v -> {
                showOrDismiss(mParentView);
                if (mInterface != null) {
                    mInterface.enterActivity(data.activity.uri);
                }
            });
        }
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;

        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            Logger.d("已经被销毁了");
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

    public interface ActivityPopuWindowInterface {

        void enterActivity(String uri);

    }

}
