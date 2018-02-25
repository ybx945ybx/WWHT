package com.a55haitao.wwht.data.model.entity;

/**
 * 评论Bean
 *
 * @author 陶声
 * @since 2016-11-09
 */

public class CommentBean {
    public int          create_dt;
    public boolean      is_liked;
    public String       content;
    public UserListBean user_info;
    public int          like_count;
    public UserListBean at_user_info;
    public int          id;
}
