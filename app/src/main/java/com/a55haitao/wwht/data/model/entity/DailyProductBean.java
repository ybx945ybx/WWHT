package com.a55haitao.wwht.data.model.entity;

/**
 * Created by a55 on 2017/9/8.
 */

public class DailyProductBean {
    public SellerInfo sellerInfo;
    public String name;          // 标题
    public String title;          // 标题
    public String img;          // 标题

    public String tag;          // 标题
    public String tag_color;    // 标题

    public String price_name;    // 标题
    public String price_color;    // 标题

    public float mallPrice;      // 商城价(单位:¥)
    public float realPrice;      // 现价
    public float  discount;
    public String spuid;          // 商品ID   (H5页面的Url后缀)

    public int stock;             //  inStock  stock   判断是否售光
    public int inStock;

public BrandInfo brand;

    public class SellerInfo {
        public String name;
        public String country;                       // 国家
        public String flag;                          // 国旗
    }

    public class BrandInfo {
        public String description;
        public String icon;
        public String name_cn;
        public String name_en;

    }
}
