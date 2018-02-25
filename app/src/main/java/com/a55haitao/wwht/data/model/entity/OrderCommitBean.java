package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by 55haitao on 2016/11/7.
 */

public class OrderCommitBean {

    /**
     * trade_no : 147848857335349001
     * need_pay_online : 1
     * order_id : 16110711161321725221
     * order_live_time : 1800
     * paydata : {"package": "Sign=WXPay", "timestamp": 1478488573, "sign": "BC6C93893ADB50AA030176219D972E85", "partnerid": "1285483601", "appid": "wxa2694e98318804c8", "prepayid": "wx20161107111613673c3968940406614475", "noncestr": "OMgyYfcmBcYjNMLWHBnl"}
     * amount : 213
     * order_ctime : 1478488573
     */

    public String trade_no;                 //流水号
    public String need_pay_online;          //是否需要支付
    public String order_id;                 //订单id
    public String paydata;                  //支付需要使用的签名信息
    public double amount;                   //金额
    public long   order_ctime;

    public ArrayList<FailedExtData> ext;    // 核价失败后的新商品信息

    public class FailedExtData {
        public String skuid;
        public String cart_id;

        public String price_status;    // "change" "invalid"
        public String oldprice;
        public String nowprice;
        public String title;
        public String cover;
        public boolean is_select;

        public void setIs_select(boolean is_select) {
            this.is_select = is_select;
        }

        // 获取商家下的商品是否全部选择
        public boolean getProductSelected() {
            if (is_select == true) {
                // 勾选
                return true;
            } else if (!"invalid".equals(price_status)) {
                // 商品无效
                return true;
            } else {
                // 其它
                return false;
            }
        }
    }
}
