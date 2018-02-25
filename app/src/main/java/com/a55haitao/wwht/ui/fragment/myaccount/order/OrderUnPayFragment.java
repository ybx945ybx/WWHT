package com.a55haitao.wwht.ui.fragment.myaccount.order;

import android.os.Handler;
import android.os.Message;

import com.a55haitao.wwht.ui.activity.myaccount.OrderListActivity;

/**
 * 代付款订单
 * {@link OrderListActivity 我的订单页面}
 * Created by 陶声 on 16/7/22.
 */
public class OrderUnPayFragment extends OrderBaseFragment {
    // 处理商品倒计时
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();

            mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
        }
    };


    @Override
    protected int getOrderListType() {
        return 1;
    }

    @Override
    public void onResume() {
        mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
        super.onResume();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}
