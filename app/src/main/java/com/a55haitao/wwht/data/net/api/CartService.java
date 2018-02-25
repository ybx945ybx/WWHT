package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 购物车相关接口
 *
 * @author 陶声
 * @since 2017-05-08
 */

public interface CartService {
    /**
     * 添加购物车
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> addCart(@Body Map<String, Object> body);

    /**
     * 购物车列表
     */
    @POST("m.api")
    Observable<ApiModel<ShoppingCartBean>> cartList(@Body Map<String, Object> body);

    /**
     * 删除购物车
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> delCart(@Body Map<String, Object> body);

    /**
     * 荆老师数据统计
     */
    @POST("m.api")
    Observable<ApiModel<Object>> activityAnalys(@Body Map<String, Object> body);
}
