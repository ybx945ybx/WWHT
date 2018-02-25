package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.entity.AddressItemBean;
import com.a55haitao.wwht.data.model.entity.AddressListBean;
import com.a55haitao.wwht.data.model.entity.CommonSuccessResult;
import com.a55haitao.wwht.data.model.entity.CouponListBean;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 地址信息
 *
 * @author 陶声
 * @since 2017-05-08
 */

public interface PassPortService {

    /**
     * 地址详情
     */
    @POST("m.api")
    Observable<ApiModel<AddressItemBean>> addressDetail(@Body Map<String, Object> body);

    /**
     * 删除地址
     */
    @POST("m.api")
    Observable<ApiModel<CommonSuccessResult>> deleteAddress(@Body Map<String, Object> body);

    /**
     * 获取地址列表
     */
    @POST("m.api")
    Observable<ApiModel<AddressListBean>> addressList(@Body Map<String, Object> body);

    /**
     * 优惠券列表
     */
    @POST("m.api")
    Observable<ApiModel<CouponListBean>> getCouponList(@Body Map<String, Object> body);

    /**
     * 领优惠券
     */
    @POST("m.api")
    Observable<ApiModel<Object>> receiveCoupon(@Body Map<String, Object> body);

    /**
     * 新增/修改地址
     */
    @POST("m.api")
    Observable<ApiModel<AddressItemBean>> addOrUpdateAddress(@Body Map<String, Object> body);

    /**
     * 获取默认收货地址
     */
    @POST("m.api")
    Observable<ApiModel<AddressItemBean>> getDefaultAddress(@Body Map<String, Object> body);

}
