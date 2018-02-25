package com.a55haitao.wwht.data.net.api;


import com.a55haitao.wwht.data.model.entity.EntriesSpecialResult;
import com.a55haitao.wwht.data.model.entity.FavorableSpecialBean;
import com.a55haitao.wwht.data.model.entity.ProductSpecialLikeBean;
import com.a55haitao.wwht.data.model.entity.SearchSpecialBean;
import com.a55haitao.wwht.data.model.result.AddPostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.GetPostSpecialCommentsResult;
import com.a55haitao.wwht.data.model.result.GetPostSpecialInfoResult;
import com.a55haitao.wwht.data.model.result.GetPostSpecialLikesResult;
import com.a55haitao.wwht.data.model.result.LikePostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.LikePostSpecialResult;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 专题相关Api
 *
 * @author 陶声
 * @since 2016-10-12
 */

public interface SpecialService {
    /**
     * 社区 - 专题详情
     */
    @POST("m.api")
    Observable<ApiModel<GetPostSpecialInfoResult>> getPostSpecialInfo(@Body Map<String, Object> body);

    /**
     * 官网特卖
     * 商品专题 - 单列
     * 专题详情
     */
    @POST("m.api")
    Observable<ApiModel<EntriesSpecialResult>> getProductSpecialInfo(@Body Map<String, Object> body);

    /**
     * 官网特卖专题详情
     */
    @POST("m.api")
    Observable<ApiModel<FavorableSpecialBean>> getFavorableSpecialInfo(@Body Map<String, Object> body);

    /**
     * 搜索专题 -  专题详情
     */
    @POST("m.api")
    Observable<ApiModel<SearchSpecialBean>> getSearchSpecialInfo(@Body Map<String, Object> body);

    /**
     * 专题 - 点赞
     */
    @POST("m.api")
    Observable<ApiModel<LikePostSpecialResult>> likePostSpecial(@Body Map<String, Object> body);

    /**
     * 专题 - 点赞
     */
    @POST("m.api")
    Observable<ApiModel<ProductSpecialLikeBean>> likeProductSpecial(@Body Map<String, Object> body);

    /**
     * 专题 - 发表评论
     */
    @POST("m.api")
    Observable<ApiModel<AddPostSpecialCommentResult>> addPostSpecialComment(@Body Map<String, Object> body);

    /**
     * 专题 - 全部点赞
     */
    @POST("m.api")
    Observable<ApiModel<GetPostSpecialLikesResult>> getPostSpecialLikes(@Body Map<String, Object> body);

    /**
     * 专题 - 点赞评论
     */
    @POST("m.api")
    Observable<ApiModel<LikePostSpecialCommentResult>> likePostSpecialComment(@Body Map<String, Object> body);

    /**
     * 专题 - 点赞评论
     */
    @POST("m.api")
    Observable<ApiModel<DeletePostSpecialCommentResult>> deletePostSpecialComment(@Body Map<String, Object> body);

    /**
     * 专题 - 点赞评论
     */
    @POST("m.api")
    Observable<ApiModel<GetPostSpecialCommentsResult>> getPostSpecialComments(@Body Map<String, Object> body);
}
