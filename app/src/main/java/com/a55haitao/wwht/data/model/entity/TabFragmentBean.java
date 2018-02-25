package com.a55haitao.wwht.data.model.entity;

import com.a55haitao.wwht.data.model.entity.HotAdBean;
import com.a55haitao.wwht.data.model.entity.MallStatementBean;
import com.a55haitao.wwht.data.model.entity.TabBannerBean;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.data.model.entity.TabFavorableBean;
import com.a55haitao.wwht.data.model.entity.CategoryBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.SellerBean;

import java.util.ArrayList;

/**
 * Created by 董猛 on 2016/10/11.
 */

/**
 * 首页Fragment数据集合
 */
public class TabFragmentBean {

    /**
     * 公用部分
     */
    public ArrayList<TabBannerBean> mBannersData;                  //Banner

    public ArrayList<ProductBaseBean> mProductsHotData;                //热卖单品
    public ArrayList<ProductBaseBean> mProductsTabData;                //今日推荐

    public ArrayList<TabFavorableBean.SpecialsBean> mFavorablesData;  //官网特卖
    public ArrayList<TabEntryBean.EntriesBean> mEntryData;            //潮流风尚

    /**
     * Tab 独有
     */
    public ArrayList<SellerBean.SellerBaseBean> mTabBrandsData ;          //人气品牌
    public ArrayList<CategoryBean> mTabCategoryData ;                     //热门分类
    /**
     * Hot 独有
     */
    public ArrayList<ProductBaseBean> mHotProductsRecomData ;             //新品推荐
    public ArrayList<HotAdBean> mHotAddsDats ;                            //广告位
    public ArrayList<MallStatementBean> mStatementData;                   //Statemnet

}
