package com.a55haitao.wwht.utils;

import android.text.TextUtils;

import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;

import java.text.DecimalFormat;

/**
 * 金额计算工具类
 * Created by 陶声 on 16/8/23.
 */
public class PriceUtils {
    public static int round(String amount) {
        return (int) Math.round(Double.valueOf(amount));
    }

    public static int round(double amount) {
        return (int) Math.round(amount);
    }

    public static boolean isZero(String amount) {
        return !(Double.valueOf(amount) > 0);
    }


    // Added by Jeff
    public static float roundf(float f) {
        return Math.round(f);
    }

    public static float rounds(String fStr) {
        Float f = toFloat(fStr);
        return roundf(f);
    }

    public static float toFloat(String str) {
        Float f = Float.parseFloat(str);
        return f;
    }

    public static boolean currentPriceGreaterOrEqualThanMallPrice(float currentPrice, float mallPrice) {

        if ((int) roundf(currentPrice) >= (int) roundf(mallPrice)) return true;
        return false;
    }

    public static String toRMBFromYuan(float yuan) {
        return toAnyCurrencyFromYuan(ApiUrl.RMB_UNICODE, yuan, false);
    }

    public static String toRMBFromYuan(String yuan) {
        return toAnyCurrencyFromYuan(ApiUrl.RMB_UNICODE, yuan, false);
    }

    public static String toRMBFromFen(float fen) {
        return toAnyCurrencyFromFen(ApiUrl.RMB_UNICODE, fen, false);
    }

    public static String toRMBFromFen(String fen) {
        return toAnyCurrencyFromFen(ApiUrl.RMB_UNICODE, fen, false);
    }

    public static String toRMBFromYuan(float yuan, String defaultZero) {
        if (yuan < 0.001) {
            return defaultZero;
        }
        return toAnyCurrencyFromYuan(ApiUrl.RMB_UNICODE, yuan, false);
    }

    public static String toRMBFromYuan(String yuan, String defaultZero) {
        if (rounds(yuan) < 0.001) {
            return defaultZero;
        }
        return toAnyCurrencyFromYuan(ApiUrl.RMB_UNICODE, yuan, false);
    }

    public static String toRMBFromFen(float fen, String defaultZero) {
        if (fen < 0.001) {
            return defaultZero;
        }
        return toAnyCurrencyFromFen(ApiUrl.RMB_UNICODE, fen, false);
    }

    public static String toRMBFromFen(String fen, String defaultZero) {
        Float f = toFloat(fen);
        if (roundf((float) (f / 100.0)) < 0.001) {
            return defaultZero;
        }
        ;
        return toAnyCurrencyFromFen(ApiUrl.RMB_UNICODE, f, false);
    }

    // 保留一位小数
    public static String FloatKeepOne(float f) {
        float         f1 = Math.round(f * 10) / 10f;
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(f1);
    }

    // 保留一位小数向下取整
    public static String FloatKeepOneFloor(float f) {
        float         f1 = ((int) Math.floor(f * 10))/ 10f;
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(f1);
    }

    // 是否显示打折
    public static boolean showDiscount(float discount) {
        if(discount == 0 || discount == 10){
            return false;
        }
        return true;
    }

    public static String toAnyCurrencyFromYuan(String currency, float yuan, boolean needTrans) {
        if (needTrans) {
            return translateUnit(currency) + (int) roundf(yuan);
        } else {
            return currency + (int) roundf(yuan);
        }

    }

    public static String toAnyCurrencyFromYuan(String currency, String yuan, boolean needTrans) {
        if (needTrans) {
            return translateUnit(currency) + (int) rounds(yuan);
        } else {
            return currency + (int) rounds(yuan);
        }
    }

    public static String toAnyCurrencyFromFen(String currency, float fen, boolean needTrans) {
        if (needTrans) {
            return translateUnit(currency) + (int) roundf((float) (fen / 100.0));
        } else {
            return currency + (int) roundf((float) (fen / 100.0));
        }
    }

    public static String toAnyCurrencyFromFen(String currency, String fen, boolean needTrans) {
        Float f = toFloat(fen);
        if (needTrans) {
            return translateUnit(currency) + (int) roundf((float) (f / 100.0));
        } else {
            return currency + (int) roundf((float) (f / 100.0));
        }
    }

    public static String translateUnit(String unit) {
        if (unit.equals("USD")) {
            return "$";
        } else if (unit.equals("CNY")) {
            return ApiUrl.RMB_UNICODE;
        } else if (unit.equals("GBP")) {
            return "£";
        } else if (unit.equals("JPY")) {
            return "JPY";
        } else if (unit.equals("EUR")) {
            return "€";
        } else if (unit.equals("AUD")) {
            return "A$"; // 或者AUD
        } else if (unit.equals("HKD")) {
            return "HK$";  // 或者HKD
        }
        return unit;
    }
}
