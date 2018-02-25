package com.a55haitao.wwht.ui.activity.myaccount.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.model.result.GetVerifyCodeResult;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.common.ChooseCountryActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.BugLyUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.TxtUtils;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

public class LoginVerifyCodeActivity extends BaseNoFragmentActivity {

    private static final int REQUEST_CODE_CHOOSE_COUNTRY = 0;   // 选择国家

    @BindView(R.id.tv_country)          HaiTextView mTvCountry;         // 国家
    @BindView(R.id.et_mobile)           EditText    mEtMobile;          // 手机号
    @BindView(R.id.img_clear_input)     ImageView   mImgClearInput;     // 清除输入
    @BindView(R.id.tv_send_verify_code) HaiTextView mTvSendVerifyCode;  // 发送验证码
    @BindView(R.id.et_verify_code)      EditText    mEtVerifyCode;      // 验证码
    @BindView(R.id.tv_pwd_login)        HaiTextView mTvPwdLogin;        // 密码登录

    @BindString(R.string.key_remember_mobile) String KEY_REMEMBER_MOBILE;

    @BindColor(R.color.colorTextYellow) int COLOR_SEND_VERIFY_CODE;

    private int            mRegionId;          // 国家码
    private CountDownTimer mCountDownTimer;         // 发送验证码倒计时
    private String         mPhone;

    private int src;     // 1 app自然注册,  3 app 0元团;
    private String activity_id;     // 导流过来的活动id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verify_code);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    private void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            src = intent.getIntExtra("src", -1);
            activity_id = intent.getStringExtra("activity_id");

        }

        mRegionId = 86;

    }

    private void initViews(Bundle savedInstanceState) {
        // 密码登录下划线
        mTvPwdLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        // 设置用户名不能为emoji
        mEtMobile.setFilters(new InputFilter[]{TxtUtils.EMOJI_FILTER, new InputFilter.LengthFilter(11)});
        // 记住账号
        mPhone = (String) SPUtils.get(mActivity, KEY_REMEMBER_MOBILE, "");
        if (!TextUtils.isEmpty(mPhone)) {
            mEtMobile.setText(mPhone);
            mEtVerifyCode.requestFocus();
            mImgClearInput.setVisibility(View.INVISIBLE);
        } else {
            mImgClearInput.setVisibility(View.VISIBLE);
        }

        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mImgClearInput.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
            }
        });

        // 焦点监听
        mEtMobile.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(mEtMobile.getText().toString()) && hasFocus) {
                mImgClearInput.setVisibility(View.VISIBLE);
            } else {
                mImgClearInput.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 跳转到本页面
     *
     * @param activity    activity
     * @param requestCode 请求码
     */
    public static void actionStart(Activity activity, int requestCode, int src) {
        Intent intent = new Intent(activity, LoginVerifyCodeActivity.class);
        intent.putExtra("src", src);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_send_verify_code)
    public void clickSendVerifyCode() {
        String mobile = mEtMobile.getText().toString();
        if (!sendVerifyCodeCheck(mobile)) { // 验证手机号不为空
            return;
        }
        showProgressDialog("正在加载", true);

        UserRepository.getInstance()
                .getVerifyCode(mobile, mRegionId)
                .subscribe(new DefaultSubscriber<GetVerifyCodeResult>() {
                    @Override
                    public void onSuccess(GetVerifyCodeResult result) {
                        ToastUtils.showToast(mActivity, result.msg);
                        if (result.status) { // 成功
                            startCountDown(); // 开始验证码倒计时
                        }
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
     * 发送验证码 验证
     *
     * @param phone 手机号
     */
    private boolean sendVerifyCodeCheck(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(mActivity, "手机号不能为空");
        } else if (phone.length() != 11) {
            ToastUtils.showToast(mActivity, "请输入正确的手机号");
        } else {
            return true;
        }
        return false;
    }

    /**
     * 清除输入
     */
    @OnClick(R.id.img_clear_input)
    public void clickClearInput() {
        mEtMobile.setText("");
    }

    /**
     * 登录
     */
    @OnClick(R.id.btn_login)
    public void clickLogin() {
        String mobile     = mEtMobile.getText().toString();
        String verifyCode = mEtVerifyCode.getText().toString();

        if (checkLogin(mobile, verifyCode)) {
            showProgressDialog("登录中", true);
            requestLogin(mobile, verifyCode);
        }
    }

    /**
     * 登录验证
     *
     * @param mobile     手机号
     * @param verifyCode 密码
     * @return 是否通过
     */
    private boolean checkLogin(String mobile, String verifyCode) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToast(mActivity, "请输入手机号");
        } else if (mobile.length() != 11) {
            ToastUtils.showToast(mActivity, "请输入正确的手机号");
        } else if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showToast(mActivity, "请输入验证码");
        } else if (verifyCode.length() != 6) {
            ToastUtils.showToast(mActivity, "请输入正确的验证码");
        } else {
            return true;
        }
        return false;
    }

    /**
     * 登录请求
     *
     * @param mobile     用户名
     * @param verifyCode 密码
     */
    private void requestLogin(final String mobile, String verifyCode) {
        UserRepository.getInstance()
                .checkMcodeLogin(mobile, verifyCode, mRegionId, src == 3 ? 3 : 1, activity_id)
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast(mActivity, "登录成功");
                        // 客服
                        NtalkerUtils.loginNtalker(userBean);
                        YbxTrace.getInstance().getTraceCommonBean().mid = userBean.getId() + "";

                        BugLyUtils.setUserId(userBean);
                        SPUtils.put(mActivity, KEY_REMEMBER_MOBILE, mobile);
                        // takling
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
     * 选择国家
     */
    @OnClick(R.id.tv_country)
    public void clickChooseCountry() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_Country, "", this, TraceUtils.PageCode_LoginVerifyCode, TraceUtils.PositionCode_SelectCountry + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_Country, TraceUtils.PageCode_LoginVerifyCode, TraceUtils.PositionCode_SelectCountry, "");

        ChooseCountryActivity.actionStart(mActivity, REQUEST_CODE_CHOOSE_COUNTRY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_COUNTRY:
                    mRegionId = data.getIntExtra("region_id", 0);
                    mTvCountry.setText(String.format("+%d", mRegionId));
                    break;
            }
        }
    }

    /**
     * 返回密码登录页
     */
    @OnClick(R.id.tv_pwd_login)
    public void clickPwdLogin() {
        finish();
    }

    /**
     * 取消
     */
    @OnClick(R.id.ib_cancel)
    public void clickBack() {
        setResult(RESULT_OK);
        finish();
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
