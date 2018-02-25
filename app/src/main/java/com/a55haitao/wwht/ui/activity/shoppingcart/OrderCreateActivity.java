package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.event.OrderCreateBackEvent;
import com.a55haitao.wwht.data.event.ProductInfoChangeEvent;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;
import com.a55haitao.wwht.data.interfaces.DdContract;
import com.a55haitao.wwht.data.interfaces.OrderModel;
import com.a55haitao.wwht.data.interfaces.OrderPayModel;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.AddressItemBean;
import com.a55haitao.wwht.data.model.entity.CommissionBean;
import com.a55haitao.wwht.data.model.entity.CouponBean;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.data.model.entity.OrderCreatePageData;
import com.a55haitao.wwht.data.model.entity.OrderLogsBean;
import com.a55haitao.wwht.data.model.entity.OrderPrepareBean;
import com.a55haitao.wwht.data.model.entity.SelectedSkuBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.model.entity.TraceOrderPreparBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.data.repository.PassPortRepository;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.discover.AddressListActivity;
import com.a55haitao.wwht.ui.activity.discover.CouponActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MyLinearLayout;
import com.a55haitao.wwht.ui.view.ProductInfoChangePopWindow;
import com.a55haitao.wwht.ui.view.ShippingFeePopuWindow;
import com.a55haitao.wwht.ui.view.TariffPopuWindow;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.TaxRuleHelper;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.a55haitao.wwht.wxapi.WXPayEntryActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.Order;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 订单创建页
 */
public class OrderCreateActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, DdContract.DdViewIml {

    @BindView(R.id.headView)             DynamicHeaderView headView;
    @BindView(R.id.nameAndPhoneTxt)      TextView          nameAndPhoneTxt;
    @BindView(R.id.addressTxt)           TextView          addressTxt;
    @BindView(R.id.sellerLayout)         LinearLayout      sellerLayout;
    @BindView(R.id.couponTxt)            TextView          couponTxt;
    @BindView(R.id.switchBtn)            SwitchButton      switchButton;
    @BindView(R.id.aliPayLayout)         RelativeLayout    aliPayLayout;
    @BindView(R.id.wxPayLayout)          RelativeLayout    wxPayLayout;
    @BindView(R.id.payment)              TextView          payment;
    @BindView(R.id.toPayBtn)             Button            toPayBtn;
    @BindView(R.id.productTotleCountTxt) TextView          productTotleCountTxt;
    @BindView(R.id.commissionTxt)        TextView          commissionTxt;

    @BindView(R.id.aliPayCheckBox) CheckBox aliPayCheckBox;
    @BindView(R.id.wxPayCheckBox)  CheckBox wxPayCheckBox;

    @BindView(R.id.shippingLayout) RelativeLayout shippingLayout;

    @BindView(R.id.arrImg)       ImageView mArrImg;
    @BindView(R.id.couponImg)    ImageView couponImg;
    @BindView(R.id.noAddressTxt) TextView  mNoAddressTxt;
    @BindView(R.id.defaultTxt)   TextView  mDefaultTxt;

    @BindView(R.id.scrollView)   ScrollView     scrollView;
    @BindView(R.id.llyt_et_desc) MyLinearLayout llytExtEdit;
    @BindView(R.id.extEdt)       EditText       mExtEdit;
    @BindView(R.id.tv_desc_num)  HaiTextView    tvDesNum;


    @BindView(R.id.llyt_achat_note) LinearLayout llytAchatNote;
    @BindView(R.id.tv_achat_note)   HaiTextView  tvAchatNote;
    @BindView(R.id.rb_aggress)      RadioButton  rbAggress;
    @BindView(R.id.tv_agree)        HaiTextView  tvAggress;
    @BindView(R.id.iv_agree)        ImageView    ivAggress;
    @BindView(R.id.rb_reject)       RadioButton  rbReject;
    @BindView(R.id.tv_reject)       HaiTextView  tvReject;
    @BindView(R.id.iv_reject)       ImageView    ivReject;

    /**
     * 收货地址的questCdoe
     */
    private final int SHIPPING_FLAG = 999;
    /**
     * 优惠券的requestCode
     */
    private final int COUPON_FLAG   = 1000;

    private OrderModel mOrderModel;

    private OrderPayModel mOrderPayModel;

    private TraceOrderPreparBean tracePre;

    private Order               mOrderInfoCollect;
    private Tracker             mTracker;                     // GA Tracker
    private ToastPopuWindow     mPwToast;
    private int                 addressId;
    private boolean             addressVisible;
    private ArrayList<String>   skulist;
    private OrderModel.CartType type;

    private boolean isAggress;
    private boolean isReject;

    private int from;          //  0是商详直接购买过来，1是其他

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_create);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initVars();
        initViews(savedInstanceState);
        loadData(false);
    }

    private void initVars() {
        addressId = -1;
        skulist = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getIntExtra("from", 1);

            type = getIntent().getParcelableExtra(OrderModel.CartType.class.getName());
            mOrderModel = new OrderModel();
            mOrderModel.setType(type);
        }
        tracePre = new TraceOrderPreparBean();
        //支付商品名称
        mOrderPayModel = new OrderPayModel(null, null);
        //初始化Order,用于保存订单详情
        mOrderInfoCollect = Order.createOrder("", -1, "CNY");
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("订单确认");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews(Bundle savedInstanceState) {
        // 客服
        headView.setHeadClickListener(() -> {
            // GA 事件
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("联系客服")
                    .setAction("在线客服")
                    .setLabel("订单确认")
                    .build());
            NtalkerUtils.contactNtalker(mActivity);
        });
        // 支付宝
        aliPayCheckBox.setOnCheckedChangeListener(this);
        // 微信
        wxPayCheckBox.setOnCheckedChangeListener(this);
        switchButton.setOnCheckedChangeListener(this);
        wxPayCheckBox.setEnabled(false);

        llytExtEdit.setEditeText(mExtEdit);
        llytExtEdit.setParentScrollview(scrollView);
        mExtEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvDesNum.setText(s.length() + "/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loadData(boolean refresh) {  //  是初始化页面  还是商品变化刷新页面
        showProgressDialog(StringConstans.TOAST_LOADING, true);
        Observable.zip(PassPortRepository.getInstance().getDefaultAddress().onErrorReturn(throwable -> null),
                OrderRepository.getInstance().orderPrepare(mOrderModel).onErrorReturn(throwable -> null),
                SnsRepository.getInstance().getCommission().onErrorReturn(throwable -> new CommissionBean()),
                OrderCreatePageData::new)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<OrderCreatePageData>() {
                    @Override
                    public void onSuccess(OrderCreatePageData orderCreatePageData) {
                        mOrderModel.setAddressBean(orderCreatePageData.mAddressItemBean);
                        mOrderModel.setCommission(orderCreatePageData.mCommissionBean.available_commission / 100);
                        tracePre.cart_list = orderCreatePageData.mOrderPrepareBean.cart_list;
                        initSkulist(orderCreatePageData.mOrderPrepareBean);
                        scrollView.scrollTo(0, 0);
                        setAddressInfo();
                        setOrderInfo(orderCreatePageData.mOrderPrepareBean);
                        setSellersView(orderCreatePageData.mOrderPrepareBean.storelist);

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        if (refresh)
                            onBackPressed();
                        return super.onFailed(e);
                    }
                });
    }

    private void initSkulist(OrderPrepareBean orderPrepareBean) {
        skulist.clear();
        if (orderPrepareBean != null && orderPrepareBean.storelist != null && orderPrepareBean.storelist.size() > 0) {
            for (OrderPrepareBean.StorelistBean storelistBean : orderPrepareBean.storelist) {
                if (HaiUtils.getSize(storelistBean.productList) > 0) {
                    for (OrderPrepareBean.StorelistBean.ProductListBean productlistBean : storelistBean.productList) {
                        skulist.add(productlistBean.skuid);
                    }
                }
            }
        }

    }

    private void orderLogs(String data) {
        ProductRepository.getInstance()
                .orderLogs(data)
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 地址信息
     */
    public void setAddressInfo() {
        AddressItemBean bean = mOrderModel.getAddressBean();

        if (bean == null) {
            mNoAddressTxt.setVisibility(View.VISIBLE);
            mDefaultTxt.setVisibility(View.GONE);
            mArrImg.setVisibility(View.VISIBLE);
        } else {
            mDefaultTxt.setVisibility(bean.is_default == 1 ? View.VISIBLE : View.GONE);

            addressId = bean.id;
            tracePre.address_id = addressId + "";
            addressVisible = true;

            mArrImg.setVisibility(View.VISIBLE);
            mNoAddressTxt.setVisibility(View.GONE);
            nameAndPhoneTxt.setVisibility(View.VISIBLE);
            nameAndPhoneTxt.setText(String.format("%s    电话: %s", bean.name, bean.phone));

            addressTxt.setVisibility(View.VISIBLE);
            addressTxt.setText(bean.province + "  " + bean.city + "  " + bean.dist + "  " + bean.street);
        }

    }

    @Subscribe
    public void onProductInfoChange(ProductInfoChangeEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.result != null && event.result.ext != null) {  // 核价失败了
                    if (event.result.ext.size() > 0) {
                        ArrayList<String> allCarts = tracePre.cart_list;
                        ArrayList<String> delete   = new ArrayList<>();
                        for (OrderCommitBean.FailedExtData data : event.result.ext) {
                            data.is_select = true;
                            if (HaiUtils.getSize(allCarts) > 0) {
                                for (String cart : allCarts) {
                                    if (cart.equals(data.cart_id)) {
                                        delete.add(cart);
                                    }
                                }
                            }
                        }
                        if (HaiUtils.getSize(delete) > 0) {
                            allCarts.removeAll(delete);
                        }
                        ProductInfoChangePopWindow productInfoChangePopWindow = new ProductInfoChangePopWindow(mActivity, event.result, event.message, allCarts, from);
                        productInfoChangePopWindow.setGoBuyInterface(new ProductInfoChangePopWindow.GoBuyInterface() {
                            @Override
                            public void goBuy(int[] selectCart) {

                                if (from == 0) { // 商详页过来
                                    mOrderModel = new OrderModel();
                                    mOrderModel.setType(type);
                                } else {
                                    OrderModel.CartType cartType = new OrderModel.CartType(selectCart);
                                    mOrderModel = new OrderModel();
                                    mOrderModel.setType(cartType);
                                }

                                tracePre = new TraceOrderPreparBean();
                                //支付商品名称
                                mOrderPayModel = new OrderPayModel(null, null);
                                //初始化Order,用于保存订单详情
                                mOrderInfoCollect = Order.createOrder("", -1, "CNY");
                                loadData(true);
                            }
                        });
                        productInfoChangePopWindow.showOrDismiss(toPayBtn);

                    }
                }
            }
        });

    }

    @Override
    public void setCouponInfo(CouponBean couponInfo) {

    }

    @Override
    public void updatePayment(int money) {
        if (money < 0) {
            money = 0;
        }
        payment.setText(ApiUrl.RMB_UNICODE + money);
    }

    @Override
    public void setSellersView(ArrayList<OrderPrepareBean.StorelistBean> storelists) {
        sellerLayout.removeAllViews();

        for (OrderPrepareBean.StorelistBean storeBean : storelists) {
            View sellerView = LayoutInflater.from(this).inflate(R.layout.dd_seller_item_layout, sellerLayout, false);

            //商家名字
            String store_name = storeBean.store_name;
            HaiUtils.setText(sellerView, R.id.sellerNameTxt, String.format("%s官网发货", store_name));

            //国籍
            ImageView flagImgView = (ImageView) sellerView.findViewById(R.id.flagImg);

            int flagResourceId = HaiUtils.getFlagResourceId(storeBean.country.regionName, false);
            if (flagResourceId == -1) {
                String flagUrl = storeBean.country.regionImgUrl;
                Glide.with(this)
                        .load(flagUrl)
                        .into(flagImgView);
            } else {
                flagImgView.setImageResource(flagResourceId);
            }

            //商品金额
            int bigStorePrice = PriceUtils.round(storeBean.bigStorePrice);
            HaiUtils.setText(sellerView, R.id.sumTxt, String.format("\u00A5%s", bigStorePrice));

            //活动优惠
            LinearLayout llytActivity = ButterKnife.findById(sellerView, R.id.llyt_activity);
            HaiTextView  activityTxt  = (HaiTextView) sellerView.findViewById(R.id.activityTxt);
            if (storeBean.bigStorefullMinusAmount > 0) {
                llytActivity.setVisibility(View.VISIBLE);
                activityTxt.setText("-" + "\u00A5" + PriceUtils.round(storeBean.bigStorefullMinusAmount));
            } else {
                llytActivity.setVisibility(View.GONE);
            }

            //官网运费
            HaiUtils.setText(sellerView, R.id.expressTxt, PriceUtils.toRMBFromYuan((float) storeBean.bigStoreShippingFee));

            //预估国际运费
            TextView storeTransFeeTxt = ButterKnife.findById(sellerView, R.id.taxTxt2);
            storeTransFeeTxt.setText(PriceUtils.toRMBFromYuan((float) storeBean.bigStoreTransFee));
            storeTransFeeTxt.setOnClickListener(v -> {
                ShippingFeePopuWindow shippingFeePopuWindow = new ShippingFeePopuWindow(mActivity);
                shippingFeePopuWindow.showOrDismiss(v);
            });

            //海外消费税
            View        storeLayout = ButterKnife.findById(sellerView, R.id.extraTaxLayout);
            HaiTextView extraTxt    = (HaiTextView) sellerView.findViewById(R.id.extraTaxTxt);
            if (storeBean.bigStoreConsumptionaxmount == 0) {
                extraTxt.setVisibility(View.GONE);
                storeLayout.setVisibility(View.GONE);
            } else {
                extraTxt.setText(PriceUtils.toRMBFromYuan((float) storeBean.bigStoreConsumptionaxmount));
                storeLayout.setVisibility(View.VISIBLE);
            }

            //关税
            TextView tairffTxt = ButterKnife.findById(sellerView, R.id.bigTariffTxt);
            tairffTxt.setText(TaxRuleHelper.tairff(storeBean.bigTariff));
            tairffTxt.setOnClickListener(v -> {
                TariffPopuWindow tariffPopuWindow = new TariffPopuWindow(mActivity, storeBean.tariffNotice == null ? "" : storeBean.tariffNotice.desc);
                tariffPopuWindow.showOrDismiss(tairffTxt);
            });

            //总计
            HaiUtils.setText(sellerView, R.id.totleTxt, PriceUtils.toRMBFromYuan((float) storeBean.bigStoreAccountsTotal));

            for (OrderPrepareBean.StorelistBean.ProductListBean productBean : storeBean.productList) {

                //TalkingData 信息
                mOrderInfoCollect.addItem(productBean.skuid, productBean.productbean.getCategoryInfo(), productBean.productbean.name,
                        (int) (productBean.bigSkuPrice * 100), 1);

                LinearLayout productLayout = (LinearLayout) sellerView.findViewById(R.id.goodsLayout);

                View productView = LayoutInflater.from(this).inflate(R.layout.dd_good_item_layout, productLayout, false);

                //商品数量
                HaiUtils.setText(productView, R.id.countTxt, String.format("X%s", productBean.count));

                //商品名称
                HaiUtils.setText(productView, R.id.goodsnameTxt, productBean.productbean.name);
                //商品价格
                HaiUtils.setText(productView, R.id.priceTxt, PriceUtils.toRMBFromYuan((float) productBean.bigSkuPrice));
                //商品图片
                int    size        = HaiConstants.CompoundSize.PX_100;
                String coverImgUrl = UPaiYunLoadManager.transformProductThumail(productBean.productbean.coverImgUrl, size, size);
                UPaiYunLoadManager.loadImage(this, productBean.productbean.coverImgUrl, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, (ImageView) productView.findViewById(R.id.goodsImg));
                LinearLayout specRootLayout = (LinearLayout) productView.findViewById(R.id.specRootLayout);

                //规格
                List<SelectedSkuBean.SkuValues> skuValues =
                        productBean.productbean.selectedSkuBean.skuValues;
                for (SelectedSkuBean.SkuValues skuValue : skuValues) {

                    TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.simple_spec_tv_layout, specRootLayout, false);
                    String   value    = skuValue.value;
                    String   specInfo = HaiUtils.transformSpec(skuValue.name);
                    textView.setText(String.format("%s: %s", specInfo, value));
                    specRootLayout.addView(textView);
                }

                // 跳转到商详页
                productView.setOnClickListener(v -> ProductMainActivity.toThisAcivity(mActivity, productBean.spuid));
                productLayout.addView(productView);
            }
            sellerLayout.addView(sellerView);
        }

    }

    @Override
    public String getPayWay() {
        return aliPayCheckBox.isChecked() ? "alipay" : "wechat";
    }


    @OnClick({R.id.shippingLayout, R.id.toPayBtn, R.id.toCouponLayout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shippingLayout: // 收件信息
                // 埋点
                //                TraceUtils.addClick(TraceUtils.PageCode_AddressList, addressId + "", this, TraceUtils.PageCode_OrderCreat, TraceUtils.PositionCode_Address + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_AddressList, TraceUtils.PageCode_OrderCreat, TraceUtils.PositionCode_Address, "");

                Intent shippingIntent = new Intent(this, AddressListActivity.class);
                shippingIntent.putExtra(Boolean.class.getName(), true);
                shippingIntent.putExtra("addressId", addressId);
                startActivityForResult(shippingIntent, SHIPPING_FLAG);
                break;
            case R.id.toPayBtn: // 前往支付
                if (mOrderModel.getAddressBean() == null) {
                    mPwToast = ToastPopuWindow.makeText(mActivity, "请填写收货地址", AlertViewType.AlertViewType_Warning);
                    mPwToast.show();
                    break;
                }
                if (!addressVisible) {
                    mPwToast = ToastPopuWindow.makeText(mActivity, "收货地址不存在", AlertViewType.AlertViewType_Warning);
                    mPwToast.show();
                    break;
                }

                if (!aliPayCheckBox.isChecked() && !wxPayCheckBox.isChecked()) {
                    mPwToast = ToastPopuWindow.makeText(mActivity, "请选择支付方式", AlertViewType.AlertViewType_Warning);
                    mPwToast.show();
                    break;
                }

                if (llytAchatNote.getVisibility() == View.VISIBLE) {
                    if (!isAggress && !isReject) {
                        mPwToast = ToastPopuWindow.makeText(mActivity, "请选择是否同意:如订单中因官网缺货导致部分商品采购失败，是否同意继续为您采购同一商家剩余商品，并在退款中扣除可能产生的官网运费。", AlertViewType.AlertViewType_Warning);
                        mPwToast.show();
                        break;
                    }
                }

                // GA 事件
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("交易相关")
                        .setAction("前往支付")
                        .build());

                // 埋点
                //                TraceUtils.addClick(TraceUtils.PageCode_PayResult, "", this, TraceUtils.PageCode_OrderCreat, TraceUtils.PositionCode_Pay + "", "");
                //                // 埋点
                //                //                TraceUtils.addClick(TraceUtils.PageCode_OrderCreat, "", ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Purchase + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_PayResult, TraceUtils.PageCode_OrderCreat, TraceUtils.PositionCode_Pay, "");

                // 创建订单请求
                requestOrderCreate();
                break;
            case R.id.toCouponLayout: // 优惠券
                ArrayList<CouponBean> couponList = mOrderModel.getCouponList();
                if (couponList != null && couponList.size() != 0) {
                    // 埋点
                    //                    TraceUtils.addClick(TraceUtils.PageCode_MyCouponList, "", this, TraceUtils.PageCode_OrderCreat, TraceUtils.PositionCode_UseCoupon + "", "");

                    //                    TraceUtils.addAnalysAct(TraceUtils.PageCode_MyCouponList, TraceUtils.PageCode_OrderCreat, TraceUtils.PositionCode_UseCoupon, "");

                    CouponActivity.toThisActivityWithResult(this, couponList, mOrderModel.getCouponId(), COUPON_FLAG);
                }
                break;
        }
    }

    /**
     * 创建订单请求
     */
    private void requestOrderCreate() {
        showProgressDialog(StringConstans.TOAST_ORDER_COMMIT);
        mOrderPayModel.setPayType(getPayWay());
        tracePre.pay_type = getPayWay();
        OrderRepository.getInstance()
                .orderCommit(rbAggress.isChecked() ? 0 : 1, switchButton.isChecked(),
                        getPayWay(),
                        mExtEdit.getText().toString(),
                        mOrderModel)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<OrderCommitBean>() {
                    @Override
                    public void onSuccess(OrderCommitBean result) {

                        // 提交订单成功 埋点上报
                        if ("1".equals(result.need_pay_online)) {
                            OrderLogsBean orderLogsBean = new OrderLogsBean();
                            orderLogsBean.serialNumber = result.trade_no;
                            orderLogsBean.infoType = 1;
                            orderLogsBean.paystatus = 0;
                            orderLogsBean.ordertimestamp = result.order_ctime;
                            orderLogsBean.paytimestamp = 0;
                            orderLogsBean.source = "";
                            orderLogsBean.orderid = result.order_id;
                            orderLogsBean.skulist = skulist;
                            orderLogsBean.payType = "wechat".equals(getPayWay()) ? 0 : 1;
                            String data = new Gson().toJson(orderLogsBean);
                            orderLogs(data);
                        }

                        orderCommitSuccess(result);

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
                        kv.put(TraceUtils.Event_Kv_Order_Amount, payment.getTag() + "");
                        kv.put(TraceUtils.Event_Kv_Orderr_PayType, getPayWay());
                        kv.put(TraceUtils.Event_Kv_Order_CashType, "rmb");
                        kv.put(TraceUtils.Event_Kv_Orderr_Tradeno, "");
                        kv.put(TraceUtils.Event_Kv_Code, "1");

                        String orderPreinfo = new Gson().toJson(tracePre);
                        kv.put(TraceUtils.Event_Kv_Order_OrderPre, orderPreinfo);

                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Pay, kv, "");

                        return super.onFailed(e);
                    }
                });
    }

    private void orderCommitSuccess(OrderCommitBean bean) {
        // 埋点
        HashMap<String, String> kv = new HashMap<>();
        kv.put(TraceUtils.Event_Kv_Order_Id, bean.order_id);
        kv.put(TraceUtils.Event_Kv_Order_Amount, bean.amount + "");
        kv.put(TraceUtils.Event_Kv_Orderr_PayType, getPayWay());
        kv.put(TraceUtils.Event_Kv_Order_CashType, "rmb");
        kv.put(TraceUtils.Event_Kv_Orderr_Tradeno, bean.trade_no);
        kv.put(TraceUtils.Event_Kv_Code, "0");
        String orderinfo = new Gson().toJson(bean);
        kv.put(TraceUtils.Event_Kv_Order_OrderInfo, orderinfo);
        String orderPreinfo = new Gson().toJson(tracePre);
        kv.put(TraceUtils.Event_Kv_Order_OrderPre, orderPreinfo);

        Logger.d("创建订单准备信息————————" + orderPreinfo);

        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Pay, kv, "");

        //通知购物车更新
        getShoppingCartBadge();

        //Talkingdata 接入
        try {
            mOrderInfoCollect.put("keyOrderId", bean.order_id);
            mOrderInfoCollect.put("keyTotalPrice", (int) (bean.amount * 100));
            TalkingDataAppCpa.onPlaceOrder(String.valueOf(UserRepository.getInstance().getUserInfo().getId()), mOrderInfoCollect);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //打开支付界面
        WXPayEntryActivity.toThisActivity(OrderCreateActivity.this,
                bean.trade_no,
                bean.order_id,
                bean.paydata,
                skulist,
                (int) bean.amount,
                mOrderPayModel.getPayType().equals("wechat"),
                "1".equals(bean.need_pay_online));
        finish();
    }

    private void getShoppingCartBadge() {
        if (!HaiUtils.isLogin()) {
            return;
        }
        ProductRepository.getInstance()
                .getShoppingCartListCnt()
                .subscribe(new DefaultSubscriber<ShoppingCartCntBean>() {
                    @Override
                    public void onSuccess(ShoppingCartCntBean shoppingCartCntBean) {
                        EventBus.getDefault().post(new ShoppingCartCntChangeEvent(shoppingCartCntBean.count));
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            switch (buttonView.getId()) {
                case R.id.switchBtn:
                    commissionTxt.setText("使用佣金");
                    updatePayment(PriceUtils.round((double) payment.getTag()));
                    break;
            }
            return;
        }

        switch (buttonView.getId()) {
            case R.id.aliPayCheckBox:
                wxPayCheckBox.setChecked(false);
                wxPayCheckBox.setEnabled(true);
                aliPayCheckBox.setEnabled(false);
                break;
            case R.id.wxPayCheckBox:
                aliPayCheckBox.setChecked(false);
                aliPayCheckBox.setEnabled(true);
                wxPayCheckBox.setEnabled(false);
                break;
            case R.id.switchBtn:
                if (mOrderModel.getCommission() == 0) {
                    mPwToast = ToastPopuWindow.makeText(this, "您的可用佣金不足，无法使用佣金支付", AlertViewType.AlertViewType_Warning);
                    mPwToast.show();
                    switchButton.setChecked(false);
                } else {
                    updatePayment((int) (PriceUtils.round((double) payment.getTag()) - mOrderModel.getCommission()));
                }
                updateCommissionMoney();

                break;
        }
    }

    /**
     * 更新佣金显示
     */
    public void updateCommissionMoney() {
        if (switchButton.isChecked()) {      //使用佣金
            float v = (float) (mOrderModel.getCommission() >= (double) payment.getTag() ? (double) payment.getTag() : mOrderModel.getCommission());
            commissionTxt.setText(String.format("使用佣金抵 %s", PriceUtils.toRMBFromYuan(v)));
        } else {                             //不适用佣金
            commissionTxt.setText("使用佣金");
        }
    }

    public void setOrderInfo(OrderPrepareBean orderPrepareBean) {
        //获取预下单总金额
        payment.setTag(orderPrepareBean.bigOrderAccountsTotal);

        if (switchButton.isChecked() == true) {
            float v = (float) (mOrderModel.getCommission() >= (double) payment.getTag() ? (double) payment.getTag() : mOrderModel.getCommission());
            commissionTxt.setText(String.format("使用佣金抵 %s", PriceUtils.toRMBFromYuan(v)));
            updatePayment(PriceUtils.round(orderPrepareBean.bigOrderAccountsTotal - v));
        } else {
            updatePayment(PriceUtils.round(orderPrepareBean.bigOrderAccountsTotal));
        }

        //获取商品数量
        productTotleCountTxt.setText(String.format("%s件商品", mOrderModel.getType().getProductCount()));

        //获取可用优惠券信息
        mOrderModel.setCouponList(orderPrepareBean.coupon_list);

        //获取优惠金额
        if (orderPrepareBean.promotion_amount == 0) {
            if (HaiUtils.getSize(orderPrepareBean.coupon_list) > 0) {
                couponTxt.setText("请选择优惠券");
                couponTxt.setTextColor(ContextCompat.getColor(mActivity, R.color.colorRed));
            } else {
                couponTxt.setText("暂无优惠券");
                couponTxt.setTextColor(ContextCompat.getColor(mActivity, R.color.colorGrayCCCCCC));

            }
        } else {
            couponTxt.setText(String.format("%s元", Math.round(orderPrepareBean.promotion_amount)));
            couponTxt.setTextColor(ContextCompat.getColor(mActivity, R.color.colorTab));
        }

        // 一个商家有多个商品，显示achate_note
        if (HaiUtils.getSize(orderPrepareBean.storelist) > 0) {
            for (OrderPrepareBean.StorelistBean storelistBean : orderPrepareBean.storelist) {
                if (HaiUtils.getSize(storelistBean.productList) > 1) {
                    llytAchatNote.setVisibility(View.VISIBLE);
                    tvAchatNote.setText(orderPrepareBean.achat_note);
                    rbAggress.setOnClickListener(v -> {
                        ivAggress.setVisibility(View.VISIBLE);
                        ivReject.setVisibility(View.GONE);
                        tvAggress.setEnabled(true);
                        tvReject.setEnabled(false);
                        isAggress = true;
                        isReject = false;
                    });
                    rbReject.setOnClickListener(v -> {
                        ivAggress.setVisibility(View.GONE);
                        ivReject.setVisibility(View.VISIBLE);
                        tvAggress.setEnabled(false);
                        tvReject.setEnabled(true);
                        isAggress = false;
                        isReject = true;
                    });
                    return;
                } else {
                    llytAchatNote.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SHIPPING_FLAG:
                    AddressItemBean bean = data.getParcelableExtra(AddressItemBean.class.getName());
                    mOrderModel.setAddressBean(bean);
                    setAddressInfo();
                    break;
                case COUPON_FLAG:
                    String code = data.getStringExtra("code");
                    mOrderModel.setCouponId(code);
                    tracePre.coupon_code = code;

                    showProgressDialog("计算优惠后金额...", true);
                    OrderRepository.getInstance()
                            .orderPrepare(mOrderModel)
                            .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                            .subscribe(new DefaultSubscriber<OrderPrepareBean>() {
                                @Override
                                public void onSuccess(OrderPrepareBean orderPrepareBean) {
                                    setOrderInfo(orderPrepareBean);
                                }

                                @Override
                                public void onFinish() {
                                    dismissProgressDialog();
                                }
                            });
                    break;
            }
        } else {
            if (requestCode == SHIPPING_FLAG) {
                addressVisible = data.getBooleanExtra("addressVisible", false);
                if (!addressVisible) {
                    mNoAddressTxt.setVisibility(View.VISIBLE);
                    mDefaultTxt.setVisibility(View.GONE);
                    mArrImg.setVisibility(View.VISIBLE);
                    nameAndPhoneTxt.setVisibility(View.INVISIBLE);
                    nameAndPhoneTxt.setText("");
                    addressTxt.setVisibility(View.INVISIBLE);
                    addressTxt.setText("");

                }
            }
        }
    }

    public static void toThisActivity(Activity activity, OrderModel.CartType cartType, int from) {
        Intent intent = new Intent(activity, OrderCreateActivity.class);
        intent.putExtra(OrderModel.CartType.class.getName(), cartType);
        intent.putExtra("from", from);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
    }

    @Override
    public void onBackPressed() {
        if (from == 0) {
            EventBus.getDefault().post(new OrderCreateBackEvent());
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "cart=" + from;

    }
}
