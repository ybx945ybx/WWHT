package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.UserListBean;

import java.util.List;

/**
 * 帖子 - 点赞列表
 *
 * @author 陶声
 * @since 2016-11-09
 */

public class GetPostLikeUserListResult {
    public int                count;
    public int                page;
    public int                allpage;
    public List<UserListBean> users;
}

/*
{
        "count":11,
        "page":1,
        "users":[
            {
                "username":"ht3003",
                "following_count":4,
                "post_count":7,
                "ucenter_token":"1579740d8d820e2.86900360",
                "id":5954561,
                "membership_point":1358,
                "is_operation":0,
                "sex":1,
                "nickname":"手机用户_007",
                "like_count":7,
                "is_following":false,
                "location":"",
                "signature":"None",
                "follower_count":5,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/10/08/a37ebe4c9a10b0a299e9ca0683291460",
                "user_title":[

                ],
                "email":""
            },
            {
                "username":"ht3060",
                "following_count":2,
                "post_count":3,
                "ucenter_token":"657a19dcf94a5c8.93025283",
                "id":5954590,
                "membership_point":532,
                "is_operation":0,
                "sex":1,
                "nickname":"w2",
                "like_count":2,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":0,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/08/04/507ece3c5fd3cbac830f4c4bd3341500",
                "user_title":[

                ],
                "email":""
            },
            {
                "username":"ht_423479dfb1",
                "following_count":7,
                "post_count":4,
                "ucenter_token":"457450d44dd34d1.60397205",
                "id":5952057,
                "membership_point":327,
                "is_operation":0,
                "sex":1,
                "nickname":"满天星⭐️ instead",
                "like_count":3,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":5,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/05/25/2adedd351a4e3d217bd269a2131e9429",
                "user_title":[

                ],
                "email":""
            },
            {
                "username":"ht_11002100017",
                "following_count":38,
                "post_count":4,
                "ucenter_token":"9574ff9a57a3b39.32589716",
                "id":5951392,
                "membership_point":840,
                "is_operation":0,
                "sex":0,
                "nickname":"噢原来是小黑菇凉",
                "like_count":8,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":3,
                "head_img":"http://st-prod.b0.upaiyun.com/fake_account/daf6b162e261a542463225e2aef293ed.jpg",
                "user_title":[

                ],
                "email":"11002100017@ent.baijie.in"
            },
            {
                "username":"ht_11002100016",
                "following_count":37,
                "post_count":6,
                "ucenter_token":"3574ff9bc4159f5.97859014",
                "id":5951391,
                "membership_point":1560,
                "is_operation":0,
                "sex":0,
                "nickname":"MissAn",
                "like_count":7,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":4,
                "head_img":"http://st-prod.b0.upaiyun.com/fake_account/40751854b8711136902e5e92f334659c.jpg",
                "user_title":[

                ],
                "email":"11002100016@ent.baijie.in"
            },
            {
                "username":"ht_11002100015",
                "following_count":33,
                "post_count":2,
                "ucenter_token":"3574ffa900d7b19.40689136",
                "id":5951390,
                "membership_point":520,
                "is_operation":0,
                "sex":0,
                "nickname":"桑小茜",
                "like_count":2,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":3,
                "head_img":"http://st-prod.b0.upaiyun.com/fake_account/9119be3a9f0d225aa0c6da83e14b29a4.jpg",
                "user_title":[

                ],
                "email":"11002100015@ent.baijie.in"
            },
            {
                "username":"ht2405",
                "following_count":47,
                "post_count":3,
                "ucenter_token":"1057639987b906e4.76819708",
                "id":5980844,
                "membership_point":580,
                "is_operation":0,
                "sex":1,
                "nickname":"Kandy|宇翔",
                "like_count":21,
                "is_following":false,
                "location":"中国,上海市",
                "signature":"偶尔喜欢逛一些时尚网站，比较喜欢Gucci、Lv、Chanel、Coach！",
                "follower_count":5,
                "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/06/24/1f668a8e28a170636b888251dbd5a292",
                "user_title":[

                ],
                "email":""
            },
            {
                "username":"slammars",
                "following_count":0,
                "post_count":0,
                "ucenter_token":"0574ff0728d24e7.04573981",
                "id":5869300,
                "membership_point":0,
                "is_operation":0,
                "sex":0,
                "nickname":"手机用户_2290",
                "like_count":0,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":1,
                "head_img":"",
                "user_title":[

                ],
                "email":""
            },
            {
                "username":"ht_11002100014",
                "following_count":36,
                "post_count":5,
                "ucenter_token":"157466fae07ef38.38655070",
                "id":5951389,
                "membership_point":900,
                "is_operation":0,
                "sex":0,
                "nickname":"超模心小个",
                "like_count":8,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":7,
                "head_img":"http://st-prod.b0.upaiyun.com/fake_account/a7692e1777577c85ac4ef746b1c68428.jpg",
                "user_title":[

                ],
                "email":"11002100014@ent.baijie.in"
            },
            {
                "username":"ht_11002100013",
                "following_count":39,
                "post_count":3,
                "ucenter_token":"5576cd82e6f5277.00509285",
                "id":5951388,
                "membership_point":780,
                "is_operation":0,
                "sex":0,
                "nickname":"咕噜",
                "like_count":0,
                "is_following":false,
                "location":"",
                "signature":"",
                "follower_count":3,
                "head_img":"http://st-prod.b0.upaiyun.com/fake_account/3313d8a9066b83b3f7408bd6556d0ad5.jpg",
                "user_title":[

                ],
                "email":"11002100013@ent.baijie.in"
            }
        ],
        "allpage":2
    }
*/