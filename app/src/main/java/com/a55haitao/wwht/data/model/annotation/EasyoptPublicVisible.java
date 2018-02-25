package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.EasyoptPublicVisible.PUBLIC_INVISIBLE;
import static com.a55haitao.wwht.data.model.annotation.EasyoptPublicVisible.PUBLIC_VISIBLE;

/**
 * Created by Haotao_Fujie on 2016/11/14.
 */

@IntDef({PUBLIC_VISIBLE, PUBLIC_INVISIBLE})
@Retention(RetentionPolicy.SOURCE)

public @interface EasyoptPublicVisible {
    int PUBLIC_VISIBLE   = 0;   // 草单公开
    
    int PUBLIC_INVISIBLE = 1;   // 草单不公开
}