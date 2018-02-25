package com.a55haitao.wwht.ui.activity.myaccount;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.NameLengthFilter;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个性签名设置
 */
public class UserSignatureActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)            DynamicHeaderView mTitle;          // 标题
    @BindView(R.id.et_signature)     EditText          mEtSignature;    // 签名
    @BindView(R.id.tv_character_num) HaiTextView       mTvCharacterNum; // 签名文字长度

    @BindString(R.string.key_signature) String KEY_SIGNATURE;

    private NameLengthFilter mLengthFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signature);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void initViews() {
        // 获取用户签名
       /* String signature = UserRepository.getInstance()
                .getUserInfo()
                .getSignature();*/
        String signature = getIntent().getStringExtra("signature");
        // 设置用户签名
        mEtSignature.setText(signature);
        mEtSignature.setSelection(signature.length());
        mLengthFilter = new NameLengthFilter(mActivity, 100);
        mEtSignature.setFilters(new InputFilter[]{mLengthFilter});

        mTvCharacterNum.setText(getTextNumberStr(signature));
        // 字数变化监听
        mEtSignature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //                mTvCharacterNum.setText(String.valueOf(50 - (s.toString().length() + mLengthFilter.getChineseCount(s.toString())) / 2));
                mTvCharacterNum.setText(getTextNumberStr(s.toString()));
            }
        });
        // 保存签名
        mTitle.setHeadClickListener(() -> {
            Intent intent       = new Intent();
            String signatureTmp = mEtSignature.getText().toString();
            intent.putExtra(KEY_SIGNATURE, TextUtils.isEmpty(signatureTmp) ? "" : signatureTmp);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    /**
     * 获取签名文本长度
     *
     * @return 签名文本长度
     */
    public String getTextNumberStr(String str) {
        return String.valueOf(50 - (str.length() + mLengthFilter.getChineseCount(str)) / 2);
    }
}
