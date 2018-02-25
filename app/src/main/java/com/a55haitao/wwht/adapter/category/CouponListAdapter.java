package com.a55haitao.wwht.adapter.category;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.CouponStatus;
import com.a55haitao.wwht.data.model.entity.CouponBean;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 优惠券列表Adapter
 *
 * @author 陶声
 * @since 2017-05-09
 */

public class CouponListAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {

    public CouponListAdapter(List<CouponBean> data) {
        super(R.layout.item_coupon_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean item) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        helper.setText(R.id.smallSizeTxt, item.getShow_desc());
        if (!TextUtils.isEmpty(item.getShow_desc())) {
            helper.setVisible(R.id.smallSizeTxt, true)
                    .setText(R.id.smallSizeTxt, item.getShow_desc());
        } else {
            helper.setVisible(R.id.smallSizeTxt, false);
        }
        helper.setText(R.id.bigSizeTxt, item.getShow_title().replace("￥", "\u00A5"))
                .setText(R.id.couponNameTxt, item.getTitle())
                .setText(R.id.couponConditionTxt, item.getSubtitle())
                .setText(R.id.couponDateTxt, String.format("%s-%s",
                        sdf.format(new Date(1000 * item.getBegintime())),
                        sdf.format(new Date(1000 * item.getEndtime()))))
                .setVisible(R.id.choiceImgView, item.isSelect())
                .setVisible(R.id.couponStatusImg, CouponStatus.UNUSED != item.getStatus())
                .setTextColor(R.id.couponNameTxt, ContextCompat.getColor(mContext,
                        item.getStatus() == 1 ? R.color.colorTextYellow : R.color.colorGrayCCCCCC))
                .setTextColor(R.id.bigSizeTxt, CouponStatus.UNUSED == item.getStatus() ?
                        ContextCompat.getColor(mContext, R.color.colorTextYellow) : ContextCompat.getColor(mContext, R.color.colorGrayCCCCCC))
                .setTextColor(R.id.smallSizeTxt, CouponStatus.UNUSED == item.getStatus() ?
                        ContextCompat.getColor(mContext, R.color.colorTextYellow) : ContextCompat.getColor(mContext, R.color.colorGrayCCCCCC))
                .setTextColor(R.id.couponConditionTxt, CouponStatus.UNUSED == item.getStatus() ?
                        ContextCompat.getColor(mContext, R.color.colorGray666666) : ContextCompat.getColor(mContext, R.color.colorGrayCCCCCC))
                .setTextColor(R.id.couponDateTxt, CouponStatus.UNUSED == item.getStatus() ?
                        ContextCompat.getColor(mContext, R.color.colorGray999999) : ContextCompat.getColor(mContext, R.color.colorGrayCCCCCC))
                .setBackgroundRes(R.id.couponInsideLayout, CouponStatus.UNUSED == item.getStatus() ?
                        R.mipmap.coupon_inside_usless_bg : R.mipmap.coupon_inside_gray_bg);

        switch (item.getStatus()) {
            case CouponStatus.OUTDATED:
                helper.setImageResource(R.id.couponStatusImg, R.mipmap.ic_coupon_out_of_date);
                break;
            case CouponStatus.USED:
                helper.setImageResource(R.id.couponStatusImg, R.mipmap.ic_coupon_used);
                break;
            case CouponStatus.UNAVAILABLE:
                helper.setImageResource(R.id.couponStatusImg, R.mipmap.ic_coupon_unavailable);
                break;
        }

        FrameLayout                  flCouponContainer = helper.getView(R.id.fl_coupon_container);
        ViewGroup.MarginLayoutParams lp                = (ViewGroup.MarginLayoutParams) flCouponContainer.getLayoutParams();
        if (mData.indexOf(item) == mData.size() - 1) {
            lp.bottomMargin = DisplayUtils.dp2px(mContext, 10);
        } else {
            lp.bottomMargin = 0;
        }
        flCouponContainer.setLayoutParams(lp);
    }
}
