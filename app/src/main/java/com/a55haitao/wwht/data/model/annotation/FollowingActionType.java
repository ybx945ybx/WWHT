package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.FollowingActionType.TYPE_COMMENT;
import static com.a55haitao.wwht.data.model.annotation.FollowingActionType.TYPE_FOLLOW_USER;
import static com.a55haitao.wwht.data.model.annotation.FollowingActionType.TYPE_LIKE_POST;
import static com.a55haitao.wwht.data.model.annotation.FollowingActionType.TYPE_REPLY_COMMENT;
import static com.a55haitao.wwht.data.model.annotation.FollowingActionType.TYPE_WRITE_POST;

/**
 * 社区 - 关注类型
 *
 * @author 陶声
 * @since 2016-11-11
 */

@IntDef({TYPE_WRITE_POST, TYPE_COMMENT, TYPE_LIKE_POST, TYPE_FOLLOW_USER, TYPE_REPLY_COMMENT})
@Retention(RetentionPolicy.SOURCE)

public @interface FollowingActionType {
    int TYPE_WRITE_POST    = 1; // 发帖
    int TYPE_COMMENT       = 2; // 评论
    int TYPE_LIKE_POST     = 3; // 点赞帖子
    int TYPE_FOLLOW_USER   = 4; // 关注用户
    int TYPE_REPLY_COMMENT = 5; // 回复评论
}
