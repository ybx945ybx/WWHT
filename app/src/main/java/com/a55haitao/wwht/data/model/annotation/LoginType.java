package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * LoginType
 *
 * @author 陶声
 * @since 2016-09-14
 */
@IntDef({LoginType.PASSWORD,
        LoginType.VERIFY_CODE})
@Retention(RetentionPolicy.SOURCE)

public @interface LoginType {
    int PASSWORD = 1;       // 密码

    int VERIFY_CODE = 2;    // 验证码
}
