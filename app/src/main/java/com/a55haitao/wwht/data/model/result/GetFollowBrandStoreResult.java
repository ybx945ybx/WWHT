package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.ui.fragment.myaccount.brandstore.SellerFragment;

import java.util.List;

/**
 * 关注的商家 & 品牌
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.brandstore.BrandFragment 品牌}
 * {@link SellerFragment 商家}
 *
 * @author 陶声
 * @since 2016-10-20
 */

public class GetFollowBrandStoreResult {

    public int            count;
    public int            page;
    public int            allpage;
    public List<DataBean> datas;

    public static class DataBean {
        public boolean   is_followed;
        public String    logo2;
        public String    logo1;
        public String    description;
        public String    logo3;
        public String    namecn;
        public String    country;
        public ShareBean share;
        public int       modified_time;
        public String    name;
        public String    img_cover;
        public int       brand_id;
        public String    flag;
        public int       created_time;
        public String    pdb_logo;
        public QueryBean query;
        public String    nameen;
        public String    desc;

        public static class ShareBean {
            public String url;
            public String title;
            public int    amount;
            public String icon;
            public String desc;
        }

    }
}

/*
{
        "count":4,
        "page":1,
        "datas":[
            {
                "is_followed":true,
                "logo2":"http://st-prod.b0.upaiyun.com/zeus/2016/07/29/d5eda6694acf50f84551e228309723bc!/format/png",
                "logo1":"http://st-prod.b0.upaiyun.com/zeus/2016/07/29/3f01e2a31938f905d4d0825aff2732f7!/format/png",
                "description":"",
                "logo3":"http://st-prod.b0.upaiyun.com/zeus/2016/07/29/82176009611062a13c29e19efea7116e!/format/png",
                "namecn":"A.P.C.",
                "country":"",
                "share":{
                    "small_icon":"http://h5.55haitao.com/brand/A.P.C.",
                    "title":"汇聚A.P.C.名品，尽在55海淘",
                    "amount":0,
                    "icon":"http://st-prod.b0.upaiyun.com/zeus/2016/07/29/3f01e2a31938f905d4d0825aff2732f7!/format/png",
                    "desc":"官网直购，一键海淘，884款A.P.C.商品任挑选"
                },
                "modified_time":1469795033,
                "name":"A.P.C.",
                "img_cover":"http://st-prod.b0.upaiyun.com/zeus/2016/07/29/1a0fb6367c965684ed18e372c34187ae!/format/png",
                "brand_id":192,
                "flag":"http://searchimg.b0.upaiyun.com/cateOptImg/US@3x.png",
                "created_time":1468845141,
                "pdb_logo":"http://",
                "query":{
                    "brand":"A.P.C."
                },
                "nameen":"A.P.C.",
                "desc":""
            },
            {
                "is_followed":true,
                "logo2":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/91f479c7db2c2d2a78baba19c7ca78ec!/format/jpg",
                "logo1":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/939c4b4e4bb80240a055c3c6fc557c12!/format/jpg",
                "description":"adidas（阿迪达斯）是德国著名的高级运动用品制造商，覆盖跑步、篮球、训练、足球、户外等领域的服装、鞋类及配件产品。旗下拥有Y-3、NEO等系列产品。adidas秉持完美制鞋的理念，持续推出及科技和时尚于一身的运动装备，提升运动表现。并且积极尝试与时尚设计师合作推出时尚运动系列。",
                "logo3":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/0c4c686e3b51fc9f1acc2c4801e1d98a!/format/png",
                "namecn":"阿迪达斯",
                "country":"德国",
                "share":{
                    "small_icon":"http://h5.55haitao.com/brand/Adidas",
                    "title":"汇聚Adidas名品，尽在55海淘",
                    "amount":0,
                    "icon":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/939c4b4e4bb80240a055c3c6fc557c12!/format/jpg",
                    "desc":"官网直购，一键海淘，22884款Adidas商品任挑选"
                },
                "modified_time":1469692209,
                "name":"Adidas",
                "img_cover":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/65f0620bc7479766b81d012abda24c3a!/format/jpg",
                "brand_id":584,
                "flag":"http://searchimg.b0.upaiyun.com/cateOptImg/DE@3x.png",
                "created_time":1467278716,
                "pdb_logo":"http://searchimg.b0.upaiyun.com/brandImg/Adidas.jpg",
                "query":{
                    "category":"女装",
                    "query":"",
                    "brand":"Adidas/阿迪达斯"
                },
                "nameen":"Adidas",
                "desc":"adidas（阿迪达斯）是德国著名的高级运动用品制造商，覆盖跑步、篮球、训练、足球、户外等领域的服装、鞋类及配件产品。旗下拥有Y-3、NEO等系列产品。adidas秉持完美制鞋的理念，持续推出及科技和时尚于一身的运动装备，提升运动表现。并且积极尝试与时尚设计师合作推出时尚运动系列。"
            },
            {
                "is_followed":true,
                "logo2":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/caa7d9e5682e9df58392f85233fa643a!/format/jpg",
                "logo1":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/96bd500fbae795b6bd590354147488c0!/format/jpg",
                "description":"",
                "logo3":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/bef9dab0610ad7004ccf2792bc088fc5!/format/png",
                "namecn":"伊索",
                "country":"",
                "share":{
                    "small_icon":"http://h5.55haitao.com/brand/Aesop",
                    "title":"汇聚Aesop名品，尽在55海淘",
                    "amount":0,
                    "icon":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/96bd500fbae795b6bd590354147488c0!/format/jpg",
                    "desc":"官网直购，一键海淘，355款Aesop商品任挑选"
                },
                "modified_time":1469610049,
                "name":"Aesop",
                "img_cover":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/081b5cd2317f7202c7cd6b942a8b4025!/format/jpg",
                "brand_id":3534,
                "flag":"http://searchimg.b0.upaiyun.com/cateOptImg/US@3x.png",
                "created_time":1467279034,
                "pdb_logo":"http://",
                "query":{
                    "category":"",
                    "query":"",
                    "brand":"Aesop/伊索"
                },
                "nameen":"Aesop",
                "desc":""
            },
            {
                "is_followed":true,
                "logo2":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/4a328f33239d58410a0fdd1b0b2634b4!/format/jpg",
                "logo1":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/b00729a74a0a2a4be35ce1c4a4f4ce7f!/format/jpg",
                "description":"AHAVA是唯一原创于死海地区的美肤保养品企业，凭著一股求真的热情，致力于揭开矿物质使肌肤保持青春的秘密。最热销的产品有AHAVA换肤修复晚霜、AHAVA清爽亚光保湿乳、AHAVA滋润防护日霜等。",
                "logo3":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/d080f599a423624d2b58d144a89b3ac9!/format/png",
                "namecn":"Ahava",
                "country":"美国",
                "share":{
                    "small_icon":"http://h5.55haitao.com/brand/Ahava",
                    "title":"汇聚Ahava名品，尽在55海淘",
                    "amount":0,
                    "icon":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/b00729a74a0a2a4be35ce1c4a4f4ce7f!/format/jpg",
                    "desc":"官网直购，一键海淘，504款Ahava商品任挑选"
                },
                "modified_time":1469610015,
                "name":"Ahava",
                "img_cover":"http://st-prod.b0.upaiyun.com/zeus/2016/06/30/d3546ac878190ae4d3f2ae20c1d8b07d!/format/jpg",
                "brand_id":2460,
                "flag":"http://searchimg.b0.upaiyun.com/cateOptImg/US@3x.png",
                "created_time":1467279032,
                "pdb_logo":"http://searchimg.b0.upaiyun.com/sellerImg/ahava.jpg",
                "query":{
                    "category":"",
                    "query":"",
                    "brand":"Ahava"
                },
                "nameen":"Ahava",
                "desc":"AHAVA是唯一原创于死海地区的美肤保养品企业，凭著一股求真的热情，致力于揭开矿物质使肌肤保持青春的秘密。最热销的产品有AHAVA换肤修复晚霜、AHAVA清爽亚光保湿乳、AHAVA滋润防护日霜等。"
            }
        ],
        "allpage":1
    }
*/