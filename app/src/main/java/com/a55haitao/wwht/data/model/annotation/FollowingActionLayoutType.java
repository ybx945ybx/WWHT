package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.FollowingActionLayoutType.TYPE_POST;
import static com.a55haitao.wwht.data.model.annotation.FollowingActionLayoutType.TYPE_USER;

/**
 * 社区 - 关注 - 布局类型
 *
 * @author 陶声
 * @since 2016-11-11
 */

@IntDef({TYPE_POST, TYPE_USER})
@Retention(RetentionPolicy.SOURCE)

public @interface FollowingActionLayoutType {
    int TYPE_POST = 1; // 帖子
    int TYPE_USER = 2; // 用户
}
