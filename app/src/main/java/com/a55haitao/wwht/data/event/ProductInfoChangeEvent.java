package com.a55haitao.wwht.data.event;

import com.a55haitao.wwht.data.model.entity.OrderCommitBean;

/**
 * Created by a55 on 2017/11/20.
 */

public class ProductInfoChangeEvent {
    public OrderCommitBean result;
    public String message;
    public ProductInfoChangeEvent(OrderCommitBean result, String message){
        this.result = result;
        this.message = message;
    }
}
