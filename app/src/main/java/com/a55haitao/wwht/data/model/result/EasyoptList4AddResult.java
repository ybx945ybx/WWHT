package com.a55haitao.wwht.data.model.result;

import java.util.List;

/**
 * 获取我的草单列表(商详页) - 返回结构
 *
 * @author 陶声
 * @since 2016-12-30
 */

public class EasyoptList4AddResult {

    public StarproductBean   starproduct;
    public List<EasyoptBean> easyopt;

    public static class StarproductBean {
        public int count;
    }

    public static class EasyoptBean {
        public String img_cover;
        public int    product_count;
        public int    easyopt_id;
        public String name;
        public int    contain;
    }
}

/*
{
    "msg":null,
    "stat":{
        "cid":"b1f0ff5136d85a84_14830832375901483083237590",
        "systime":1483083197616
    },
    "code":0,
    "data":{
        "easyopt":[
            {
                "img_cover":"http://st-prod.b0.upaiyun.com/post/2016/12/30/8f9hx1hdnuiaxm0vkz80o8nke700tklv",
                "product_count":0,
                "easyopt_id":35,
                "name":"wasd"
            },
            {
                "img_cover":"http://st-prod.b0.upaiyun.com/post/2016/12/30/r21y7nqmtbw2aqb8g1h6ziz2a25o5wo9",
                "product_count":0,
                "easyopt_id":36,
                "name":"wasd"
            },
            {
                "img_cover":"http://st-prod.b0.upaiyun.com/post/2016/12/30/a424489s4zw22u5kcf5xu4kmcq97t3v2",
                "product_count":0,
                "easyopt_id":37,
                "name":"hello"
            }
        ],
        "starproduct":{
            "count":10
        }
    }
}
*/
