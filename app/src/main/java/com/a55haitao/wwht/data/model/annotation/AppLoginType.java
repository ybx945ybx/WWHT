package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 登录方式
 *
 * @author 陶声
 * @since 2016-09-14
 */
@IntDef({AppLoginType.MOBILE,
        AppLoginType.MAIL_USERNAME})

@Retention(RetentionPolicy.SOURCE)
public @interface AppLoginType {
    int MOBILE = 0;         // 手机

    int MAIL_USERNAME = 1;  // 邮箱/用户名
}
