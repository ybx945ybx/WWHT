package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 订单类型
 * <p>
 * Created by 陶声 on 16/8/19.
 */
@StringDef({OrderFragmentType.ALL,
        OrderFragmentType.UNPAY,
        OrderFragmentType.UNRECEIVE,
        OrderFragmentType.COMPLETE,
        OrderFragmentType.UNDELIVER})
@Retention(RetentionPolicy.SOURCE)

public @interface OrderFragmentType {
    String ALL = "全部";        // 全部订单

    String UNPAY = "未付款";      // 未付款

    String UNRECEIVE = "待收货";  // 待收货

    String COMPLETE = "已完成";   // 已完成

    String UNDELIVER = "待发货";  // 待发货
}
