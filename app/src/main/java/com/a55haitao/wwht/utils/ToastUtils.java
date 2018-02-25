package com.a55haitao.wwht.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.a55haitao.wwht.HaiApplication;

/**
 * Toast工具类 解决Toast连续显示的问题
 * <p/>
 * Created by 陶声 on 16/7/28.
 */
public class ToastUtils {

    private static Toast sToast;

    public static void showToast(Context context, String content) {
        if (TextUtils.isEmpty(content))
            return;
        if (sToast == null) {
            sToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
        }

        sToast.show();
    }

    public static void showToast(String content) {
        if (TextUtils.isEmpty(content))
            return;
        if (sToast == null) {
            sToast = Toast.makeText(HaiApplication.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
        }

        sToast.show();
    }
}
