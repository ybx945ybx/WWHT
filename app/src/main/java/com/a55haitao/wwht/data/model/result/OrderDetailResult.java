package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.SelectedSkuBean;

import java.util.List;

/**
 * Created by 55haitao on 2016/11/6.
 */

public class OrderDetailResult {
    public OrderDetailBean order_detail;

    public static class OrderDetailBean {
        public String                     shipping_amount;
        public String                     total;
        public String                     consumption_tax_amount;
        public int                        bigOrderPrice;
        public float                      bigOrderfullminusAmount;
        public String                     pre_tariff_amount;
        public String                     order_id;
        public int                        couponq_promotion;
        public String                     shipping_store_amount;
        public OrderAddressBean           order_address;
        public int                        ctime;
        public int                        overtime;
        public String                     now;
        public int                        commission_promotion;
        public List<StorelistBean>        storelist;
        public List<StorePackageListBean> store_package_list;
        public String                     ext;                  // 订单备注
        public String                     order_status;         // 订单状态
        public String                     order_status_name;    // 订单状态名
        public int                        paied_time;           // 支付时间
        public String                     paied_source;         // 支付方式
        public int                        confirm_time;         // 确认收货时间
        public TrackUpdateInfo            track_update_info;    // 物流信息
        public String                     order_detail_note;    // 温馨提示
        public TariffNotice               tariffNotice;         // 关税提示
        public String                     cancel_info;          // 取消订单理由
        public boolean                    order_refund_status;  // 查看退款进度
        public boolean                    order_refund_bnt;     // true 有可退款商品，false 商品已全部退款
        public String                     achat_note;
        public int                        is_achat;

        public static class TariffNotice {
            public String desc;
        }

        public static class TrackUpdateInfo {
            public String track_update_content;
            public String track_update_date;
        }

        public static class OrderAddressBean {
            public String  city;
            public String  dist;
            public String  idt_number;
            public boolean hasidt;
            public String  prov;
            public String  consignee;
            public String  phone;
            public String  street;
            public String  detail_address;
        }

        public static class StorelistBean {
            public CountryBean           country;
            public int                   bigStoreShippingFee;
            public float                 bigStorefullminusAmount;
            public int                   store_id;
            public double                bigStoreWeight;
            public int                   bigStoreTransFee;
            public int                   bigStoreAccountsTotal;
            public boolean               priceHasTariff;
            public String                store_name_en;
            public String                store_name_cn;
            public int                   bigStoreCnt;
            public int                   bigStorePrice;
            public int                   bigStoreTariff;
            public int                   bigOrginStoreTariff;
            public String                store_logo;
            public int                   bigStoreConsumptionaxmount;
            public List<ProductListBean> productlist;

            public static class CountryBean {
                public String regionImgUrl;
                public int    regionCode;
                public String flag;
                public int    regionId;
                public String regionName;
            }

            public static class ProductListBean {
                public int             count;
                public int             bigSkuConsumptionTax;
                public String          coverImgUrl;
                public int             total;
                public double          weight;
                public int             bigOrginSkuTariff;
                public double          tax;
                public double          taxOrgin;
                public String          track_number;
                public int             bigSkuPrice;
                public SelectedSkuBean selected_sku;
                public String          product_status;      // 商品状态
                public String          product_status_code; // 商品状态码
                public String          istariff;
                public double          shipping_fee;
                public int             tariff;
                public String          product_name;
            }
        }

        public static class StorePackageListBean {
            public List<PackageListBean> packageList;

            public static class PackageListBean {
                public String            packageName;
                public String            package_status;
                public boolean           hasPackageInfo;
                public List<ProductBean> product;

                public static class ProductBean {
                    public int             count;
                    public String          coverImgUrl;
                    public double          weight;
                    public int             bigSkuPrice;
                    public SelectedSkuBean selected_sku;
                    public String          product_name;

                }
            }
        }
    }

}
