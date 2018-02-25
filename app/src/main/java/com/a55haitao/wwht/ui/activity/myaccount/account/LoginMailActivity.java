package com.a55haitao.wwht.ui.activity.myaccount.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AppLoginType;
import com.a55haitao.wwht.data.model.annotation.LoginType;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.CheckThirdAccountResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.settings.BindPhoneActivity;
import com.a55haitao.wwht.utils.BugLyUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.TxtUtils;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 邮箱/用户名登录
 */
public class LoginMailActivity extends BaseNoFragmentActivity {
    @BindView(R.id.et_username)              EditText  mEtUsername;               // 用户名
    @BindView(R.id.et_password)              EditText  mEtPassword;               // 密码
    @BindView(R.id.img_clear_input_username) ImageView mImgClearInputUsername;    // 清除邮箱用户名输入
    @BindView(R.id.img_clear_input_pwd)      ImageView mImgClearInputPwd;         // 清除密码输入

    @BindString(R.string.key_nickname)          String KEY_NICKNAME;
    @BindString(R.string.key_mobile)            String KEY_MOBILE;
    @BindString(R.string.key_account_provider)  String KEY_ACCOUNT_PROVIDER;
    @BindString(R.string.key_open_id)           String KEY_OPEN_ID;
    @BindString(R.string.key_head_img)          String KEY_HEAD_IMG;
    @BindString(R.string.key_remember_username) String KEY_REMEMBER_USERNAME;
    @BindString(R.string.key_access_token)      String KEY_ACCESS_TOKEN;

    private static final int REQUEST_CODE_REGISTER   = 1;         // 注册
    private static final int REQUEST_CODE_BIND_PHONE = 2;       // 绑定手机

    private UMShareAPI mShareAPI;
    private String     mAccessToken;
    private String     mOpenid;
    private String     mHeadImg;
    private String     mNickname;
    private String     mPhone;

    private int    src;             // 1 app自然注册,  3 app 0元团;
    private String activity_id;     // 导流过来的活动id

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mail);
        ButterKnife.bind(this);
        initVars();
        initViews();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            src = intent.getIntExtra("src", -1);
            activity_id = intent.getStringExtra("activity_id");

        }
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        // 设置用户名不能为emoji
        mEtUsername.setFilters(new InputFilter[]{TxtUtils.EMOJI_FILTER});

        mPhone = (String) SPUtils.get(mActivity, KEY_REMEMBER_USERNAME, "");
        if (!TextUtils.isEmpty(mPhone)) {
            mEtUsername.setText(mPhone);
            mEtPassword.requestFocus();
            mImgClearInputUsername.setVisibility(View.INVISIBLE);
        } else {
            mImgClearInputUsername.setVisibility(View.VISIBLE);
        }

        // 清除邮箱/用户名输入
        mImgClearInputUsername.setOnClickListener(v -> {
            mEtUsername.setText("");
        });
        // 清除密码输入
        mImgClearInputPwd.setOnClickListener(v -> {
            mEtPassword.setText("");
        });
        // 用户名监听
        mEtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mImgClearInputUsername.setVisibility(View.VISIBLE);
                } else {
                    mImgClearInputUsername.setVisibility(View.INVISIBLE);
                }
            }
        });
        // 密码监听
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mImgClearInputPwd.setVisibility(View.VISIBLE);
                } else {
                    mImgClearInputPwd.setVisibility(View.INVISIBLE);
                }
            }
        });
        // 用户名焦点监听
        mEtUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(mEtUsername.getText().toString()) && hasFocus) {
                mImgClearInputUsername.setVisibility(View.VISIBLE);
            } else {
                mImgClearInputUsername.setVisibility(View.INVISIBLE);
            }
        });
        // 密码焦点监听
        mEtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(mEtPassword.getText().toString()) && hasFocus) {
                mImgClearInputPwd.setVisibility(View.VISIBLE);
            } else {
                mImgClearInputPwd.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 加载数据
     */
    public void loadData() {
    }

    /**
     * 登录
     */
    @OnClick(R.id.btn_login)
    public void login() {
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();

        if (checkLogin(username, password)) {
            showProgressDialog("登录中", true);
            requestLogin(username, password);
        }
    }

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否通过
     */
    private boolean checkLogin(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showToast(mActivity, "请输入账号");
        } else if (TextUtils.isEmpty(password)) {
            ToastUtils.showToast(mActivity, "请输入密码");
        } else {
            return true;
        }
        return false;
    }

    /**
     * 登录请求
     *
     * @param username 用户名
     * @param password 密码
     */
    private void requestLogin(final String username, String password) {
        UserRepository.getInstance()
                .login(username, password, LoginType.PASSWORD, AppLoginType.MAIL_USERNAME, "", src == 3 ? 3 : 1, activity_id)
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) { // 登录成功
                        ToastUtils.showToast(mActivity, "登录成功");
                        // 客服
                        NtalkerUtils.loginNtalker(userBean);
                        YbxTrace.getInstance().getTraceCommonBean().mid = userBean.getId() + "";
                        BugLyUtils.setUserId(userBean);
                        SPUtils.put(mActivity, KEY_REMEMBER_USERNAME, username);
                        // talkingData
                        TalkingDataAppCpa.onLogin(String.valueOf(userBean.getId()));
                        // 保存用户信息
                        UserRepository.getInstance()
                                .saveUserInfo(userBean);
                        EventBus.getDefault().post(new LoginStateChangeEvent(true));
                        setResult(RESULT_OK);
                        finish();

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }


    /**
     * 跳转到注册页面
     */
    @OnClick(R.id.btn_register)
    public void onClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("src", src);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }

    /**
     * 第三方登录
     */
    @OnClick({R.id.ib_wechat, R.id.ib_weibo, R.id.ib_qq})
    public void onClick(View view) {
        showProgressDialog();

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        mShareAPI = UMShareAPI.get(this);
        mShareAPI.setShareConfig(config);

        SHARE_MEDIA platform = null;
        switch (view.getId()) {
            case R.id.ib_wechat:  // 微信
                platform = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.ib_weibo:   // 微博
                platform = SHARE_MEDIA.SINA;
                break;
            case R.id.ib_qq:      // QQ
                platform = SHARE_MEDIA.QQ;
                break;
            default:
                break;
        }

        if (platform == SHARE_MEDIA.WEIXIN && !mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) { // 微信需要客户端才能登陆
            return;
        }

        getUserData(platform);
    }


    /**
     * 获取用户信息
     */
    private void getUserData(final SHARE_MEDIA platform) {
        mShareAPI.getPlatformInfo(mActivity, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Logger.t(TAG).d("获取用户信息成功");

                String account_provider = null;

                switch (platform) {
                    case QQ:
                        account_provider = "qq";
                        break;
                    case WEIXIN:
                        account_provider = "wechat";
                        break;
                    case SINA:
                        account_provider = "weibo";
                        break;
                    default:
                        return;
                }

                // 获取 access token & open id
                mAccessToken = map.get("access_token");
                if (platform == SHARE_MEDIA.WEIXIN) { // 微信openid的key就是openid
                    mOpenid = map.get("openid");
                } else {
                    mOpenid = map.get("uid");
                }

                mNickname = map.get("screen_name");
                mHeadImg = map.get("profile_image_url");

                showProgressDialog("登录中");
                checkThirdAccount(account_provider);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                dismissProgressDialog();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                dismissProgressDialog();
            }
        });
    }


    /**
     * 检测第三方账号是否已经绑定
     *
     * @param platform 平台
     */
    private void checkThirdAccount(final String platform) {
        UserRepository.getInstance()
                .checkThirdAccount(platform, mOpenid, mAccessToken)
                .subscribe(new DefaultSubscriber<CheckThirdAccountResult>() {
                    @Override
                    public void onSuccess(CheckThirdAccountResult result) {
                        if (result.is_existed) {
                            thirdLogin(platform); // 登录
                        } else {
                            Intent intent = new Intent(mActivity, BindPhoneActivity.class);
                            intent.putExtra(KEY_HEAD_IMG, mHeadImg);
                            intent.putExtra(KEY_OPEN_ID, mOpenid);
                            intent.putExtra(KEY_ACCESS_TOKEN, mAccessToken);
                            intent.putExtra(KEY_NICKNAME, mNickname);
                            intent.putExtra(KEY_ACCOUNT_PROVIDER, platform);

                            dismissProgressDialog();

                            startActivityForResult(intent, REQUEST_CODE_BIND_PHONE);
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
     * 第三方登录
     *
     * @param platform 平台
     */
    private void thirdLogin(String platform) {
        Logger.t(TAG).d("第三方登录开始");
        Logger.t(TAG).d("access token = " + mAccessToken + " open_id = " + mOpenid);
        UserRepository.getInstance()
                .thirdLogin(platform, mAccessToken, mOpenid, src == 3 ? 3 : 1, activity_id)
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast(mActivity, "登录成功");
                        // 保存用户信息
                        UserRepository.getInstance().saveUserInfo(userBean);
                        // talkingData
                        TalkingDataAppCpa.onLogin(String.valueOf(userBean.getId()));
                        EventBus.getDefault().post(new LoginStateChangeEvent());
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_BIND_PHONE:
                    onBackPressed();
                    break;
                case REQUEST_CODE_REGISTER: // 注册成功后直接登录
                    if (data != null) {
                        String mobile   = data.getStringExtra("mobile");
                        String password = data.getStringExtra("password");
                        requestLogin(mobile, password);
                    }
                    break;
                default:
                    if (mShareAPI != null)
                        mShareAPI.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 手机登录
     */
    @OnClick(R.id.ib_phone_login)
    public void mobileLogin() {
        finish();
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.tv_forgetPwd)
    public void forgetPwd() {
        Intent intent = new Intent(mActivity, ResetPasswordActivity.class);
        intent.putExtra(KEY_MOBILE, mPhone);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_back)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
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
