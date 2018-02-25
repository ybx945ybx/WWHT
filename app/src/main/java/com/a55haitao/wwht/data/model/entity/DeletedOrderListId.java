package com.a55haitao.wwht.data.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 已删除订单号
 *
 * @author 陶声
 * @since 2017-04-19
 */

public class DeletedOrderListId extends RealmObject {
    @PrimaryKey
    public String orderId;

    public DeletedOrderListId() {
    }

    public DeletedOrderListId(String orderId) {
        this.orderId = orderId;
    }
}
