package com.a55haitao.wwht.ui.activity.myaccount.settings;

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
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.model.annotation.IdentifyCodeType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.data.repository.VerifyRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.common.ChooseCountryActivity;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.TraceUtils;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

public class BindPhoneActivity extends BaseNoFragmentActivity {
    @BindView(R.id.et_phone)           EditText    mEtPhone;                         // 手机号
    @BindView(R.id.et_verify_code)     EditText    mEtVerifyCode;              // 验证码
    @BindView(R.id.tv_send_verifyCode) TextView    mTvSendVerifyCode;      // 发送验证码
    @BindView(R.id.btn_submit)         Button      mBtnSubmit;                       // 提交
    @BindView(R.id.ib_cancel)          ImageButton mIbCancel;                    // 返回
    @BindView(R.id.tv_country)         TextView    mTvCountry;                     // 国家
    @BindView(R.id.img_clear_input)    ImageView   mImgClearInput;           // 清除输入

    @BindString(R.string.key_head_img)         String KEY_HEAD_IMG;
    @BindString(R.string.key_open_id)          String KEY_OPEN_ID;
    @BindString(R.string.key_access_token)     String KEY_ACCESS_TOKEN;
    @BindString(R.string.key_nickname)         String KEY_NICKNAME;
    @BindString(R.string.key_password)         String KEY_PASSWORD;
    @BindString(R.string.key_account_provider) String KEY_ACCOUNT_PROVIDER;
    @BindString(R.string.key_method)           String KEY_METHOD;
    @BindString(R.string.key_country)          String KEY_COUNTRY;
    @BindString(R.string.key_mobile)           String KEY_MOBILE;
    @BindString(R.string.key_identify_type)    String KEY_IDENTIFY_TYPE;
    @BindString(R.string.key_identify_code)    String KEY_IDENTIFY_CODE;

    @BindColor(R.color.colorBanana) int COLOR_SEND_VERIFY_CODE;

    private static final int REQUEST_CODE_CHOOSE_COUNTRY = 1;

    private String         mPhone;
    private String         mVerifyCode;
    private CountDownTimer mCountDownTimer;
    private String         mHeadImg;
    private String         mOpenId;
    private String         mAccessToken;
    private String         mNickname;
    private String         mAccountProvider;
    private int            mRegionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mHeadImg = intent.getStringExtra(KEY_HEAD_IMG);
            mOpenId = intent.getStringExtra(KEY_OPEN_ID);
            mAccessToken = intent.getStringExtra(KEY_ACCESS_TOKEN);
            mNickname = intent.getStringExtra(KEY_NICKNAME);
            mAccountProvider = intent.getStringExtra(KEY_ACCOUNT_PROVIDER);
        }
        mRegionId = 86;
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
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
    }


    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_send_verifyCode)
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
     * @return 是否通过验证
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
     *
     * @param phone 手机号
     */
    private void requestSendVerifyCode(String phone) {
        VerifyRepository.getInstance()
                .getNoLoginVerifyCode(IdentifyCodeType.THIRD_LOGIN, phone, mRegionId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean commonDataBean) {
                        ToastUtils.showToast(mActivity, "验证码已发送");
                        startCountDown();
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

        // 开始倒计时
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
     * 提交
     */
    @OnClick(R.id.btn_submit)
    public void submit() {
        String phone      = mEtPhone.getText().toString();
        String verifyCode = mEtVerifyCode.getText().toString();

        if (checkSubmit(phone, verifyCode)) {
            showProgressDialog(true);
            requestSubmit(phone, verifyCode);
        }
    }

    /**
     * 提交验证
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @return 是否通过验证
     */
    private boolean checkSubmit(String phone, String verifyCode) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(mActivity, "手机号不能为空");
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
     * 提交请求
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     */
    private void requestSubmit(String phone, String verifyCode) {
        UserRepository.getInstance()
                .thirdRegister(mRegionId, phone, mHeadImg, mOpenId, mAccessToken, mNickname, mAccountProvider, verifyCode)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast(mActivity, "绑定成功");

                        // 埋点
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Mobile, phone);
                        kv.put(TraceUtils.Event_Kv_Country, mRegionId + "");
                        kv.put(TraceUtils.Event_Kv_Openid, mOpenId);
                        kv.put(TraceUtils.Event_Kv_Account, mAccountProvider);
                        kv.put(TraceUtils.Event_Kv_Code, "0");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Bind3rdParty, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Register3, kv, "");

                        UserRepository.getInstance()
                                .saveUserInfo(userBean);

                        EventBus.getDefault().post(new LoginStateChangeEvent());
                        setResult(RESULT_OK);
                        onBackPressed();
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
                        kv.put(TraceUtils.Event_Kv_Openid, mOpenId);
                        kv.put(TraceUtils.Event_Kv_Account, mAccountProvider);
                        kv.put(TraceUtils.Event_Kv_Code, "1");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Bind3rdParty, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Register3, kv, "");

                        return super.onFailed(e);
                    }
                });
    }


    /**
     * 选择国家
     */
    @OnClick(R.id.tv_country)
    public void chooseCountry() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_Country, "", this, TraceUtils.PageCode_BindPhone, TraceUtils.PositionCode_SelectCountry + "", "");

//        TraceUtils.addAnalysAct(TraceUtils.PageCode_Country, TraceUtils.PageCode_BindPhone, TraceUtils.PositionCode_SelectCountry, "");

        Intent intent = new Intent(mActivity, ChooseCountryActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_COUNTRY);
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_COUNTRY) {
            mRegionId = data.getIntExtra("region_id", 0);
            mTvCountry.setText("+" + mRegionId);
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_cancel)
    public void back() {
        onBackPressed();
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
