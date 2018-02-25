package com.a55haitao.wwht.ui.activity.myaccount.settings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置 - 意见反馈
 */
public class FeedbackActivity extends BaseNoFragmentActivity {
    @BindView(R.id.ib_cancel)       ImageButton mIbBack;        // 返回按钮
    @BindView(R.id.tv_submit)       TextView    mTvSubmit;      // 提交
    @BindView(R.id.et_content)      EditText    mEtContent;     // 反馈内容
    @BindView(R.id.et_contact_info) EditText    mEtContactInfo; // 联系方式

    @BindString(R.string.key_content) String KEY_CONTENT;
    @BindString(R.string.key_method)  String KEY_METHOD;
    @BindString(R.string.key_email)   String KEY_EMAIL;
    @BindString(R.string.key_qq)      String KEY_QQ;
    @BindString(R.string.key_mobile)  String KEY_MOBILE;

    private Dialog          mDialog;    // 感谢反馈Dialog
    private String          mMobile;
    private boolean         hasCommit;  // 是否已经提交反馈
    private Tracker         mTracker;   // GA Tracker
    private ToastPopuWindow mPwToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initVariables();
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mMobile = intent.getStringExtra("mobile");
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("意见反馈");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * 提交
     */
    @OnClick(R.id.tv_submit)
    public void submit() {
        // 读取文本
        String content     = mEtContent.getText().toString();
        String contactInfo = mEtContactInfo.getText().toString();

        // 输入验证
        if (!submitCheck(content)) {
            mPwToast = ToastPopuWindow.makeText(FeedbackActivity.this, "请输入反馈内容", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return;
        }

        // 网络请求
        SnsRepository.getInstance()
                .feedback(content, contactInfo)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean result) {
                        if (result.success) {
                            hasCommit = true;
                            showThanksDialog();
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 提交验证
     *
     * @param content 内容
     * @return 是否通过验证
     */
    private boolean submitCheck(String content) {
        return !TextUtils.isEmpty(content);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_cancel)
    public void back() {
        onBackPressed();
    }

    /**
     * 弹出感谢反馈Dialog
     */
    public void showThanksDialog() {
        mDialog = new Dialog(this);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View     v          = LayoutInflater.from(this).inflate(R.layout.dlg_feedback, null);
        TextView tvNoThanks = (HaiTextView) v.findViewById(R.id.tv_no_thanks);
        tvNoThanks.setOnClickListener(v1 -> {
            mDialog.dismiss();
            onBackPressed();
        });

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mDialog.dismiss();
                onBackPressed();
            }
            return true;
        });

        mDialog.setContentView(v);
        mDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(mEtContent.getText().toString()) && !hasCommit) {
            new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                    .setMessage(R.string.alert_feedback_quit)
                    .setPositiveButton("确定", (dialog, which) -> {
                        super.onBackPressed();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }
}
