package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.OrderChangeEvent;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.firstpage.CenterActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

public class PayResultActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)               DynamicHeaderView headerView;
    @BindView(R.id.payStatusImg)        ImageView         payStatusImg;
    @BindView(R.id.payStatusTxt)        TextView          payStatusTxt;
    @BindView(R.id.payStatusBtn)        Button            payStatusBtn;
    @BindView(R.id.hangBtn)             Button            hangBtn;
    @BindView(R.id.payStatusRootLayout) LinearLayout      payStatusRootLayout;
    @BindView(R.id.remindTxt)           TextView          remindTxt;

    private boolean mPaySuccess;
    private String  mOrderId;
    private final String CANCEL_PAY = "用户取消支付";
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id = " + mOrderId;
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            mPaySuccess = intent.getBooleanExtra("pay_success", false);
            mOrderId = intent.getStringExtra("order_id");
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();

    }

    private void initViews(Bundle savedInstanceState) {
        headerView.setHeaderExtraListener(v -> jumpControl());
        payStatusRootLayout.setVisibility(View.VISIBLE);
        payStatusImg.setImageResource(mPaySuccess ? R.mipmap.ic_pay_success_2 : R.mipmap.ic_pay_fail);
        payStatusTxt.setText(mPaySuccess ? "已付款成功" : "付款失败");
        remindTxt.setText(mPaySuccess ? "我们会尽快为你发货" : CANCEL_PAY);
        payStatusBtn.setText(mPaySuccess ? "订单详情" : "重新支付");
        hangBtn.setVisibility(mPaySuccess ? View.VISIBLE : View.GONE);
        payStatusBtn.setTag(mPaySuccess);
        //发送一条通知
        if (mPaySuccess) {
            EventBus.getDefault().post(new OrderChangeEvent());
        }
        // GA Tracker
        mTracker.setScreenName(mPaySuccess ? "支付成功" : "支付失败");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void jumpControl() {
        boolean paySuccess = (boolean) payStatusBtn.getTag();
        if (paySuccess) {
            finish();
        } else {
            Intent intent = new Intent(mActivity, OrderDetailActivity.class);
            intent.putExtra("order_id", mOrderId);
            intent.putExtra("order_status", "NEW");
            mActivity.startActivity(intent);
            finish();
        }
    }

    public static void toThisActivity(Context context, String orderId, boolean paySuccess) {
        Intent intent = new Intent(context, PayResultActivity.class);
        intent.putExtra("pay_success", paySuccess);
        intent.putExtra("order_id", orderId);
        context.startActivity(intent);
    }

    @OnClick(R.id.hangBtn)
    public void onHangClick(View view) {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_Hot, "", this, TraceUtils.PageCode_PayResult, TraceUtils.PositionCode_GoAround + "", "");
        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_GoAround, TraceUtils.Event_Category_Click, "", null, "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_Hot, TraceUtils.PageCode_PayResult, TraceUtils.PositionCode_GoAround, "");

        Intent intent = new Intent(this, CenterActivity.class);
        intent.putExtra("position", 0);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.payStatusBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payStatusBtn:
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_OrderDetail, mOrderId + "", mActivity, TraceUtils.PageCode_PayResult, TraceUtils.PositionCode_ViewOrder + "", "");
                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_ViewOrder, TraceUtils.Event_Category_Click, "", null, "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_OrderDetail, TraceUtils.PageCode_PayResult, TraceUtils.PositionCode_ViewOrder, mOrderId);

                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(getString(R.string.key_order_id), mOrderId);
                startActivity(intent);
                finish();
                break;
        }
    }
}
