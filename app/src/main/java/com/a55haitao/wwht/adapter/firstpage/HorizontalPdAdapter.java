package com.a55haitao.wwht.adapter.firstpage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.ArrayList;
import java.util.HashMap;

import tom.ybxtracelibrary.YbxTrace;

/**
 * Created by 55haitao on 2016/11/12.
 */

public class HorizontalPdAdapter extends RVBaseAdapter<TabEntryBean.EntriesBean.ItemsBean> {

    public  String mUri;
    private int    type;     // 1是首页tab的
    private String Tag;

    private ProductAdapter.ActivityAnalysListener activityAnalysListener;  //  点击是否统计转换率

    public HorizontalPdAdapter(Context context, ArrayList<TabEntryBean.EntriesBean.ItemsBean> arrayList, int resId) {
        super(context, arrayList, resId);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    @Override
    public void bindView(RVBaseHolder holder, TabEntryBean.EntriesBean.ItemsBean tabEntryItemBean) {
        if (tabEntryItemBean.showSeeAll) {
            ImageView ivMore = holder.getView(R.id.iv_more);
            ivMore.setImageResource(R.mipmap.ic_more);
            ivMore.setVisibility(View.VISIBLE);
            holder.getView(R.id.llyt_pd).setVisibility(View.GONE);
            holder.itemView.setTag(mUri);
        } else {
            holder.getView(R.id.iv_more).setVisibility(View.GONE);
            holder.getView(R.id.llyt_pd).setVisibility(View.VISIBLE);

            UPaiYunLoadManager.loadImage(mContext, tabEntryItemBean.img_cover, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.getView(R.id.pdMainImg));
            ((TextView) holder.getView(R.id.pdNameTxt)).setText(tabEntryItemBean.title);
            ((TextView) holder.getView(R.id.pdBrandTxt)).setText(tabEntryItemBean.brand);
            ((TextView) holder.getView(R.id.realPriceTxt)).setText(PriceUtils.toRMBFromFen((float) tabEntryItemBean.real_price));
            if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice((float) tabEntryItemBean.real_price, (float) tabEntryItemBean.mall_price)) {
                holder.getView(R.id.mallPriceTxt).setVisibility(View.GONE);
            } else {
                holder.getView(R.id.mallPriceTxt).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.mallPriceTxt)).setText(PriceUtils.toRMBFromFen((float) tabEntryItemBean.mall_price));
            }
            holder.itemView.setTag(tabEntryItemBean.spuid);
        }

        holder.itemView.setOnClickListener(v -> {

            if (tabEntryItemBean.showSeeAll) {
                // 埋点
                YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", TraceUtils.PositionCode_SpecialCollection, TraceUtils.Event_Category_Click, "", null, "");

                HaiUriMatchUtils.matchUri((BaseActivity) mContext, mUri);
            } else {
                // 埋点
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Goods_Id, tabEntryItemBean.spuid);
                YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", TraceUtils.PositionCode_SpecialCollection, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                ProductMainActivity.toThisAcivity(mContext, tabEntryItemBean.spuid, tabEntryItemBean.title);
            }
        });

    }

}
