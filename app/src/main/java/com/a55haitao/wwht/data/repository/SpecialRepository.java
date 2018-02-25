package com.a55haitao.wwht.data.repository;

import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
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
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.SpecialService;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.TreeMap;

import rx.Observable;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-11-10
 */

public class SpecialRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "55haitao_sns.SpecialAPI/";
    private static final int    REQUEST_COUNT   = 20;

    private static volatile SpecialRepository instance;
    private final           SpecialService    mSpecialService;

    private SpecialRepository() {
        mSpecialService = RetrofitFactory.createService(SpecialService.class);
    }

    public static SpecialRepository getInstance() {
        if (instance == null) {
            synchronized (SpecialRepository.class) {
                if (instance == null) {
                    instance = new SpecialRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 社区 - 专题详情
     *
     * @param specialId 专题Id
     */
    public Observable<GetPostSpecialInfoResult> getPostSpecialInfo(int specialId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_special_info");
        paramMap.put("special_id", specialId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSpecialService.getPostSpecialInfo(paramMap));
    }

    /**
     * 官网特卖 (商品专题 - 单列)
     * 专题详情
     *
     * @param specialId 专题Id
     */
    public Observable<EntriesSpecialResult> getProductSpecialInfo(String specialId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_product_special_info");
        paramMap.put("special_id", specialId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSpecialService.getProductSpecialInfo(paramMap));
    }

    /**
     * 官网特卖专题详情
     *
     * @param specialId 专题Id
     */
    public Observable<FavorableSpecialBean> getFavorableSpecialInfo(String specialId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_favorable_special_info");
        paramMap.put("special_id", specialId);
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSpecialService.getFavorableSpecialInfo(paramMap));
    }

    /**
     * 搜索专题 -  专题详情
     *
     * @param specialId 专题Id
     */
    public Observable<SearchSpecialBean> getSearchSpecialInfo(int specialId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_search_special_info");
        paramMap.put("special_id", specialId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSpecialService.getSearchSpecialInfo(paramMap));
    }

    /**
     * 社区 - 点赞专题
     *
     * @param specialId 专题Id
     */
    public Observable<LikePostSpecialResult> likePostSpecial(int specialId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "like_post_special");
        paramMap.put("special_id", specialId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSpecialService.likePostSpecial(paramMap));
    }

    /**
     * 商品专题 - 点赞
     *
     * @param specialId 专题Id
     */
    public Observable<ProductSpecialLikeBean> likeProductSpecial(String specialId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "like_product_special");
        paramMap.put("special_id", specialId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSpecialService.likeProductSpecial(paramMap));
    }

    /**
     * 专题详情 - 发表评论
     *
     * @param specialId 专题Id
     * @param content   评论内容
     * @param commentId 评论Id
     */
    public Observable<AddPostSpecialCommentResult> addPostSpecialComment(int specialId, String content, int commentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "add_post_special_comment");
        paramMap.put("special_id", specialId);
        paramMap.put("content", content);
        paramMap.put("comment_id", commentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSpecialService.addPostSpecialComment(paramMap));
    }

    /**
     * 专题 - 全部点赞
     *
     * @param specialId 专题Id
     * @param page      页数
     */
    public Observable<GetPostSpecialLikesResult> getPostSpecialLikes(int specialId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_special_likes");
        paramMap.put("special_id", specialId);
        paramMap.put("conunt", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSpecialService.getPostSpecialLikes(paramMap));
    }

    /**
     * 专题 - 点赞评论
     *
     * @param commentId 评论Id
     */
    public Observable<LikePostSpecialCommentResult> likePostSpecialComment(int commentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "like_post_special_comment");
        paramMap.put("comment_id", commentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSpecialService.likePostSpecialComment(paramMap));
    }

    /**
     * 专题 - 点赞评论
     *
     * @param commentId 评论Id
     */
    public Observable<DeletePostSpecialCommentResult> deletePostSpecialComment(int commentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "delete_post_special_comment");
        paramMap.put("comment_id", commentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSpecialService.deletePostSpecialComment(paramMap));
    }

    /**
     * 专题 - 全部评论
     *
     * @param specialId 专题Id
     * @param page      页数
     */
    public Observable<GetPostSpecialCommentsResult> getPostSpecialComments(int specialId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_special_comments");
        paramMap.put("special_id", specialId);
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSpecialService.getPostSpecialComments(paramMap));
    }

    public Observable<GetPostSpecialLikesResult> getProductSpecialLikes(int specialId, int page) {
        TreeMap<String, Object> param = generateMTParam(ApiUrl.MT_PRODUCT_SP_ALL_LIKE);
        param.put("special_id", String.valueOf(specialId));
        param.put("page", page);
        param.put("count", 20);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, param);
        return transform(mSpecialService.getPostSpecialLikes(param));
    }
}
