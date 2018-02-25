package com.a55haitao.wwht.data.model.entity;

/**
 * 草单评论实体
 *
 * @author 陶声
 * @since 2017-01-12
 */

public class EasyoptCommentBean {
    public int                user_id;
    public int                easyopt_id;
    public int                comment_id;
    public String             content;
    public int                created_time;
    public int                like_count;
    public UserBean           owner;
    public int                parent_comment_id;
    public int                islike;
    public EasyoptCommentBean parent;
}
