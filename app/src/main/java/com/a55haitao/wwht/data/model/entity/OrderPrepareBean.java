package com.a55haitao.wwht.data.model.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 55haitao on 2016/11/6.
 */

public class OrderPrepareBean {
    public double bigOrderAccountsTotal;
    public double balance;
    public double promotion_amount;
    public String achat_note;

    public ArrayList<StorelistBean> storelist;

    public ArrayList<CouponBean> coupon_list;

    public ArrayList<String> cart_list;

    public static class StorelistBean {
        public String store_name;

        public CountryBean country;

        public ProductDetailBean.ProductDetailFeesNotice tariffNotice;

        public double bigStoreTransFee;

        public double bigStorefullMinusAmount;   // 活动优惠金额

        //海外消费税
        public double bigStoreConsumptionaxmount;
        //关税
        public double bigTariff;
        public double bigStoreAccountsTotal;
        public double bigStoreShippingFee;
        public double bigStorePrice;

        public List<ProductListBean> productList;

        public static class CountryBean {
            public String regionImgUrl;
            public String flag;
            public String regionName;
        }

        public static class ProductListBean {
            public int             count;
            public String          skuid;
            public String          spuid;
            public double          bigSkuPrice;
            public ProductbeanBean productbean;

            public static class ProductbeanBean {
                public String coverImgUrl;
                public String skuid;

                public SelectedSkuBean selectedSkuBean;

                public BrandBean    brand;
                public int          count;
                public String       name;
                public String       spuid;
                public double       realPriceOrg;
                public List<String> tags;

                public String cate1;
                public String cate2;
                public String cate3;

                public String getCategoryInfo() {
                    String result = "";

                    if (!TextUtils.isEmpty(cate1)) {
                        result += cate1;
                    }
                    if (!TextUtils.isEmpty(cate2)) {
                        result += cate2;
                    }
                    if (!TextUtils.isEmpty(cate3)) {
                        result += cate3;
                    }
                    return result;
                }

                public static class BrandBean {
                    public String logo;
                    public int    id;
                    public int    region_id;
                }
            }
        }
    }

}
