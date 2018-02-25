package com.a55haitao.wwht.data.model.entity;

/**
 * 分享
 *
 * @author 陶声
 * @since 2016-11-07
 */

public class ShareBean {
    public String url;
    public String desc;
    public String icon;
    public String title;

    @Override
    public String toString() {
        return "ShareBean{" +
                "small_icon='" + url + '\'' +
                ", desc='" + desc + '\'' +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
