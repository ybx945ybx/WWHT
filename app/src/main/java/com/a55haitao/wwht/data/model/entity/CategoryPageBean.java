package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by 董猛 on 2016/10/20.
 */

public class CategoryPageBean {

    public ArrayList<HotSiteBean> hot_brands;

    public ArrayList<CategoryBean> hot_categories;

    public ArrayList<HotSiteBean> hot_sellers;

    public static class HotSiteBean {
        public String logo2;
        public String logo3;
        public String logo1;
        public String name;
        public String img_cover;
        public String desc;
        public QueryBean query ;
    }
}
