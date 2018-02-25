package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.ProductBaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a55 on 2017/6/8.
 */

public class RefundProductsListResult {

    /**
     * order_id : 17051211054846349673
     * storelist : [{"store_id":84,"store_name_en":"Nordstrom","store_name_cn":"Nordstrom","store_logo":"http://st-prod.b0.upaiyun.com/zeus/2016/07/21/dbfa6c7bc485c936604cda268b271a89!/format/jpg","productlist":[{"count":1,"total":680,"product_name":"Stecy Wow Clutch","selected_sku":{"styleId":"Pale Purple","spuid":"1918a200f62625befb5784c5f9715ac2","stock":1,"realPriceOrg":94,"mallPrice":1296,"skuid":"bd6e958030d7cce9252f0d2f67d7de4f","inStock":2,"realPrice":680,"skuValues":[{"name":"Color","value":"Pale Purple"}],"mallPriceOrg":179}}]}]
     */

    private String                   order_id;
    private ArrayList<StorelistBean> storelist;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<StorelistBean> getStorelist() {
        return storelist;
    }

    public void setStorelist(ArrayList<StorelistBean> storelist) {
        this.storelist = storelist;
    }

    public static class StorelistBean {
        /**
         * store_id : 84
         * store_name_en : Nordstrom
         * store_name_cn : Nordstrom
         * store_logo : http://st-prod.b0.upaiyun.com/zeus/2016/07/21/dbfa6c7bc485c936604cda268b271a89!/format/jpg
         * productlist : [{"count":1,"total":680,"product_name":"Stecy Wow Clutch","selected_sku":{"styleId":"Pale Purple","spuid":"1918a200f62625befb5784c5f9715ac2","stock":1,"realPriceOrg":94,"mallPrice":1296,"skuid":"bd6e958030d7cce9252f0d2f67d7de4f","inStock":2,"realPrice":680,"skuValues":[{"name":"Color","value":"Pale Purple"}],"mallPriceOrg":179}}]
         */

        private int                   store_id;
        private String                store_name_en;
        private String                store_name_cn;
        private String                store_logo;
        private ArrayList<ProductBaseBean> productlist;

        public int getStore_id() {
            return store_id;
        }

        public void setStore_id(int store_id) {
            this.store_id = store_id;
        }

        public String getStore_name_en() {
            return store_name_en;
        }

        public void setStore_name_en(String store_name_en) {
            this.store_name_en = store_name_en;
        }

        public String getStore_name_cn() {
            return store_name_cn;
        }

        public void setStore_name_cn(String store_name_cn) {
            this.store_name_cn = store_name_cn;
        }

        public String getStore_logo() {
            return store_logo;
        }

        public void setStore_logo(String store_logo) {
            this.store_logo = store_logo;
        }

        public ArrayList<ProductBaseBean> getProductlist() {
            return productlist;
        }

        public void setProductlist(ArrayList<ProductBaseBean> productlist) {
            this.productlist = productlist;
        }

    }
}
