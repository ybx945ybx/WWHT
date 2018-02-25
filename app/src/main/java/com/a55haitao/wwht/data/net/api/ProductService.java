package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.entity.CategoryPageBean;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.GrantCouponsBean;
import com.a55haitao.wwht.data.model.entity.HotWordsBean;
import com.a55haitao.wwht.data.model.entity.LikeProductBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.ProductDetailBean;
import com.a55haitao.wwht.data.model.entity.ProductRecommendBean;
import com.a55haitao.wwht.data.model.entity.ProductSearchBean;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.data.model.entity.SellerBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.model.entity.SimilarBrandBean;
import com.a55haitao.wwht.data.model.entity.TranslateBean;
import com.a55haitao.wwht.data.model.result.AllBrandOrSellerBean;
import com.a55haitao.wwht.data.model.result.AllCategoryResult;
import com.a55haitao.wwht.data.model.result.GetFollowBrandStoreResult;
import com.a55haitao.wwht.data.model.result.GetUserStarProductsResult;
import com.a55haitao.wwht.data.model.result.RelateSiteResult;
import com.a55haitao.wwht.data.model.result.RelateSpecialsResult;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-10-12
 */

public interface ProductService {

    // Rx Style
    @POST("m.api")
    Observable<ApiModel<ProductDetailBean>> retrieveProductDetail(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<GrantCouponsBean>> retrieveGrantCoupon(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<ArrayList<ProductBaseBean>>> retrieveRecommendProduct(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<ProductRecommendBean>> retrieveProductRecom(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<SimilarBrandBean>> retrieveSimilarBrand(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<RelateSpecialsResult>> retrieveRelateSpecial(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<TranslateBean>> retrieveTranslateResult(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<LikeProductBean>> likeProduct(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<ProductSearchBean>> retriveSearchProduct(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<ProductDetailBean>> retriveProductRT(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<Object>> productAsyncCheck(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel> getCoupon(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> putShoppingCartList(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<ShoppingCartBean>> getShoppingCartList(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> removeShoppingCartList(@Body Map<String, Object> body);

    @POST("m.api")
    Observable<ApiModel<ShoppingCartCntBean>> getShoppingCartListCnt(@Body Map<String, Object> body);

    /**
     * 个人中心 - 心愿单
     */
    @POST("m.api")
    Observable<ApiModel<GetUserStarProductsResult>> getUserStarProducts(@Body Map<String, Object> body);

    /**
     * 关注的商家 or 品牌
     */
    @POST("m.api")
    Observable<ApiModel<GetFollowBrandStoreResult>> getFollowBrandStore(@Body Map<String, Object> body);

    /**
     * 关注品牌
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> followBrandV11(@Body Map<String, Object> body);

    /**
     * 关注商家
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> followSellerV11(@Body Map<String, Object> body);

    /**
     * 关注商家/商家
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> followBrandSeller(@Body Map<String, Object> body);

    /**
     * 全部品牌/商家列表
     */
    @POST("m.api")
    Observable<ApiModel<ArrayList<AllBrandOrSellerBean>>> getAllBrandSeller(@Body Map<String, Object> body);

    /**
     * 全部分类
     */
    @POST("m.api")
    Observable<ApiModel<ArrayList<AllCategoryResult>>> getAllCategories(@Body Map<String, Object> body);

    /**
     * 获取分类页面数据
     */
    @POST("m.api")
    Observable<ApiModel<CategoryPageBean>> getHotHome(@Body Map<String, Object> body);

    /**
     * 获取相关商家/商家
     */
    @POST("m.api")
    Observable<ApiModel<RelateSiteResult>> getRelateBrandSeller(@Body Map<String, Object> body);

    /**
     * 推荐商品
     */
    @POST("m.api")
    Observable<ApiModel<SearchResultBean>> getRecommandProduct(@Body Map<String, Object> body);

    /**
     * 品牌/商家信息
     */
    @POST("m.api")
    Observable<ApiModel<SellerBean.SiteDescBaseBean>> getBrandSellerInfo(@Body Map<String, Object> body);

    /**
     * 获取关联词
     */
    @POST("m.api")
    Observable<ApiModel<ArrayList<String[]>>> getAutoFill(@Body Map<String, Object> body);

    /**
     * 搜索商品
     */
    @POST("m.api")
    Observable<ApiModel<SearchResultBean>> productSearch(@Body Map<String, Object> body);

    /**
     * 热门搜索词
     */
    @POST("m.api")
    Observable<ApiModel<HotWordsBean>> getHotSearchWords(@Body Map<String, Object> body);

    /**
     * 搜索打点上报
     */
    @POST("m.api")
    Observable<ApiModel<Object>> searchLogServer(@Body Map<String, Object> body);

    /**
     * 搜索结果页点击商品打点上报
     */
    @POST("m.api")
    Observable<ApiModel<Object>> clickLogServer(@Body Map<String, Object> body);

    /**
     * 加入购物车或直接购买打点上报
     */
    @POST("m.api")
    Observable<ApiModel<Object>> cartLogs(@Body Map<String, Object> body);

    /**
     * 提交订单打点上报
     */
    @POST("m.api")
    Observable<ApiModel<Object>> orderLogs(@Body Map<String, Object> body);

    /**
     * 批量删除心愿单
     */
    @POST("m.api")
    Observable<ApiModel<Object>> cancelStarProds(@Body Map<String, Object> body);
}
