package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.a55haitao.wwht.R;

/**
 * 已退款弹窗
 *
 * @author 陶声
 * @since 2017-04-25
 */

public class RefundPopupWindow extends PopupWindow {
    private Activity mContext;

    public RefundPopupWindow(Activity context) {
        super(context);
        init(context);
    }

    private void init(Activity context) {
        mContext = context;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_triangle, null, false);
        setContentView(contentView);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);

        Window window = mContext.getWindow();
        setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = window.getAttributes();
            lp1.alpha = 1f;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(lp1);
        });
    }

    /**
     * 显示或隐藏
     *
     * @param v anchorView
     */
    public void showOrDismiss(View v) {
        Window window = mContext.getWindow();
        if (isShowing()) {
            dismiss();
        } else {
            // 产生背景变暗效果
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.4f;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(lp);
            showAsDropDown(v, 0, mContext.getResources().getDimensionPixelSize(R.dimen.margin_medium));
        }
    }
}
