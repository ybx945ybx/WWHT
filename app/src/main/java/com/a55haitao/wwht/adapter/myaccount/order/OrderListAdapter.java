package com.a55haitao.wwht.adapter.myaccount.order;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.OrderListLayoutType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.result.OrderListResult;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 订单列表Adapter
 *
 * @author 陶声
 * @since 210-18
 */

public class OrderListAdapter extends BaseMultiItemQuickAdapter<OrderListResult.OrderListBean, BaseViewHolder> {

    private Activity mActivity;
    private Fragment mFragment;
    private int      mPicSize;
    private int      mPicMargin;
    private int      mColorRed;
    private int      mColorOrange;
    private int      mColorGrey333;
    private int      mColorGrey999;
    private long     mTimeLag;

    public OrderListAdapter(List<OrderListResult.OrderListBean> data, Fragment fragment) {
        super(data);
        addItemType(OrderListLayoutType.UNPAY, R.layout.item_order_unpay);
        addItemType(OrderListLayoutType.NORMAL, R.layout.item_order_normal);
        addItemType(OrderListLayoutType.SHIPPING, R.layout.item_order_shipping);
        addItemType(OrderListLayoutType.COMPLETE, R.layout.item_order_complete);
        addItemType(OrderListLayoutType.CANCEL, R.layout.item_order_cancel);
        initVars(fragment);
    }

    private void initVars(Fragment fragment) {
        mFragment = fragment;
        mActivity = mFragment.getActivity();
        mTimeLag = -1;
        mPicSize = DisplayUtils.dp2px(mActivity, 70);
        mPicMargin = DisplayUtils.dp2px(mActivity, 5);
        mColorOrange = ContextCompat.getColor(mActivity, R.color.order_orange);
        mColorRed = ContextCompat.getColor(mActivity, R.color.colorRed);
        mColorGrey333 = ContextCompat.getColor(mActivity, R.color.colorGray333333);
        mColorGrey999 = ContextCompat.getColor(mActivity, R.color.colorGray999999);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListResult.OrderListBean order) {
        helper.setText(R.id.tv_order_number, "订单号:  " + order.order_id)
                .setText(R.id.tv_order_status, order.order_status_name) // 订单状态
                .setText(R.id.tv_product_count, String.format("共%d件 总计：", order.detailCount)) // 订单商品个数
                .setText(R.id.tv_order_price, HaiConstants.RMB_UNICODE + String.valueOf(PriceUtils.round(order.total))); // 订单总价

        switch (helper.getItemViewType()) {
            case OrderListLayoutType.UNPAY:
                if (mTimeLag == -1) {
                    mTimeLag = order.now * 1000L - System.currentTimeMillis();
                }
                long time = order.overtime * 1000L - System.currentTimeMillis() - mTimeLag;
                helper.setText(R.id.tv_order_info, time >= 0 ? String.format("剩余%d分%d秒, 超时订单自动取消", time / 1000 / 60, time / 1000 % 60) : "")
                        .setTextColor(R.id.tv_order_status, mColorRed)
                        .setTextColor(R.id.tv_order_info, mColorRed)
                        .addOnClickListener(R.id.tv_cancel_order)
                        .addOnClickListener(R.id.tv_pay_order);
                break;
            case OrderListLayoutType.NORMAL:
                helper.setTextColor(R.id.tv_order_status, mColorOrange);
                break;
            case OrderListLayoutType.SHIPPING:
                helper.setTextColor(R.id.tv_order_status, mColorOrange)
                        .addOnClickListener(R.id.tv_shipping_info)
                        .addOnClickListener(R.id.tv_confirm_received);
                break;
            case OrderListLayoutType.COMPLETE:
                helper.setTextColor(R.id.tv_order_status, mColorGrey333)
                        .addOnClickListener(R.id.tv_delete_order)
                        .addOnClickListener(R.id.tv_shipping_info);
                break;
            case OrderListLayoutType.CANCEL:
                helper.setTextColor(R.id.tv_order_status, mColorGrey999)
                        .addOnClickListener(R.id.tv_delete_order);
                break;
        }

        // 商品图片
        LinearLayout mLlImgContainer = helper.getView(R.id.ll_img_container);
        mLlImgContainer.removeAllViews();
        for (int i = 0; i < order.detail.size(); i++) {
            ImageView                 img    = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mPicSize, mPicSize);
            if (i != 0) {
                params.leftMargin = mPicMargin;
            }
            img.setLayoutParams(params);
            img.setBackgroundResource(R.drawable.border_grey);
            //            String coverImgUrl = HaiUtils.transformProductThumail(order.detail.get(i).coverImgUrl, mPicSize, mPicSize);
            UPaiYunLoadManager.loadImage(mContext, order.detail.get(i).coverImgUrl, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_small, img);
            mLlImgContainer.addView(img);
        }


    }
}
