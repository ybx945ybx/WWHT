package com.a55haitao.wwht.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.a55haitao.wwht.R;

/**
 * App 默认通用Dialog
 * Created by 陶声 on 16/8/9.
 */
public class HaiProgressDialog extends Dialog {
    private String mMsg;
    private TextView mTvMsg;

    public HaiProgressDialog(Context context) {
        this(context, R.style.ProgressDialog, null);
    }

    public HaiProgressDialog(Context context, int themeResId, String message) {
        this(context, themeResId, message, false);
    }

    public HaiProgressDialog(Context context, int themeResId, String message, boolean cancelable) {
        super(context, themeResId);

        this.mMsg = message;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_normal);

        mTvMsg = (HaiTextView) findViewById(R.id.tv_msg);
        setMessage(mMsg);
    }


    /**
     * 设置Dialog显示文字
     */
    public void setMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            mTvMsg.setText(msg);
        }
    }


}
