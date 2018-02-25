package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/3/22.
 */

public class HotFavorableBean {
    public int                                      page;
    public double                                   allpage;
    public String                                   small_icon;
//    public ArrayList<SpecialsBean>                  specials;
    public ArrayList<SpecialsBean> favorables;

    public static class SpecialsBean {
        public int                    type;                                //热门  单双列使用
        public ArrayList<SpecialItem> special_items;

        public static class SpecialItem {
            public int    special_id;
            public String image;
            public String small_icon;
            public String uri;
        }
    }
}
