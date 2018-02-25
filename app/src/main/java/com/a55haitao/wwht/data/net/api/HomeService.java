package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.entity.ActivityBean;
import com.a55haitao.wwht.data.model.entity.TabBean;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.data.model.entity.TabFavorableBean;
import com.a55haitao.wwht.data.model.result.AdResult;
import com.a55haitao.wwht.data.model.result.DailyProductResult;
import com.a55haitao.wwht.data.model.result.FlashSaleResult;
import com.a55haitao.wwht.data.net.ApiModel;
import com.google.gson.JsonArray;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Home Interface
 *
 * @author 陶声
 * @since 2017-05-08
 */

public interface HomeService {
    /**
     * 专题底部推荐
     */
    @POST("m.api")
    Observable<ApiModel<TabFavorableBean>> getRelateFavorable(@Body Map<String, Object> body);

    /**
     * For hot, position 0 热卖单品 position 1 新品推荐
     */
    @POST("m.api")
    Observable<ApiModel<FlashSaleResult>> hotProductsV11(@Body Map<String, Object> body);

    /**
     * 开屏页广告图
     */
    @POST("m.api")
    Observable<ApiModel<AdResult>> indexAdvert(@Body Map<String, Object> body);

    /**
     * 首页Tab
     */
    @POST("m.api")
    Observable<ApiModel<TabBean>> homeTabs(@Body Map<String, Object> body);

    /**
     * 首页数据
     */
    @GET("comm/index/entry")
    Observable<ResponseBody> retrieveTabDataWithName(@Query("key") String key, @Query("user_id") int uid, @Query("version") String verion);

    /**
     * 获取首页的活动
     */
    @POST("m.api")
    Observable<ApiModel<ActivityBean>> getActivity(@Body Map<String, Object> body);

    /**
     * 官网特卖及精选合集页Tab
     */
    @POST("m.api")
    Observable<ApiModel<JsonArray>> Tabs(@Body Map<String, Object> body);

    /**
     * 获取精选合集列表数据
     */
    @POST("m.api")
    Observable<ApiModel<TabEntryBean>> getEntriesList(@Body Map<String, Object> body);

    /**
     * 获取官网特卖列表数据
     */
    @POST("m.api")
    Observable<ApiModel<TabFavorableBean>> getFavorablesList(@Body Map<String, Object> body);

    /**
     * 获取官网特卖列表数据
     */
    @POST("m.api")
    Observable<ApiModel<DailyProductResult>> getDailyProduct(@Body Map<String, Object> body);

}
