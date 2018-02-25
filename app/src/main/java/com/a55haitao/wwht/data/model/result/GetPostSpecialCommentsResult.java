package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.CommentBean;

import java.util.List;

/**
 * 专题 - 全部评论 - 返回结构
 *
 * @author 陶声
 * @since 2016-11-18
 */

public class GetPostSpecialCommentsResult {

    public int               count;
    public int               page;
    public int               allpage;
    public List<CommentBean> comments;
}
