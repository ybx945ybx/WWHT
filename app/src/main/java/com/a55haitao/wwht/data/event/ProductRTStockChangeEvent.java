package com.a55haitao.wwht.data.event;

/**
 * Created by Haotao_Fujie on 2016/11/7.
 */

public class ProductRTStockChangeEvent {
    public boolean hasStock;
    public ProductRTStockChangeEvent(boolean hasStock) {
        this.hasStock = hasStock;
    }
}
