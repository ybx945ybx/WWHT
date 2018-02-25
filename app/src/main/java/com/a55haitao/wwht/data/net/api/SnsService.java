package com.a55haitao.wwht.data.net.api;


import com.a55haitao.wwht.data.model.entity.CommissionBean;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.ConvertCouponBean;
import com.a55haitao.wwht.data.model.entity.EnforceUpdateBean;
import com.a55haitao.wwht.data.model.entity.PostBannerBean;
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
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Sns接口
 *
 * @author 陶声
 * @since 2016-10-10
 */

public interface SnsService {

    /**
     * 社区 - banner图
     */
    @POST("m.api")
    Observable<ApiModel<List<PostBannerBean>>> postBanner(@Body Map<String, Object> body);

    /**
     * 社区 - 关注
     */
    @POST("m.api")
    Observable<ApiModel<GetFollowingActionListResult>> getFollowingActionList(@Body Map<String, Object> body);

    /**
     * 社区 - 精选晒物列表
     */
    @POST("m.api")
    Observable<ApiModel<PostListResult>> getRecommendPostList(@Body Map<String, Object> body);

    /**
     * 社区 - 精选 - 热门标签列表
     */
    @POST("m.api")
    Observable<ApiModel<List<TagBean>>> getPostHotTagForHomeList(@Body Map<String, Object> body);

    /**
     * 社区 - 热门标签页面 - 热门标签列表
     */
    @POST("m.api")
    Observable<ApiModel<HotTagListResult>> getPostHotTagList(@Body Map<String, Object> body);

    /**
     * 社区 - 发布笔记
     */
    @POST("m.api")
    Observable<ApiModel<PublishPostResult>> publishPost(@Body Map<String, Object> body);

    /**
     * 社区 - 编辑笔记
     */
    @POST("m.api")
    Observable<ApiModel<EditPostResult>> editPost(@Body Map<String, Object> body);

    /**
     * 社区 - 添加标签 - 获取热门标签
     */
    @POST("m.api")
    Observable<ApiModel<List<TagBean>>> getPostTagList(@Body Map<String, Object> body);

    /**
     * 社区 - 晒物详情
     */
    @POST("m.api")
    Observable<ApiModel<PostDetailResult>> getPostDetail(@Body Map<String, Object> body);

    /**
     * 社区 - 相关晒物
     */
    @POST("m.api")
    Observable<ApiModel<RandomPostListResult>> getRandomPostList(@Body Map<String, Object> body);

    /**
     * 标签详情 - 获取标签详情
     */
    @POST("m.api")
    Observable<ApiModel<GetPostTagResult>> getPostTag(@Body Map<String, Object> body);

    /**
     * 标签详情 - 获取标签帖子列表 热门
     */
    @POST("m.api")
    Observable<ApiModel<GetTagHotPostHotListResult>> getTagHotPostHotList(@Body Map<String, Object> body);

    /**
     * 标签详情 - 获取标签帖子列表 最新
     */
    @POST("m.api")
    Observable<ApiModel<GetTagHotPostHotListResult>> getTagPostList(@Body Map<String, Object> body);

    /**
     * 点赞帖子
     */
    @POST("m.api")
    Observable<ApiModel<LikePostResult>> likePost(@Body Map<String, Object> body);

    /**
     * 帖子 - 发表评论
     */
    @POST("m.api")
    Observable<ApiModel<AddPostCommentResult>> addPostComment(@Body Map<String, Object> body);

    /**
     * 删除帖子
     */
    @POST("m.api")
    Observable<ApiModel<DeletePostResult>> deletePost(@Body Map<String, Object> body);

    /**
     * 删除评论
     */
    @POST("m.api")
    Observable<ApiModel<DeletePostCommentResult>> deletePostComment(@Body Map<String, Object> body);

    /**
     * 社区 - 推荐用户
     */
    @POST("m.api")
    Observable<ApiModel<GetHotUserListResult>> getHotUserList(@Body Map<String, Object> body);

    /**
     * 社区 - 通讯录好友
     */
    @POST("m.api")
    Observable<ApiModel<GetUserListByMobilesResult>> getUserListByMobiles(@Body Map<String, Object> body);

    /**
     * 个人中心 - 获取消息列表
     */
    @POST("m.api")
    Observable<ApiModel<MessageListResult>> getMessageList(@Body Map<String, Object> body);

    /**
     * 个人中心 - 获取通知列表
     */
    @POST("m.api")
    Observable<ApiModel<NotificationListResult>> getNotificationList(@Body Map<String, Object> body);

    /**
     * 个人中心 - 关注
     */
    @POST("m.api")
    Observable<ApiModel<UserListResultData>> getFollowingList(@Body Map<String, Object> body);

    /**
     * 个人中心 - 粉丝
     */
    @POST("m.api")
    Observable<ApiModel<UserListResultData>> getFollowerList(@Body Map<String, Object> body);

    /**
     * 个人中心 - 获取积分历史记录
     */
    @POST("m.api")
    Observable<ApiModel<GetMembershipPointHistoryResult>> getMembershipPointHistory(@Body Map<String, Object> body);

    /**
     * 个人中心 - 详细信息
     */
    @POST("m.api")
    Observable<ApiModel<GetUserStarInfoCountsV12Result>> getUserStarInfoCountsV12(@Body Map<String, Object> body);

    /**
     * 个人中心 - 点赞tab
     */
   /* @POST("m.api")
    Observable<ApiModel<GetLikePostListResult>> getLikePostList(@Body Map<String, Object> body);*/
    @POST("m.api")
    Observable<ApiModel<GetUserPostListResult>> getLikePostList(@Body Map<String, Object> body);

    /**
     * 个人中心 - 专题
     */
    @POST("m.api")
    Observable<ApiModel<GetUserStarSpecialsV11Result>> getUserStarSpecialsV11(@Body Map<String, Object> body);

    /**
     * 他人主页 - 帖子列表
     */
    @POST("m.api")
    Observable<ApiModel<GetUserPostListResult>> getUserPostList(@Body Map<String, Object> body);

    /**
     * 添加好友 - 搜索好友
     */
    @POST("m.api")
    Observable<ApiModel<SearchUserResult>> searchUser(@Body Map<String, Object> body);

    /**
     * 帖子 - 评论列表
     */
    @POST("m.api")
    Observable<ApiModel<getCommentListResult>> getCommentList(@Body Map<String, Object> body);

    /**
     * 点赞列表
     */
    @POST("m.api")
    Observable<ApiModel<GetPostLikeUserListResult>> getPostLikeUserList(@Body Map<String, Object> body);

    /**
     * 点赞列表
     */
    @POST("m.api")
    Observable<ResponseBody> getPostSpecialLikes(@Body Map<String, Object> body);

    /**
     * 点赞列表
     */
    @POST("m.api")
    Observable<ResponseBody> getProductSpecialLikes(@Body Map<String, Object> body);

    /**
     * 社区 关注用户
     */
    @POST("m.api")
    Observable<ApiModel<FollowUserResult>> followUser(@Body Map<String, Object> body);

    /**
     * 意见反馈
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> feedback(@Body Map<String, Object> body);

    /**
     * 分享应用
     */
    @POST("m.api")
    Observable<ApiModel<ShareBean>> getShareAppInfo(@Body Map<String, Object> body);

    /**
     * 点赞笔记评论
     */
    @POST("m.api")
    Observable<ApiModel<likePostCommentResult>> likePostComment(@Body Map<String, Object> body);

    /**
     * 草单评论列表
     */
    @POST("m.api")
    Observable<ApiModel<EasyoptCommentListResult>> easyoptCommentList(@Body Map<String, Object> body);

    /**
     * 草单评论
     */
    @POST("m.api")
    Observable<ApiModel<AddEasyoptCommentResult>> easyoptComment(@Body Map<String, Object> body);

    /**
     * 草单评论点赞
     */
    @POST("m.api")
    Observable<ApiModel<EasyoptCommentLikeResult>> easyoptCommentLike(@Body Map<String, Object> body);

    /**
     * 添加或更新草单
     */
    @POST("m.api")
    Observable<ApiModel<Integer>> easyoptUpdate(@Body Map<String, Object> body);

    /**
     * 删除草单
     */
    @POST("m.api")
    Observable<ApiModel<Object>> easyoptDelete(@Body Map<String, Object> body);

    /**
     * 删除草单评论
     */
    @POST("m.api")
    Observable<ApiModel<Object>> easyoptCommentDel(@Body Map<String, Object> body);

    /**
     * 获取我的草单列表，商详位置
     */
    @POST("m.api")
    Observable<ApiModel<EasyoptList4AddResult>> easyoptList4Add(@Body Map<String, Object> body);

    /**
     * 添加商品至我的草单
     */
    @POST("m.api")
    Observable<ApiModel<Object>> easyoptAdd(@Body Map<String, Object> body);

    /**
     * 优惠码兑换
     */
    @POST("m.api")
    Observable<ApiModel<ConvertCouponBean>> exchangeCopuon(@Body Map<String, Object> body);

    /**
     * 获取最新版本
     */
    @POST("m.api")
    Observable<ApiModel<EnforceUpdateBean>> latestAppVer(@Body Map<String, Object> body);

    /**
     * 分享成功
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> shareSuccess(@Body Map<String, Object> body);

    /**
     * 精选草单
     */
    @POST("m.api")
    Observable<ApiModel<EasyOptListResult>> easyoptSelect(@Body Map<String, Object> body);

    /**
     * 草单详情
     */
    @POST("m.api")
    Observable<ApiModel<EasyOptListResult>> easyoptDetail(@Body Map<String, Object> body);

    /**
     * 获取草单TAG列表
     */
    @POST("m.api")
    Observable<ApiModel<EasyOptTagResult>> easyoptTagList(@Body Map<String, Object> body);

    /**
     * 获取对应TAG的草单列表
     */
    @POST("m.api")
    Observable<ApiModel<EasyOptListResult>> easyoptList4Tag(@Body Map<String, Object> body);

    /**
     * 获取对应用户的草单列表
     */
    @POST("m.api")
    Observable<ApiModel<EasyOptListResult>> easyoptList4User(@Body Map<String, Object> body);

    /**
     * 我收藏的草单
     */
    @POST("m.api")
    Observable<ApiModel<EasyOptListResult>> easyoptList4Like(@Body Map<String, Object> body);

    /**
     * 收藏草单
     */
    @POST("m.api")
    Observable<ApiModel<EasyoptStarResult>> easyoptLike(@Body Map<String, Object> body);

    /**
     * 点赞草单
     */
    @POST("m.api")
    Observable<ApiModel<EasyoptLikeResult>> easyoptPoint(@Body Map<String, Object> body);

    /**
     * 用户佣金
     */
    @POST("m.api")
    Observable<ApiModel<CommissionBean>> getCommission(@Body Map<String, Object> body);
}
