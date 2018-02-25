package com.a55haitao.wwht.data.model.entity;

/**
 * 消息实体
 *
 * @author 陶声
 * @since 2017-05-02
 */

public class MessageBean {
    public int    create_dt;
    public int    operator_id;
    public int    user_id;
    public String title;
    public String content;
    public int    type;
    public String target_id;
    public int    isread;
    public String image_url;
    public int    device_type;
    public String head_img;
    public String nickname;
    public int    id;

    @Override
    public String toString() {
        return "MessageBean{" +
                "create_dt=" + create_dt +
                ", operator_id=" + operator_id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", target_id='" + target_id + '\'' +
                ", isread=" + isread +
                ", image_url='" + image_url + '\'' +
                ", device_type=" + device_type +
                ", head_img='" + head_img + '\'' +
                ", nickname='" + nickname + '\'' +
                ", id=" + id +
                '}';
    }
}