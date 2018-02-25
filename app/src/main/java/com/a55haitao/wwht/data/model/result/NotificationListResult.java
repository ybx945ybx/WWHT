package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.MessageBean;

import java.util.List;

/**
 * 个人中心 - 通知列表
 *
 * @author 陶声
 * @since 2016-10-18
 */

public class NotificationListResult {
    public int               count;
    public int               unread_count;
    public int               page;
    public int               allpage;
    public List<MessageBean> notifications;

}
/*
{
        "count":21,
        "notifications":[
            {
                "create_dt":1464243563,
                "operator_id":5952580,
                "user_id":5948320,
                "title":"你有新的佣金入账啦",
                "target_id":"5952580",
                "content":"你有新的佣金入账啦",
                "isread":1,
                "device_type":0,
                "type":11,
                "id":2017
            },
            {
                "create_dt":1464243459,
                "operator_id":5952580,
                "user_id":5948320,
                "title":"你有新的佣金入账啦",
                "target_id":"5952580",
                "content":"你有新的佣金入账啦",
                "isread":1,
                "device_type":0,
                "type":11,
                "id":2014
            },
            {
                "create_dt":1464243105,
                "operator_id":0,
                "user_id":0,
                "title":"测试中！！",
                "target_id":"0",
                "content":"测试中！！",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":2008
            },
            {
                "create_dt":1464242500,
                "operator_id":0,
                "user_id":0,
                "title":"◼︎◼︎◼︎◼︎◼︎◼︎五姐送你一张时光穿梭票~",
                "target_id":"0",
                "content":"◼︎◼︎◼︎◼︎◼︎◼︎五姐送你一张时光穿梭票~",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":2007
            },
            {
                "create_dt":1464242369,
                "operator_id":0,
                "user_id":0,
                "title":"今日独家 夏日旅行必备",
                "target_id":"0",
                "content":"今日独家 夏日旅行必备",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":2006
            },
            {
                "create_dt":1464242351,
                "operator_id":0,
                "user_id":0,
                "title":"今日独家 夏日旅行必备",
                "target_id":"0",
                "content":"今日独家 夏日旅行必备",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":2005
            },
            {
                "create_dt":1464236093,
                "operator_id":0,
                "user_id":0,
                "title":"大家好，我是刘其峰",
                "target_id":"0",
                "content":"大家好，我是刘其峰",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":2004
            },
            {
                "create_dt":1464235767,
                "operator_id":0,
                "user_id":0,
                "title":"大家好，我是刘琪峰",
                "target_id":"0",
                "content":"大家好，我是刘琪峰",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":2003
            },
            {
                "create_dt":1464225796,
                "operator_id":0,
                "user_id":0,
                "title":"大家好，我是Han Meimei",
                "target_id":"0",
                "content":"大家好，我是Han Meimei",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":1994
            },
            {
                "create_dt":1464182839,
                "operator_id":0,
                "user_id":0,
                "title":"大家好，我是Jim Green",
                "target_id":"0",
                "content":"大家好，我是Jim Green",
                "isread":1,
                "device_type":0,
                "type":10000,
                "id":1993
            }
        ],
        "unread_count":0,
        "page":1,
        "allpage":3
    }
*/