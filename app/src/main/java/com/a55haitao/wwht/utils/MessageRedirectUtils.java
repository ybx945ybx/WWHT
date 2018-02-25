package com.a55haitao.wwht.utils;

import android.content.Context;
import android.content.Intent;

import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.MessageBean;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.discover.CategorySpecialActivity;
import com.a55haitao.wwht.ui.activity.discover.CouponListActivity;
import com.a55haitao.wwht.ui.activity.firstpage.EntriesSpecialActivity;
import com.a55haitao.wwht.ui.activity.firstpage.FavorableSpecialActivity;
import com.a55haitao.wwht.ui.activity.firstpage.SplashActivity;
import com.a55haitao.wwht.ui.activity.myaccount.MyMembershipPointActivity;
import com.a55haitao.wwht.ui.activity.myaccount.NotificationDetailActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.OrderDetailActivity;
import com.a55haitao.wwht.ui.activity.social.PostDetailActivity;
import com.a55haitao.wwht.ui.activity.social.SocialSpecialActivity;

import static com.a55haitao.wwht.data.model.annotation.MessageType.COMMENT_POST;
import static com.a55haitao.wwht.data.model.annotation.MessageType.DRAW_OBJECT;
import static com.a55haitao.wwht.data.model.annotation.MessageType.DRAW_POINT;
import static com.a55haitao.wwht.data.model.annotation.MessageType.DRAW_TICKET;
import static com.a55haitao.wwht.data.model.annotation.MessageType.FOLLOW_YOU;
import static com.a55haitao.wwht.data.model.annotation.MessageType.H5;
import static com.a55haitao.wwht.data.model.annotation.MessageType.LIKE_POST;
import static com.a55haitao.wwht.data.model.annotation.MessageType.LIKE_YOUR_COMMENT;
import static com.a55haitao.wwht.data.model.annotation.MessageType.ORDER;
import static com.a55haitao.wwht.data.model.annotation.MessageType.ORDER_DELIVERING;
import static com.a55haitao.wwht.data.model.annotation.MessageType.ORDER_SHIPPED;
import static com.a55haitao.wwht.data.model.annotation.MessageType.REFUND_SUCCESS;
import static com.a55haitao.wwht.data.model.annotation.MessageType.REPLY_YOUR_COMMENT;
import static com.a55haitao.wwht.data.model.annotation.MessageType.SERVICE;
import static com.a55haitao.wwht.data.model.annotation.MessageType.SPECIAL;
import static com.a55haitao.wwht.data.model.annotation.MessageType.SYSTEM;
import static com.a55haitao.wwht.data.model.annotation.MessageType.TUAN;
import static com.a55haitao.wwht.data.model.annotation.MessageType.UPDATE_POST;
import static com.a55haitao.wwht.data.model.annotation.MessageType.WORKORDER;
import static com.a55haitao.wwht.data.model.annotation.MessageType.YOUR_POST_GET_FEATURED;

/**
 * 消息跳转
 *
 * @author 陶声
 * @since 2017-04-21
 */

public class MessageRedirectUtils {
    public static void redirect(Context context, MessageBean message, boolean newTask) {  //  newTask为true是消息推送过来
        // 消息类型
        int type = message.type;
        // 目标Id
        String targetId = message.target_id;

        switch (type) {
            case LIKE_POST:
            case COMMENT_POST:
            case REPLY_YOUR_COMMENT:
            case LIKE_YOUR_COMMENT:
            case YOUR_POST_GET_FEATURED:
            case UPDATE_POST:
                // 笔记详情
                PostDetailActivity.toThisActivity(context, Integer.valueOf(targetId), newTask);
                break;
            case FOLLOW_YOU:
                // 他人主页
                OthersHomePageActivity.toThisActivity(context, message.operator_id, newTask);
                break;
            case ORDER_SHIPPED:
            case ORDER_DELIVERING:
            case REFUND_SUCCESS:
            case ORDER:
                // 订单详情
                OrderDetailActivity.toThisActivity(context, targetId, newTask);
                break;
            case SPECIAL:
                // H5专题
                if (targetId.contains("http://") || targetId.contains("https://")) {
                    H5Activity.toThisActivity(context, targetId, "", true);
                    return;
                }
                String specialId = targetId.substring(targetId.lastIndexOf("/") + 1);
                if (targetId.contains("PostSpecial/")) {
                    // 社区专题
                    SocialSpecialActivity.toThisActivity(context, Integer.valueOf(specialId), newTask);
                } else if (targetId.contains("FavorableSpecial/")) {
                    // 特卖专题
                    FavorableSpecialActivity.toThisActivity(context, specialId, newTask);
                } else if (targetId.contains("ProductSpecial/")) {
                    // 商品专题
                    EntriesSpecialActivity.toThisActivity(context, specialId, newTask);
                } else if (targetId.contains("SearchSpecial/")) {
                    // 搜索专题（类目专题）
                    CategorySpecialActivity.toThisActivity(context, Integer.valueOf(specialId), PageType.SPECIAL, newTask);
                } else if (targetId.contains("Product/")) {
                    // 商品详情
                    ProductMainActivity.toThisAcivity(context, specialId, newTask);
                }
                break;
            case H5:
            case DRAW_OBJECT:
                H5Activity.toThisActivity(context, targetId, "", newTask);
                break;
            case DRAW_POINT:
                // 我的积分
                MyMembershipPointActivity.toThisActivity(context, newTask);
                break;
            case DRAW_TICKET:
                // 我的优惠券列表
                CouponListActivity.toThisActivity(context);
                break;
            case SERVICE:
                // 客服
                break;
            case WORKORDER:
                // 工单
                break;
            case SYSTEM:
                // 系统通知
                NotificationDetailActivity.toThisActivity(context, message.content, message.create_dt, newTask);
                break;
            case TUAN:
                // 0元团
                H5Activity.toThisActivity(context, targetId, "", true);
                break;
            default:
                if (HaiUtils.isForeground())
                    return;
                Intent intent = new Intent(context, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
        }
    }
}
