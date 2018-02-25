package com.a55haitao.wwht.data.model.result;

import java.util.List;

/**
 * 个人中心 - 心愿单 - 返回数据
 *
 * @author 陶声
 * @since 2016-11-15
 */

public class GetUserStarProductsResult {
    public int               count;
    public int               page;
    public int               allpage;
    public List<ProductBean> datas;

    public static class ProductBean {
        public String         coverImgUrl;
        public String         name;
        public float          mallPrice;
        public String         brand;
        public boolean        is_star;
        public int            star_count;
        public SellerInfoBean sellerInfo;
        public int            priority;
        public String         spuid;
        public int            inStock;
        public float          realPrice;
        public int            stock;
        public String         image;
        public String         desc;
        public float          discount;
        public boolean        checked;

        public static class SellerInfoBean {
            public String logo2;
            public String logo3;
            public String logo1;
            public String excise;
            public String logo4;
            public String namecn;
            public String country;
            public String descriptionen;
            public String flag;
            public String bunnleImg;
            public String shipping;
            public String noshipping;
            public String descriptioncn;
            public String logo;
            public String nameen;
            public String isAdvancetariff;
            public int    id;
            public String transport;
            public String officialurl;
        }
    }
}
