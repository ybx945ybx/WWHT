package com.a55haitao.wwht.ui.activity.myaccount.account;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.annotation.IdentifyCodeType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.data.repository.VerifyRepository;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.common.ChooseCountryActivity;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重置密码
 * Created by 陶声 on 2016-08-25
 */
public class ResetPasswordActivity extends BaseNoFragmentActivity {

    @BindView(R.id.ib_cancel)             ImageButton mIbCancel;            // 返回
    @BindView(R.id.tv_country)            TextView    mTvCountry;           // 国家
    @BindView(R.id.et_phone)              EditText    mEtPhone;             // 手机号
    @BindView(R.id.et_verify_code)        EditText    mEtVerifyCode;        // 验证码
    @BindView(R.id.tv_send_verify_code)   TextView    mTvSendVerifyCode;    // 发送验证码
    @BindView(R.id.et_new_pwd)            EditText    mEtNewPwd;            // 新的密码
    @BindView(R.id.btn_submit)            Button      mBtnSubmit;           // 提交
    @BindView(R.id.img_clear_input_phone) ImageView   mImgClearInputPhone;  // 清除输入的手机号
    @BindView(R.id.img_clear_input_pwd)   ImageView   mImgClearInputPwd;    // 清除输入的密码

    @BindString(R.string.key_user_id)       String KEY_USER_ID;
    @BindString(R.string.key_mobile)        String KEY_MOBILE;
    @BindString(R.string.key_identify_type) String KEY_IDENTIFY_TYPE;
    @BindString(R.string.key_password)      String KEY_PASSWORD;
    @BindString(R.string.key_identify_code) String KEY_IDENTIFY_CODE;
    @BindString(R.string.key_country)       String KEY_COUNTRY;
    @BindString(R.string.key_method)        String KEY_METHOD;

    @BindColor(R.color.colorTextYellow) int COLOR_SEND_VERIFY_CODE;

    private static final int REQUEST_CODE_CHOOSE_COUNTRY = 1;

    private CountDownTimer mCountDownTimer; // 发送验证码倒计时
    private int            mRegionId;       // 国家码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    protected int getLayout() {
        return R.layout.activity_reset_password;
    }

    public void initVariables() {
        mRegionId = 86;
        Intent intent = getIntent();
        if (intent != null) {
            String phone       = intent.getStringExtra(KEY_MOBILE);
            int    countryCode = intent.getIntExtra(KEY_COUNTRY, -1);

            if (countryCode != -1) {
                mRegionId = countryCode;
            }

            if (!TextUtils.isEmpty(phone)) {
                mEtPhone.setText(phone);
                mEtNewPwd.requestFocus();
                mImgClearInputPhone.setVisibility(View.INVISIBLE);
            } else {
                mImgClearInputPhone.setVisibility(View.VISIBLE);
            }
        }

    }

    public void initViews(Bundle savedInstanceState) {
        // 清除输入
        mImgClearInputPhone.setOnClickListener(v -> mEtPhone.setText(""));
        mImgClearInputPwd.setOnClickListener(v -> mEtNewPwd.setText(""));

        // 输入监听
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
                    mImgClearInputPhone.setVisibility(View.VISIBLE);
                } else {
                    mImgClearInputPhone.setVisibility(View.INVISIBLE);
                }
            }
        });
        mEtNewPwd.addTextChangedListener(new TextWatcher() {
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

        // 焦点监听
        mEtPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(mEtPhone.getText().toString()) && hasFocus) {
                mImgClearInputPhone.setVisibility(View.VISIBLE);
            } else {
                mImgClearInputPhone.setVisibility(View.INVISIBLE);
            }
        });
        mEtNewPwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(mEtNewPwd.getText().toString()) && hasFocus) {
                mImgClearInputPwd.setVisibility(View.VISIBLE);
            } else {
                mImgClearInputPwd.setVisibility(View.INVISIBLE);
            }
        });
    }


    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_send_verify_code)
    public void sendVerifyCode() {
        String phone = mEtPhone.getText().toString();
        if (checkSendVerifyCode(phone)) {
            showProgressDialog(true);
            requestSendVerifyCode(phone);
        }

    }

    /**
     * 发送验证码验证
     *
     * @param phone 手机号
     */
    private boolean checkSendVerifyCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(mActivity, "手机号不能为空");
            return false;
        }
        return true;
    }

    /**
     * 发送验证码请求
     */
    private void requestSendVerifyCode(String phone) {
        VerifyRepository.getInstance()
                .getNoLoginVerifyCode(IdentifyCodeType.FORGOT_PSW, phone)
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean commonDataBean) {
                        ToastUtils.showToast(mActivity, "验证码已发送");
                        startCountDown(); // 发送验证码倒计时开始
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 提交
     */
    @OnClick(R.id.btn_submit)
    public void submit() {
        String phone      = mEtPhone.getText().toString();
        String verifyCode = mEtVerifyCode.getText().toString();
        String newPwd     = mEtNewPwd.getText().toString();

        if (checkSubmit(phone, verifyCode, newPwd)) {
            showProgressDialog(true);
            requestResetPassword(phone, verifyCode, newPwd);
        }
    }

    /**
     * 重置密码验证
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @param newPwd     密码
     */
    private boolean checkSubmit(String phone, String verifyCode, String newPwd) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(mActivity, "手机号不能为空");
        } else if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showToast(mActivity, "验证码不能为空");
        } else if (6 != verifyCode.length()) {
            ToastUtils.showToast(mActivity, "验证码格式有误");
        } else if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.showToast(mActivity, "密码不能为空");
        } else {
            return true;
        }
        return false;
    }

    /**
     * 重置密码请求
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @param newPwd     新密码
     */
    private void requestResetPassword(String phone, String verifyCode, String newPwd) {
        UserRepository.getInstance()
                .resetPassword(mRegionId, phone, newPwd, verifyCode)
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast(mActivity, "修改成功");

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
    @OnClick(R.id.ib_cancel)
    public void back() {
        onBackPressed();
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_COUNTRY) {
            // intent.putExtra("region_id", country.getCode());
            mRegionId = data.getIntExtra("region_id", 0);
            mTvCountry.setText(String.valueOf("+" + mRegionId));
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
