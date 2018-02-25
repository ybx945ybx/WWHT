package com.a55haitao.wwht.data.interfaces;

import java.util.HashMap;

/**
 * Created by 董猛 on 16/7/14.
 */
public class DdPresenter implements DdContract.DdPresenterIml {

    private DdContract.DdViewIml ddView ;

    public DdPresenter(DdContract.DdViewIml ddView) {
        this.ddView = ddView;
    }

    @Override
    public void useBrokerage() {

    }

    @Override
    public double reComputePayment() {
        return 0 ;
    }

//    @Override
//    public boolean examAddressInfo(NetCouponInfo couponInfo) {
//        return false;
//    }

    @Override
    public HashMap<String, Object> buildDdParameter(boolean isCreatDd) {
        return null;
    }

}
