package com.a55haitao.wwht.adapter.firstpage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.ui.view.CountDownTimerView;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by a55 on 2017/3/17.
 */

public class FlashSaleAdapter extends BaseQuickAdapter<ProductBaseBean, BaseViewHolder> {

    private Context mContext;

    public FlashSaleAdapter(List<ProductBaseBean> data, Context mContext) {
        super(R.layout.flash_sale_list_item, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBaseBean item) {
        long sysTime = HaiApplication.systemTime / 1000;
        helper.setText(R.id.tv_title, item.brand + item.name)
                .setText(R.id.tv_site, item.sellerInfo.nameen + "官网发货")
                .setText(R.id.tv_origin_price, "市场价" + PriceUtils.toRMBFromFen(item.mallPrice))
                .setText(R.id.tv_discount, PriceUtils.toAnyCurrencyFromFen("", item.realPrice, false))
                .setText(R.id.btn_buy, isFinish(sysTime, item.end_time) ? "已结束" : "马上抢")
                .setImageResource(R.id.iv_timer, isFinish(sysTime, item.end_time) ? R.mipmap.ic_timer_gray : R.mipmap.ic_timer_red)
                .setVisible(R.id.viewline, helper.getLayoutPosition() == 0 ? false : true);
        UPaiYunLoadManager.loadImage(mContext, item.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_small, helper.getView(R.id.iv_goods));
//        Glide.with(mContext)
//                .load(UpyUrlManager.getUrl(item.coverImgUrl, 140))
//                .placeholder(R.mipmap.ic_default_background)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .dontAnimate()
//                .into((ImageView) helper.getView(R.id.iv_goods));
        helper.getView(R.id.btn_buy).setEnabled(isFinish(sysTime, item.end_time) ? false : true);

        if (item.sellerInfo != null && item.sellerInfo.country != null) {
            int flagResourceId = HaiUtils.getFlagResourceId(item.sellerInfo.country, false);
            if (flagResourceId == -1) {
                int px20 = HaiConstants.CompoundSize.PX_20;
                Glide.with(ActivityCollector.getTopActivity())
                        .load(item.sellerInfo.flag)
                        .override((int) (1.8 * px20), px20)
                        .into((ImageView) helper.getView(R.id.iv_flag));
            } else {
                ((ImageView) helper.getView(R.id.iv_flag)).setImageResource(flagResourceId);
            }
        }

        RelativeLayout     rlytTimer = (RelativeLayout) helper.getView(R.id.rlyt_timer);
        CountDownTimerView tvTimer   = (CountDownTimerView) helper.getView(R.id.tv_timer);
        tvTimer.setBackgroundRed();
        if (item.end_time == 0) {
            rlytTimer.setVisibility(View.INVISIBLE);
        } else {
            rlytTimer.setVisibility(View.VISIBLE);
            if (isFinish(sysTime, item.end_time)) {
                tvTimer.setTime(0, 0, 0);

            } else {
                float[] time = HaiTimeUtils.counterTimeLong(String.valueOf(sysTime), String.valueOf(item.end_time));
                tvTimer.setTime((int) time[2], (int) time[1], (int) time[0]);
                tvTimer.start();
            }
        }
    }

    private boolean isFinish(long sysTime, long end_time) {
        if (end_time == 0) {
            return false;
        }
        if (end_time - sysTime < 0) {
            return true;
        }
        return false;
    }


}
