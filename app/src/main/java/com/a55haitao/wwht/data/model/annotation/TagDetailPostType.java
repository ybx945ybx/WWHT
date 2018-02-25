package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标签详情页 帖子类型
 *
 * @author 陶声
 * @since 2016-11-23
 */
@IntDef({TagDetailPostType.HOT, TagDetailPostType.LATEST})
@Retention(RetentionPolicy.SOURCE)
public @interface TagDetailPostType {
    int HOT    = 1;
    int LATEST = 2;
}
