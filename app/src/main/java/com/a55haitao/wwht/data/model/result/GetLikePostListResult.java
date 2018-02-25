package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.PostBean;

import java.util.List;

/**
 * 个人中心 - 点赞tab 返回结果
 *
 * @author 陶声
 * @since 2016-11-15
 */

public class GetLikePostListResult {

    public int            count;
    public int            page;
    public int            allpage;
    public List<PostBean> post_list;
}
