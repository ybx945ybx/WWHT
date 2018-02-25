package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({PageType.SEARCH, PageType.CATEGORY, PageType.BRAND, PageType.SELLER, PageType.SPECIAL, PageType.FULLCUT})
@Retention(RetentionPolicy.SOURCE)

public @interface PageType {
    int SEARCH   = 1;      //搜索
    int CATEGORY = 2;      //分类
    int BRAND    = 3;      //品牌
    int SELLER   = 4;      //商家
    int SPECIAL  = 5;      //专题
    int FULLCUT  = 6;      //专题
}
