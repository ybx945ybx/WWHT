package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import com.a55haitao.wwht.ui.activity.social.CommentListActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link CommentListActivity 评论页类型}
 *
 * @author 陶声
 * @since 2016-12-09
 */

@IntDef({CommentActivityType.POST,
        CommentActivityType.SPECIAL,
        CommentActivityType.EASYOPT})

@Retention(RetentionPolicy.SOURCE)
public @interface CommentActivityType {
    int POST = 0;    // 帖子

    int SPECIAL = 1;    // 专题

    int EASYOPT = 2;    // 草单
}
