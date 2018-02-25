package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by a55 on 2017/6/7.
 */

public class RefundCommitResultActivity extends BaseNoFragmentActivity {

    @BindView(R.id.headerView) DynamicHeaderView headerView;

    @BindString(R.string.key_order_id)  String KEY_ORDER_ID;
    @BindString(R.string.key_refund_no) String KEY_REFUND_NO;

    private String order_id;
    private String refund_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_commit_result);
        ButterKnife.bind(this);

        initVars();
        initViews();
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra(KEY_ORDER_ID);
            refund_no = intent.getStringExtra(KEY_REFUND_NO);
        }
    }

    private void initViews() {
        headerView.setHeaderRightHidden();
    }


    /**
     * 返回查看订单详情
     */
    @OnClick(R.id.tv_goto_order_detail)
    public void gotoOrderDetail() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_OrderDetail, order_id, this, TraceUtils.PageCode_RefundCommitResult, TraceUtils.PositionCode_ViewOrder + "", "");

//        TraceUtils.addAnalysAct(TraceUtils.PageCode_OrderDetail, TraceUtils.PageCode_RefundCommitResult, TraceUtils.PositionCode_ViewOrder, order_id);

        onBackPressed();
    }

    /**
     * 查看申请单详情
     */
    @OnClick(R.id.tv_goto_apply_detail)
    public void gotoApplyDetail() {
        // 埋点
//        TraceUtils.addAnalysAct(TraceUtils.PageCode_RefundDetail, TraceUtils.PageCode_RefundCommitResult, TraceUtils.PositionCode_ViewAfterSale, refund_no);

        Intent intent = new Intent(this, RefundDetailActivity.class);
        intent.putExtra(KEY_ORDER_ID, order_id);
        intent.putExtra(KEY_REFUND_NO, refund_no);
        startActivity(intent);
        finish();
    }
}
