package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 个人中心 - 消息Fragment类型
 *
 * @author 陶声
 * @since 2016-10-14
 */
@IntDef({MessageFragmentType.MESSAGE,
        MessageFragmentType.NOTIFICATION})
@Retention(RetentionPolicy.SOURCE)

public @interface MessageFragmentType {
    int NOTIFICATION = 0;   // 通知
    int MESSAGE      = 1;        // 消息
}
