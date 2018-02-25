package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by 董猛 on 2016/10/11.
 */

public class TabFavorableBean {

    /**
     * priority : 0
     * special_id : 103
     * image : http://st-prod.b0.upaiyun.com/zeus/2016/07/11/96f3be5b845eeea1ae3cc1382bde5636!/format/jpg
     * start_time : 0
     * end_time : 0
     */
    public int                     page;
    public double                  allpage;
    public String                  small_icon;
    //    public ArrayList<SpecialsBean> specials;
    public ArrayList<SpecialsBean> favorables;

    public static class SpecialsBean {
        public int                                                  special_id;
        public String                                               image;
        public String                                               small_icon;
        public String                                               uri;
        public int                                                  type;                                //热门  单双列使用
        public ArrayList<HotFavorableBean.SpecialsBean.SpecialItem> special_items;

        public static class SpecialItem {
            public int    special_id;
            public String image;
            public String small_icon;
            public String uri;
        }
    }
}
