package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 订单类型
 * <p>
 * Created by 陶声 on 16/8/19.
 */
@StringDef({OrderType.NEW,
        OrderType.ARRIVED,
        OrderType.SENT2,
        OrderType.SENT1,
        OrderType.OK,
        OrderType.FAIL,
        OrderType.BOUGHT,
        OrderType.PAIED,
        OrderType.ROB})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderType {
    String NEW     = "NEW";     // 待付款

    String ARRIVED = "ARRIVED"; // 到达转运仓库，待配送

    String SENT2   = "SENT2";   // 已转运配送，待收货

    String SENT1   = "SENT1";   // 官网已发货，待转运

    String OK      = "OK";      // 订单完成

    String FAIL    = "FAIL";    // 订单取消

    String BOUGHT  = "BOUGHT";  // 已采购，待发货

    String PAIED   = "PAIED";   // 已付款，待发货

    String ROB     = "ROB";     // 已抢单
}
