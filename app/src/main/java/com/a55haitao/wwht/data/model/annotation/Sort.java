package com.a55haitao.wwht.data.model.annotation;

/**
 * 性别
 *
 * @author 陶声
 * @since 2016-08-29
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Sort.HOT, Sort.SALE, Sort.PRICE_ASCEND,Sort.PRICE_DESCEND})
@Retention(RetentionPolicy.SOURCE)

public @interface Sort {
     int HOT = 0;               //热门
     int SALE = 1;              //折扣
     int PRICE_ASCEND = 2;      //升序
     int PRICE_DESCEND = 3;     //降序
}
