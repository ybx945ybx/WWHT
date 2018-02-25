package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.RefundStateType.ORDER_REFUND_ACCEPTED;
import static com.a55haitao.wwht.data.model.annotation.RefundStateType.ORDER_REFUND_CANCEL;
import static com.a55haitao.wwht.data.model.annotation.RefundStateType.ORDER_REFUND_REQUEST;
import static com.a55haitao.wwht.data.model.annotation.RefundStateType.ORDER_REFUND_SUCCESS;
import static com.a55haitao.wwht.data.model.annotation.RefundStateType.ORDER_REFUND_USER_CANCEL;

/**
 * 申请退款状态
 * Created by a55 on 2017/6/8.
 */
@IntDef({ORDER_REFUND_ACCEPTED, ORDER_REFUND_SUCCESS, ORDER_REFUND_REQUEST, ORDER_REFUND_CANCEL, ORDER_REFUND_USER_CANCEL})
@Retention(RetentionPolicy.SOURCE)
public @interface RefundStateType {

    int ORDER_REFUND_ACCEPTED    = 0;            //   受理中
    int ORDER_REFUND_SUCCESS     = 1;            //   已受理
    int ORDER_REFUND_REQUEST     = 2;            //   待受理
    int ORDER_REFUND_CANCEL      = 3;            //   已关闭
    int ORDER_REFUND_USER_CANCEL = 4;            //   已取消


}
