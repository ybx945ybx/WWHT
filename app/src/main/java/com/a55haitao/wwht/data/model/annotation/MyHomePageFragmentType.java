package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 个人主页 Fragment类型
 *
 * @author 陶声
 * @since 2016-10-12
 */
@IntDef({MyHomePageFragmentType.POST,
        MyHomePageFragmentType.LIKE})
@Retention(RetentionPolicy.SOURCE)

public @interface MyHomePageFragmentType {
    int POST = 0;       // 晒物

    int LIKE = 1;       // 点赞
}
