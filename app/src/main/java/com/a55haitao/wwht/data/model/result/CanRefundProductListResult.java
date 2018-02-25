package com.a55haitao.wwht.data.model.result;

import android.text.TextUtils;

import com.a55haitao.wwht.data.model.entity.OrderPrepareBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.utils.SpecTranslateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a55 on 2017/6/8.
 */

public class CanRefundProductListResult {

    /**
     * order_id : 17051211054846349673
     * storelist : [{"store_id":84,"store_name_en":"Nordstrom","store_name_cn":"Nordstrom","store_logo":"http://st-prod.b0.upaiyun.com/zeus/2016/07/21/dbfa6c7bc485c936604cda268b271a89!/format/jpg","productlist":[{"count":1,"total":680,"product_name":"Stecy Wow Clutch","selected_sku":{"styleId":"Pale Purple","spuid":"1918a200f62625befb5784c5f9715ac2","stock":1,"realPriceOrg":94,"mallPrice":1296,"skuid":"bd6e958030d7cce9252f0d2f67d7de4f","inStock":2,"realPrice":680,"skuValues":[{"name":"Color","value":"Pale Purple"}],"mallPriceOrg":179}}]}]
     */

    public String                   order_id;
    public ArrayList<StorelistBean> storelist;

    public void selectAll(boolean selected) {
        for (StorelistBean storelistBean : storelist) {
            storelistBean.is_selected = selected;
            for (StorelistBean.ProductlistBean productlistBean : storelistBean.productlist) {
                productlistBean.isSelected = selected;
            }
        }
    }

    public void selectAll(String store_id, boolean selected) {
        for (StorelistBean storelistBean : storelist) {
            if (store_id.equals(String.valueOf(storelistBean.store_id))) {
                storelistBean.is_selected = selected;
                for (StorelistBean.ProductlistBean productlistBean : storelistBean.productlist) {
                    productlistBean.isSelected = selected;
                }
                break;
            }
        }
    }

    public int getSelectedProductedCount() {
        int ret = 0;
        for (StorelistBean storelistBean : storelist) {
            for (StorelistBean.ProductlistBean productlistBean : storelistBean.productlist) {
                if (productlistBean.isSelected) {
                    ret += productlistBean.count;
                }
            }
        }
        return ret;
    }

    public void selectCartId(String store_id, String skuid, boolean selected) {
        for (StorelistBean storelistBean : storelist) {
            int storeId_int = Integer.parseInt(store_id);
            if (storeId_int == storelistBean.store_id) {
                for (StorelistBean.ProductlistBean productlistBean : storelistBean.productlist){
                    if (skuid.equals(productlistBean.selected_sku.skuid)){
                        productlistBean.isSelected = selected;
                        if (selected == true) {
                            // 判断是否该商户下的商品全部已经选择？
                            storelistBean.is_selected = storelistBean.getSellerProductsSelected();
                        } else {
                            storelistBean.is_selected = false;
                        }
                        break;
                    }
                }

            }
        }
    }

    public boolean getAllSelected() {
        for (StorelistBean storelistBean : storelist) {
            if (storelistBean.getSellerProductsSelected() == false) {
                return false;
            }
        }
        return true;
    }

    public static class StorelistBean {
        /**
         * store_id : 84
         * store_name_en : Nordstrom
         * store_name_cn : Nordstrom
         * store_logo : http://st-prod.b0.upaiyun.com/zeus/2016/07/21/dbfa6c7bc485c936604cda268b271a89!/format/jpg
         * productlist : [{"count":1,"total":680,"product_name":"Stecy Wow Clutch","selected_sku":{"styleId":"Pale Purple","spuid":"1918a200f62625befb5784c5f9715ac2","stock":1,"realPriceOrg":94,"mallPrice":1296,"skuid":"bd6e958030d7cce9252f0d2f67d7de4f","inStock":2,"realPrice":680,"skuValues":[{"name":"Color","value":"Pale Purple"}],"mallPriceOrg":179}}]
         */

        public int                          store_id;
        public String                       store_name_en;
        public String                       store_name_cn;
        public String                       store_logo;
        public ShoppingCartBean.CountryData country;
        public ArrayList<ProductlistBean>   productlist;
        public boolean                      is_selected;


        // 获取商家下的所有商品是否全部选择
        public boolean getSellerProductsSelected() {
            for (ProductlistBean productlistBean : productlist) {
                if (productlistBean.isSelected == false) {
                    return false;
                }
            }
            return true;
        }

        // 获取商家名称
        public String getStoreName() {
            if (!TextUtils.isEmpty(store_name_en)) {
                if (store_name_en.contains("官网发货")) {
                    return store_name_en;
                } else {
                    return store_name_en + "官网发货";
                }

            } else if (!TextUtils.isEmpty(store_name_cn)) {
                if (store_name_cn.contains("官网发货")) {
                    return store_name_cn;
                } else {
                    return store_name_cn + "官网发货";
                }
            }
            return "";
        }

        public static class ProductlistBean {
            /**
             * count : 1
             * total : 680
             * product_name : Stecy Wow Clutch
             * selected_sku : {"styleId":"Pale Purple","spuid":"1918a200f62625befb5784c5f9715ac2","stock":1,"realPriceOrg":94,"mallPrice":1296,"skuid":"bd6e958030d7cce9252f0d2f67d7de4f","inStock":2,"realPrice":680,"skuValues":[{"name":"Color","value":"Pale Purple"}],"mallPriceOrg":179}
             */

            public int             count;
            public int             total;
            public String          product_name;
            public SelectedSkuBean selected_sku;
            public boolean isSelected;
            public String coverImg_url;

            public static class SelectedSkuBean {
                /**
                 * styleId : Pale Purple
                 * spuid : 1918a200f62625befb5784c5f9715ac2
                 * stock : 1
                 * realPriceOrg : 94
                 * mallPrice : 1296
                 * skuid : bd6e958030d7cce9252f0d2f67d7de4f
                 * inStock : 2
                 * realPrice : 680
                 * skuValues : [{"name":"Color","value":"Pale Purple"}]
                 * mallPriceOrg : 179
                 */

                public String              styleId;
                public String              spuid;
                public int                 stock;
                public int                 realPriceOrg;
                public int                 mallPrice;
                public String              skuid;
                public int                 inStock;
                public int                 realPrice;
                public int                 mallPriceOrg;
                public List<SkuValuesBean> skuValues;

                public String selectedSpecsDescription() {
                    if (TextUtils.isEmpty(skuid)) {
                        return "";
                    }

                    if (skuValues == null || skuValues.size() == 0)
                        return "";
                    String str = "";
                    for (SkuValuesBean skuValuesBean : skuValues) {
                        if (skuValuesBean.equals(skuValues.get(skuValues.size() - 1))) {
                            str += SpecTranslateUtils.translateToChinese(skuValuesBean.name) + ": " + skuValuesBean.value;
                        } else {
                            str += SpecTranslateUtils.translateToChinese(skuValuesBean.name) + ": " + skuValuesBean.value + "\n";
                        }

                    }
                    return str;
                }

                public static class SkuValuesBean {
                    /**
                     * name : Color
                     * value : Pale Purple
                     */

                    public String name;
                    public String value;

                }
            }
        }
    }
}
