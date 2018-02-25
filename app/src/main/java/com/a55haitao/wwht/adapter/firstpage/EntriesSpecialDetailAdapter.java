package com.a55haitao.wwht.adapter.firstpage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.model.entity.EntriesSpecialResult;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 55haitao on 2016/10/31.
 */

public class EntriesSpecialDetailAdapter extends BaseAdapter {
    private Activity                              mActivity;
    private List<EntriesSpecialResult.Item>       mDatas;
    private ProductAdapter.ActivityAnalysListener activityAnalysListener;  //  点击是否统计转换率


    public EntriesSpecialDetailAdapter(Activity context, List<EntriesSpecialResult.Item> dataset) {
        mActivity = context;
        mDatas = dataset;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder                    holder = null;
        EntriesSpecialResult.Item item   = mDatas.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_entries_sp_layout, parent, false);
            holder = new Holder(convertView);
            View.OnClickListener l = v -> {
                if (activityAnalysListener != null){
                    activityAnalysListener.OnActivityAnalys(item.spuid);
                }
                ProductMainActivity.toThisAcivity(mActivity, item.spuid, item.title);
            };
            holder.lblBuyNow.setTag(R.id.tag_for_img, holder);
            holder.lblBuyNow.setOnClickListener(l);
            holder.imgMain.setTag(R.id.tag_for_img, holder);
            holder.imgMain.setOnClickListener(l);
            holder.lblPriceOriginal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (item.real_price != item.mall_price) {
            holder.lblPriceOriginal.setVisibility(View.VISIBLE);
        } else {
            holder.lblPriceOriginal.setVisibility(View.INVISIBLE);
        }

        holder.haiFlag.setText(String.format("%02d", position + 1));
        holder.lblTopic.setText(item.title);

        UPaiYunLoadManager.loadImage(mActivity, item.img_cover, UpaiPictureLevel.SINGLE, R.id.u_pai_yun_null_holder_tag, holder.imgMain);

        holder.lblDesc.setText(item.desc);
        holder.lblPriceCurrent.setText(PriceUtils.toRMBFromFen(item.real_price));
        holder.lblPriceOriginal.setText(PriceUtils.toRMBFromFen(item.mall_price));

        holder.spuid = item.spuid;
        return convertView;
    }

    public class Holder {

        @BindView(R.id.lblDesc)
        TextView       lblDesc;
        @BindView(R.id.lblPriceCurrent)
        TextView       lblPriceCurrent;
        @BindView(R.id.lblPriceOriginal)
        TextView       lblPriceOriginal;
        @BindView(R.id.lblBuyNow)
        TextView       lblBuyNow;
        @BindView(R.id.relPrice)
        RelativeLayout relPrice;
        @BindView(R.id.imgMain)
        ImageView      imgMain;
        @BindView(R.id.haiFlag)
        TextView       haiFlag;
        @BindView(R.id.lblTopic)
        TextView       lblTopic;

        // Data
        String spuid;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setActivityAnalysListener(ProductAdapter.ActivityAnalysListener activityAnalysListener) {
        this.activityAnalysListener = activityAnalysListener;
    }
}
