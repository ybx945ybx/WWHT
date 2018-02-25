package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 个人中心 - 关注 & 粉丝 Fragment类型
 *
 * @author 陶声
 * @since 2016-10-10
 */
@IntDef({FollowFansFragmentType.FOLLOW,
        FollowFansFragmentType.FANS})
@Retention(RetentionPolicy.SOURCE)

public @interface FollowFansFragmentType {
    int FOLLOW = 0; // 关注

    int FANS = 1;   // 粉丝
}
