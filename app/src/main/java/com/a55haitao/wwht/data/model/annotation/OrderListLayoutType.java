package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 订单列表Layout类型
 *
 * @author 陶声
 * @since 2016-10-18
 */

@IntDef({OrderListLayoutType.UNPAY,
        OrderListLayoutType.NORMAL,
        OrderListLayoutType.SHIPPING,
        OrderListLayoutType.COMPLETE,
        OrderListLayoutType.CANCEL})
@Retention(RetentionPolicy.SOURCE)

public @interface OrderListLayoutType {

    int UNPAY = 0;      // 待支付

    int NORMAL = 1;

    int SHIPPING = 2; // 物流中

    int COMPLETE = 3; // 完成

    int CANCEL = 4; // 取消

}

