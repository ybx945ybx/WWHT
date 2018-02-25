package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by a55 on 2017/5/25.
 */
@IntDef({UseSynType.SYN_NOMAL, UseSynType.SYN_USE})
@Retention(RetentionPolicy.SOURCE)
public @interface UseSynType {
    int SYN_NOMAL = 0;      // 不使用相似词
    int SYN_USE   = 1;      //  使用相似词

}
