package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.data.event.OrderChangeEvent;
import com.a55haitao.wwht.data.model.annotation.RefundStateType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.result.OrderRefundListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 退款／售后进度列表
 * Created by a55 on 2017/6/8.
 */

public class RefundListActivity extends BaseNoFragmentActivity {
    @BindView(R.id.headerView)   DynamicHeaderView  headerView;
    @BindView(R.id.msv)          MultipleStatusView msView;
    @BindView(R.id.swipe)        SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.content_view) RecyclerView       rycvRefund;

    @BindString(R.string.key_order_id)  String KEY_ORDER_ID;
    @BindString(R.string.key_refund_no) String KEY_REFUND_NO;

    private RVBaseAdapter<OrderRefundListResult.OrderRefundListBean> mAdapter;

    private int mPicSize;
    private int mPicMargin;

    private String order_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initData();
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra("order_id");
        }

        mPicSize = DisplayUtils.dp2px(this, 60);
        mPicMargin = DisplayUtils.dp2px(this, 10);

    }

    private void initViews() {
        headerView.setHeaderRightHidden();

        swipeRefreshLayout.setOnRefreshListener(() -> initData());

        msView.setOnRetryClickListener(v -> initData());

        rycvRefund.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVBaseAdapter<OrderRefundListResult.OrderRefundListBean>(this, new ArrayList<>(), R.layout.refund_list_item) {
            @Override
            public void bindView(RVBaseHolder holder, OrderRefundListResult.OrderRefundListBean orderRefundListBean) {
                // 申请单号
                holder.setText(R.id.tv_order_number, "申请单号:  " + orderRefundListBean.refund_no);
                // 申请单状态
                HaiTextView tvState = (HaiTextView) holder.getView(R.id.tv_order_status);
                setRefundState(tvState, orderRefundListBean);
                // 商品个数
                holder.setText(R.id.tv_product_count, String.format("共%d件商品", orderRefundListBean.goods_size));
                // 商品图片
                LinearLayout mLlImgContainer = holder.getView(R.id.ll_img_container);
                mLlImgContainer.removeAllViews();
                for (int i = 0; i < orderRefundListBean.order_refund_items.size(); i++) {
                    ImageView                 img    = new ImageView(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mPicSize, mPicSize);
                    if (i != 0) {
                        params.leftMargin = mPicMargin;
                    }
                    img.setLayoutParams(params);
                    img.setBackgroundResource(R.drawable.border_grey);
                    UPaiYunLoadManager.loadImage(mContext, orderRefundListBean.order_refund_items.get(i).cover_img_url, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_small, img);
                    mLlImgContainer.addView(img);
                }

                holder.itemView.setTag(orderRefundListBean.refund_no);

            }
        };
        mAdapter.setOnItemClickListener(v -> {

            Intent intent = new Intent(RefundListActivity.this, RefundDetailActivity.class);
            intent.putExtra(KEY_ORDER_ID, order_id);
            intent.putExtra(KEY_REFUND_NO, String.valueOf((int) v.getTag()));
            startActivity(intent);
        });
        rycvRefund.setAdapter(mAdapter);

    }

    private void setRefundState(HaiTextView tvState, OrderRefundListResult.OrderRefundListBean orderRefundListBean) {
        tvState.setText(orderRefundListBean.refund_status_name);
        switch (orderRefundListBean.refund_status) {
            case RefundStateType.ORDER_REFUND_REQUEST:
            case RefundStateType.ORDER_REFUND_ACCEPTED:
                tvState.setTextColor(ContextCompat.getColor(mActivity, R.color.product_status_red));
                break;
            case RefundStateType.ORDER_REFUND_SUCCESS:
                tvState.setTextColor(ContextCompat.getColor(mActivity, R.color.product_status_orange));
                break;
            case RefundStateType.ORDER_REFUND_CANCEL:
            case RefundStateType.ORDER_REFUND_USER_CANCEL:
                tvState.setTextColor(ContextCompat.getColor(mActivity, R.color.colorGray999999));
                break;
        }
    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        getRefundList();
    }

    /**
     * 获取退款申请单列表
     */
    private void getRefundList() {
        OrderRepository.getInstance()
                .getRefundList(order_id)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<OrderRefundListResult>() {
                    @Override
                    public void onSuccess(OrderRefundListResult orderRefundListResult) {
                        msView.showContent();
                        mAdapter.changeDatas(orderRefundListResult.order_refund_list);
                        mHasLoad = true;
                    }

                    @Override
                    public void onFinish() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    /**
     * 申请单状态有改变时刷新列表
     */
    @Subscribe
    public void onRefundChange(OrderChangeEvent orderChangeEvent) {
        getRefundList();
    }

}
