package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

/**
 * 活动类型
 * Created by a55 on 2017/7/10.
 */
@IntDef({ActivityType.ActivityType_Site, ActivityType.ActivityType_Product})
public @interface ActivityType {
    int ActivityType_Site    = 0;      // 商家活动
    int ActivityType_Product = 1;      // 商品活动
}
