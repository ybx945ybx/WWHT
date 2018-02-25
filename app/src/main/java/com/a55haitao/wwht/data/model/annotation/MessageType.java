package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 消息类型
 *
 * @author 陶声
 * @since 2016-08-26
 */
@IntDef({MessageType.LIKE_POST,
        MessageType.COMMENT_POST,
        MessageType.FOLLOW_YOU,
        MessageType.REPLY_YOUR_COMMENT,
        MessageType.LIKE_YOUR_COMMENT,
        MessageType.YOUR_POST_GET_FEATURED,
        MessageType.UPDATE_POST,
        MessageType.ORDER_SHIPPED,
        MessageType.ORDER_DELIVERING,
        MessageType.REFUND_SUCCESS,
        MessageType.COMMISSION,
        MessageType.FRIENDS_JOIN,
        MessageType.H5,
        MessageType.SPECIAL,
        MessageType.TALK,
        MessageType.DRAW_POINT,
        MessageType.DRAW_TICKET,
        MessageType.DRAW_OBJECT,
        MessageType.SERVICE,
        MessageType.WORKORDER,
        MessageType.ORDER,
        MessageType.SYSTEM,
        MessageType.TUAN})
@Retention(RetentionPolicy.SOURCE)

public @interface MessageType {
    // 帖子相关
    int LIKE_POST              = 1;     // 点赞笔记 (笔记详情)
    int COMMENT_POST           = 2;     // 评论笔记 (笔记详情)
    int FOLLOW_YOU             = 3;     // 关注用户 (用户中心)
    int REPLY_YOUR_COMMENT     = 4;     // 回复评论 (笔记详情)
    int LIKE_YOUR_COMMENT      = 5;     // 点赞评论 (笔记详情)
    int YOUR_POST_GET_FEATURED = 6;     // 入选精贴 (笔记详情)
    int UPDATE_POST            = 7;     // 修改笔记 (笔记详情)
    // 订单相关
    int ORDER_SHIPPED          = 8;     // 已发货 (订单详情)
    int ORDER_DELIVERING       = 9;     // 配送中 (订单详情)
    int REFUND_SUCCESS         = 10;    // 退款成功 (订单详情)
    // 佣金相关
    int COMMISSION             = 11;    // 收到佣金 (佣金明细)
    int FRIENDS_JOIN           = 12;    // 好友加入 (我的佣金)
    // 其他
    int H5                     = 13;    // H5链接 (H5页面)
    int SPECIAL                = 14;    // 专题 (社区&电商专题)
    int TALK                   = 15;    // 话题 (后期)
    // 抽奖
    int DRAW_POINT             = 16;    // 中奖积分 (我的积分页面)
    int DRAW_TICKET            = 17;    // 中奖劵 (我的优惠券列表)
    int DRAW_OBJECT            = 18;    // 中奖实物 (H5)
    int SERVICE                = 19;    // 客服消息
    int WORKORDER              = 20;    // 工单信息
    int ORDER                  = 21;    // 订单信息推送 (订单详情)
    int TUAN                   = 22;    // 0元团
    int SYSTEM                 = 10000; // 系统消息(手动)
}
