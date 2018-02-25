package com.a55haitao.wwht.data.model.entity;

import com.a55haitao.wwht.data.model.result.PostListResult;

import java.util.List;

/**
 * 社区 - 关注Fragment 网络请求返回实体组合
 *
 * @author 陶声
 * @since 2016-11-16
 */

public class PostFragmentData {
    public List<PostBannerBean> bannerList;
    public List<TagBean>        tagList;
    public PostListResult       postListResult;

    public PostFragmentData() {
    }

    public PostFragmentData(List<PostBannerBean> bannerList, List<TagBean> tagList, PostListResult postListResult) {
        this.bannerList = bannerList;
        this.tagList = tagList;
        this.postListResult = postListResult;
    }
}
