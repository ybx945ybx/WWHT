package com.a55haitao.wwht.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.ShareImageEvent;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

/**
 * 分享对话框
 * Created by 陶声 on 16/7/23.
 */
public class ShareUtils {
    private static Dialog   sShareDlg;
    private static Activity mActivity;

    /**
     * 一般分享
     *
     * @param activity activity
     * @param title    标题
     * @param text     文本
     * @param url      small_icon
     * @param imageUrl 图片url
     */
    public static void showShareBoard(final Activity activity, final String title, final String text, final String url, final String imageUrl, boolean canEarnMembership) {
        showShareBoard(activity, null, null, title, text, url, imageUrl);
    }

    /**
     * @param activity activity
     * @param dlgTitle shareBoard标题
     * @param dlgText  shareBoard文本
     * @param title    标题
     * @param text     文本
     * @param url      small_icon
     * @param imageUrl 图片url
     */
    public static void showShareBoard(final Activity activity, String dlgTitle, String dlgText, final String title, final String text, final String url, final String imageUrl) {
        mActivity = activity;
        sShareDlg = new Dialog(activity);
        sShareDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sShareDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(activity).inflate(R.layout.dlg_share_board, null);

        if (TextUtils.isEmpty(dlgText) || TextUtils.isEmpty(dlgText)) {
            view.findViewById(R.id.tv_common_title).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llyt_board_title).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.tv_common_title).setVisibility(View.GONE);
            view.findViewById(R.id.llyt_board_title).setVisibility(View.VISIBLE);
            ((HaiTextView) view.findViewById(R.id.tv_board_title)).setText(dlgTitle);
            ((HaiTextView) view.findViewById(R.id.tv_board_desc)).setText(dlgText);
        }

        // 分享监听
        View.OnClickListener listener = v1 -> {
            // 选择平台
            SHARE_MEDIA platform = null;
            switch (v1.getId()) {
                case R.id.tv_cancle:
                    dismissShareBoard();
                    return;
                case R.id.img_share_copy_url:
                    ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", url);
                    clipboard.setPrimaryClip(clip);
                    ToastUtils.showToast(mActivity, "复制成功");
                    dismissShareBoard();
                    return;
                case R.id.img_share_wechat:
                    platform = SHARE_MEDIA.WEIXIN;
                    break;
                case R.id.img_share_wechat_moments:
                    platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                    break;
                case R.id.img_share_weibo:
                    platform = SHARE_MEDIA.SINA;
                    break;
                case R.id.img_share_qq:
                    platform = SHARE_MEDIA.QQ;
                    break;
                default:
                    break;
            }
            if ((platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN)) {
                ToastUtils.showToast(activity, "请安装微信客户端");
                return;
            }

            if (platform == SHARE_MEDIA.QQ && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.QQ)) {
                ToastUtils.showToast(activity, "请安装QQ客户端");
                return;
            }

            if (platform == SHARE_MEDIA.SINA && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.SINA)) {
                ToastUtils.showToast(activity, "请安装微博客户端");
                return;
            }

            doShare(platform, activity, title, text, url, imageUrl, false);
        };

        // 设置监听
        view.findViewById(R.id.img_share_wechat).setOnClickListener(listener);
        view.findViewById(R.id.img_share_wechat_moments).setOnClickListener(listener);
        view.findViewById(R.id.img_share_weibo).setOnClickListener(listener);
        view.findViewById(R.id.img_share_qq).setOnClickListener(listener);
        view.findViewById(R.id.img_share_copy_url).setOnClickListener(listener);
        view.findViewById(R.id.tv_cancle).setOnClickListener(listener);

        sShareDlg.setContentView(view);

        Window dialogWindow = sShareDlg.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

        sShareDlg.show();
    }

    /**
     * 分享单张图片
     *
     * @param activity activity
     * @param imageUrl 图片url
     */
    public static void showShareBoard(final Activity activity, final String imageUrl) {
        mActivity = activity;
        sShareDlg = new Dialog(activity);
        sShareDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sShareDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(activity).inflate(R.layout.dlg_share_board, null);

        view.findViewById(R.id.tv_common_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.llyt_board_title).setVisibility(View.GONE);

        // 分享监听
        View.OnClickListener listener = v1 -> {
            // 选择平台
            SHARE_MEDIA platform = null;
            switch (v1.getId()) {
                case R.id.tv_cancle:
                    dismissShareBoard();
                    return;
                case R.id.img_share_copy_url:
                    ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", imageUrl);
                    clipboard.setPrimaryClip(clip);
                    ToastUtils.showToast(mActivity, "复制成功");
                    dismissShareBoard();
                    return;
                case R.id.img_share_wechat:
                    platform = SHARE_MEDIA.WEIXIN;
                    break;
                case R.id.img_share_wechat_moments:
                    platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                    break;
                case R.id.img_share_weibo:
                    platform = SHARE_MEDIA.SINA;
                    break;
                case R.id.img_share_qq:
                    platform = SHARE_MEDIA.QQ;
                    break;
                default:
                    break;
            }
            if ((platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN)) {
                ToastUtils.showToast(activity, "请安装微信客户端");
                return;
            }

            if (platform == SHARE_MEDIA.QQ && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.QQ)) {
                ToastUtils.showToast(activity, "请安装QQ客户端");
                return;
            }

            if (platform == SHARE_MEDIA.SINA && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.SINA)) {
                ToastUtils.showToast(activity, "请安装微博客户端");
                return;
            }

            doImageShare(platform, activity, imageUrl);
        };

        // 设置监听
        view.findViewById(R.id.img_share_wechat).setOnClickListener(listener);
        view.findViewById(R.id.img_share_wechat_moments).setOnClickListener(listener);
        view.findViewById(R.id.img_share_weibo).setOnClickListener(listener);
        view.findViewById(R.id.img_share_qq).setOnClickListener(listener);
        view.findViewById(R.id.img_share_copy_url).setOnClickListener(listener);
        view.findViewById(R.id.tv_cancle).setOnClickListener(listener);

        sShareDlg.setContentView(view);

        Window dialogWindow = sShareDlg.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

        sShareDlg.show();
    }


    /**
     * 自定义ShareBoard 分享
     *
     * @param platform 平台
     * @param activity Activity
     * @param title    标题
     * @param text     文本
     * @param url      small_icon
     * @param imageUrl imageUrl
     */
    public static void doShare(SHARE_MEDIA platform, final Activity activity, final String title, String text, String url, String imageUrl, boolean canEarnMembership) {
        imageUrl = HaiUtils.transformSharIcon(imageUrl);

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, imageUrl));  //缩略图
        web.setDescription(text);//描述

        // 分享
        ShareAction shareAction = new ShareAction(activity)
                .setPlatform(platform)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        ((BaseActivity) activity).showProgressDialog(true);
                        dismissShareBoard();
                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        ToastPopuWindow.makeText(activity, "分享成功啦", AlertViewType.AlertViewType_OK).show();

                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        //                        ToastPopuWindow.makeText(activity, "分享失败啦", AlertViewType.AlertViewType_Error).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        //                        ToastPopuWindow.makeText(activity, "分享取消啦", AlertViewType.AlertViewType_Error).show();
                    }
                });

        if (platform == SHARE_MEDIA.SINA) { // 微博使用图片分享，拼url到text里
            String shareTextHead = title + text;
            String shareText     = shareTextHead + url;
            if (shareText.length() > 140) {
                shareText = shareTextHead.substring(0, (140 - url.length() - 3)) + "..." + url;
            }
            shareAction.withMedia(new UMImage(activity, imageUrl))
                    .withText(shareText);
        } else {
            shareAction.withMedia(web);
        }

        shareAction.share();
    }

    /**
     * 分享单张图片
     * @param platform
     * @param activity
     * @param imgUrl
     */
    public static void doImageShare(SHARE_MEDIA platform, final Activity activity, final String imgUrl) {
        UMImage image = new UMImage(activity, imgUrl);//网络图片
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(image)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        ((BaseActivity) activity).showProgressDialog(true);
                        dismissShareBoard();
                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        ToastPopuWindow.makeText(activity, "分享成功啦", AlertViewType.AlertViewType_OK).show();
                        EventBus.getDefault().post(new ShareImageEvent());
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        //                        ToastPopuWindow.makeText(activity, "分享失败啦", AlertViewType.AlertViewType_Error).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        //                        ToastPopuWindow.makeText(activity, "分享取消啦", AlertViewType.AlertViewType_Error).show();
                    }
                }).share();
    }

    /**
     * 隐藏分享面板
     */
    public static void dismissShareBoard() {
        if (sShareDlg != null && sShareDlg.isShowing()) {
            sShareDlg.dismiss();
        }
    }

}
