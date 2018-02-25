package com.a55haitao.wwht.data.model.entity;

/**
 * Created by a55 on 2017/4/24.
 */

public class SearchSpecialBean {
    // 背景图
    public String img_cover;
    // 搜索 条件
    public SearchSpecialQueryBean query;

    public String name;

    public ShareModel share;

    public String desc;

    public static class SearchSpecialQueryBean {
        public String[] brands;
        public String[] categories;
        public String[] sellers;

    }
}
