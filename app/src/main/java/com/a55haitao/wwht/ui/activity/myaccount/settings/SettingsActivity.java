package com.a55haitao.wwht.ui.activity.myaccount.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.ShareBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.discover.AddressListActivity;
import com.a55haitao.wwht.ui.activity.myaccount.account.BindThirdActivity;
import com.a55haitao.wwht.ui.activity.myaccount.account.ChangePasswordActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.BugLyUtils;
import com.a55haitao.wwht.utils.DataCleanManager;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.baoyz.actionsheet.ActionSheet;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

import static com.a55haitao.wwht.R.string.key_mobile;
import static com.a55haitao.wwht.R.string.key_user_id;

/**
 * 设置页面
 * <p>
 * Created by 陶声 on 16/5/25.
 */
public class SettingsActivity extends BaseNoFragmentActivity {
    @BindView(R.id.title)                     DynamicHeaderView mTitle;                     // 标题
    //    @BindView(R.id.rl_bind_phone) RelativeLayout mRlBindPhone;                          // 绑定手机
    @BindView(R.id.tv_bindPhone)              TextView          mTvBindPhone;               // 绑定的手机号
    @BindView(R.id.ll_settings_bindThird)     LinearLayout      mLlSettingsBindThird;       // 绑定第三方Logo容器
    @BindView(R.id.rl_settings_bindThird)     RelativeLayout    mRlSettingsBindThird;       // 绑定第三方
    @BindView(R.id.rl_settings_changePwd)     RelativeLayout    mRlSettingsChangePwd;       // 修改密码
    @BindView(R.id.rl_settings_manageAddress) RelativeLayout    mRlSettingsManageAddress;   // 地址管理
    @BindView(R.id.rl_settings_shareApp)      RelativeLayout    mRlSettingsShareApp;        // 分享应用
    @BindView(R.id.rl_helpdoc)                RelativeLayout    mRlSettingsHelpDoc;         // 常见问题
    @BindView(R.id.rl_settings_aboutUs)       RelativeLayout    mRlSettingsAboutUs;         // 关于我们
    @BindView(R.id.rl_settings_feedback)      RelativeLayout    mRlSettingsFeedback;        // 意见反馈
    @BindView(R.id.tv_settings_cacheSize)     TextView          mTvSettingsCacheSize;       // 缓存大小
    @BindView(R.id.rl_settings_clearCache)    RelativeLayout    mRlSettingsClearCache;      // 清除缓存
    @BindView(R.id.tv_settings_logout)        TextView          mTvSettingsLogout;          // 退出登录
    @BindView(R.id.img_3rd_wechat)            ImageView         mImg3rdWechat;              // 微信
    @BindView(R.id.img_3rd_weibo)             ImageView         mImg3rdWeibo;               // 微博
    @BindView(R.id.img_3rd_qq)                ImageView         mImg3rdQq;                  // QQ

    @BindString(key_user_id)                       String KEY_USER_ID;
    @BindString(key_mobile)                        String KEY_MOBILE;
    @BindString(R.string.key_wb_open_id)           String KEY_WB_OPEN_ID; // key : wb_open_id
    @BindString(R.string.key_wx_open_id)           String KEY_WX_OPEN_ID; // key : wx_open_id
    @BindString(R.string.key_qq_open_id)           String KEY_QQ_OPEN_ID; // key : qq_open_id
    @BindString(R.string.key_user_token)           String KEY_USER_TOKEN;
    @BindString(R.string.key_head_img)             String KEY_HEAD_IMG;
    @BindString(R.string.key_open_id)              String KEY_OPEN_ID;
    @BindString(R.string.key_access_token)         String KEY_ACCESS_TOKEN;
    @BindString(R.string.key_nickname)             String KEY_NICKNAME;
    @BindString(R.string.key_account_provider)     String KEY_ACCOUNT_PROVIDER;
    @BindString(R.string.key_method)               String KEY_METHOD;
    @BindString(R.string.settings_share_app_title) String SETTINGS_SHARE_APP_TITLE; // 分享标题
    @BindString(R.string.settings_share_app_text)  String SETTINGS_SHARE_APP_TEXT;   // 分享文本

    private static final int REQUEST_CODE_BIND_THIRD = 1002;
    public static final  int REQUEST_CODE_SHARE      = 123;

    private int     mUserId;    // 用户Id
    private String  mMobile;    // 手机
    private String  mWbOpenId;  // 微博 openId
    private String  mWxOpenId;  // 微信 openId
    private String  mQqOpenId;  // QQ openId
    private Tracker mTracker;   // GA Tracker


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("设置");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // 获取用户信息
        getUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        if (!HaiUtils.isLogin())
            return;
        mUserId = HaiUtils.getUserId();
        mMobile = HaiUtils.getMobile();
        mWbOpenId = UserRepository.getInstance().getUserInfo().getWbOpenId();
        mWxOpenId = UserRepository.getInstance().getUserInfo().getWxOpenId();
        mQqOpenId = UserRepository.getInstance().getUserInfo().getQqOpenId();
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        refreshView();
        setCacheSize();
    }

    /**
     * 刷新UI
     */
    private void refreshView() {
        mTvBindPhone.setText(mMobile);
        mImg3rdQq.setVisibility(TextUtils.isEmpty(mQqOpenId) ?
                View.GONE : View.VISIBLE);
        mImg3rdWechat.setVisibility(TextUtils.isEmpty(mWxOpenId) ?
                View.GONE : View.VISIBLE);
        mImg3rdWeibo.setVisibility(TextUtils.isEmpty(mWbOpenId) ?
                View.GONE : View.VISIBLE);
        mTvSettingsLogout.setVisibility(HaiUtils.isLogin() ?
                View.VISIBLE : View.GONE);
    }

    /**
     * 获取缓存大小
     */
    private void setCacheSize() {
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(mActivity);
            mTvSettingsCacheSize.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.tv_settings_logout)
    public void logout() {
        new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                .setMessage("是否确认退出当前登录账号")
                .setPositiveButton("确定", (dialog, which) -> {
                    showProgressDialog("正在退出...");
                    requestLogout();
                }).setNegativeButton("取消", (dialog, which) -> {
        }).show();
    }

    /**
     * 退出登录请求
     */
    private void requestLogout() {
        UserRepository.getInstance()
                .logout()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean commonDataBean) {
                        if (commonDataBean.success) { // 成功
                            NtalkerUtils.logoutNtalker();
                            YbxTrace.getInstance().getTraceCommonBean().mid = "0";
                            BugLyUtils.setUserId(null);
                            UserRepository.getInstance() // 清除用户信息
                                    .clearUserInfo();
                            EventBus.getDefault().post(new LoginStateChangeEvent(false));
                            finish();
                        }
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }


    /**
     * 页面跳转 - 需要登录
     */
    @OnClick({R.id.rl_bind_phone, R.id.rl_settings_changePwd, R.id.rl_settings_manageAddress})
    public void jumpActivityNeedLogin(View view) {
        if (HaiUtils.needLogin(mActivity))
            return;
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_bind_phone:
                if (!HaiUtils.needLogin(mActivity)) { // 跳转到绑定手机
                    return;
                }
                break;
            case R.id.rl_settings_changePwd:
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_ChangePassWord, mUserId+"", this, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_ChangePassword + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_ChangePassWord, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_ChangePassword, "");

                intent = new Intent(mActivity, ChangePasswordActivity.class);   // 跳转到修改密码
                intent.putExtra(KEY_USER_ID, mUserId);
                intent.putExtra(KEY_MOBILE, mMobile);
                break;
            case R.id.rl_settings_manageAddress:
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_AddressList, "", this, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_Address + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_AddressList, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_Address, "");

                intent = new Intent(mActivity, AddressListActivity.class);     // 跳转到地址管理
                break;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
    }

    @OnClick({R.id.rl_settings_bindThird})
    public void bindThird() {
        if (!HaiUtils.needLogin(mActivity)) {
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_BindThird, "", this, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_Bind3rdParty + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_BindThird, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_Bind3rdParty, "");

            Intent intent = new Intent(mActivity, BindThirdActivity.class);        // 跳转到绑定第三方
            startActivityForResult(intent, REQUEST_CODE_BIND_THIRD);
        }
    }

    /**
     * 页面跳转 — 不需要登录
     */
    @OnClick({R.id.rl_settings_aboutUs, R.id.rl_settings_feedback})
    public void jumpActivityNoLogin(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_settings_aboutUs:
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_AboutUs, "", this, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_AboutUs + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_AboutUs, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_AboutUs, "");

                intent = new Intent(mActivity, AboutUsActivity.class);  // 跳转到关于我们
                break;
            case R.id.rl_settings_feedback:
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_FeedBack, "", this, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_Feedback + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_FeedBack, TraceUtils.PageCode_UserInfoSet, TraceUtils.PositionCode_Feedback, "");

                intent = new Intent(mActivity, FeedbackActivity.class); // 跳转到意见反馈
                intent.putExtra("mobile", mMobile);
                break;
        }
        startActivity(intent);
    }

    /**
     * 常见问题
     */
    @OnClick(R.id.rl_helpdoc)
    public void toHelpDoc() {
        H5Activity.toThisActivity(this, ServerUrl.HELP_INTRODUCE_URL, "常见问题");
    }

    /**
     * 购物须知
     */
    @OnClick(R.id.rl_sale_after)
    public void toSaleAfter() {
        H5Activity.toThisActivity(this, ServerUrl.SALE_AFTER_URL, "购物须知-55海淘");
    }

    /**
     * 分享应用
     */
    @OnClick(R.id.rl_settings_shareApp)
    public void shareApp() {
        showProgressDialog();
        SnsRepository.getInstance()
                .getShareAppInfo()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<ShareBean>() {
                    @Override
                    public void onSuccess(ShareBean shareBean) {
                        doShare(shareBean);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        doShare();
                        //                        return super.onFailed(e);
                        return false;
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 弹出分享框
     */
    private void doShare() {
        ShareUtils.showShareBoard(mActivity,
                SETTINGS_SHARE_APP_TITLE,
                SETTINGS_SHARE_APP_TEXT,
                ServerUrl.SHARE_APP_URL + mUserId,
                ServerUrl.SHARE_APPICON_URL,
                false);
    }

    /**
     * 弹出分享框
     *
     * @param sharebean 服务器返回的ShareBean
     */
    private void doShare(ShareBean sharebean) {
        ShareUtils.showShareBoard(mActivity,
                sharebean.title,
                sharebean.desc,
                sharebean.url,
                sharebean.icon,
                false);
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.rl_settings_clearCache)
    public void clearCache() {
        try {
            String cacheSize = DataCleanManager.getTotalCacheSize(mActivity);

            if ("0MB".equals(cacheSize)) {
                ToastUtils.showToast(mActivity, "没有缓存可以清啦>_<,请不要再试啦");
            } else {
                ActionSheet.createBuilder(mActivity, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles(String.format("确认清除缓存(%s)", cacheSize))
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                showProgressDialog("正在清除缓存...");
                                DataCleanManager.clearAllCache(mActivity);
                                dismissProgressDialog();
                                setCacheSize();
                            }
                        }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_BIND_THIRD) { // 绑定第三方
            getUserInfo();
            refreshView();
        } else {
            UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismissProgressDialog();
    }

    @Override
    protected void onPause() {
        ShareUtils.dismissShareBoard();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }
}
