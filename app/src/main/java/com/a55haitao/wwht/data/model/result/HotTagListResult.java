package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.TagBean;

import java.util.List;

/**
 * 热门标签页面
 *
 * @author 陶声
 * @since 2016-10-27
 */

public class HotTagListResult {
    public int           count;
    public int           page;
    public int           allpage;
    public List<RetBean> ret;

    public static class RetBean {
        public TagBean         tag;
        public List<PostsBean> posts;


        public static class PostsBean {
            public int    post_id;
            public String image_url;
        }
    }
}
/*
{
        "count":4,
        "page":1,
        "ret":[
            {
                "tag":{
                    "status":1,
                    "is_hot":1,
                    "id":14,
                    "name":"YSL圣罗兰"
                },
                "posts":[
                    {
                        "post_id":145,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/05/27/46bee0a74150a4c0c0a727eb008cefdf"
                    },
                    {
                        "post_id":178,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/06/db60960410bdbe80eb9e4febcf229eb3"
                    },
                    {
                        "post_id":185,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/07/b1e64e6cb0a639c2a72a80430b5f3e65"
                    }
                ]
            },
            {
                "tag":{
                    "status":1,
                    "is_hot":1,
                    "id":5,
                    "name":"护肤美容"
                },
                "posts":[
                    {
                        "post_id":220,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/19/b4d7a870a74f545b2cf51b5d73ad1477"
                    },
                    {
                        "post_id":240,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/23/d4495cc04e5d86293a5d44608bfcfcf3"
                    },
                    {
                        "post_id":255,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/24/24757b48324af3f379675b995477691d"
                    }
                ]
            },
            {
                "tag":{
                    "status":1,
                    "is_hot":1,
                    "id":6,
                    "name":"时尚穿搭"
                },
                "posts":[
                    {
                        "post_id":154,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/02/c0289fd748af0dd694b0b3c76e229597"
                    },
                    {
                        "post_id":175,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/06/857ce3d559aaa7296fe5308ab35f8573"
                    },
                    {
                        "post_id":180,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/06/07/9435f477d3e3818ec5a058e249f8adee"
                    }
                ]
            },
            {
                "tag":{
                    "status":1,
                    "is_hot":1,
                    "id":21,
                    "name":"热卖包包"
                },
                "posts":[
                    {
                        "post_id":141,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/05/27/c3c5f913a782d0e78f81d18c44f8d2d9"
                    },
                    {
                        "post_id":146,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/05/27/136044db375d90ddaef5108f891b2cb4"
                    },
                    {
                        "post_id":149,
                        "image_url":"http://st-prod.b0.upaiyun.com/post/2016/05/30/cf9ceaa8f83e75680fe1a11c1c7f9b93"
                    }
                ]
            }
        ],
        "allpage":1
    }
*/