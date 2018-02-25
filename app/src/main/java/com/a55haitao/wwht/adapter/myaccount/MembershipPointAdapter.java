package com.a55haitao.wwht.adapter.myaccount;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.GetMembershipPointHistoryResult.MembershipPointBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 个人中心 - 积分历史记录列表 Adapter
 *
 * @author 陶声
 * @since 2016-11-03
 */

public class MembershipPointAdapter extends BaseQuickAdapter<MembershipPointBean, BaseViewHolder> {
    public MembershipPointAdapter(List<MembershipPointBean> data) {
        super(R.layout.item_membership_point, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MembershipPointBean data) {
        //        int      colorPlus  = mContext.getResources().getColor(R.color.membership_point_plus);
        int colorPlus = ContextCompat.getColor(mContext, R.color.membership_point_plus);
        //        int      colorMinus = mContext.getResources().getColor(R.color.membership_point_minus);
        int colorMinus = ContextCompat.getColor(mContext, R.color.membership_point_minus);
        //        Drawable imgPlus    = mContext.getResources().getDrawable(R.mipmap.ic_membership_point_plus);
        Drawable imgPlus = ContextCompat.getDrawable(mContext, R.mipmap.ic_membership_point_plus);
        //        Drawable imgMinus = mContext.getResources().getDrawable(R.mipmap.ic_membership_point_minus);
        Drawable imgMinus = ContextCompat.getDrawable(mContext, R.mipmap.ic_membership_point_minus);

        imgPlus.setBounds(0, 0, 48, 48);
        imgMinus.setBounds(0, 0, 48, 48);

        TextView tvCount = helper.getView(R.id.tv_count);
        tvCount.setCompoundDrawables(null, null, data.membership_point.contains("-") ? imgMinus : imgPlus, null);

        helper.setText(R.id.tv_desc, data.comment)
                .setText(R.id.tv_time, data.created)
                .setText(R.id.tv_count, data.membership_point)
                .setTextColor(R.id.tv_count, data.membership_point.contains("-") ? colorMinus : colorPlus);
    }
}
