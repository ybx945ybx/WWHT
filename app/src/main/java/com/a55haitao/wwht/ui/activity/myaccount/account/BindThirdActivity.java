package com.a55haitao.wwht.ui.activity.myaccount.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.event.UserInfoChangeEvent;
import com.a55haitao.wwht.data.exception.HTException;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.result.CheckThirdAccountResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.type;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;


/**
 * 绑定第三方
 */
public class BindThirdActivity extends BaseNoFragmentActivity {

    @BindView(R.id.sb_wechat) SwitchButton      mSbWechat;    // 微信
    @BindView(R.id.sb_weibo)  SwitchButton      mSbWeibo;     // 微博
    @BindView(R.id.sb_qq)     SwitchButton      mSbQq;        // QQ
    @BindView(R.id.title)     DynamicHeaderView mTitle;       // 标题
    @BindView(R.id.tv_wechat) TextView          mTvWechat;    // 微信已绑定文字
    @BindView(R.id.tv_weibo)  TextView          mTvWeibo;     // 微博已绑定文字
    @BindView(R.id.tv_qq)     TextView          mTvQq;        // QQ已绑定文字

    @BindString(R.string.key_account_provider) String KEY_ACCOUNT_PROVIDER;
    @BindString(R.string.key_wb_open_id)       String KEY_WB_OPEN_ID;
    @BindString(R.string.key_wx_open_id)       String KEY_WX_OPEN_ID;
    @BindString(R.string.key_qq_open_id)       String KEY_QQ_OPEN_ID;
    @BindString(R.string.key_open_id)          String KEY_OPEN_ID;
    @BindString(R.string.key_method)           String KEY_METHOD;
    @BindString(R.string.key_access_token)     String KEY_ACCESS_TOKEN;
    //    @BindString(R.string.mt_bind_sso) String MT_BIND_SSO;
    //    @BindString(R.string.mt_check_third_account) String MT_CHECK_THIRD_ACCOUNT;

    private String     mWbOpenId;   // 微博 openId
    private String     mWxOpenId;   // 微信 openId
    private String     mQqOpenId;   // QQ openId
    private UMShareAPI mShareAPI;
    private String     mAccessToken;
    private String     mOpenid;
    private Tracker    mTracker;    // GA Tracker

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_bind_third);
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
        mTracker.setScreenName("绑定第三方");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (HaiUtils.isLogin()) {
            mWbOpenId = UserRepository.getInstance().getUserInfo().getWbOpenId();
            mWxOpenId = UserRepository.getInstance().getUserInfo().getWxOpenId();
            mQqOpenId = UserRepository.getInstance().getUserInfo().getQqOpenId();
        }

    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        switchView();
    }

    /**
     * 切换视图
     */
    private void switchView() {
        mSbWechat.setVisibility(TextUtils.isEmpty(mWxOpenId) ? View.VISIBLE : View.GONE);
        mSbWeibo.setVisibility(TextUtils.isEmpty(mWbOpenId) ? View.VISIBLE : View.GONE);
        mSbQq.setVisibility(TextUtils.isEmpty(mQqOpenId) ? View.VISIBLE : View.GONE);

        mTvWechat.setVisibility(TextUtils.isEmpty(mWxOpenId) ? View.GONE : View.VISIBLE);
        mTvWeibo.setVisibility(TextUtils.isEmpty(mWbOpenId) ? View.GONE : View.VISIBLE);
        mTvQq.setVisibility(TextUtils.isEmpty(mQqOpenId) ? View.GONE : View.VISIBLE);
    }

    /**
     * 检测第三方账号是否已经绑定
     *
     * @param platform 平台
     */
    private void checkThirdAccount(final String platform) {
        showProgressDialog();
        UserRepository.getInstance()
                .checkThirdAccount(platform, mOpenid, mAccessToken)
                .subscribe(new DefaultSubscriber<CheckThirdAccountResult>() {
                    @Override
                    public void onSuccess(CheckThirdAccountResult result) {
                        if (result.is_existed) {
                            ToastUtils.showToast(mActivity, "该账号已被绑定");
                            clearState(platform);
                            dismissProgressDialog();
                        } else {
                            requestBindSso(platform, mOpenid, mAccessToken);
                        }
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        dismissProgressDialog();
                        return super.onFailed(e);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }


    /**
     * 绑定第三方登录 请求
     */
    private void requestBindSso(String platform, String openId, String accessToken) {
        UserRepository.getInstance()
                .bindSSO(platform, openId, accessToken)
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean result) {
                        ToastUtils.showToast(mActivity, "绑定成功");
                        switch (platform) {
                            case "qq":
                                //                                mSbQq.setChecked(true);
                                mSbQq.setVisibility(View.GONE);
                                mTvQq.setVisibility(View.VISIBLE);
                                break;
                            case "wechat":
                                //                                mSbWechat.setChecked(true);
                                mSbWechat.setVisibility(View.GONE);
                                mTvWechat.setVisibility(View.VISIBLE);
                                break;
                            case "weibo":
                                //                                mSbWeibo.setChecked(true);
                                mSbWeibo.setVisibility(View.GONE);
                                mTvWeibo.setVisibility(View.VISIBLE);
                                break;
                        }
                        EventBus.getDefault().post(new UserInfoChangeEvent());
                        Intent intent = new Intent();
                        intent.putExtra("type", type);
                        intent.putExtra("open_id", openId);
                        setResult(RESULT_OK, intent);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        if (e instanceof HTException && ((HTException) e).code == HaiConstants.ReponseCode.CODE_SSO_ALREADY_BINDED) {
                            ToastUtils.showToast(((HTException) e).msg);
                            setBinded(platform);
                        } else {
                            clearState(platform);
                        }
                        return super.onFailed(e);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    @OnClick({R.id.sb_wechat, R.id.sb_weibo, R.id.sb_qq})
    public void onClick(View view) {
        if (!((SwitchButton) view).isChecked())
            return;
        mShareAPI = UMShareAPI.get(this);
        SHARE_MEDIA platform         = null;
        String      account_provider = null;
        switch (view.getId()) {
            case R.id.sb_wechat:
                platform = WEIXIN;
                account_provider = "wechat";
                break;
            case R.id.sb_weibo:
                platform = SINA;
                account_provider = "weibo";
                break;
            case R.id.sb_qq:
                platform = QQ;
                account_provider = "qq";
                break;
        }

        if (platform == WEIXIN && !mShareAPI.isInstall(this, WEIXIN)) { // 微信需要客户端才能登陆
            return;
        }

        // 授权
        final String finalAccount_provider = account_provider;

        mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                ToastUtils.showToast(mActivity, "授权成功");

                // 获取 access token & open id
                mAccessToken = data.get("access_token");
                if (platform == WEIXIN) { // 微信openid的key就是openid
                    mOpenid = data.get("openid");
                } else {
                    mOpenid = data.get("uid");
                }

                Logger.d("access token ->" + mAccessToken);
                Logger.d("uid ->" + mOpenid);

                // 检验第三方
                checkThirdAccount(finalAccount_provider);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                ToastUtils.showToast(mActivity, "授权失败");
                clearState(platform);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                ToastUtils.showToast(mActivity, "授权取消");
                clearState(platform);
            }
        });
    }

    /**
     * 清除switchButton状态
     *
     * @param platform 平台
     */
    public void clearState(SHARE_MEDIA platform) {
        switch (platform) {
            case QQ:
                mSbQq.setChecked(false);
                break;
            case WEIXIN:
                mSbWechat.setChecked(false);
                break;
            case SINA:
                mSbWeibo.setChecked(false);
                break;
        }
    }

    /**
     * 清除switchButton状态
     *
     * @param platform 平台
     */
    public void clearState(String platform) {
        switch (platform) {
            case "qq":
                mSbQq.setChecked(false);
                break;
            case "wechat":
                mSbWechat.setChecked(false);
                break;
            case "weibo":
                mSbWeibo.setChecked(false);
                break;
        }
    }

    /**
     * 设置为已绑定状态
     *
     * @param platform 平台
     */
    public void setBinded(String platform) {
        switch (platform) {
            case "qq":
                mSbQq.setVisibility(View.GONE);
                mTvQq.setVisibility(View.VISIBLE);
                break;
            case "wechat":
                mSbWechat.setVisibility(View.GONE);
                mTvWechat.setVisibility(View.VISIBLE);
                break;
            case "weibo":
                mSbWeibo.setVisibility(View.GONE);
                mTvWeibo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
