package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by 董猛 on 2016/10/26.
 */

public class EntriesSpecialResult {

    public ArrayList<Item>     items;
    public ArrayList<LikeBean> likes;
    public ShareModel          share;
    public String              desc;
    public String              name;
    public String              img_cover;
    public int                 special_id;
    public int                 like_count;
    public int                 created_time;
    public boolean             is_liked;

    public class Item {

        public String img_cover;
        public String img_cover2;
        public String title;
        public String spuid;
        public int    mall_price;
        public int    real_price;
        public int    id;
        public String desc;
    }

}
