package com.a55haitao.wwht.data.event;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-09-20
 */

public class OrderChangeEvent {
    public boolean isDeleteOrder;

    public OrderChangeEvent() {
    }

    public OrderChangeEvent(boolean isDeleteOrder) {
        this.isDeleteOrder = isDeleteOrder;
    }

}
