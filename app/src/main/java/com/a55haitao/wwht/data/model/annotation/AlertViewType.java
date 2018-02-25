package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlertViewType_Error;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlertViewType_OK;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlertViewType_Warning;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Add_Cart;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Pray;

/**
 * Created by Haotao_Fujie on 2016/11/14.
 */

@IntDef({AlertViewType_OK, AlertViewType_Warning, AlertViewType_Error, AlterViewType_Pray, AlterViewType_Add_Cart})
@Retention(RetentionPolicy.SOURCE)
public @interface AlertViewType {
    // 普通消息
    int AlertViewType_OK       = 0;           // OK
    int AlertViewType_Warning  = 1;           // Warning
    int AlertViewType_Error    = 2;           // Error
    int AlterViewType_Pray     = 3;           // 点赞商品，加入心愿单
    int AlterViewType_Add_Cart = 4;           // 加入购物车异常
}