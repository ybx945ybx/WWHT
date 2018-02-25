package com.a55haitao.wwht.utils;

/**
 * Created by 董猛 on 2016/9/27.
 */

import android.widget.TextView;

/**
 * 订单相关,控制关税 和 官网运费等的显示
 * 待扩充
 */
public class TaxRuleHelper {

    /**
     * 官网运费显示规则
     * @param shopShipping
     * @param shopNoShipping
     * @return
     */
    public static String shopShipping(int price, int shopShipping, int shopNoShipping){

        if (shopNoShipping < 0){
            return "免运费";
        }

        if (shopNoShipping > 0){
            return price > shopNoShipping ? "免运费" : String.format("购物满\u00A5%s免官网运费",shopNoShipping) ;
        }else {
            return String.format("\u00A5%s", shopShipping);
        }

    }

    /**
     * 关税显示规格
     */
    public static String tairff(double tairff){
        if (tairff == 0){
            return "免关税" ;
        }else if (tairff < 0){
            return "商品价格包含关税";
        }else {
            return PriceUtils.toRMBFromYuan((float) tairff);
        }
    }

}
