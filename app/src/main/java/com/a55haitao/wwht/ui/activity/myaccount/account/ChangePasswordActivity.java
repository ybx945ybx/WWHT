package com.a55haitao.wwht.ui.activity.myaccount.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.IdentifyCodeType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.data.repository.VerifyRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改密码页面
 * <p>
 * Created by 55haitao1 on 16/5/26.
 */
public class ChangePasswordActivity extends BaseNoFragmentActivity {
    @BindView(R.id.title)             DynamicHeaderView mTitle;             // 标题
    @BindView(R.id.et_verifyCode)     EditText          mEtVerifyCode;      // 验证码
    @BindView(R.id.img_clear_input)   ImageView         ivClear;            // 清除输入
    @BindView(R.id.tv_sendVerifyCode) TextView          mTvSendVerifyCode;  // 发送验证码
    @BindView(R.id.et_newPwd)         EditText          mEtNewPwd;          // 新密码
    @BindView(R.id.btn_submit)        Button            mBtnSubmit;         // 提交
    @BindView(R.id.tv_phone)          HaiTextView       mTvPhone;           // 手机号

    @BindString(R.string.key_user_id)       String KEY_USER_ID;
    @BindString(R.string.key_mobile)        String KEY_MOBILE;
    @BindString(R.string.key_password)      String KEY_PASSWORD;
    @BindString(R.string.key_identify_code) String KEY_IDENTIFY_CODE;
    @BindString(R.string.key_identify_type) String KEY_IDENTIFY_TYPE;

    private Activity       mActivity;
    private String         mMobile;
    private CountDownTimer mCountDownTimer; // 发送验证码倒计时
    private Tracker        mTracker;        // GA Tracker


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        mActivity = this;

        Intent intent = getIntent();
        if (intent != null) {
            mMobile = intent.getStringExtra(KEY_MOBILE);  // 获取mobile
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("修改密码");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        ivClear.setOnClickListener(v -> mEtNewPwd.setText(""));
        mTvPhone.setText(mMobile);
        mEtNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    ivClear.setVisibility(View.INVISIBLE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitle.setHeadClickListener(() -> {

        });
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_sendVerifyCode)
    public void clickSendVerifyCode() {
        showProgressDialog(true);
        VerifyRepository.getInstance()
                .getNoLoginVerifyCode(IdentifyCodeType.FORGOT_PSW, mMobile)
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
     * 提交密码修改
     */
    @OnClick(R.id.btn_submit)
    public void clickSubmit() {
        String verifyCode = mEtVerifyCode.getText().toString();
        String newPwd     = mEtNewPwd.getText().toString();

        if (!checkSubmit(verifyCode, newPwd)) // 输入验证
            return;

        showProgressDialog();

        UserRepository.getInstance()
                .resetPassword(86, mMobile, newPwd, verifyCode)
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast(mActivity, "修改成功");
                        UserRepository.getInstance()
                                .saveUserInfo(userBean);

                        onBackPressed();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
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
                mTvSendVerifyCode.setText("发送验证码");
            }
        }.start();
    }


    /**
     * 修改密码 验证
     *
     * @param verifyCode 验证码
     * @param newPwd     新密码
     * @return 是否通过
     */
    private boolean checkSubmit(String verifyCode, String newPwd) {
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showToast(mActivity, "验证码不能为空");
        } else if (verifyCode.length() != 6) {
            ToastUtils.showToast(mActivity, "请输入正确的验证码");
        } else if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.showToast(mActivity, "密码不能为空");
        } else {
            return true;
        }
        return false;
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
