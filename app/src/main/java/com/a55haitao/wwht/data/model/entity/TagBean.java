package com.a55haitao.wwht.data.model.entity;

/**
 * 标签
 *
 * @author 陶声
 * @since 2016-11-07
 */

public class TagBean {
    public int    status;
    public int    create_dt;
    public String name;
    public int    weight;
    public String image_url_small;
    public String content;
    public String image_url;
    public int    is_hot;
    public int    id;

    @Override
    public String toString() {
        return "TagBean{" +
                "status=" + status +
                ", create_dt=" + create_dt +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", image_url_small='" + image_url_small + '\'' +
                ", content='" + content + '\'' +
                ", image_url='" + image_url + '\'' +
                ", is_hot=" + is_hot +
                ", id=" + id +
                '}';
    }
}
