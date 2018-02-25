package com.a55haitao.wwht.data.repository;

import com.a55haitao.wwht.data.interfaces.OrderModel;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.entity.CommonSuccessResult;
import com.a55haitao.wwht.data.model.entity.EnsureRecievedBean;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.data.model.entity.OrderDelResult;
import com.a55haitao.wwht.data.model.entity.OrderPrepareBean;
import com.a55haitao.wwht.data.model.entity.RefundItemsBean;
import com.a55haitao.wwht.data.model.result.CanRefundProductListResult;
import com.a55haitao.wwht.data.model.result.CancelReasonResult;
import com.a55haitao.wwht.data.model.result.OrderDetailResult;
import com.a55haitao.wwht.data.model.result.OrderListResult;
import com.a55haitao.wwht.data.model.result.OrderRefundListResult;
import com.a55haitao.wwht.data.model.result.RefundCommitResult;
import com.a55haitao.wwht.data.model.result.RefundDetailResult;
import com.a55haitao.wwht.data.model.result.TrackInfoResult;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.OrderService;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.ArrayList;
import java.util.TreeMap;

import rx.Observable;

/**
 * 订单仓库
 *
 * @author 陶声
 * @since 2016-10-14
 */

public class OrderRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "minishop_sns.OrderAPI/";
    private static final int    REQUEST_COUNT   = 20;

    private static volatile OrderRepository instance;
    private final           OrderService    mOrderService;

    private OrderRepository() {
        mOrderService = RetrofitFactory.createService(OrderService.class);
    }

    public static OrderRepository getInstance() {
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 订单列表
     *
     * @param type      类型
     * @param searchKey
     * @param time
     * @param page      页数
     */
    public Observable<OrderListResult> orderList(int type, String searchKey, String time, int page) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_list_V2");
        map.put("type", type);
        map.put("searchkey", searchKey);
        map.put("time", time);
        map.put("page", page);
        map.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderList(map));
    }

    /**
     * 订单详情
     *
     * @param orderId 订单号
     */
    public Observable<OrderDetailResult> orderDetail(String orderId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_detail_v3");
        map.put("order_id", orderId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderDetail(map));
    }

    /**
     * 取消订单
     *
     * @param orderId 订单号
     * @param reason  取消理由
     */
    public Observable<CommonSuccessResult> orderCancel(String orderId, String reason) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_cancel");
        map.put("order_id", orderId);
        map.put("reason", reason);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderCancel(map));
    }

    /**
     * 取消订单理由
     */
    public Observable<CancelReasonResult> cancelReasons() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "cancel_reasons");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.cancelReasons(map));
    }

    /**
     * 删除订单
     *
     * @param orderId 订单号
     */
    public Observable<OrderDelResult> orderDel(String orderId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_del");
        map.put("order_id", orderId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderDel(map));
    }

    /**
     * 确认收货
     *
     * @param orderId 订单号
     */
    public Observable<EnsureRecievedBean> orderConfirm(String orderId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_confirm");
        map.put("order_id", orderId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderConfirm(map));
    }

    /**
     * 物流信息
     *
     * @param orderId 订单号
     */
    public Observable<TrackInfoResult> getTrackInfo(String orderId, String trackNum) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "getTrackInfo");
        map.put("order_id", orderId);
        map.put("track_number", trackNum);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.getTrackInfo(map));
    }

    /**
     * 订单准备
     *
     * @param orderModel 订单实体
     */
    public Observable<OrderPrepareBean> orderPrepare(OrderModel orderModel) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_prepare");

        OrderModel.CartType type = orderModel.getType();
        map.put("carttype", type.getCarttype());
        map.put("cart_list", type.getCart_list());

        map.put("product_list", type.getProduct_list());

        map.put("spuid", type.getSpuid());
        map.put("skuid", type.getSkuid());
        map.put("count", type.getCount());

        //优惠券,优惠码,暂定为空
        map.put("coupon_code", orderModel.getCouponId());

        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderPrepare(map));
    }

    /**
     * 提交订单
     *
     * @param orderId 订单号
     * @param payWay  支付方式
     */
    public Observable<OrderCommitBean> orderCommit(String orderId, String payWay) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_commit_v2");
        map.put("order_id", orderId);
        //是否使用余额
        map.put("use_balance", "0");
        //订单ID
        map.put("order_id", orderId);
        //地址ID
        map.put("address_id", "");

        map.put("carttype", "");
        map.put("cart_list", "");

        map.put("product_list", "");

        map.put("spuid", "");
        map.put("skuid", "");
        map.put("count", "");

        //优惠券,优惠码,暂定为空
        map.put("coupon_code", "");
        //是否使用佣金
        map.put("is_commission", "0");
        map.put("pay_type", payWay);

        //用户备注 =
        map.put("ext", "");

        //冗余字段
        map.put("openid", "");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderCommit(map));
    }

    /**
     * 提交订单
     *
     * @param is_achat      0 同意 1 不同意
     * @param useCommission 是否使用佣金
     * @param payWay        支付方式
     * @param ext           备注
     * @param orderModel    订单实体
     */
    public Observable<OrderCommitBean> orderCommit(int is_achat, boolean useCommission, String payWay, String ext, OrderModel orderModel) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_commit_v2");

        //是否使用余额
        map.put("use_balance", "0");
        //是否同意is_achat
        map.put("is_achat", is_achat);
        //订单ID
        map.put("order_id", "");
        //地址ID
        map.put("address_id", String.valueOf(orderModel.getAddressBean().id));

        OrderModel.CartType type = orderModel.getType();
        map.put("carttype", type.getCarttype());
        map.put("cart_list", type.getCart_list());

        map.put("product_list", type.getProduct_list());

        map.put("spuid", type.getSpuid());
        map.put("skuid", type.getSkuid());

        map.put("count", type.getCount());

        //优惠券,优惠码,暂定为空
        map.put("coupon_code", orderModel.getCouponId());
        //是否使用佣金
        map.put("is_commission", useCommission ? "1" : "0");
        map.put("pay_type", payWay);

        //用户备注 =
        map.put("ext", ext);

        //冗余字段
        map.put("openid", "");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.orderCommit(map));
    }

    /**
     * 订单可退款商品列表
     *
     * @param orderId 订单号
     */
    public Observable<CanRefundProductListResult> getCanRefundProductList(String orderId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "can_refund_goods_list");
        map.put("order_id", orderId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.getCanRefundProductList(map));
    }

    /**
     * 申请退款
     *
     * @param orderId 订单号
     *                private String                     linkmanContacts;         // 联系人
     *                private String                     phone;                   // 联系人电话
     *                private String                     note;                    // 问题描述
     *                private String                     voucher;                 // 凭证base64字符串
     *                private ArrayList<ProductBaseBean> refundItems;             // 受理的商品
     */
    public Observable<RefundCommitResult> applyForRefund(String orderId, String linkmanContacts, String phone
            , String note, String refundItems, String voucher) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_refund");
        map.put("order_id", orderId);
        map.put("linkmanContacts", linkmanContacts);
        map.put("phone", phone);
        map.put("note", note);
        map.put("refundItems", refundItems);
        map.put("voucher", voucher);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.applyForRefund(map));
    }

    /**
     * 申请退款V1
     * @param orderId 订单号
     * note                    问题描述
     */
    public Observable<RefundCommitResult> applyForRefundV1(String orderId, String note) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_refund_V1");
        map.put("order_id", orderId);
        map.put("note", note);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.applyForRefundV1(map));
    }

    /**
     * 获取申请退款进度列表
     *
     * @param orderId 订单号
     */
    public Observable<OrderRefundListResult> getRefundList(String orderId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "refund_order_list");
        map.put("order_id", orderId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.getRefundList(map));
    }

    /**
     * 获取申请退款详情
     *
     * @param orderId 订单号
     */
    public Observable<RefundDetailResult> getRefundDetail(String orderId, String refundNo) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "order_refund_detail");
        map.put("order_id", orderId);
        map.put("refund_no", refundNo);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.getRefundDetail(map));
    }

    /**
     * 取消申请退款
     *
     * @param orderId 订单号
     */
    public Observable<OrderDelResult> refundCancle(String orderId, String refundNo) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "refund_cancel");
        map.put("order_id", orderId);
        map.put("refund_no", refundNo);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.refundCancle(map));
    }

    /**
     * 取消订单(申请退款)理由
     */
    public Observable<CancelReasonResult> refundReasons() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "refund_reson_list");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mOrderService.refundReasons(map));
    }

}
