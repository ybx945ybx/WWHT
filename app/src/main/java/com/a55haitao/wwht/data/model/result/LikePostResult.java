package com.a55haitao.wwht.data.model.result;

/**
 * 点赞帖子
 *
 * @author 陶声
 * @since 2016-11-07
 */

public class LikePostResult {

    public int post_id;
    public int     membership_point;
    public int     like_count;
    public boolean is_like_now;
    public String  user_id;
}

/*
{
    "msg":null,
    "stat":{
        "cid":"b1f0ff5136d85a84_14784871049171478487104917",
        "systime":1478487104908
    },
    "code":0,
    "data":{
        "post_id":8371,
        "membership_point":1,
        "like_count":31,
        "is_like_now":true,
        "user_id":"5952916"
    }
}
*/
