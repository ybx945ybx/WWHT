package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.UserTitle;
import com.a55haitao.wwht.data.model.entity.CommCountsBean;

import java.util.List;

/**
 * 用户信息Bean
 *
 * @author 陶声
 * @since 2016-11-07
 */

public class UserInfoResult {

    public int             following_count;
    public int             commissions;
    public int             sex;
    public int             like_count;
    public boolean         is_following;
    public int             id;
    public int             post_count;
    public int             register_dt;
    public String          location;
    public int             follower_count;
    public String          email;
    public String          username;
    public String          ucenter_token;
    public int             is_operation;
    public int             lastlogin;
    public String          nickname;
    public String          lastloginip;
    public String          mobile;
    public int             membership_point;
    public String          signature;
    public String          head_img;
    public List<UserTitle> user_title;
    public CommCountsBean  comm_counts;
}
/*
{
        "following_count":4,
        "commissions":0,
        "sex":1,
        "like_count":7,
        "is_following":false,
        "id":5954561,
        "post_count":7,
        "register_dt":1469530328,
        "location":"",
        "follower_count":5,
        "user_title":[

        ],
        "email":"",
        "username":"ht3003",
        "ucenter_token":"1579740d8d820e2.86900360",
        "is_operation":0,
        "lastlogin":1469530328,
        "nickname":"手机用户_007",
        "lastloginip":"127.0.0.1",
        "mobile":"18000000007",
        "membership_point":1358,
        "signature":"None",
        "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/10/08/a37ebe4c9a10b0a299e9ca0683291460"
    }
*/