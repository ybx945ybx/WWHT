package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.MessageBean;

import java.util.List;

/**
 * 个人中心 - 消息列表
 *
 * @author 陶声
 * @since 2016-10-14
 */

public class MessageListResult {

    public int               count;
    public int               unread_count;
    public int               page;
    public int               allpage;
    public List<MessageBean> notifications;


}

/*
{
        "count":24,
        "notifications":[
            {
                "create_dt":1467949744,
                "operator_id":5985203,
                "user_id":5948320,
                "title":"赞了您的评论",
                "type":5,
                "target_id":"921",
                "isread":1,
                "image_url":null,
                "device_type":0,
                "head_img":"",
                "nickname":"手机用户_2673",
                "id":3278
            },
            {
                "create_dt":1467949583,
                "operator_id":5985203,
                "user_id":5948320,
                "title":"赞了您的评论",
                "type":5,
                "target_id":"922",
                "isread":1,
                "image_url":null,
                "device_type":0,
                "head_img":"",
                "nickname":"手机用户_2673",
                "id":3276
            },
            {
                "create_dt":1467949527,
                "operator_id":5985203,
                "user_id":5948320,
                "title":"赞了您的评论",
                "type":5,
                "target_id":"923",
                "isread":1,
                "image_url":null,
                "device_type":0,
                "head_img":"",
                "nickname":"手机用户_2673",
                "id":3275
            },
            {
                "create_dt":1467945638,
                "operator_id":5985203,
                "user_id":5948320,
                "title":"评论了您的Post",
                "type":2,
                "target_id":"264",
                "isread":1,
                "image_url":"http://st-prod.b0.upaiyun.com/post/2016/07/06/3307c9323ae851cc0a6ecf09f0a7d6ef",
                "device_type":0,
                "head_img":"",
                "nickname":"手机用户_2673",
                "id":3265
            },
            {
                "create_dt":1467944591,
                "operator_id":5985203,
                "user_id":5948320,
                "title":"赞了您的Post",
                "type":1,
                "target_id":"264",
                "isread":1,
                "image_url":"http://st-prod.b0.upaiyun.com/post/2016/07/06/3307c9323ae851cc0a6ecf09f0a7d6ef",
                "device_type":0,
                "head_img":"",
                "nickname":"手机用户_2673",
                "id":3264
            },
            {
                "create_dt":1467944579,
                "operator_id":5985203,
                "user_id":5948320,
                "title":"关注了您",
                "type":3,
                "target_id":"5948320",
                "isread":1,
                "image_url":"",
                "device_type":0,
                "head_img":"",
                "nickname":"手机用户_2673",
                "id":3263
            },
            {
                "create_dt":1467895923,
                "operator_id":1326739,
                "user_id":5948320,
                "title":"评论了您的Post",
                "type":2,
                "target_id":"264",
                "isread":1,
                "image_url":"http://st-prod.b0.upaiyun.com/post/2016/07/06/3307c9323ae851cc0a6ecf09f0a7d6ef",
                "device_type":0,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/07/06/cd1e066b171e57aee2823a74e647e1e2",
                "nickname":"手机用户_111111111",
                "id":3256
            },
            {
                "create_dt":1467895805,
                "operator_id":1326739,
                "user_id":5948320,
                "title":"评论了您的Post",
                "type":2,
                "target_id":"264",
                "isread":1,
                "image_url":"http://st-prod.b0.upaiyun.com/post/2016/07/06/3307c9323ae851cc0a6ecf09f0a7d6ef",
                "device_type":0,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/07/06/cd1e066b171e57aee2823a74e647e1e2",
                "nickname":"手机用户_111111111",
                "id":3254
            },
            {
                "create_dt":1467894990,
                "operator_id":1326739,
                "user_id":5948320,
                "title":"关注了您",
                "type":3,
                "target_id":"5948320",
                "isread":1,
                "image_url":"",
                "device_type":0,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/07/06/cd1e066b171e57aee2823a74e647e1e2",
                "nickname":"手机用户_111111111",
                "id":3252
            },
            {
                "create_dt":1467815291,
                "operator_id":5952357,
                "user_id":5948320,
                "title":"关注了您",
                "type":3,
                "target_id":"5948320",
                "isread":1,
                "image_url":"",
                "device_type":0,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/07/06/2573556cea6110261419d95beaf68489",
                "nickname":"满天星003",
                "id":3245
            }
        ],
        "unread_count":0,
        "page":1,
        "allpage":3
    }
*/
