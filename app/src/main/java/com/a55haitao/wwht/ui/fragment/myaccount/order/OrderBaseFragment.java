package com.a55haitao.wwht.ui.fragment.myaccount.order;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.order.OrderListAdapter;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.event.OrderChangeEvent;
import com.a55haitao.wwht.data.interfaces.OrderPayModel;
import com.a55haitao.wwht.data.model.entity.CommonSuccessResult;
import com.a55haitao.wwht.data.model.entity.EnsureRecievedBean;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.data.model.entity.OrderDelResult;
import com.a55haitao.wwht.data.model.result.CancelReasonResult;
import com.a55haitao.wwht.data.model.result.OrderListResult;
import com.a55haitao.wwht.data.model.result.OrderListResult.OrderListBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.OrderDetailActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.TransferDetailActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.DividerItemDecoration;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.wxapi.WXPayEntryActivity;
import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 我的订单 BaseFragment
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderAllFragment 全部订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderUnPayFragment 未付款订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderUnReceiveFragment 未收货订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderCompleteFragment 已完成订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderUnDeliverFragment 未发货订单}
 * Created by 陶声 on 16/7/22.
 */
public abstract class OrderBaseFragment extends BaseFragment {
    @BindView(R.id.content_view)      RecyclerView       mRvContent;  // 内容
    @BindView(R.id.msv)               MultipleStatusView mSv;         // 多状态布局
    @BindView(R.id.swipe)             SwipeRefreshLayout mSwipe;      // 下拉刷新
    @BindColor(R.color.color_swipe)   int                colorSwipe;  // 下拉刷新颜色
    @BindDrawable(R.drawable.divider) Drawable           DIVIDER_BG;  // 分割线

    @BindDimen(R.dimen.margin_medium) int DIVIDER_WIDTH;  // 分割线高度

    @BindColor(R.color.colorGray666666) int mColorGrey666;

    protected static final long DELAY_TIME = 1000;      // 刷新时间

    protected Unbinder            mUnbinder;
    protected int                 mCurrentPage;         // 当前页
    protected int                 mAllPage;             // 总页数
    protected OrderListAdapter    mAdapter;
    protected List<OrderListBean> mOrderList;           // 订单列表数据
    private   AlertDialog         mDlgConfirmReceived;  // 确认收货Dialog
    private   OptionsPickerView   mPickerView;          // 取消订单理由弹出框
    private   ArrayList<String>   mOptions;             // 取消订单理由
    private   ArrayList<String>   skulist;             //

    private String payType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, null, false);
        mUnbinder = ButterKnife.bind(this, v);
        initVariables();
        initViews(v, savedInstanceState);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        skulist = new ArrayList<>();
        mCurrentPage = 1;
        mAllPage = 1;
        mOrderList = new ArrayList<>();
    }

    /**
     * 初始化布局
     */
    protected void initViews(View v, Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerItemDecoration divider = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, DIVIDER_BG);
        divider.setHeight(DIVIDER_WIDTH);
        mRvContent.addItemDecoration(divider);
        mAdapter = new OrderListAdapter(mOrderList, mFragment);
        mRvContent.setAdapter(mAdapter);

        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
                // 埋点
                YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

                Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                intent.putExtra("order_id", mAdapter.getData().get(position).order_id);
                mActivity.startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // order_id
                String orderId = mAdapter.getData().get(position).order_id;
                switch (view.getId()) {
                    case R.id.tv_cancel_order: // 取消订单
                        cancelOrder(orderId);
                        break;
                    case R.id.tv_delete_order: // 删除订单
                        orderDelete(orderId);
                        break;
                    case R.id.tv_pay_order: // 支付订单
                        doPay(orderId);
                        break;
                    case R.id.tv_shipping_info: // 物流详情
                        TransferDetailActivity.toThisActivity(getActivity(), orderId, "");
                        break;
                    case R.id.tv_confirm_received: // 确认收货
                        orderReceived(orderId);
                        break;
                }
            }
        });

        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                loadMore();
                //                if (mAdapter.getData().size() < PAGE_SIZE) {
                //                    mAdapter.loadMoreEnd(true);
                //                } else if (mCurrentPage < mAllPage) {
                //                    mCurrentPage++;
                //                    mSwipe.setEnabled(false);
                //                    requestOrderList();
                //                }
            });
        });
        // 刷新
        mSwipe.setOnRefreshListener(this::refreshData);
        // 重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    private void loadMore() {
        if (mCurrentPage < mAllPage) {
            mCurrentPage++;
            requestOrderList();
        }
    }

    protected void refreshData() {
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

    /**
     * 加载数据
     */
    public void loadData() {
        mCurrentPage = 1;
        mSwipe.setRefreshing(true);
        requestOrderList();
    }

    /**
     * 请求订单列表
     */
    private void requestOrderList() {
        OrderRepository.getInstance()
                .orderList(getOrderListType(), "", "", mCurrentPage)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<OrderListResult>() {
                    @Override
                    public void onSuccess(OrderListResult orderListResult) {
                        mCurrentPage = orderListResult.page;
                        mAllPage = orderListResult.totalPage;
                        initskulist(orderListResult);
                        if (orderListResult.totalCount == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            // 服务器获取订单列表
                            List<OrderListBean> orderList = orderListResult.order_list;

                            mSv.showContent();
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(orderList);

                                if (mCurrentPage < mAllPage) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    if (orderListResult.order_list.size() > 2) {
                                        mAdapter.loadMoreEnd();
                                    } else {
                                        mAdapter.loadMoreEnd(true);
                                    }
                                }

                            } else {
                                mAdapter.addData(orderList);
                                if (mCurrentPage < mAllPage) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            }

                            //                            if (orderListResult.page < orderListResult.totalPage) {
                            //                                mAdapter.loadMoreComplete();
                            //                            } else {
                            //                                if (HaiUtils.getSize(orderListResult.order_list) > 2) {
                            //                                    mAdapter.loadMoreEnd();
                            //                                } else {
                            //                                    mAdapter.setEnableLoadMore(false);
                            //                                }
                            //                            }
                        }
                        mHasData = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasData);
                        return mHasData;
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setEnabled(true);
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }

    /**
     * 获取订单中商品的skuid
     */
    private void initskulist(OrderListResult orderListResult) {
        if (orderListResult != null && HaiUtils.getSize(orderListResult.order_list) > 0) {
            for (OrderListBean orderListBean : orderListResult.order_list) {
                if (HaiUtils.getSize(orderListBean.detail) > 0) {
                    for (OrderListBean.DetailBean detailBean : orderListBean.detail) {
                        skulist.add(detailBean.skuid);
                    }
                }
            }
        }
    }

    /**
     * 订单类型
     */
    protected abstract int getOrderListType();

    /**
     * 删除订单
     *
     * @param orderId 订单号
     */
    private void orderDelete(String orderId) {
        new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                .setMessage("确定删除此订单?")
                .setPositiveButton("确认", (dialog, which) -> {
                    // 删除订单网络请求
                    OrderRepository.getInstance()
                            .orderDel(orderId)
                            .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                            .subscribe(new DefaultSubscriber<OrderDelResult>() {
                                @Override
                                public void onSuccess(OrderDelResult result) {
                                    if (TextUtils.equals(result.success, "1")) {
                                        ToastUtils.showToast(mActivity, "删除成功");
                                        EventBus.getDefault().post(new OrderChangeEvent());
                                    }
                                }

                                @Override
                                public void onFinish() {

                                }
                            });

                })
                .setNegativeButton("取消", (dialog, which) -> {
                }).show();
    }

    /**
     * 取消订单
     */
    private void cancelOrder(String orderId) {
        if (mOptions != null && mOptions.size() > 0) {
            showCancelReasons(orderId);
        } else {
            mOptions = new ArrayList<>();
            showProgressDialog();
            OrderRepository.getInstance()
                    .cancelReasons()
                    .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                    .subscribe(new DefaultSubscriber<CancelReasonResult>() {
                        @Override
                        public void onSuccess(CancelReasonResult result) {
                            List<CancelReasonResult.ResonListBean> reasonList = result.reson_list;
                            for (CancelReasonResult.ResonListBean reason : reasonList) {
                                mOptions.add(reason.reson_note);
                            }
                            showCancelReasons(orderId);

                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    });
        }
    }

    /**
     * 取消订单理由弹窗
     */
    private void showCancelReasons(String orderId) {
        mPickerView = new OptionsPickerView.Builder(mActivity, (options1, options2, options3, v) -> requestOrderCancel(orderId, mOptions.get(options1)))
                .setCyclic(false, false, false)
                .setSubmitText("确定")
                .setCancelText("取消")
                .setSubmitColor(mColorGrey666)
                .setCancelColor(mColorGrey666)
                .setSubCalSize(17)
                .setContentTextSize(20)
                .build();
        mPickerView.setPicker(mOptions);
        mPickerView.show();
    }


    /**
     * 取消订单请求
     *
     * @param orderId 订单号
     */
    private void requestOrderCancel(String orderId, String reason) {
        OrderRepository.getInstance()
                .orderCancel(orderId, reason)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
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
     * 确认收货
     */
    private void orderReceived(String orderId) {
        if (mDlgConfirmReceived == null) {
            mDlgConfirmReceived = new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                    .setMessage("确认收货")
                    .setPositiveButton("确认", (dialog, which) -> requestOrderReceived(orderId))
                    .setNegativeButton("取消", null)
                    .create();
        }
        mDlgConfirmReceived.show();
    }

    /**
     * 确认收货网络请求
     */
    private void requestOrderReceived(String orderId) {
        OrderRepository.getInstance()
                .orderConfirm(orderId)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<EnsureRecievedBean>() {
                    @Override
                    public void onSuccess(EnsureRecievedBean result) {
                        if (TextUtils.equals(result.success, "1")) {
                            // 埋点
                            HashMap<String, String> kv = new HashMap<>();
                            kv.put(TraceUtils.Event_Kv_Order_Id, orderId);
                            kv.put(TraceUtils.Event_Kv_Code, "0");
                            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", "", TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Sign, kv, "");

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
                        kv.put(TraceUtils.Event_Kv_Order_Id, orderId);
                        kv.put(TraceUtils.Event_Kv_Code, "1");
                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", "", TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Sign, kv, "");

                        return super.onFailed(e);
                    }
                });
    }

    /**
     * 去支付
     */
    private void doPay(String orderId) {
        ActionSheet.createBuilder(mActivity, getActivity().getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("微信", "支付宝")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        payType = "";

                        switch (index) {
                            case 0:
                                payType = OrderPayModel.PAY_TYPE_WX;
                                break;
                            case 1:
                                payType = OrderPayModel.PAY_TYPE_ALI;
                                break;
                        }
                        OrderPayModel orderPayModel = new OrderPayModel(null, null);
                        orderPayModel.setPayType(payType);

                        showProgressDialog(StringConstans.TOAST_ORDER_COMMIT_2, true);

                        OrderRepository.getInstance()
                                .orderCommit(orderId, payType)
                                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                                .subscribe(new DefaultSubscriber<OrderCommitBean>() {
                                    @Override
                                    public void onSuccess(OrderCommitBean result) {

                                        // 埋点
                                        HashMap<String, String> kv = new HashMap<>();
                                        kv.put(TraceUtils.Event_Kv_Order_Id, result.order_id);
                                        kv.put(TraceUtils.Event_Kv_Order_Amount, result.amount + "");
                                        kv.put(TraceUtils.Event_Kv_Orderr_PayType, payType);
                                        kv.put(TraceUtils.Event_Kv_Order_CashType, "rmb");
                                        kv.put(TraceUtils.Event_Kv_Orderr_Tradeno, result.trade_no);
                                        kv.put(TraceUtils.Event_Kv_Code, "0");
                                        String orderinfo = new Gson().toJson(result);
                                        kv.put(TraceUtils.Event_Kv_Order_OrderInfo, orderinfo);
                                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Pay, kv, "");

                                        WXPayEntryActivity.toThisActivity(getActivity(),
                                                "",
                                                result.order_id,
                                                result.paydata,
                                                skulist,
                                                (int) result.amount,
                                                orderPayModel.getPayType().equals("wechat"),
                                                "1".equals(result.need_pay_online));
                                    }

                                    @Override
                                    public void onFinish() {
                                        dismissProgressDialog();
                                    }

                                    @Override
                                    public boolean onFailed(Throwable e) {
                                        // 埋点
                                        HashMap<String, String> kv = new HashMap<>();
                                        kv.put(TraceUtils.Event_Kv_Order_Id, orderId);
                                        kv.put(TraceUtils.Event_Kv_Order_Amount, "");
                                        kv.put(TraceUtils.Event_Kv_Orderr_PayType, payType);
                                        kv.put(TraceUtils.Event_Kv_Order_CashType, "rmb");
                                        kv.put(TraceUtils.Event_Kv_Orderr_Tradeno, "");
                                        kv.put(TraceUtils.Event_Kv_Code, "1");

                                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Pay, kv, "");

                                        return super.onFailed(e);
                                    }
                                });
                    }
                }).show();

    }

    @Subscribe
    public void onOrderChangeEvent(OrderChangeEvent event) {
        refreshData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSwipe.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHasData = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
