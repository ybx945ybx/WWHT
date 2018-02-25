package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ExpandableListView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.shoppingcart.CanRefundProductListAdapter;
import com.a55haitao.wwht.data.model.result.CanRefundProductListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请退款可选择受理商品列表
 * Created by a55 on 2017/6/7.
 */

public class SelectRefundProductActivity extends BaseNoFragmentActivity {

    @BindView(R.id.msView)           MultipleStatusView msView;
    @BindView(R.id.content_view)     SwipeRefreshLayout swipe;
    @BindView(R.id.content_view_exp) ExpandableListView rycvProducts;
    @BindView(R.id.tv_commit)        HaiTextView        tvCommit;
    @BindView(R.id.headRightTxt)     HaiTextView        tvSelectAll;

    @BindString(R.string.key_order_id) String KEY_ORDER_ID;

    private CanRefundProductListAdapter mAdapter;

    private CanRefundProductListResult canRefundProductListResult;
    private String                     order_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_refund_products);
        ButterKnife.bind(this);

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
            order_id = intent.getStringExtra(KEY_ORDER_ID);
            String products = intent.getStringExtra("products");
            canRefundProductListResult = new Gson().fromJson(products, CanRefundProductListResult.class);
        }
    }

    private void initViews() {
        msView.setOnRetryClickListener(v -> initData());

        swipe.setOnRefreshListener(() -> getCanRefundProductList());

    }

    private void initData() {
        getData();

    }

    private void getData() {
        if (HaiUtils.getSize(canRefundProductListResult.storelist) > 0) {
            setListView();
        } else {
            swipe.setRefreshing(true);
            getCanRefundProductList();
        }
    }

    private void getCanRefundProductList() {
        OrderRepository.getInstance()
                .getCanRefundProductList(order_id)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CanRefundProductListResult>() {
                    @Override
                    public void onSuccess(CanRefundProductListResult canRefundProductListResult) {
                        SelectRefundProductActivity.this.canRefundProductListResult = canRefundProductListResult;
                        setListView();
                    }

                    @Override
                    public void onFinish() {
                        mHasLoad = true;
                        swipe.setRefreshing(false);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }
                });

    }

    private void setListView() {
        msView.showContent();

        setAdapter(canRefundProductListResult);

        rycvProducts.setGroupIndicator(null);
        rycvProducts.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            return true;
        });

        tvCommit.setText("确定（已选" + canRefundProductListResult.getSelectedProductedCount() + "件）");

        if (canRefundProductListResult.getAllSelected()) {
            tvSelectAll.setText("取消全选");
        } else {
            tvSelectAll.setText("全选");
        }
    }

    private void setAdapter(CanRefundProductListResult canRefundProductListResult) {
        mAdapter = new CanRefundProductListAdapter(mActivity);
        mAdapter.setCanRefundProductData(canRefundProductListResult);
        rycvProducts.setAdapter(mAdapter);
        // 展开所有子项
        expandAllGroup();

        // 设置Adapter的Listener
        mAdapter.setmChangeListener((headerView, store_id, skuid, checked) -> {
            if (headerView) {
                canRefundProductListResult.selectAll(store_id, checked);
                updateContent();
            } else {
                canRefundProductListResult.selectCartId(store_id, skuid, checked);
                updateContent();
            }
        });
    }

    /**
     * 更新界面状态
     */
    private void updateContent() {
        mAdapter.setCanRefundProductData(canRefundProductListResult);
        mAdapter.notifyDataSetChanged();
        expandAllGroup();
        tvCommit.setText("确定（已选" + canRefundProductListResult.getSelectedProductedCount() + "件）");

        if (canRefundProductListResult.getAllSelected()) {
            tvSelectAll.setText("取消全选");
        } else {
            tvSelectAll.setText("全选");
        }
    }

    /**
     * 打开所有子项
     */
    private void expandAllGroup() {
        if (canRefundProductListResult != null && canRefundProductListResult.storelist != null)
            for (int i = 0; i < canRefundProductListResult.storelist.size(); i++) {
                rycvProducts.expandGroup(i);
            }
    }

    /**
     * 全选
     */
    @OnClick(R.id.headRightTxt)
    public void selectAll() {
        canRefundProductListResult.selectAll(tvSelectAll.getText().toString().equals("全选") ? true : false);
        updateContent();
    }

    /**
     * 确定
     */
    @OnClick(R.id.tv_commit)
    public void commit() {
        Intent intent   = new Intent();
        String products = new Gson().toJson(canRefundProductListResult);
        intent.putExtra("products", products);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
