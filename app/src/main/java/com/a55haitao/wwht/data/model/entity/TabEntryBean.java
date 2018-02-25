package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by 董猛 on 2016/10/11.
 */

public class TabEntryBean {

    public int page;

    public double allpage;

    //活动小图
    public String small_icon;

    public ArrayList<EntriesBean> entries;

    public static class EntriesBean {
        public String name;
        public String image;
        public String uri;
        public int like_count;
        public boolean is_liked;

        //需要将EntriesBean中的small_icon 赋值给它
        public String small_icon;

        public ArrayList<ItemsBean> items;

        public static class ItemsBean {
            public String img_cover;
            public String title;
            public String spuid;
            public double mall_price;
            public int id;
            public double real_price;
            public String img_cover2;
            public String brand;
            public String desc;

            public boolean showSeeAll;     //展示查看更多
        }
    }
}
