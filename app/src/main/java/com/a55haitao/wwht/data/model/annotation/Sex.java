package com.a55haitao.wwht.data.model.annotation;

/**
 * 性别
 *
 * @author 陶声
 * @since 2016-08-29
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Sex.UNDEFINED, Sex.MAN, Sex.WOMAN})
@Retention(RetentionPolicy.SOURCE)

public @interface Sex {
    int UNDEFINED = 0;  // 未定义

    int MAN = 1;        // 男

    int WOMAN = 2;      // 女
}
