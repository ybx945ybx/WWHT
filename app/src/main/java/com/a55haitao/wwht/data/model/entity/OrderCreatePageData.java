package com.a55haitao.wwht.data.model.entity;

/**
 * Created by 55haitao on 2016/11/6.
 */

public class OrderCreatePageData {
    public AddressItemBean mAddressItemBean ;
    public OrderPrepareBean mOrderPrepareBean ;
    public CommissionBean mCommissionBean ;

    public OrderCreatePageData(AddressItemBean addressItemBean, OrderPrepareBean orderPrepareBean, CommissionBean commissionBean) {
        mAddressItemBean = addressItemBean;
        mOrderPrepareBean = orderPrepareBean;
        mCommissionBean = commissionBean;
    }
}
