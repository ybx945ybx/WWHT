package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by Haotao_Fujie on 16/10/10.
 */

public class SellerBean{
    public static class SellerDetailBean extends SellerDescBaseBean {

        public String shipping;
        public String noshipping;

        public ArrayList<ProductSellerExtModel> sellerExt;
        public ArrayList<AssuranceBean> stag;

        class ProductSellerExtModel {

            public String fulitxt;
            public String img;
            public String fuliname;

        }

    }

    public static class SiteDescBaseBean extends SellerDescBaseBean {
        public ShareModel share;
    }

    public static class SellerDescBaseBean extends SellerBaseBean {

        public String desc;

        public String nameen;

        public String namecn;

        public String country;

        public String flag;

        public boolean is_followed;

    }

    public static class SellerBaseBean {
        public int seller_id;
        public String name;

        // 列表 小图
        public String logo1;

        // 首页方块 中图
        public String logo2;

        // 主页 背景上的logo
        public String logo3;

        // 背景图
        public String img_cover;

        //原始logo，如果要用需要适配
        public String pdb_logo;

        // 搜索 条件
        public QueryBean query;

        // 品牌名称
        public String brand ;
    }
}
