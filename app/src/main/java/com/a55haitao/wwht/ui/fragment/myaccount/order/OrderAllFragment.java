package com.a55haitao.wwht.ui.fragment.myaccount.order;

import android.os.Handler;
import android.os.Message;

/**
 * 全部订单
 * <p>
 * Created by 陶声 on 16/7/22.
 */
public class OrderAllFragment extends OrderBaseFragment {

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
    public void onResume() {
        mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
        super.onResume();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    protected int getOrderListType() {
        return 0;
    }
}
