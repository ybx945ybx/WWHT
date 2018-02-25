package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 社区 - 精选 - Layout类型
 *
 * @author 陶声
 * @since 2016-11-21
 */
@IntDef({PostFragmentLayoutType.SINGLE, PostFragmentLayoutType.DOUBLE})
@Retention(RetentionPolicy.SOURCE)
public @interface PostFragmentLayoutType {
    int SINGLE = 1;
    int DOUBLE = 2;
}
