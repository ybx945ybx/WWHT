package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-11-10
 */
@IntDef({SpecialDetailContentType.TYPE_TEXT,
        SpecialDetailContentType.TYPE_IMG,
        SpecialDetailContentType.TYPE_PRODUCT})
@Retention(RetentionPolicy.SOURCE)

public @interface SpecialDetailContentType {
    int TYPE_TEXT    = 0;
    int TYPE_IMG     = 1;
    int TYPE_PRODUCT = 2;
}
