package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.LoginLevels.DEVICE_LEVEL;
import static com.a55haitao.wwht.data.model.annotation.LoginLevels.NONE;
import static com.a55haitao.wwht.data.model.annotation.LoginLevels.USER_LEVEL;

/**
 * 登录级别
 *
 * @author 陶声
 * @since 2016-10-10
 */
@IntDef({NONE, DEVICE_LEVEL, USER_LEVEL})
@Retention(RetentionPolicy.SOURCE)

public @interface LoginLevels {

    int NONE = 1;

    int DEVICE_LEVEL = 2;

    int USER_LEVEL = 3;
}
