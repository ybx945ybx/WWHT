package com.a55haitao.wwht.data.repository;


import android.content.Context;

import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.annotation.PageType;
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
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.model.entity.SimilarBrandBean;
import com.a55haitao.wwht.data.model.entity.TranslateBean;
import com.a55haitao.wwht.data.model.result.AllBrandOrSellerBean;
import com.a55haitao.wwht.data.model.result.AllCategoryResult;
import com.a55haitao.wwht.data.model.result.GetFollowBrandStoreResult;
import com.a55haitao.wwht.data.model.result.GetUserStarProductsResult;
import com.a55haitao.wwht.data.model.result.RelateSiteResult;
import com.a55haitao.wwht.data.model.result.RelateSpecialsResult;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.ProductService;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.ArrayList;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Haotao_Fujie on 16/10/12.
 */

public class ProductRepository extends BaseRepository {

    private static final String BASE_METHOD_URL = "55haitao_sns.ProductAPI/";
    private static final int    REQUEST_COUNT   = 20;

    private static volatile ProductRepository instance;

    private ProductService  mProductService;

    public static ProductRepository getInstance() {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository();
                }
            }
        }
        return instance;
    }

    private ProductRepository() {
        mProductService = RetrofitFactory.createService(ProductService.class);
    }

    /**
     * 获取商详
     *
     * @param spuid
     * @return
     */
    public Observable<ProductDetailBean> getProductDetail(String spuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_detail");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveProductDetail(paramMap));
    }

    public Observable<ResponseBody> getProductDetail(String spuid, boolean needSystime) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_detail");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return createObservable(paramMap);
    }

    public Observable<ProductDetailBean> getProductDetail(String spuid, Context context, boolean needSystemTime) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_detail");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveProductDetail(paramMap), context, needSystemTime);
    }

    /**
     * 获取优惠券
     *
     * @param spuid
     * @return
     */
    public Observable<GrantCouponsBean> getGrantCoupons(String spuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", ApiUrl.MT_PRODUCT_GRANTCOUPON);
        paramMap.put("product_id", spuid);
        paramMap.put("store_id", "");
        paramMap.put("brand_id", "");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveGrantCoupon(paramMap));
    }

    public Observable<ArrayList<ProductBaseBean>> getRecommendProduct(String spuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_recommand");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveRecommendProduct(paramMap));
    }

    public Observable<SimilarBrandBean> getSimilarBrandBean(String spuid, String nameen) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_related_brand_v11");
        paramMap.put("brand", nameen);
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveSimilarBrand(paramMap));
    }

    /**
     * 获取相关笔记和专题
     * @param spuid
     * @return
     */
    public Observable<RelateSpecialsResult> getRelateSpecial(String spuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_relate_specials");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveRelateSpecial(paramMap));
    }

    public Observable<ProductSearchBean> getSearchProduct(String spuid, ProductDetailBean.BrandBaseDesModel brandInfo) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_search");
        paramMap.put("sortDiscount", "");
        paramMap.put("reqType", 0);
        paramMap.put("enableGroup", "0");
        paramMap.put("sortPrice", "");
        paramMap.put("page", 1);
        paramMap.put("count", 20);
        if (brandInfo.query != null) {
            paramMap.put("query", brandInfo.query.query == null ? "" : brandInfo.query.query);
            paramMap.put("category", brandInfo.query.category == null ? "" : brandInfo.query.category);
            paramMap.put("seller", brandInfo.query.seller == null ? "" : brandInfo.query.seller);
            paramMap.put("brand", brandInfo.query.brand == null ? "" : brandInfo.query.brand);
        } else {
            paramMap.put("query", "");
            paramMap.put("seller", "");
            paramMap.put("category", "");
            paramMap.put("brand", brandInfo.getBrandName());
        }
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retriveSearchProduct(paramMap));
    }

    /**
     * 核价
     *
     * @param spuid
     * @return
     */
    public Observable<ProductDetailBean> getProductRT(String spuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_RT");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retriveProductRT(paramMap));
    }

    /**
     * 默默核价
     *
     * @param defaultSkuid
     * @return
     */
    public Observable<Object> getProductAsyncCheck(String defaultSkuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_asyncCheck");
        paramMap.put("defaultSkuid", defaultSkuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.productAsyncCheck(paramMap));
    }

    /**
     * 一键翻译
     *
     * @param txt 文本
     */
    public Observable<TranslateBean> translateTxt(String txt, int pageId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", ApiUrl.MT_PRODUCT_TRANSLATE);
        paramMap.put("text", txt);
        paramMap.put("id", pageId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveTranslateResult(paramMap));
    }

    /**
     * 获取购物车数量
     */
    public Observable<ShoppingCartCntBean> getShoppingCartListCnt() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", ApiUrl.MT_GET_SHOPPINGCART_LIST_CNT);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mProductService.getShoppingCartListCnt(paramMap));
    }

    /**
     * 商品下架时获取大家都在买
     */
    public Observable<ProductRecommendBean> productRecom(String title) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "product_recom");
        paramMap.put("query", title);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.retrieveProductRecom(paramMap));
    }

    /**
     * 加入心愿单
     *
     * @param spuid
     * @return
     */
    public Observable<LikeProductBean> likeProduct(String spuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "star_product_v11");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mProductService.likeProduct(paramMap));
    }

    /**
     * 获取关注的商家 or 品牌
     *
     * @param type 类型 商家 or 品牌
     * @param page 页数
     */
    public Observable<GetFollowBrandStoreResult> getFollowBrandStore(@BrandSellerFragmentType int type, int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        switch (type) {
            case BrandSellerFragmentType.BRAND:
                paramMap.put("_mt", BASE_METHOD_URL + "get_follow_brand_v11");
                break;
            case BrandSellerFragmentType.SELLER:
                paramMap.put("_mt", BASE_METHOD_URL + "get_follow_seller_v11");
                break;
        }
        paramMap.put("page", page);
        paramMap.put("count", 20);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getFollowBrandStore(paramMap));
    }

    /**
     * 获取他人关注的商家 or 品牌
     *
     * @param type 类型 商家 or 品牌
     * @param page 页数
     */
    public Observable<GetFollowBrandStoreResult> getFollowBrandStore(@BrandSellerFragmentType int type, int page, int userId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        switch (type) {
            case BrandSellerFragmentType.BRAND:
                paramMap.put("_mt", BASE_METHOD_URL + "get_follow_brand_v11");
                break;
            case BrandSellerFragmentType.SELLER:
                paramMap.put("_mt", BASE_METHOD_URL + "get_follow_seller_v11");
                break;
        }
        paramMap.put("page", page);
        paramMap.put("count", 20);
        paramMap.put("user_id", userId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getFollowBrandStore(paramMap));
    }

    /**
     * 个人中心 - 心愿单
     *
     * @param page 页数
     */
    public Observable<GetUserStarProductsResult> getUserStarProducts(int page) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("page", page);
        paramMap.put("count", 20);
        paramMap.put("_mt", BASE_METHOD_URL + "get_user_star_products_v11");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getUserStarProducts(paramMap));
    }

    /**
     * 关注商家
     *
     * @param type 类型 品牌/商家
     * @param name 品牌/商家名
     */
    public Observable<CommonDataBean> followBrandSeller(@PageType int type, String name) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        if (PageType.BRAND == type) {
            paramMap.put("_mt", BASE_METHOD_URL + "follow_brand_v11");
            paramMap.put("brand_name", name);
        } else {
            paramMap.put("_mt", BASE_METHOD_URL + "follow_seller_v11");
            paramMap.put("seller_name", name);
        }
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mProductService.followBrandSeller(paramMap));
    }

    /**
     * 全部品牌/商家列表
     *
     * @param type 类型 品牌/商家
     */
    public Observable<ArrayList<AllBrandOrSellerBean>> getAllBrandSeller(@PageType int type) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        if (PageType.BRAND == type) {
            paramMap.put("_mt", BASE_METHOD_URL + "get_whole_brands_v11");
        } else if (PageType.SELLER == type) {
            paramMap.put("_mt", BASE_METHOD_URL + "get_whole_sellers_v11");
        }
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getAllBrandSeller(paramMap));
    }

    /**
     * 获取全部分类
     */
    public Observable<ArrayList<AllCategoryResult>> getAllCategories() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_whole_categories_v11");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getAllCategories(paramMap));
    }

    /**
     * 获取分类页面数据
     */
    public Observable<CategoryPageBean> getHotHome() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_hot_home_v11");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getHotHome(paramMap));
    }

    /**
     * 获取相关商家/商家
     *
     * @param type 类型
     * @param name 商家/品牌名
     */
    public Observable<RelateSiteResult> getRelateBrandSeller(@PageType int type, String name) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        if (PageType.BRAND == type) {
            paramMap.put("_mt", BASE_METHOD_URL + "get_related_brand_v11");
            paramMap.put("brand", name);
        } else {
            paramMap.put("_mt", BASE_METHOD_URL + "get_related_seller_v11");
            paramMap.put("seller", name);
        }
        paramMap.put("page", 1);
        paramMap.put("count", REQUEST_COUNT / 2);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getRelateBrandSeller(paramMap));
    }

    /**
     * 推荐商品
     */
    public Observable<SearchResultBean> getRecommandProduct() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_recommend_product_v11");
        paramMap.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getRecommandProduct(paramMap));
    }

    /**
     * 品牌/商家信息
     */
    public Observable<SellerBean.SiteDescBaseBean> getBrandSellerInfo(@PageType int type, String name) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        if (PageType.BRAND == type) {
            paramMap.put("_mt", BASE_METHOD_URL + "get_brand_info_v11");
            paramMap.put("brand", name);
        } else {
            paramMap.put("_mt", BASE_METHOD_URL + "get_seller_info_v11");
            paramMap.put("seller", name);
        }
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getBrandSellerInfo(paramMap));
    }

    /**
     * 获取关联词
     */
    public Observable<ArrayList<String[]>> getAutoFill(String word) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_autofill_v2");
        paramMap.put("word", word);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getAutoFill(paramMap));
    }

    /**
     * 热门搜索词
     */
    public Observable<HotWordsBean> getHotSearchWords() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "hotSearchWords");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.getHotSearchWords(paramMap));
    }

    /**
     * 搜索上报
     */
    public Observable<Object> searchLogServer(String query, int page, String category, String brand, String seller, int resNum) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "searchlogserver");
        paramMap.put("query", "hotSearchWords");
        paramMap.put("page", "hotSearchWords");
        paramMap.put("category", "hotSearchWords");
        paramMap.put("brand", "hotSearchWords");
        paramMap.put("seller", "hotSearchWords");
        paramMap.put("resNum", "hotSearchWords");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.searchLogServer(paramMap));
    }

    /**
     * 搜索结果页点击商品上报
     * "pagenum":1, // 当前的翻页情况
     * "num":1, // 该商品为当前页面的第num个商品
     */
    public Observable<Object> clickLogServer(String query, String spuid, int pagenum, int num) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "clicklogserver");
        paramMap.put("query", query);
        paramMap.put("spuid", spuid);
        paramMap.put("pagenum", pagenum);
        paramMap.put("num", num);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.clickLogServer(paramMap));
    }

    /**
     * 加入购物车或直接购买动作上报
     */
    public Observable<Object> cartLogs(String data) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "cartlogs");
        paramMap.put("data", data);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.cartLogs(paramMap));
    }

    /**
     * 提交订单动作上报
     */
    public Observable<Object> orderLogs(String data) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "orderlogs");
        paramMap.put("data", data);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mProductService.orderLogs(paramMap));
    }

    /**
     * 批量删除心愿单55haitao_sns.ProductAPI/cancel_star_product
     */
    public Observable<Object> cancelStarProds(String spuids) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "cancel_star_product");
        paramMap.put("spuids", spuids);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mProductService.cancelStarProds(paramMap));
    }

}
