package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.event.OrderChangeEvent;
import com.a55haitao.wwht.data.interfaces.OrderPayModel;
import com.a55haitao.wwht.data.model.annotation.OrderType;
import com.a55haitao.wwht.data.model.entity.CommonSuccessResult;
import com.a55haitao.wwht.data.model.entity.EnsureRecievedBean;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.data.model.entity.OrderDelResult;
import com.a55haitao.wwht.data.model.result.CancelReasonResult;
import com.a55haitao.wwht.data.model.result.OrderDetailResult;
import com.a55haitao.wwht.data.model.result.RefundCommitResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ShippingFeePopuWindow;
import com.a55haitao.wwht.ui.view.TariffPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.OrderDetailHelper;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.wxapi.WXPayEntryActivity;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 订单详情页
 */
public class OrderDetailActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)                    DynamicHeaderView  mTitle;                 // 标题
    @BindView(R.id.tv_order_create_time)     TextView           mTvOrderCreateTime;     // 创建时间
    @BindView(R.id.tv_order_number)          TextView           mTvOrderNumber;         // 订单编号
    @BindView(R.id.tv_address)               TextView           mTvAddress;             // 收货地址
    @BindView(R.id.aliPayCheckBox)           CheckBox           mAliPayCheckBox;
    @BindView(R.id.aliPayLayout)             RelativeLayout     mAliPayLayout;
    @BindView(R.id.wxPayCheckBox)            CheckBox           mWxPayCheckBox;
    @BindView(R.id.wxPayLayout)              RelativeLayout     mWxPayLayout;
    @BindView(R.id.msView)                   MultipleStatusView mSv;
    @BindView(R.id.tv_product_price)         TextView           mTvProductPrice;        // 商品金额
    @BindView(R.id.tv_total_price)           TextView           mTvTotalPrice;          // 总计金额
    @BindView(R.id.tv_tax_fee)               TextView           mTvTaxFee;              // 关税
    @BindView(R.id.tv_consumption_tax)       TextView           mTvConsumptionTax;      // 海外消费税
    @BindView(R.id.ll_pay)                   LinearLayout       mLlPay;                 // 支付
    @BindView(R.id.toPayBtn)                 HaiTextView        mToPayBtn;              // 去付款
    @BindView(R.id.goodsRootLayout)          LinearLayout       mGoodsRootLayout;       // 商品显示的根布局
    @BindView(R.id.tv_promote)               TextView           promoteTxt;             // 优惠金额
    @BindView(R.id.tv_commission)            TextView           commissionTxt;          // 佣金抵扣
    @BindView(R.id.tv_head_info)             HaiTextView        mTvHeadInfo;            // 温馨提示
    @BindView(R.id.tv_copy)                  HaiTextView        mTvCopy;                // 复制订单号
    @BindView(R.id.ll_order_pay_time)        LinearLayout       mLlOrderPayTime;        // 支付时间容器
    @BindView(R.id.tv_order_pay_time)        HaiTextView        mTvOrderPayTime;        // 支付时间
    @BindView(R.id.tv_order_status)          HaiTextView        mTvOrderStatus;         // 订单状态
    @BindView(R.id.tv_transfer_info)         HaiTextView        mTvTransferInfo;        // 物流信息
    @BindView(R.id.tv_transfer_info_time)    HaiTextView        mTvTransferInfoTime;    // 物流信息时间
    @BindView(R.id.ll_transfer_info)         RelativeLayout     mLlTransferInfo;        // 物流信息容器
    @BindView(R.id.tv_remark)                HaiTextView        mTvRemark;              // 备注
    @BindView(R.id.rl_remark)                RelativeLayout     mRlRemark;              // 备注容器
    @BindView(R.id.tv_receive_time)          HaiTextView        mTvReceiveTime;         // 确认收货时间
    @BindView(R.id.ll_receive_time)          LinearLayout       mLlReceiveTime;         // 确认收货时间容器
    @BindView(R.id.tv_shipping_fee_store)    HaiTextView        mTvShippingFeeStore;    // 官网运费
    @BindView(R.id.tv_shipping_fee_estimate) HaiTextView        mTvShippingFeeEstimate; // 预估国际运费
    @BindView(R.id.ll_order_action)          LinearLayout       mLlOrderAction;         // 订单操作
    @BindView(R.id.tv_get_transfer_info)     HaiTextView        mTvGetTransferInfo;     // 查看物流
    @BindView(R.id.tv_confirm_received)      HaiTextView        mTvOrderReceived;       // 确认收货
    @BindView(R.id.tv_delete_order)          HaiTextView        mTvOrderDelete;         // 删除订单
    @BindView(R.id.tv_cancel_order)          HaiTextView        mTvOrderCancel;         // 取消订单
    //    @BindView(R.id.tv_back_money)            HaiTextView        tvRefund;               // 申请退款
    @BindView(R.id.tv_cancle_buy)            HaiTextView        tvCancleBuy;               // 取消订单（付款后抢单前）
    @BindView(R.id.rl_consumption_tax)       RelativeLayout     mRlConsumptionTax;      // 消费税
    @BindView(R.id.rl_promote)               RelativeLayout     mRlPromote;             // 优惠
    @BindView(R.id.rl_activity)              RelativeLayout     rlytActivity;             // 活动优惠
    @BindView(R.id.tv_activity)              HaiTextView        tvActivity;             // 活动优惠金额
    @BindView(R.id.rl_commission)            RelativeLayout     mRlCommission;          // 佣金
    @BindView(R.id.tv_user_info)             HaiTextView        mTvUserInfo;            // 用户信息

    @BindString(R.string.key_order_id)     String KEY_ORDER_ID;
    @BindString(R.string.key_order_status) String KEY_ORDER_STATUS;

    @BindColor(R.color.colorRedEE1741)  int mColorRed;
    @BindColor(R.color.order_orange)    int mColorOrange;
    @BindColor(R.color.colorGray999999) int mColorGrey999;
    @BindColor(R.color.colorGray333333) int mColorGrey333;
    @BindColor(R.color.colorGray666666) int mColorGrey666;

    @BindDrawable(R.drawable.border_bottom) Drawable orderStatusBorder;

    private String                                         mOrderId;            // 订单号
    private OrderPayModel                                  mOrderPayModel;      //订单信息封装类
    private Tracker                                        mTracker;            // GA Tracker
    private OptionsPickerView                              mPickerView;
    private OptionsPickerView                              mRefundPickerView;
    private ArrayList<String>                              mOptions;
    private ArrayList<String>                              mRefundResons;
    private AlertDialog                                    mDlgDeleteOrder;
    private CountDownTimer                                 mCountDownTimer;
    private OrderDetailResult.OrderDetailBean.TariffNotice mTariffNotice;       // 关税提示
    private ShippingFeePopuWindow                          mShippingFeePopuWindow;
    private TariffPopuWindow                               mTariffPopuWindow;
    private ArrayList<String>                              skulist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVariables();
        initViews();
        loadData();
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        skulist = new ArrayList<>();
        mOrderPayModel = new OrderPayModel();

        Intent intent = getIntent();
        if (intent != null) {
            mOrderId = intent.getStringExtra(KEY_ORDER_ID);
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("订单详情");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /**
     * 初始化UI
     */
    public void initViews() {
        mSv.setOnRetryClickListener(v -> loadData());
        mWxPayCheckBox.setChecked(true);
        mWxPayCheckBox.setEnabled(false);
        mTitle.setHeadClickListener(() -> {
            // GA 事件
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("联系客服")
                    .setAction("在线客服")
                    .setLabel("订单详情")
                    .build());
            NtalkerUtils.contactNtalker(mActivity);
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        mSv.showLoading();
        getOrderDetail();

    }

    private void getOrderDetail() {
        OrderRepository.getInstance()
                .orderDetail(mOrderId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<OrderDetailResult>() {
                    @Override
                    public void onSuccess(OrderDetailResult orderDetailResult) {
                        mSv.showContent();
                        // 关税提示
                        if (orderDetailResult.order_detail.tariffNotice != null) {
                            mTariffNotice = orderDetailResult.order_detail.tariffNotice;
                        }
                        initskulist(orderDetailResult.order_detail);
                        setOrderDetailView(orderDetailResult.order_detail);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }
                });
    }

    /**
     * 根据订单状态改变UI
     */
    private void switchView(OrderDetailResult.OrderDetailBean order) {
        String status = order.order_status;
        // 温馨提示
        mTvHeadInfo.setVisibility(TextUtils.equals(OrderType.NEW, status)
                || TextUtils.equals(OrderType.OK, status)
                || TextUtils.equals(OrderType.FAIL, status)
                ? View.GONE : View.VISIBLE);
        // 支付时间
        mLlOrderPayTime.setVisibility(TextUtils.equals(OrderType.NEW, status)
                ? View.GONE : View.VISIBLE);
        // 确认收货时间
        mLlReceiveTime.setVisibility(OrderType.OK.equals(status)
                ? View.VISIBLE : View.GONE);
        // 物流信息
        mLlTransferInfo.setVisibility(TextUtils.equals(OrderType.SENT1, status)
                || TextUtils.equals(OrderType.SENT2, status)
                || TextUtils.equals(OrderType.ARRIVED, status)
                || TextUtils.equals(OrderType.OK, status)
                ? View.VISIBLE : View.GONE);
        // 支付选项
        mLlPay.setVisibility(TextUtils.equals(OrderType.NEW, status)
                ? View.VISIBLE : View.GONE);
        // 是否显示订单操作栏
        //        if (TextUtils.equals(OrderType.PAIED, status) || TextUtils.equals(OrderType.ROB, status) || TextUtils.equals(OrderType.BOUGHT, status)) {
        //            //            mLlOrderAction.setVisibility(View.GONE);
        //            mLlOrderAction.setVisibility(View.VISIBLE);
        ////            tvRefund.setVisibility(View.VISIBLE);
        //
        //        } else {
        // 申请退款按钮
        //            tvRefund.setVisibility(TextUtils.equals(OrderType.NEW, status) || TextUtils.equals(OrderType.FAIL, status) ? View.GONE : View.VISIBLE);
        // 立即支付
        mToPayBtn.setVisibility(TextUtils.equals(OrderType.NEW, status) ? View.VISIBLE : View.GONE);
        // 取消订单
        mTvOrderCancel.setVisibility(TextUtils.equals(OrderType.NEW, status) ? View.VISIBLE : View.GONE);
        // 查看物流
        mTvGetTransferInfo.setVisibility(TextUtils.equals(OrderType.SENT1, status)
                || TextUtils.equals(OrderType.SENT2, status)
                || TextUtils.equals(OrderType.ARRIVED, status)
                || TextUtils.equals(OrderType.OK, status)
                ? View.VISIBLE : View.GONE);
        // 确认收货
        mTvOrderReceived.setVisibility(TextUtils.equals(OrderType.SENT1, status)
                || TextUtils.equals(OrderType.SENT2, status)
                || TextUtils.equals(OrderType.ARRIVED, status)
                ? View.VISIBLE : View.GONE);
        // 删除订单
        mTvOrderDelete.setVisibility(TextUtils.equals(OrderType.OK, status) || TextUtils.equals(OrderType.FAIL, status)
                ? View.VISIBLE : View.GONE);
        // 取消订单(付款后抢单前)
        tvCancleBuy.setVisibility(TextUtils.equals(OrderType.PAIED, status) && !order.order_refund_status ? View.VISIBLE : View.GONE);

        boolean hasVisible = false;
        for (int i = 0; i < mLlOrderAction.getChildCount(); i++) {
            if (mLlOrderAction.getChildAt(i).getVisibility() == View.VISIBLE) {
                hasVisible = true;
                break;
            }
        }
        mLlOrderAction.setVisibility(hasVisible ? View.VISIBLE : View.GONE);

        //        }
    }

    /**
     * 填充数据
     */
    private void setOrderDetailView(OrderDetailResult.OrderDetailBean order) {
        // 切换布局
        //        tvRefund.setSelected(order.order_refund_bnt);
        switchView(order);
        mOrderPayModel.setOrderId(mOrderId);
        // 温馨提示
        if (mTvHeadInfo.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(order.order_detail_note)) {
            SpannableString spanString = new SpannableString("注意：" + order.order_detail_note);
            Drawable        d          = ContextCompat.getDrawable(mActivity, R.mipmap.ic_info_orange);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvHeadInfo.setText(spanString);
        }
        // 订单号
        String order_id = order.order_id;
        mTvOrderNumber.setText(order_id);
        mOrderPayModel.setOrderId(order_id);
        // 订单创建时间
        mTvOrderCreateTime.setText(HaiTimeUtils.showDetailWSecTime(order.ctime));
        // 支付时间
        if (mLlOrderPayTime.getVisibility() == View.VISIBLE
                && !TextUtils.isEmpty(order.paied_source)
                && order.paied_time != 0) {
            String paySource = "";
            switch (order.paied_source) {
                case "alipay":
                    paySource = "支付宝支付";
                    break;
                case "wechatpay":
                    paySource = "微信支付";
                    break;
                case "commission":
                    paySource = "佣金支付";
                    break;
            }
            mTvOrderPayTime.setText(HaiTimeUtils.showDetailWSecTime(order.paied_time) + "  " + paySource);
        } else {
            mLlOrderPayTime.setVisibility(View.GONE);
        }
        // 确认收货时间
        if (mLlReceiveTime.getVisibility() == View.VISIBLE
                && order.confirm_time != 0) {
            mTvReceiveTime.setText(HaiTimeUtils.showDetailWSecTime(order.confirm_time));
        }
        // 订单状态名
        setOrderStatusName(order);
        //        // 查看退款进度
        //        findViewById(R.id.rlyt_back_money).setVisibility(order.order_refund_status ? View.VISIBLE : View.GONE);
        // 物流信息
        if (mLlTransferInfo.getVisibility() == View.VISIBLE
                && order.track_update_info != null
                && !TextUtils.isEmpty(order.track_update_info.track_update_content)
                && !TextUtils.isEmpty(order.track_update_info.track_update_date)) {
            // 物流信息
            mTvTransferInfo.setText(order.track_update_info.track_update_content);
            // 物流时间
            mTvTransferInfoTime.setText(order.track_update_info.track_update_date);
            // 订单状态底部border
            mTvOrderStatus.setBackground(orderStatusBorder);
        } else {
            mLlTransferInfo.setVisibility(View.GONE);
            // 订单状态底部border
            mTvOrderStatus.setBackground(null);
        }
        // 收货信息
        String userName = order.order_address.consignee;
        String phone    = String.format("电话：%s", order.order_address.phone);
        String text     = userName + "    " + phone;

        float textLength = mTvUserInfo.getPaint().measureText(text);
        if (mTvUserInfo.getLeft() + textLength > DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 12)) {
            text = userName + "\n" + phone;
        }

        mTvUserInfo.setText(text);

        mTvAddress.setText(HaiUtils.getAddressDetail(order.order_address)); // 详细地址
        // 商品金额
        mTvProductPrice.setText(String.format("\u00A5%d", order.bigOrderPrice));
        // 官网运费
        mTvShippingFeeStore.setText(String.format("\u00A5%d", PriceUtils.round(order.shipping_store_amount)));
        // 预估国际运费
        mTvShippingFeeEstimate.setText(String.format("\u00A5%d", PriceUtils.round(order.shipping_amount)));
        // 海外消费税
        int consumptionTax = PriceUtils.round(order.consumption_tax_amount);
        if (consumptionTax == 0) {
            mRlConsumptionTax.setVisibility(View.GONE);
        } else {
            mRlConsumptionTax.setVisibility(View.VISIBLE);
            mTvConsumptionTax.setText(String.format("\u00A5%d", consumptionTax));
        }
        //佣金
        if (order.commission_promotion == 0) {
            mRlCommission.setVisibility(View.GONE);
        } else {
            mRlCommission.setVisibility(View.VISIBLE);
            commissionTxt.setText(String.format("-\u00A5%d", order.commission_promotion));
        }
        //优惠券
        if (order.couponq_promotion == 0) {
            mRlPromote.setVisibility(View.GONE);
        } else {
            mRlPromote.setVisibility(View.VISIBLE);
            promoteTxt.setText(String.format("-\u00A5%d", order.couponq_promotion));
        }
        //活动优惠
        if (order.bigOrderfullminusAmount == 0) {
            rlytActivity.setVisibility(View.GONE);
        } else {
            rlytActivity.setVisibility(View.VISIBLE);
            tvActivity.setText(String.format("-\u00A5%d", (int) order.bigOrderfullminusAmount));
        }
        // 关税
        int tariffAmount = PriceUtils.round(order.pre_tariff_amount);
        mTvTaxFee.setText(String.format("\u00A5%d", tariffAmount));
        // 总金额
        String total = order.total;
        mOrderPayModel.setTradeAmount(String.valueOf(total));
        // 设置总金额颜色为红色
        String              totalPrice     = String.format("实付款:  \u00A5%d", PriceUtils.round(total));
        SpannableString     spanTotalPrice = new SpannableString(totalPrice);
        ForegroundColorSpan colorSpan      = new ForegroundColorSpan(mColorRed);
        spanTotalPrice.setSpan(colorSpan, 5, totalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvTotalPrice.setText(spanTotalPrice);
        //显示备注  if一个商家有多个商品，显示achate_note
        boolean showAchat = false;
        if (HaiUtils.getSize(order.storelist) > 0) {
            for (OrderDetailResult.OrderDetailBean.StorelistBean storelistBean : order.storelist) {
                if (HaiUtils.getSize(storelistBean.productlist) > 1) {
                    showAchat = true;
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(order.ext) && !showAchat) {
            mRlRemark.setVisibility(View.GONE);
        } else {
            mRlRemark.setVisibility(View.VISIBLE);
            String agree = order.is_achat == 0 ? "同意：" : "不同意：";

            if (TextUtils.isEmpty(order.ext)) {
                mTvRemark.setText(agree + order.achat_note);
            } else {
                if (showAchat) {
                    mTvRemark.setText(order.ext + "\n" + agree + order.achat_note);
                } else {
                    mTvRemark.setText(order.ext);
                }
            }
        }
        // 商品列表
        mGoodsRootLayout.removeAllViews();
        OrderDetailHelper.renderUI(this, mGoodsRootLayout, order);
    }

    /**
     * 设置订单状态文本
     *
     * @param data 订单数据
     */
    private void setOrderStatusName(OrderDetailResult.OrderDetailBean data) {
        switch (data.order_status) {
            case OrderType.NEW:
                mTvOrderStatus.setTextColor(mColorRed);
                long time = (data.overtime - Long.valueOf(data.now)) * 1000L;
                mTvOrderStatus.setText(time >= 0 ? String.format("%s，剩余%d分%d秒", data.order_status_name, time / 1000 / 60, time / 1000 % 60) : data.order_status_name);
                if (time > 1000) {
                    startCountDown(data.order_status_name, time);
                } else {
                    mTvOrderStatus.setText(data.order_status_name);
                    cancelCountDown();
                }
                return;
            case OrderType.FAIL:
                cancelCountDown();
                mTvOrderStatus.setTextColor(mColorGrey999);
                if (TextUtils.isEmpty(data.cancel_info)) {
                    mTvOrderStatus.setText(data.order_status_name);
                } else {
                    mTvOrderStatus.setText(String.format("%s（关闭原因：%s）", data.order_status_name, data.cancel_info));
                }
                return;
            case OrderType.OK:
                cancelCountDown();
                mTvOrderStatus.setTextColor(mColorGrey333);
                break;
            default:
                cancelCountDown();
                mTvOrderStatus.setTextColor(mColorOrange);
                break;
        }
        mTvOrderStatus.setText(data.order_status_name);
    }

    @OnCheckedChanged({R.id.aliPayCheckBox, R.id.wxPayCheckBox})
    public void onCheckedChaged(CompoundButton button, boolean newStatus) {

        if (!newStatus) {
            return;
        }

        if (button.getId() == R.id.aliPayCheckBox) {
            mWxPayCheckBox.setChecked(false);
            mAliPayCheckBox.setEnabled(false);
            mWxPayCheckBox.setEnabled(true);
            mOrderPayModel.setPayType(OrderPayModel.PAY_TYPE_ALI);
            return;
        }

        if (button.getId() == R.id.wxPayCheckBox) {
            mAliPayCheckBox.setChecked(false);
            mWxPayCheckBox.setEnabled(false);
            mAliPayCheckBox.setEnabled(true);
            mOrderPayModel.setPayType(OrderPayModel.PAY_TYPE_WX);
            return;
        }
    }

    /**
     * 复制订单号
     */
    @OnClick(R.id.tv_copy)
    public void clickCopyOrderId() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData         clip      = ClipData.newPlainText("Copied Text", mOrderId);
        clipboard.setPrimaryClip(clip);
        ToastUtils.showToast(mActivity, "复制成功");
    }

    /**
     * 取消订单
     */
    @OnClick(R.id.tv_cancel_order)
    public void clickOrderCancel() {
        if (mOptions == null) {
            mOptions = new ArrayList<>();
            showProgressDialog();
            OrderRepository.getInstance()
                    .cancelReasons()
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<CancelReasonResult>() {
                        @Override
                        public void onSuccess(CancelReasonResult result) {
                            List<CancelReasonResult.ResonListBean> reasonList = result.reson_list;
                            for (CancelReasonResult.ResonListBean reason : reasonList) {
                                mOptions.add(reason.reson_note);
                            }
                            showCancelReasons();

                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    });
        } else {
            showCancelReasons();
        }

    }

    /**
     * 取消订单理由弹窗
     */
    private void showCancelReasons() {
        if (mPickerView == null) {
            mPickerView = new OptionsPickerView.Builder(mActivity, (options1, options2, options3, v) -> requestOrderCancel(mOrderId, mOptions.get(options1)))
                    .setCyclic(false, false, false)
                    .setSubmitText("确定")
                    .setCancelText("取消")
                    .setSubmitColor(mColorGrey666)
                    .setCancelColor(mColorGrey666)
                    .setSubCalSize(17)
                    .setContentTextSize(20)
                    .build();
            mPickerView.setPicker(mOptions);
        }
        mPickerView.show();
    }

    /**
     * 取消订单(退款)理由弹窗
     */
    private void showRefundReasons() {
        if (mRefundPickerView == null) {
            mRefundPickerView = new OptionsPickerView.Builder(mActivity, (options1, options2, options3, v) -> requestRefund(mOrderId, mRefundResons.get(options1)))
                    .setCyclic(false, false, false)
                    .setSubmitText("确定")
                    .setCancelText("取消")
                    .setSubmitColor(mColorGrey666)
                    .setCancelColor(mColorGrey666)
                    .setSubCalSize(17)
                    .setContentTextSize(20)
                    .build();
            mRefundPickerView.setPicker(mRefundResons);
        }
        mRefundPickerView.show();
    }

    private void requestRefund(String mOrderId, String reson) {
        OrderRepository.getInstance()
                .applyForRefundV1(mOrderId, "")
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<RefundCommitResult>() {
                    @Override
                    public void onSuccess(RefundCommitResult refundCommitResult) {
                        if (TextUtils.equals("1", refundCommitResult.success)) {
                            ToastUtils.showToast(mActivity, "取消成功");
                            // 埋点
                            HashMap<String, String> kv = new HashMap<>();
                            kv.put(TraceUtils.Event_Kv_Order_Id, mOrderId);
                            kv.put(TraceUtils.Event_Kv_Code, "0");
                            YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Refund, kv, "");

                            EventBus.getDefault().post(new OrderChangeEvent());
                        }
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        // 埋点
                        HashMap<String, String> kv = new HashMap<>();
                        kv.put(TraceUtils.Event_Kv_Order_Id, mOrderId);
                        kv.put(TraceUtils.Event_Kv_Code, "1");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Refund, kv, "");

                        return super.onFailed(e);
                    }
                });
    }

    /**
     * 取消订单网络请求
     *
     * @param reason 取消理由
     */
    private void requestOrderCancel(String orderId, String reason) {
        OrderRepository.getInstance()
                .orderCancel(orderId, reason)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonSuccessResult>() {
                    @Override
                    public void onSuccess(CommonSuccessResult result) {
                        if (TextUtils.equals("1", result.success)) {
                            ToastUtils.showToast(mActivity, "取消成功");
                            EventBus.getDefault().post(new OrderChangeEvent());
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 查看物流
     */
    @OnClick({R.id.ll_transfer_info, R.id.tv_get_transfer_info})
    public void clickGetTransferInfo() {
        // 埋点
        //        TraceUtils.addClick(TraceUtils.PageCode_TransDetail, mOrderId + "", this, TraceUtils.PageCode_OrderDetail, TraceUtils.PositionCode_Logistic + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_TransDetail, TraceUtils.PageCode_OrderDetail, TraceUtils.PositionCode_Logistic, mOrderId);

        TransferDetailActivity.toThisActivity(mActivity, mOrderId, "");
    }

    /**
     * 确认收货
     */
    @OnClick(R.id.tv_confirm_received)
    public void clickOrderReceived() {
        new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                .setMessage("确认收货")
                .setPositiveButton("确认", (dialog, which) -> {
                    requestOrderReceived();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 确认收货网络请求
     */
    private void requestOrderReceived() {
        OrderRepository.getInstance()
                .orderConfirm(mOrderId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<EnsureRecievedBean>() {
                    @Override
                    public void onSuccess(EnsureRecievedBean result) {
                        if (TextUtils.equals(result.success, "1")) {
                            // 埋点
                            HashMap<String, String> kv = new HashMap<>();
                            kv.put(TraceUtils.Event_Kv_Order_Id, mOrderId);
                            kv.put(TraceUtils.Event_Kv_Code, "0");
                            YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Sign, kv, "");

                            ToastUtils.showToast(mActivity, "确认收货成功");
                            EventBus.getDefault().post(new OrderChangeEvent());
                        }
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        // 埋点
                        HashMap<String, String> kv = new HashMap<>();
                        kv.put(TraceUtils.Event_Kv_Order_Id, mOrderId);
                        kv.put(TraceUtils.Event_Kv_Code, "1");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Sign, kv, "");

                        return super.onFailed(e);
                    }
                });
    }

    /**
     * 删除订单
     */
    @OnClick(R.id.tv_delete_order)
    public void clickOrderDelete() {
        if (mDlgDeleteOrder == null) {
            mDlgDeleteOrder = new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                    .setMessage("确定删除此订单?")
                    .setPositiveButton("确认", (dialog, which) -> {
                        // 删除订单网络请求
                        OrderRepository.getInstance()
                                .orderDel(mOrderId)
                                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                                .subscribe(new DefaultSubscriber<OrderDelResult>() {
                                    @Override
                                    public void onSuccess(OrderDelResult result) {
                                        if (TextUtils.equals(result.success, "1")) {
                                            ToastUtils.showToast(mActivity, "删除成功");
                                            EventBus.getDefault().post(new OrderChangeEvent(true));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                });
                    })
                    .setNegativeButton("取消", (dialog, which) -> {

                    }).create();
        }
        mDlgDeleteOrder.show();
    }

    /**
     * 取消订单（付款后抢单前）
     */
    @OnClick(R.id.tv_cancle_buy)
    public void cancleBuy() {
        if (mRefundResons != null && mRefundResons.size() > 0) {
            showRefundReasons();
        } else {
            mRefundResons = new ArrayList<>();
            showProgressDialog();
            OrderRepository.getInstance()
                    .refundReasons()
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<CancelReasonResult>() {
                        @Override
                        public void onSuccess(CancelReasonResult result) {
                            List<CancelReasonResult.ResonListBean> reasonList = result.reson_list;
                            for (CancelReasonResult.ResonListBean reason : reasonList) {
                                mRefundResons.add(reason.reson_note);
                            }
                            showRefundReasons();

                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    });
        }

    }

    /**
     * 申请退款
     */
    //    @OnClick({R.id.tv_back_money})
    //    public void applyForRefund() {
    //        if (!tvRefund.isSelected()) {
    //            new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
    //                    .setMessage("您订单中的商品均已申请退款，暂无可退商品。如有疑问请联系客服。")
    //                    .setPositiveButton("确定", (dialog, which) -> {
    //
    //                    }).show();
    //            return;
    //        }
    //
    //        // 埋点
    //        TraceUtils.addClick(TraceUtils.PageCode_Refund, mOrderId + "", this, TraceUtils.PageCode_OrderDetail, TraceUtils.PositionCode_ApplyAfterSale + "", "");
    //
    //        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_Refund, TraceUtils.PageCode_OrderDetail, TraceUtils.PositionCode_ApplyAfterSale, mOrderId);
    //
    //        Intent intent = new Intent(this, ApplyRefundActivity.class);
    //        intent.putExtra(KEY_ORDER_ID, mOrderId);
    //        startActivity(intent);
    //    }

    /**
     * 查看退款进度
     */
    //    @OnClick({R.id.rlyt_back_money})
    //    public void gotoRefundList() {
    //        // 埋点
    //        TraceUtils.addClick(TraceUtils.PageCode_RefundList, mOrderId + "", this, TraceUtils.PageCode_OrderDetail, TraceUtils.PositionCode_AfterSaleProgress + "", "");
    //
    //        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_RefundList, TraceUtils.PageCode_OrderDetail, TraceUtils.PositionCode_AfterSaleProgress, mOrderId);
    //
    //        Intent intent = new Intent(this, RefundListActivity.class);
    //        intent.putExtra(KEY_ORDER_ID, mOrderId);
    //        startActivity(intent);
    //    }

    /**
     * 去付款
     */
    @OnClick({R.id.toPayBtn})
    public void clickPay() {
        showProgressDialog(StringConstans.TOAST_ORDER_COMMIT_2, false);
        OrderRepository.getInstance()
                .orderCommit(mOrderPayModel.getOrderId(), mOrderPayModel.getPayType())
                .subscribe(new DefaultSubscriber<OrderCommitBean>() {
                    @Override
                    public void onSuccess(OrderCommitBean result) {
                        // 埋点
                        HashMap<String, String> kv = new HashMap<>();
                        kv.put(TraceUtils.Event_Kv_Order_Id, result.order_id);
                        kv.put(TraceUtils.Event_Kv_Order_Amount, result.amount + "");
                        kv.put(TraceUtils.Event_Kv_Orderr_PayType, mOrderPayModel.getPayType());
                        kv.put(TraceUtils.Event_Kv_Order_CashType, "rmb");
                        kv.put(TraceUtils.Event_Kv_Orderr_Tradeno, result.trade_no);
                        kv.put(TraceUtils.Event_Kv_Code, "0");
                        String orderinfo = new Gson().toJson(result);
                        kv.put(TraceUtils.Event_Kv_Order_OrderInfo, orderinfo);

                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Pay, kv, "");

                        WXPayEntryActivity.toThisActivity(
                                OrderDetailActivity.this,
                                "",
                                result.order_id,
                                result.paydata,
                                skulist,
                                (int) result.amount,
                                mOrderPayModel.getPayType().equals("wechat"),
                                "1".equals(result.need_pay_online));
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        // 埋点
                        HashMap<String, String> kv = new HashMap<>();
                        kv.put(TraceUtils.Event_Kv_Order_Id, "");
                        kv.put(TraceUtils.Event_Kv_Order_Amount, mOrderPayModel.getTradeAmount());
                        kv.put(TraceUtils.Event_Kv_Orderr_PayType, mOrderPayModel.getPayType());
                        kv.put(TraceUtils.Event_Kv_Order_CashType, "rmb");
                        kv.put(TraceUtils.Event_Kv_Orderr_Tradeno, "");
                        kv.put(TraceUtils.Event_Kv_Code, "1");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Pay, kv, "");

                        return super.onFailed(e);
                    }
                });
    }

    /**
     * 获取订单中所有商品的skuid
     *
     * @param orderDetailBean
     */
    private void initskulist(OrderDetailResult.OrderDetailBean orderDetailBean) {
        if (orderDetailBean != null && HaiUtils.getSize(orderDetailBean.storelist) > 0) {
            for (OrderDetailResult.OrderDetailBean.StorelistBean storelistBean : orderDetailBean.storelist) {
                if (HaiUtils.getSize(storelistBean.productlist) > 0) {
                    for (OrderDetailResult.OrderDetailBean.StorelistBean.ProductListBean productListBean : storelistBean.productlist) {
                        skulist.add(productListBean.selected_sku.skuid);
                    }
                }
            }
        }
    }

    /**
     * 待付款倒计时
     *
     * @param orderStatusName 订单状态文案
     * @param timeMillis      剩余时间（秒）
     */
    private void startCountDown(String orderStatusName, long timeMillis) {
        mCountDownTimer = new CountDownTimer(timeMillis, 1000) { // 开始倒计时
            @Override
            public void onTick(long millisUntilFinished) {
                mTvOrderStatus.setText(String.format("%s，剩余付款时间：%d分%d秒", orderStatusName, millisUntilFinished / 1000 / 60, millisUntilFinished / 1000 % 60));
            }

            @Override
            public void onFinish() {
                mTvOrderStatus.setText(orderStatusName);
            }
        }.start();
    }

    /**
     * 预估国际运费弹窗
     */
    @OnClick(R.id.tv_shipping_fee_estimate)
    public void clickShippingFee(View v) {
        if (mShippingFeePopuWindow == null) {
            mShippingFeePopuWindow = new ShippingFeePopuWindow(mActivity);
        }
        mShippingFeePopuWindow.showOrDismiss(v);
    }

    /**
     * 关税弹窗
     */
    @OnClick(R.id.tv_tax_fee)
    public void clickTaxFee(View v) {
        if (mTariffPopuWindow == null) {
            mTariffPopuWindow = new TariffPopuWindow(mActivity, mTariffNotice == null ? "" : mTariffNotice.desc);
        }
        mTariffPopuWindow.showOrDismiss(v);
    }

    /**
     * 订单状态发生改变刷新页面
     *
     * @param event
     */
    @Subscribe
    public void onOrderChangeEvent(OrderChangeEvent event) {
        if (!event.isDeleteOrder) {
            loadData();
        }
    }

    /**
     * 取消倒计时
     */
    private void cancelCountDown() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel(); // 取消倒计时
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param orderId 订单号
     */
    public static void toThisActivity(Context context, String orderId) {
        toThisActivity(context, orderId, false);
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param orderId 订单号
     * @param newTask 是否开启新栈
     */
    public static void toThisActivity(Context context, String orderId, boolean newTask) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("order_id", orderId);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelCountDown();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + mOrderId;
    }

}
