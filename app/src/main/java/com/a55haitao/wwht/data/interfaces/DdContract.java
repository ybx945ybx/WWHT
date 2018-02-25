package com.a55haitao.wwht.data.interfaces;

import com.a55haitao.wwht.data.model.entity.CouponBean;
import com.a55haitao.wwht.data.model.entity.OrderPrepareBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 董猛 on 16/7/14.
 */
public interface DdContract {

    interface DdViewIml {

        /**
         * 设置收货信息
         */
        void setAddressInfo() ;

        /**
         * 设置优惠券信息
         * @param couponInfo
         */
        void setCouponInfo(CouponBean couponInfo) ;

        /**
         * 更新支付金额
         */
        void updatePayment(int money) ;

        /**
         * 显示商家产品信息
         * @param storelists
         */
        void setSellersView(ArrayList<OrderPrepareBean.StorelistBean> storelists) ;

        /**
         * 获取支付方式
         * @return 支付方式,返回alipay 或 wechat
         */
        String getPayWay() ;
    }

    interface DdPresenterIml{
        /**
         * 使用佣金
         */
        void useBrokerage() ;

        /**
         * 重新计算最终支付金额
         */
        double reComputePayment() ;

        /**
         * 校验收货信息
         * @return
         */
//        boolean examAddressInfo(NetCouponInfo couponInfo) ;

        /**
         * 生成订单需要的所有参数
         * @param isCreatDd 是否创建订单
         * @return 订单所需要的所有参数
         */
        HashMap<String,Object> buildDdParameter(boolean isCreatDd) ;

    }

}
