package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Ads;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Banner;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Category;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Collect_Specials;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Daily_Product;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Easy;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.FlashSale;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Flavor_Hot_Foot_All;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Foot;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.ImageTitle;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Image_Entry;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Image_Flavor;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Image_Flavor_Hot;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Image_Flavor_Twice_Hot;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Invalid;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.MallStatement;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.NewsFlash;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Product_Twice;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Recycler_Brand;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Recycler_Product_200;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.Recycler_Product_280;
import static com.a55haitao.wwht.data.model.annotation.FirstPageViewType.SubTitle;

/**
 * Created by 董猛 on 2016/10/12.
 */

@IntDef({Invalid, Banner, Image_Flavor_Hot, Image_Flavor_Twice_Hot, SubTitle, Recycler_Brand, Recycler_Product_280, Recycler_Product_200, Image_Flavor, Image_Entry, Product_Twice, Ads, Category, MallStatement, ImageTitle, FlashSale, Easy, NewsFlash, Foot, Flavor_Hot_Foot_All
,Daily_Product,Collect_Specials})
@Retention(RetentionPolicy.SOURCE)

public @interface FirstPageViewType {
    int Invalid                = -1;
    int Banner                 = 0;          // banner
    int SubTitle               = 1;        // 标题
    int Recycler_Brand         = 2;        // 人气品牌
    int Flavor_Hot_Foot_All    = 3;      // 首页官网特卖底部查看全部
    int Recycler_Product_280   = 4;
    int Recycler_Product_200   = 7;      // 精选合集商品横向列表
    int Image_Flavor           = 5;
    int Image_Entry            = 6;
    int Product_Twice          = 8;
    int Ads                    = 9;
    int Category               = 10;
    int MallStatement          = 11;
    int ImageTitle             = 12;
    int Image_Flavor_Twice     = 13;
    int FlashSale              = 14;
    int Easy                   = 15;
    int NewsFlash              = 16;
    int Image_Flavor_Hot       = 19;
    int Image_Flavor_Twice_Hot = 20;
    int Foot                   = 21;
    int Daily_Product          = 22;
    int Collect_Specials         = 23;

}
