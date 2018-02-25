package com.a55haitao.wwht.data.model.entity;

import android.databinding.BaseObservable;
import android.text.TextUtils;

/**
 * Created by Haotao_Fujie on 16/10/12.
 */

public class ProductBaseBean extends BaseObservable {
    public String name;          // 标题
    public float mallPrice;      // 商城价(单位:¥)
    public float realPrice;      // 现价
    public float mallPriceOrg;              // 商城价(外币单位)
    public float realPriceOrg;              // 现价

    public String coverImgUrl;    // 海报
    public String brand;          // 品牌
    public String country;        // 国家
    public String sellerName;     // 商户平台
    public float  discount;

    public String spuid;          // 商品ID   (H5页面的Url后缀)
    public String DOCID;          // == spuid

    public int stock;             //  inStock  stock   判断是否售光
    public int inStock;

    public SellerModel sellerInfo;
    public EasyOptSeller seller;

    // 收藏
    public boolean is_star;
    public int star_count;

    //编辑模式
    public boolean checked;

    // 活动缩略图
    public String small_icon;

    public long end_time;        //  限时抢购结束时间

    public String[] relateQuery;

    public String cover_img_url;

    public String promotionTag;  //  角标


    public String getSpuid() {
        if (spuid != null && !TextUtils.isEmpty(spuid)) {
            return spuid;
        }else if (DOCID != null && !TextUtils.isEmpty(DOCID)) {
            return DOCID;
        }
        return null;
    }

    public String getSellerName() {
        return this.sellerName + "官网发货";
    }

    public class SellerModel {
        public String namecn;
        public String nameen;
        public String country;                       // 国家
        public String flag;                          // 国旗
        public String logo;                          // 商家logo
        public String descriptioncn;
    }
}
