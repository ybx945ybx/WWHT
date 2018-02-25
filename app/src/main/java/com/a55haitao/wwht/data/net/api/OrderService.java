package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.entity.CommonSuccessResult;
import com.a55haitao.wwht.data.model.entity.EnsureRecievedBean;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.data.model.entity.OrderDelResult;
import com.a55haitao.wwht.data.model.entity.OrderPrepareBean;
import com.a55haitao.wwht.data.model.result.CanRefundProductListResult;
import com.a55haitao.wwht.data.model.result.CancelReasonResult;
import com.a55haitao.wwht.data.model.result.OrderDetailResult;
import com.a55haitao.wwht.data.model.result.OrderListResult;
import com.a55haitao.wwht.data.model.result.OrderRefundListResult;
import com.a55haitao.wwht.data.model.result.RefundCommitResult;
import com.a55haitao.wwht.data.model.result.RefundDetailResult;
import com.a55haitao.wwht.data.model.result.TrackInfoResult;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 订单接口
 *
 * @author 陶声
 * @since 2016-10-14
 */

public interface OrderService {
    /**
     * 获取订单列表
     */
    @POST("m.api")
    Observable<ApiModel<OrderListResult>> orderList(@Body Map<String, Object> body);

    /**
     * 获取订单列表
     */
    @POST("m.api")
    Observable<ApiModel<OrderDetailResult>> orderDetail(@Body Map<String, Object> body);

    /**
     * 取消订单
     */
    @POST("m.api")
    Observable<ApiModel<CommonSuccessResult>> orderCancel(@Body Map<String, Object> body);

    /**
     * 取消订单理由
     */
    @POST("m.api")
    Observable<ApiModel<CancelReasonResult>> cancelReasons(@Body Map<String, Object> body);

    /**
     * 删除订单
     */
    @POST("m.api")
    Observable<ApiModel<OrderDelResult>> orderDel(@Body Map<String, Object> body);

    /**
     * 确认收货
     */
    @POST("m.api")
    Observable<ApiModel<EnsureRecievedBean>> orderConfirm(@Body Map<String, Object> body);

    /**
     * 提交订单
     */
    @POST("m.api")
    Observable<ApiModel<OrderCommitBean>> orderCommit(@Body Map<String, Object> body);

    /**
     * 物流信息
     */
    @POST("m.api")
    Observable<ApiModel<TrackInfoResult>> getTrackInfo(@Body Map<String, Object> body);

    /**
     * 订单准备
     */
    @POST("m.api")
    Observable<ApiModel<OrderPrepareBean>> orderPrepare(@Body Map<String, Object> body);

    /**
     * 可申请退款商品列表
     */
    @POST("m.api")
    Observable<ApiModel<CanRefundProductListResult>> getCanRefundProductList(@Body Map<String, Object> body);

    /**
     * 申请退款
     */
    @POST("m.api")
    Observable<ApiModel<RefundCommitResult>> applyForRefund(@Body Map<String, Object> body);

    /**
     * 申请退款V1
     */
    @POST("m.api")
    Observable<ApiModel<RefundCommitResult>> applyForRefundV1(@Body Map<String, Object> body);

    /**
     * 退款理由
     */
    @POST("m.api")
    Observable<ApiModel<CancelReasonResult>> refundReasons(@Body Map<String, Object> body);

    /**
     * 取消申请退款
     */
    @POST("m.api")
    Observable<ApiModel<OrderDelResult>> refundCancle(@Body Map<String, Object> body);

    /**
     * 获取申请退款单详情
     */
    @POST("m.api")
    Observable<ApiModel<RefundDetailResult>> getRefundDetail(@Body Map<String, Object> body);

    /**
     * 获取申请退款进度列表
     */
    @POST("m.api")
    Observable<ApiModel<OrderRefundListResult>> getRefundList(@Body Map<String, Object> body);

}
