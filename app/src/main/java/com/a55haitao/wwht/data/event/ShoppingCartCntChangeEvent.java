package com.a55haitao.wwht.data.event;

/**
 * Created by Haotao_Fujie on 2016/11/8.
 */

public class ShoppingCartCntChangeEvent {

    public int shoppingCartBadgeCnt;

    public ShoppingCartCntChangeEvent(int shoppingCartBadgeCnt) {
        this.shoppingCartBadgeCnt = shoppingCartBadgeCnt;
    }
}
