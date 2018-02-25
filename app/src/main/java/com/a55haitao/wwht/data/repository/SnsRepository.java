package com.a55haitao.wwht.data.repository;


import com.a55haitao.wwht.data.model.annotation.EasyoptPublicVisible;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.annotation.NotificationListRequestType;
import com.a55haitao.wwht.data.model.entity.CommissionBean;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.ConvertCouponBean;
import com.a55haitao.wwht.data.model.entity.EnforceUpdateBean;
import com.a55haitao.wwht.data.model.entity.PostBannerBean;
import com.a55haitao.wwht.data.model.entity.PriorityModel;
import com.a55haitao.wwht.data.model.entity.ShareBean;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.data.model.result.AddEasyoptCommentResult;
import com.a55haitao.wwht.data.model.result.AddPostCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostResult;
import com.a55haitao.wwht.data.model.result.EasyOptListResult;
import com.a55haitao.wwht.data.model.result.EasyOptTagResult;
import com.a55haitao.wwht.data.model.result.EasyoptCommentLikeResult;
import com.a55haitao.wwht.data.model.result.EasyoptCommentListResult;
import com.a55haitao.wwht.data.model.result.EasyoptLikeResult;
import com.a55haitao.wwht.data.model.result.EasyoptList4AddResult;
import com.a55haitao.wwht.data.model.result.EasyoptStarResult;
import com.a55haitao.wwht.data.model.result.EditPostResult;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.GetFollowingActionListResult;
import com.a55haitao.wwht.data.model.result.GetHotUserListResult;
import com.a55haitao.wwht.data.model.result.GetMembershipPointHistoryResult;
import com.a55haitao.wwht.data.model.result.GetPostLikeUserListResult;
import com.a55haitao.wwht.data.model.result.GetPostTagResult;
import com.a55haitao.wwht.data.model.result.GetTagHotPostHotListResult;
import com.a55haitao.wwht.data.model.result.GetUserListByMobilesResult;
import com.a55haitao.wwht.data.model.result.GetUserPostListResult;
import com.a55haitao.wwht.data.model.result.GetUserStarInfoCountsV12Result;
import com.a55haitao.wwht.data.model.result.GetUserStarSpecialsV11Result;
import com.a55haitao.wwht.data.model.result.HotTagListResult;
import com.a55haitao.wwht.data.model.result.LikePostResult;
import com.a55haitao.wwht.data.model.result.MessageListResult;
import com.a55haitao.wwht.data.model.result.NotificationListResult;
import com.a55haitao.wwht.data.model.result.PostDetailResult;
import com.a55haitao.wwht.data.model.result.PostListResult;
import com.a55haitao.wwht.data.model.result.PublishPostResult;
import com.a55haitao.wwht.data.model.result.RandomPostListResult;
import com.a55haitao.wwht.data.model.result.SearchUserResult;
import com.a55haitao.wwht.data.model.result.UserListResultData;
import com.a55haitao.wwht.data.model.result.getCommentListResult;
import com.a55haitao.wwht.data.model.result.likePostCommentResult;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.SnsService;
import com.a55haitao.wwht.utils.HaiParamPrepare;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import rx.Observable;

/**
 * Sns数据仓库
 *
 * @author 陶声
 * @since 2016-10-10
 */

public class SnsRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "55haitao_sns.SnsAPI/";
    private static final int    REQUEST_COUNT   = 20;

    private static volatile SnsRepository instance;
    private                 SnsService    mSnsService;

    private SnsRepository() {
        mSnsService = RetrofitFactory.createService(SnsService.class);
    }

    public static SnsRepository getInstance() {
        if (instance == null) {
            synchronized (SnsRepository.class) {
                if (instance == null) {
                    instance = new SnsRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 社区 - 精选 - banner
     */
    public Observable<List<PostBannerBean>> postBanner() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "post_banner");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.postBanner(paramMap));
    }

    /**
     * 社区 - 精选 - banner
     *
     * @param page 页数
     */
    public Observable<GetFollowingActionListResult> getFollowingActionList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_following_action_list");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.getFollowingActionList(paramMap));
    }

    /**
     * 社区 - 精选 - 晒物列表
     *
     * @param page 页数
     */
    public Observable<PostListResult> getRecommendPostList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_recommend_post_list");
        //        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getRecommendPostList(paramMap));
    }

    /**
     * 社区 - 精选 - 热门标签列表
     */
    public Observable<List<TagBean>> getPostHotTagForHomeList() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_hot_tag_for_home_list");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getPostHotTagForHomeList(paramMap));
    }

    /**
     * 热门标签页 - 热门标签列表
     *
     * @param page 页数
     */
    public Observable<HotTagListResult> getPostHotTagList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_hot_tag_list");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getPostHotTagList(paramMap));
    }

    /**
     * 社区 - 发布帖子
     *
     * @param data 数据
     */
    public Observable<PublishPostResult> publishPost(String data) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "publish_post_v13");
        paramMap.put("data", data);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.publishPost(paramMap));
    }

    /**
     * 社区 - 编辑帖子
     *
     * @param postId 帖子Id
     * @param data   数据
     */
    public Observable<EditPostResult> editPost(int postId, String data) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "update_post_v13");
        paramMap.put("post_id", postId);
        paramMap.put("data", data);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.editPost(paramMap));
    }

    /**
     * 社区 - 添加标签 - 获取热门标签
     */
    public Observable<List<TagBean>> getPostTagList() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_tag_list");
        paramMap.put("is_hot", 1);
        paramMap.put("name", "");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getPostTagList(paramMap));
    }

    /**
     * 社区 - 添加标签 - 获取热门标签
     *
     * @param name 标签名
     */
    public Observable<List<TagBean>> getPostTagList(String name) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_tag_list");
        paramMap.put("is_hot", 1);
        paramMap.put("name", name);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getPostTagList(paramMap));
    }

    /**
     * 社区 - 晒物详情
     *
     * @param postId 晒物Id
     */
    public Observable<PostDetailResult> getPostDetail(int postId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_detail");
        paramMap.put("post_id", postId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getPostDetail(paramMap));
    }

    /**
     * 社区 - 点赞帖子
     *
     * @param postId 晒物Id
     */
    public Observable<LikePostResult> likePost(int postId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "like_post");
        paramMap.put("post_id", postId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.likePost(paramMap));
    }

    /**
     * 帖子详情 - 发表评论
     *
     * @param postId    帖子Id
     * @param content   评论内容
     * @param commentId 评论Id
     */
    public Observable<AddPostCommentResult> addPostComment(int postId, String content, int commentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "add_post_comment");
        paramMap.put("post_id", postId);
        paramMap.put("content", content);
        paramMap.put("comment_id", commentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.addPostComment(paramMap));
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子Id
     */
    public Observable<DeletePostResult> deletePost(int postId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "delete_post");
        paramMap.put("post_id", postId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.deletePost(paramMap));
    }

    /**
     * 帖子详情 - 删除评论
     *
     * @param commentId 评论Id
     */
    public Observable<DeletePostCommentResult> deletePostComment(int commentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "delete_post_comment");
        paramMap.put("comment_id", commentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.deletePostComment(paramMap));
    }

    /**
     * 帖子详情 - 相关晒物
     *
     * @param postId 晒物Id
     */
    public Observable<RandomPostListResult> getRandomPostList(int postId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_random_post_list");
        paramMap.put("post_id", postId);
        paramMap.put("count", REQUEST_COUNT / 2);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getRandomPostList(paramMap));
    }

    /**
     * 标签详情 - 获取标签详情
     *
     * @param tagId tagId
     */
    public Observable<GetPostTagResult> getPostTag(int tagId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_tag");
        paramMap.put("post_tag_id", tagId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getPostTag(paramMap));
    }

    /**
     * 标签详情 - 获取标签帖子列表 精选
     *
     * @param tagId tagId
     * @param page  页数
     */
    public Observable<GetTagHotPostHotListResult> getTagHotPostHotList(int tagId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_tag_hot_post_hot_list");
        paramMap.put("tag_id", tagId);
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getTagHotPostHotList(paramMap));
    }

    /**
     * 标签详情 - 获取标签帖子列表 最新
     *
     * @param tagId tagId
     * @param page  页数
     */
    public Observable<GetTagHotPostHotListResult> getTagPostList(int tagId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_tag_post_list");
        paramMap.put("tag_id", tagId);
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getTagPostList(paramMap));
    }

    /**
     * 社区 - 推荐用户
     */
    public Observable<GetHotUserListResult> getHotUserList() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_hot_user_list");
        paramMap.put("count", REQUEST_COUNT >> 1);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getHotUserList(paramMap));
    }

    /**
     * 社区 - 通讯录好友
     */
    public Observable<GetUserListByMobilesResult> getUserListByMobiles(String mobiles) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_user_list_by_mobiles");
        paramMap.put("mobiles", mobiles);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getUserListByMobiles(paramMap));
    }

    /**
     * 个人中心 - 消息列表
     *
     * @param page 页数
     */
    public Observable<MessageListResult> getMessageList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_notification_list");
        paramMap.put("count", REQUEST_COUNT);
        //        paramMap.put("count", 10);
        paramMap.put("type", NotificationListRequestType.MESSAGE);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.getMessageList(paramMap));
    }

    /**
     * 个人中心 - 通知列表
     *
     * @param page 页数
     */
    public Observable<NotificationListResult> getNotificationList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_notification_list");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("type", NotificationListRequestType.NOTIFICATION);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.getNotificationList(paramMap));
    }

    /**
     * 个人中心 - 关注列表
     */
    public Observable<UserListResultData> getFollowingList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_following_list");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getFollowingList(paramMap));
    }

    /**
     * 他人主页 - 关注列表
     */
    public Observable<UserListResultData> getFollowingList(int page, int userId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_following_list");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        paramMap.put("user_id", userId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getFollowingList(paramMap));
    }

    /**
     * 个人中心 - 粉丝列表
     */
    public Observable<UserListResultData> getFollowerList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_follower_list");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getFollowingList(paramMap));
    }

    /**
     * 他人主页 - 粉丝列表
     */
    public Observable<UserListResultData> getFollowerList(int page, int userId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_follower_list");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        paramMap.put("user_id", userId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getFollowingList(paramMap));
    }

    /**
     * 个人中心 - 获取积分历史记录
     */
    public Observable<GetMembershipPointHistoryResult> getMembershipPointHistory(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_membership_point_history");
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.getMembershipPointHistory(paramMap));
    }

    /**
     * 个人中心 - 详细信息
     */
    public Observable<GetUserStarInfoCountsV12Result> getUserStarInfoCountsV12() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_user_star_info_counts_v12");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.getUserStarInfoCountsV12(paramMap));
    }

    /**
     * 个人中心 - 点赞tab
     *
     * @param page 页数
     */
    public Observable<GetUserPostListResult> getLikePostList(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_like_post_list");
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getLikePostList(paramMap));
    }

    /**
     * 个人中心 - 专题tab
     *
     * @param page 页数
     */
    public Observable<GetUserStarSpecialsV11Result> getUserStarSpecialsV11(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_user_star_specials_v11");
        paramMap.put("page", page);
        paramMap.put("count", 10);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.getUserStarSpecialsV11(paramMap));
    }

    /**
     * 社区 - 搜索用户
     */
    public Observable<SearchUserResult> searchUser(String name, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "search_user");
        paramMap.put("name", name);
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.searchUser(paramMap));
    }

    /**
     * 帖子 - 评论列表
     */
    public Observable<getCommentListResult> getCommentList(int postId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_comment_list");
        paramMap.put("post_id", postId);
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getCommentList(paramMap));
    }

    /**
     * 帖子 - 点赞列表
     */
    public Observable<GetPostLikeUserListResult> getPostLikeUserList(int postId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_post_like_user_list");
        paramMap.put("post_id", postId);
        paramMap.put("count", REQUEST_COUNT);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getPostLikeUserList(paramMap));
    }

    /**
     * 他人主页 - 帖子列表
     */
    public Observable<GetUserPostListResult> getUserPostList(int userId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_user_post_list");
        paramMap.put("user_id", userId);
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getUserPostList(paramMap));
    }

    /**
     * 社区 - 关注用户
     */
    public Observable<FollowUserResult> followUser(int followingId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "follow_user");
        paramMap.put("following_id", followingId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.followUser(paramMap));
    }

    /**
     * 意见反馈
     *
     * @param content 反馈内容
     * @param mobile  联系方式
     */
    public Observable<CommonDataBean> feedback(String content, String mobile) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "feedback");
        paramMap.put("content", content);
        paramMap.put("mobile", mobile);
        paramMap.put("email", "");
        paramMap.put("qq", "");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.feedback(paramMap));
    }

    /**
     * 分享应用
     */
    public Observable<ShareBean> getShareAppInfo() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_share_app_info");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.getShareAppInfo(paramMap));
    }

    /**
     * 点赞笔记评论
     *
     * @param commentId 评论Id
     */
    public Observable<likePostCommentResult> likePostComment(int commentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "like_post_comment");
        paramMap.put("comment_id", commentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.likePostComment(paramMap));
    }

    /**
     * 草单评论列表
     *
     * @param easyoptId 草单Id
     * @param page      页数
     */
    public Observable<EasyoptCommentListResult> easyoptCommentList(int easyoptId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_comment_list");
        paramMap.put("easyopt_id", easyoptId);
        paramMap.put("page", page);
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.easyoptCommentList(paramMap));
    }

    /**
     * 草单评论
     *
     * @param easyoptId       草单Id
     * @param content         评论内容
     * @param parentCommentId 父评论Id
     */
    public Observable<AddEasyoptCommentResult> easyoptComment(int easyoptId, String content, int parentCommentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_comment");
        paramMap.put("easyopt_id", easyoptId);
        paramMap.put("content", content);
        paramMap.put("parent_comment_id", parentCommentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptComment(paramMap));
    }

    /**
     * 草单评论点赞
     *
     * @param commentId 评论Id
     */
    public Observable<EasyoptCommentLikeResult> easyoptCommentLike(int commentId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_comment_like");
        paramMap.put("comment_id", commentId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptCommentLike(paramMap));
    }

    /**
     * 添加草单
     *
     * @param name     草单名
     * @param content  草单描述
     * @param imgCover 草单封面图
     * @param visible  草单是否公开可见
     */
    public Observable<Integer> easyoptUpdate(String name, String content, String imgCover, int easyoptId, @EasyoptPublicVisible int visible, String selectids) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_update");
        paramMap.put("name", name);
        paramMap.put("img_cover", imgCover);
        paramMap.put("content", content);
        paramMap.put("easyopt_id", easyoptId);
        paramMap.put("is_visible", visible);
        paramMap.put("selectids", selectids);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptUpdate(paramMap));
    }

    /**
     * 更新草单
     *
     * @param name     草单名
     * @param content  草单描述
     * @param imgCover 草单封面图
     * @param visible  草单是否公开可见
     */
    public Observable<Integer> easyoptUpdate(String name, String content, String imgCover, int easyoptId, @EasyoptPublicVisible int visible) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_update");
        paramMap.put("name", name);
        paramMap.put("img_cover", imgCover);
        paramMap.put("content", content);
        paramMap.put("easyopt_id", easyoptId);
        paramMap.put("is_visible", visible);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptUpdate(paramMap));
    }

    /**
     * 更新草单
     */
    public Observable<Integer> easyoptUpdate(int easyOptId, ArrayList<PriorityModel> lists) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_update");
        paramMap.put("easyopt_id", easyOptId);

        String selectIds = new Gson().toJson(lists);
        paramMap.put("selectids", selectIds);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptUpdate(paramMap));
    }

    /**
     * 删除草单
     *
     * @param easyoptId 草单Id
     */
    public Observable<Object> easyoptDelete(int easyoptId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_update");
        paramMap.put("easyopt_id", easyoptId);
        paramMap.put("deleted", 1);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptDelete(paramMap));
    }

    /**
     * 删除草单评论
     *
     * @param easyoptId 草单Id
     */
    public Observable<Object> easyoptCommentDel(int commentid, int easyoptId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_comment_del");
        paramMap.put("comment_id", commentid);
        paramMap.put("easyopt_id", easyoptId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptCommentDel(paramMap));
    }

    /**
     * 获取我的草单列表，商详位置
     *
     * @param spuId 商品spuId
     */
    public Observable<EasyoptList4AddResult> easyoptList4Add(String spuId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_list4add");
        paramMap.put("spuid", spuId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptList4Add(paramMap));
    }

    /**
     * 添加商品至我的草单
     */
    public Observable<Object> easyoptAdd(String productId, int easyoptId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_add");
        paramMap.put("productid", productId);
        paramMap.put("easyopts", Arrays.toString(new int[]{easyoptId}));
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptAdd(paramMap));
    }

    /**
     * 兑换优惠券
     */
    public Observable<ConvertCouponBean> exchangeCopuon(String code) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "receive_coupon_from_code");
        paramMap.put("code_id", code);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.exchangeCopuon(paramMap));
    }

    /**
     * 获取最新版本
     */
    public Observable<EnforceUpdateBean> latestAppVer() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "latest_appver");
        HaiParamPrepare.buildParams(LoginLevels.NONE, paramMap);
        return transform(mSnsService.latestAppVer(paramMap));
    }

    /**
     * 分享成功
     */
    public Observable<CommonDataBean> shareSuccess() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "share_success");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.shareSuccess(paramMap));
    }

    /**
     * 精选草单
     */
    public Observable<EasyOptListResult> easyoptSelect() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_select");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.easyoptSelect(paramMap));
    }

    /**
     * 草单详情
     */
    public Observable<EasyOptListResult> easyoptDetail(int easyoptId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_detail");
        paramMap.put("easyopt_id", easyoptId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.easyoptDetail(paramMap));
    }

    /**
     * 获取草单TAG列表
     */
    public Observable<EasyOptTagResult> easyoptTagList() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_tag_list");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.easyoptTagList(paramMap));
    }

    /**
     * 获取对应TAG的草单列表
     *
     * @param tagId TAG ID
     * @param page  页数
     */
    public Observable<EasyOptListResult> easyoptList4Tag(int tagId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_list4tag");
        paramMap.put("tag_id", tagId);
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.easyoptList4Tag(paramMap));
    }

    /**
     * 获取对应用户的草单列表
     *
     * @param userId 用户 ID
     * @param page   页数
     */
    public Observable<EasyOptListResult> easyoptList4User(int userId, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_list4user");
        if (userId != 0) {
            paramMap.put("opt_user", userId);
        }
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mSnsService.easyoptList4User(paramMap));
    }

    /**
     * 我收藏的草单
     *
     * @param page 页数
     */
    public Observable<EasyOptListResult> easyoptList4Like(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_list4like");
        paramMap.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptList4Like(paramMap));
    }

    /**
     * 收藏草单
     *
     * @param easyOptId 草单Id
     */
    public Observable<EasyoptStarResult> easyoptLike(int easyOptId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_like");
        paramMap.put("easyopt_id", easyOptId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptLike(paramMap));
    }

    /**
     * 点赞草单
     *
     * @param easyOptId 草单Id
     */
    public Observable<EasyoptLikeResult> easyoptPoint(int easyOptId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "easyopt_point");
        paramMap.put("easyopt_id", easyOptId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.easyoptPoint(paramMap));
    }

    /**
     * 用户佣金
     */
    public Observable<CommissionBean> getCommission() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_commission");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mSnsService.getCommission(paramMap));
    }
}
