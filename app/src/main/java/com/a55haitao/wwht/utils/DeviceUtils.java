package com.a55haitao.wwht.utils;

import android.app.Activity;
import android.graphics.Point;
import android.provider.Settings;

/**
 * Created by 董猛 on 2016/10/8.
 */

public class DeviceUtils {

    private static String DEVICE_ID;           //设备ID
    private static int    SCREEN_WIDTH;           //屏幕宽度
    private static int    SCREEN_HEIGHT;          //屏幕高度
    private static float  sDensity;              //屏幕密度

    public static void init(Activity context) {
        DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Point point = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(point);
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;

        sDensity = context.getResources().getDisplayMetrics().density;
    }

    public static String getDevideId() {
        return DEVICE_ID == null ? "" : DEVICE_ID;
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static float getDensity() {
        return sDensity;
    }

    public static float dipToPx(int dip) {
        return sDensity * dip;
    }
}
