package com.a55haitao.wwht.ui.activity.myaccount.account;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.model.annotation.IdentifyCodeType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.data.repository.VerifyRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.common.ChooseCountryActivity;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.TraceUtils;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 注册页面
 * <p>
 * Created by 55haitao1 on 16/5/25.
 * <p>
 * Modified by 陶声 on 16/6/30
 */
public class RegisterActivity extends BaseNoFragmentActivity {
    @BindView(R.id.tv_country)          TextView  mTvCountry;          // 国家
    @BindView(R.id.et_mobile)           EditText  mEtPhone;            // 手机号
    @BindView(R.id.img_clear_input)     ImageView mImgClearInput;      // 清除输入
    @BindView(R.id.et_verify_code)      EditText  mEtVerifyCode;       // 验证码
    @BindView(R.id.tv_send_verify_code) TextView  mTvSendVerifyCode;   // 发送验证码
    @BindView(R.id.et_password)         EditText  mEtPassword;         // 密码
    @BindView(R.id.tv_user_agreement)   TextView  mTvUserAgreement;    // 用户协议
    @BindView(R.id.tv_private_policy)   TextView  mTvPrivatePolicy;    // 隐私声明
    @BindView(R.id.tv_login)            TextView  mTvLogin;            // 登录

    @BindColor(R.color.colorTextYellow) int COLOR_SEND_VERIFY_CODE;

    private static final int REQUEST_CODE_CHOOSE_COUNTRY = 1;
    private static final int REQUEST_SET_USER_INFO       = 2;

    private CountDownTimer mCountDownTimer;         // 发送验证码倒计时
    private int            mRegionId;               // 国家码
    private int            src;                     // 1 app自然注册,  3 app 0元团;
    private String         activity_id;             // 导流过来的活动id

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initVariables();
        initViews();
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            src = intent.getIntExtra("src", -1);
            activity_id = intent.getStringExtra("activity_id");
        }
        mRegionId = 86;
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        // 清除输入
        mImgClearInput.setOnClickListener(v -> mEtPhone.setText(""));

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mImgClearInput.setVisibility(View.VISIBLE);
                } else {
                    mImgClearInput.setVisibility(View.INVISIBLE);
                }
            }
        });
        // 焦点监听
        mEtPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(mEtPhone.getText().toString()) && hasFocus) {
                mImgClearInput.setVisibility(View.VISIBLE);
            } else {
                mImgClearInput.setVisibility(View.INVISIBLE);
            }
        });

        mTvUserAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvPrivatePolicy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_send_verify_code)
    public void sendVerifyCode() {
        String phone = mEtPhone.getText().toString();

        if (!sendVerifyCodeCheck(phone)) { // 验证手机号不为空
            return;
        }

        showProgressDialog("正在加载", true);

        VerifyRepository.getInstance()
                .getNoLoginVerifyCode(IdentifyCodeType.REGISTER, phone)
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean commonDataBean) {
                        ToastUtils.showToast(mActivity, "验证码已发送");
                        startCountDown(); // 开始验证码倒计时
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 注册
     */
    @OnClick(R.id.btn_register)
    public void register() {
        String phone      = mEtPhone.getText().toString();
        String verifyCode = mEtVerifyCode.getText().toString();
        String password   = mEtPassword.getText().toString();
        if (!registerCheck(phone, verifyCode, password)) { // 输入验证
            return;
        }

        showProgressDialog("正在加载", true);

        UserRepository.getInstance()
                .register(phone, password, verifyCode, mRegionId, src == 3 ? 3 : 1, activity_id)
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast(mActivity, "注册成功");

                        // 埋点
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Mobile, phone);
                        kv.put(TraceUtils.Event_Kv_Country, mRegionId + "");
                        kv.put(TraceUtils.Event_Kv_Code, "0");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Register, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Register, kv, "");

                        // 保存用户信息
                        UserRepository.getInstance()
                                .saveUserInfo(userBean);
                        // talkingData
                        TalkingDataAppCpa.onRegister(String.valueOf(userBean.getId()));
                        EventBus.getDefault().post(new LoginStateChangeEvent());
                        //  引导用户填写昵称和头像
                        Intent intent = new Intent(RegisterActivity.this, RegisterSetUserInfoActivity.class);
                        startActivityForResult(intent, REQUEST_SET_USER_INFO);

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Mobile, phone);
                        kv.put(TraceUtils.Event_Kv_Country, mRegionId + "");
                        kv.put(TraceUtils.Event_Kv_Code, "1");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Register, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Register, kv, "");

                        return super.onFailed(e);
                    }
                });

    }

    /**
     * 发送验证码 验证
     *
     * @param phone 手机号
     */
    private boolean sendVerifyCodeCheck(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(mActivity, "手机号不能为空");
            return false;
        }
        return true;
    }

    /**
     * 发送验证码 验证
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @param password   密码
     */
    private boolean registerCheck(String phone, String verifyCode, String password) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(mActivity, "手机号不能为空");
        } else if (TextUtils.isEmpty(password)) {
            ToastUtils.showToast(mActivity, "密码不能为空");
        } else if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showToast(mActivity, "验证码不能为空");
        } else if (6 != verifyCode.length()) {
            ToastUtils.showToast(mActivity, "验证码格式有误");
        } else {
            return true;
        }

        return false;
    }

    /**
     * 发送验证码倒计时
     */
    private void startCountDown() {
        mTvSendVerifyCode.setEnabled(false);
        mTvSendVerifyCode.setTextColor(Color.GRAY);

        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) { // 开始倒计时
            @Override
            public void onTick(long millisUntilFinished) {
                mTvSendVerifyCode.setText("等待" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                mTvSendVerifyCode.setEnabled(true);
                mTvSendVerifyCode.setTextColor(COLOR_SEND_VERIFY_CODE);

                mTvSendVerifyCode.setText("发送验证码");
            }
        }.start();
    }

    /**
     * 用户协议 & 隐私政策
     */
    @OnClick({R.id.tv_user_agreement, R.id.tv_private_policy})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_user_agreement:
                H5Activity.toThisActivity(mActivity, ServerUrl.USER_AGREEMENT_URL, "用户协议");
                break;
            case R.id.tv_private_policy:
                H5Activity.toThisActivity(mActivity, ServerUrl.PRIVACY_POLICY_URL, "隐私声明");

                break;
        }
    }


    /**
     * 选择国家
     */
    @OnClick(R.id.tv_country)
    public void chooseCountry() {
        Intent intent = new Intent(mActivity, ChooseCountryActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_COUNTRY);
        overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
    }

    /**
     * 返回
     */
    @OnClick({R.id.ib_cancel, R.id.tv_login})
    public void back() {
        onBackPressed();
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_COUNTRY) {
                mRegionId = data.getIntExtra("region_id", 0);
                mTvCountry.setText("+" + mRegionId);
            } else if (requestCode == REQUEST_SET_USER_INFO) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCountDownTimer != null)
            mCountDownTimer.cancel(); // 取消倒计时
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
