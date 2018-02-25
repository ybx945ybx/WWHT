package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by drolmen on 2016/12/27.
 */

public class EasyOptBean {

    /**
     * img_cover : http://searchimg.b0.upaiyun.com/cateOptImg/19/b00e40e84396637eec88ab34378d80f6
     * user_id : 5954588
     * easyopt_id : 5
     * deleted : 0
     * reply_count : 0
     * likeit : 0
     * modified_time : 1481610779
     * selectids : [{"spuid":"4cec00be2d65a6d385d8e7b572d33f07","priority":5},{"spuid":"2c37bf1d8b53c5952dfe306c3cf97637","priority":3},{"spuid":"2c37bf1d8b53c5952dfe306c3cf97638","priority":4},{"spuid":"31b47c2e31f724dbd753b25e4b606576","priority":2},{"spuid":"b5e0c363b19b2ffd0f92f4d75f6d1243","priority":1},{"spuid":"f9be0e071d4685745e3d543ef14b8f06","priority":1},{"spuid":"029a4d21f7be168a13ec080406af0346","priority":10}]
     * content : 可以吗，可以的2
     * product_count : 5
     * like_count : 0
     * products : {"msg":"","code":0,"data":{"products":[{"category":"女装>夹克/外套","status":"2","coverImgUrl":"http://www.zappos.com/images/z/2/7/7/5/5/5/2775558-p-2x.jpg","_id":4104290,"DOCID":"029a4d21f7be168a13ec080406af0346","name":"Ruthie","mallPrice":2008,"brand":"Bb Dakota","pageUrl":"http://www.6pm.com/bb-dakota-ruthie-black","inStock":1,"defaultSkuid":"595c8eb3a4a7305fece30524b3af7c34","realPrice":606,"realPriceOrg":82.98999786377,"sellerName":"6pm","unit":"USD","stock":1},{"category":"运动户外>运动鞋包","status":"0","coverImgUrl":"http://s4.thcdn.com/productimg/600/600/11105271-1754286779061034.jpg","_id":2323104,"DOCID":"4cec00be2d65a6d385d8e7b572d33f07","name":"Grafea Violet Baby Backpack - Lilac","mallPrice":1783,"brand":"Grafea","pageUrl":"http://www.thehut.com/bags-clothing/women/accessories/grafea-violet-baby-backpack-lilac/11105271.html","inStock":1,"defaultSkuid":"4cec00be2d65a6d385d8e7b572d33f07","realPrice":1783,"realPriceOrg":197.19999694824,"sellerName":"the Hut","unit":"GBP","stock":0},{"category":"包袋>女包>双肩包/背包","status":"0","coverImgUrl":"http://s1.thcdn.com/productimg/0/600/600/65/10641865-1424185556-871368.jpg","_id":3000200,"DOCID":"2c37bf1d8b53c5952dfe306c3cf97637","name":"Grafea Pink Lemonade Medium Leather Rucksack - Pink","mallPrice":1628,"brand":"Grafea","pageUrl":"http://www.coggles.com/bags-clothing/women/accessories/grafea-pink-lemonade-medium-leather-rucksack-pink/10641865.html","inStock":1,"defaultSkuid":"2c37bf1d8b53c5952dfe306c3cf97637","realPrice":1628,"realPriceOrg":180,"sellerName":"Coggles","unit":"GBP","stock":0},{"category":"包袋>女包>双肩包/背包","status":"0","coverImgUrl":"http://g-ec4.images-amazon.com/images/G/01/Shopbop/p/prod/products/grafe/grafe3002410607/grafe3002410607_q1_1-0.jpg","_id":1732098,"DOCID":"31b47c2e31f724dbd753b25e4b606576","name":"Amorino 背包","mallPrice":1378,"brand":"Grafea","pageUrl":"http://cn.shopbop.com/amorino-backpack-grafea/vp/v=1/1547971720.htm","inStock":1,"defaultSkuid":"f91300de435521d5fa9b16e8f007e650","realPrice":1378,"realPriceOrg":189,"sellerName":"Shopbop","unit":"USD","stock":0},{"category":"女装>连衣裙","status":"2","coverImgUrl":"http://www.zappos.com/images/z/3/1/7/3/2/4/3173249-p-2x.jpg","_id":4240525,"DOCID":"b5e0c363b19b2ffd0f92f4d75f6d1243","name":"Fairy Tale Dress","mallPrice":1628,"brand":"Stop Staring!","pageUrl":"http://www.6pm.com/stop-staring-fairy-tale-dress-pink","inStock":2,"defaultSkuid":"4dc95b2c5d481126666679091242ae1f","realPrice":497,"realPriceOrg":67.980003356934,"sellerName":"6pm","unit":"USD","stock":1},{"category":"女装>泳装","status":"2","coverImgUrl":"http://www.zappos.com/images/z/3/2/0/3/0/5/3203058-p-2x.jpg","_id":4587167,"DOCID":"f9be0e071d4685745e3d543ef14b8f06","name":"Azul Top","mallPrice":417,"brand":"Tart","pageUrl":"http://www.6pm.com/tart-azul-top-black-white-stripe","inStock":2,"defaultSkuid":"f8e3aa864b2cda8bab0540e34fb52e8f","realPrice":146,"realPriceOrg":19.979999542236,"sellerName":"6pm","unit":"USD","stock":1}]}}
     * tag_id : 1
     * created_time : 1481610779
     * owner : {"username":"ht3058","following_count":0,"post_count":11,"ucenter_token":"157a19c4e48c2d9.37724773","id":5954588,"membership_point":968,"is_operation":0,"sex":1,"nickname":"w0","like_count":9,"location":"","signature":"None","follower_count":4,"head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/12/15/7bcbaf1ef7771299950d2adaa4c7a843","user_title":[],"email":""}
     * is_hot : 1
     * name : 第一个草单1
     * is_visible : 0 可见 1 不可见
     */

    public String                     img_cover;
    public int                        easyopt_id;
    public int                        reply_count;
    public int                        likeit;
    public String                     content;
    public int                        like_count;
    public ArrayList<ProductBaseBean> products;
    public int                        tag_id;
    public UserBean                   owner;
    public String                     name;
    public int                        point_count;
    public int                        product_count;
    public int                        is_visible;
    public int                        pointit;

}

