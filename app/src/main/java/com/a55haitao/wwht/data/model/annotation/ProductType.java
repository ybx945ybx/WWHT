package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 商品状态
 *
 * @author 陶声
 * @since 2017-04-19
 */
@StringDef({ProductType.REFUND_ACCEPTED,
        ProductType.REFUND_SUCCESS,
        ProductType.PROCUREMENT_SUCCESS,
        ProductType.PROCUREMENT})
@Retention(RetentionPolicy.SOURCE)

public @interface ProductType {

    // 退款部分
    String REFUND_REQUEST     = "REFUND_REQUEST";         // 申请退款待受理
    String REFUND_ACCEPTED    = "REFUND_ACCEPTED";        // 退款中
    String USER_REFUND_CANCEL = "USER_REFUND_CANCEL";     // 已取消
    String REFUND_CANCEL      = "REFUND_CANCEL";          // 已关闭
    String REFUND_SUCCESS     = "REFUND_SUCCESS";         // 退款成功

    // 采购部分
    String PROCUREMENT_SUCCESS = "PROCUREMENT_SUCCESS";   // 采购成功
    String PROCUREMENT         = "PROCUREMENT";           // 已采购，待海外官网确认

}