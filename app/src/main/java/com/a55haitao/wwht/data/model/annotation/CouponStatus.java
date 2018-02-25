package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 优惠券状态
 *
 * @author 陶声
 * @since 2017-05-09
 */

@IntDef({CouponStatus.UNUSED, CouponStatus.OUTDATED, CouponStatus.USED, CouponStatus.UNAVAILABLE})
@Retention(RetentionPolicy.SOURCE)

public @interface CouponStatus {
    int UNUSED      = 1; // 未使用
    int OUTDATED    = 2; // 已过期
    int USED        = 3; // 已使用
    int UNAVAILABLE = 4; // 不可用
}
