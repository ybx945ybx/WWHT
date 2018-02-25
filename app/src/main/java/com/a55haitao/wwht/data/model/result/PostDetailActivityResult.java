package com.a55haitao.wwht.data.model.result;

/**
 * 帖子详情页 网络请求返回实体组合
 *
 * @author 陶声
 * @since 2016-11-16
 */

public class PostDetailActivityResult {
    public PostDetailResult     postDetailResult;
    public RandomPostListResult randomPostListResult;

    public PostDetailActivityResult() {
    }

    public PostDetailActivityResult(PostDetailResult postDetailResult, RandomPostListResult randomPostListResult) {
        this.postDetailResult = postDetailResult;
        this.randomPostListResult = randomPostListResult;
    }
}
