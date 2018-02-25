package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.UserListBean;

import java.util.List;

/**
 * 专题 - 全部点赞页面
 *
 * @author 陶声
 * @since 2016-11-18
 */

public class GetPostSpecialLikesResult {

    public int                count;
    public int                page;
    public int                allpage;
    public List<UserListBean> users;

}
