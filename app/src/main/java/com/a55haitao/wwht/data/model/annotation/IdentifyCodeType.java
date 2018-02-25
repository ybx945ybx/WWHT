package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 获取短信验证码类型
 * Created by 陶声 on 16/7/7.
 */
@IntDef({IdentifyCodeType.REGISTER,
        IdentifyCodeType.FORGOT_PSW,
        IdentifyCodeType.PAY,
        IdentifyCodeType.CHANGE_PSW,
        IdentifyCodeType.BIND,
        IdentifyCodeType.LOGIN_BY_CODE,
        IdentifyCodeType.THIRD_LOGIN})
@Retention(RetentionPolicy.SOURCE)

public @interface IdentifyCodeType {
    int REGISTER = 1;       //注册

    int FORGOT_PSW = 2;     //忘记密码

    int PAY = 3;            //支付

    int CHANGE_PSW = 4;     //修改密码

    int BIND = 5;           //绑定手机(绑定手机，也用这个类型)

    int LOGIN_BY_CODE = 6;  //验证码登录

    int THIRD_LOGIN = 7;    //三方账号第一次登录

}
