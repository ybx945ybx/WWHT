package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.PostBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取用户帖子列表
 *
 * @author 陶声
 * @since 2016-11-07
 */

public class GetUserPostListResult {
    public int                 count;
    public int                 page;
    public int                 allpage;
    public ArrayList<PostBean> post_list;
}
