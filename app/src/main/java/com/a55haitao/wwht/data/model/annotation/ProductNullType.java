package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by a55 on 2017/5/22.
 */
@IntDef({ProductNullType.PRODUCT_MAIN, ProductNullType.PRODUCT_SEARCH})
@Retention(RetentionPolicy.SOURCE)
public @interface ProductNullType {
    int PRODUCT_MAIN   = 0;         // 商详页
    int PRODUCT_SEARCH = 1;         // 搜索结果页
}
