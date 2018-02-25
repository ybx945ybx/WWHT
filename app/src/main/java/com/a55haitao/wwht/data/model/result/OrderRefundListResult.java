package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.ProductBaseBean;

import java.util.ArrayList;

/**
 * 订单退款列表
 * Created by a55 on 2017/6/8.
 */

public class OrderRefundListResult {

    /**
     * order_id : 17051511012057127783
     * order_refund_list : [{"refund_no":695,"refund_status":0,"refund_status_code":"REFUND_ACCEPTED","refund_status_name":"受理中","refound_amount":0,"goods_size":1,"order_refund_items":[{"product_name":"Ceramic Ladies Watch","cover_img_url":"http://cdn2.jomashop.com/media/catalog/product/a/k/akribos-xxiv-ceramic-ladies-watch-ak514bk.jpg","selected_sku":{"styleId":"","spuid":"dee5b05966775711bf589ca543a15c6f","stock":1,"realPriceOrg":53,"mallPrice":2498,"skuid":"dee5b05966775711bf589ca543a15c6f","inStock":1,"realPrice":385,"skuValues":[],"mallPriceOrg":345}}]}]
     */

    public String                         order_id;
    public ArrayList<OrderRefundListBean> order_refund_list;

    public static class OrderRefundListBean {
        /**
         * refund_no : 695
         * refund_status : 0
         * refund_status_code : REFUND_ACCEPTED
         * refund_status_name : 受理中
         * refound_amount : 0
         * goods_size : 1
         * order_refund_items : [{"product_name":"Ceramic Ladies Watch","cover_img_url":"http://cdn2.jomashop.com/media/catalog/product/a/k/akribos-xxiv-ceramic-ladies-watch-ak514bk.jpg","selected_sku":{"styleId":"","spuid":"dee5b05966775711bf589ca543a15c6f","stock":1,"realPriceOrg":53,"mallPrice":2498,"skuid":"dee5b05966775711bf589ca543a15c6f","inStock":1,"realPrice":385,"skuValues":[],"mallPriceOrg":345}}]
         */

        public int                            refund_no;
        public int                            refund_status;
        public String                         refund_status_code;
        public String                         refund_status_name;
        public String                         refound_amount;
        public int                            goods_size;
        public ArrayList<OrderRefundItemBean> order_refund_items;

    }

    public class OrderRefundItemBean {
        public String cover_img_url;
    }
}
