package com.a55haitao.wwht.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by drolmen on 2016/2/29.
 */
public class CheckHelper {
    private CheckHelper(){} ;

    public static boolean isEdtNull(EditText e){
        if (e == null)
            return false ;
        return TextUtils.isEmpty(e.getText().toString()) ;
    }

    /**
     * 判断身份证号码是否合法
     * @param e
     * @return
     */
    public static boolean isIDNumber(EditText e){
        if (e == null){
            return false ;
        } else {
            String num = e.getText().toString() ;
            return num.matches("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])") ;
        }
    }

    /**
     * 判断是否含数字
     * @param e
     * @return
     */
    public static boolean hasDigit(EditText e) {
        if (e == null){
            return true ;
        } else {
            boolean flag = false;
            String num = e.getText().toString() ;

            Pattern p    = Pattern.compile(".*\\d+.*");
            Matcher m    = p.matcher(num);
            if (m.matches()) {
                flag = true;
            }
            return flag;
        }
    }

    /**
     * 判断手机号码是否合法
     * @param e
     * @return
     */
    public static boolean isPhoneNumber(EditText e){
        if (e.getVisibility() == View.INVISIBLE || e.getVisibility() == View.GONE)
            return true ;
        if (e == null){
            return false ;
        } else {
            String num = e.getText().toString() ;
            return num.matches("^1\\d{10}$") ;
        }
    }

    /**
     * 判断银行卡号是否合法
     * @param bankNum
     * @return
     */
    public static boolean isBankCardNumber(String bankNum){
        if (TextUtils.isEmpty(bankNum)){
            return false ;
        }
        char[] cc = bankNum.toCharArray();
        int[] n = new int[cc.length + 1];
        int j = 1;
        for (int i = cc.length - 1; i >= 0; i--) {
            n[j++] = cc[i] - '0';
        }
        int even = 0;
        int odd = 0;
        for (int i = 1; i < n.length; i++) {
            if (i % 2 == 0) {
                int temp = n[i] * 2;
                if (temp < 10) {
                    even += temp;
                } else {
                    temp = temp - 9;
                    even += temp;
                }
            } else {
                odd += n[i];
            }
        }

        int total = even + odd;
        if (total % 10 == 0)
            return true;
        return false;
    }
}
