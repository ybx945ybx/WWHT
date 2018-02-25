package com.a55haitao.wwht.data.model.entity;

import com.a55haitao.wwht.data.model.entity.GrantCouponsBean;
import com.a55haitao.wwht.data.model.entity.ProductDetailBean;
import com.a55haitao.wwht.data.model.entity.ProductSearchBean;
import com.a55haitao.wwht.data.model.entity.SimilarBrandBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Haotao_Fujie on 16/10/13.
 */

public class PageProductBean implements Serializable {

    // 商品详情
    public ProductDetailBean mProductDetailData;
    // 优惠券
    public ArrayList<GrantCouponsBean.GrantCoupon> mCouponData;
    // 大家都在买
    public ArrayList<ProductBaseBean> mRecommendProductData;
    // 相似品牌
    public ArrayList<SimilarBrandBean.SimilarBrands> mSimilarBrandsData;
    // 搜索品牌商品
    public ProductSearchBean mBrandsProuct;

}
