package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 个人中心 Fragment类型（old）
 *
 * @author 陶声
 * @since 2016-10-12
 */
@IntDef({MyAccountFragmentType.POST,
        MyAccountFragmentType.LIKE,
        MyAccountFragmentType.WISHLIST,
        MyAccountFragmentType.SPECIAL})
@Retention(RetentionPolicy.SOURCE)

public @interface MyAccountFragmentType {
    int POST = 0;       // 晒物

    int LIKE = 1;       // 点赞

    int WISHLIST = 2;   // 心愿单

    int SPECIAL = 3;    // 专题
}
