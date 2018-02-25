package com.a55haitao.wwht.adapter.easyopt;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.PriorityModel;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a55 on 2017/5/27.
 */

public class EasyOptDragAdapter extends BaseItemDraggableAdapter<ProductBaseBean, BaseViewHolder> {

    private Activity                              mActivity;
    private CheckClickListener                    checkClickListener;
    private boolean                               isEditable;
    private ProductAdapter.ActivityAnalysListener activityAnalysListener;  //  点击是否统计转换率

    public EasyOptDragAdapter(Activity activity, List data) {
        super(R.layout.item_product_list, data);
        mActivity = activity;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public void setActivityAnalysListener(ProductAdapter.ActivityAnalysListener activityAnalysListener) {
        this.activityAnalysListener = activityAnalysListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBaseBean item) {
        // 商品图
        UPaiYunLoadManager.loadImage(mActivity, item.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_small, helper.getView(R.id.img_pic));

        // 品牌 商品名
        helper.setText(R.id.tv_brand_name, item.brand) // 品牌名
                .setText(R.id.tv_desc, item.name) // 商品名
                .setText(R.id.tv_real_price, String.format("%s%d", ApiUrl.RMB_UNICODE, (int) item.realPrice)); // 现价

        // 现价  原价 折扣标签
        TextView tvDiscount = helper.getView(R.id.tvDiscount);

        if (Math.abs(item.realPrice - item.mallPrice) < 0.0001) { // 原价现价相同
            helper.setVisible(R.id.tv_mall_price, false);
            tvDiscount.setVisibility(View.GONE);
        } else {
            helper.setVisible(R.id.tv_mall_price, true)
                    .setText(R.id.tv_mall_price, String.format("%s%d", ApiUrl.RMB_UNICODE, (int) item.mallPrice)); // 原价
            // 原价删除线
            ((HaiTextView) helper.getView(R.id.tv_mall_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(PriceUtils.FloatKeepOneFloor(item.realPrice * 10 / item.mallPrice) + "折");

        }

        // 是否售光
        helper.setVisible(R.id.iv_soldout, HaiUtils.isSoldOut(item.inStock, item.stock))
                .setVisible(R.id.like_button, false)
                .setVisible(R.id.checkedTxt, isEditable ? true : false)
                .addOnClickListener(R.id.checkedTxt);

        //国旗
        String flag       = null;
        String country    = null;
        String sellerName = null;
        if (item.sellerInfo != null) {
            flag = item.sellerInfo.flag;
            country = item.sellerInfo.country;
            sellerName = item.sellerInfo.nameen;
        } else if (item.seller != null) {
            flag = item.seller.flag;
            country = item.seller.country;
            sellerName = item.seller.name;
        }
        int flagResourceId = HaiUtils.getFlagResourceId(country, false);
        if (flagResourceId == -1) {
            int px20 = HaiConstants.CompoundSize.PX_20;
            Glide.with(ActivityCollector.getTopActivity())
                    .load(flag)
                    .override((int) (1.8 * px20), px20)
                    .into((ImageView) helper.getView(R.id.img_country));
        } else {
            Glide.with(mActivity)
                    .load(flagResourceId)
                    .placeholder(R.mipmap.ic_default_square_small)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into((ImageView) helper.getView(R.id.img_country));
        }

        helper.setText(R.id.tv_seller, String.format("%s官网发货", sellerName));

        CheckedTextView mCheckedTextView = helper.getView(R.id.checkedTxt);
        mCheckedTextView.setOnClickListener(v -> {
            mCheckedTextView.toggle();
            item.checked = mCheckedTextView.isChecked();
            if (checkClickListener != null) {
                checkClickListener.onCheckClick(getCheckedCount());
            }
        });

        helper.getView(R.id.rlyt_product).setOnClickListener(v -> {
            if (isEditable)
                return;
            if (activityAnalysListener != null) {
                activityAnalysListener.OnActivityAnalys(item.DOCID);
            }
            ProductMainActivity.toThisAcivity(mContext, item.DOCID, item.name);
        });
    }

    public void setOnCheckClickListener(CheckClickListener checkClickListener) {
        this.checkClickListener = checkClickListener;
    }


    public interface CheckClickListener {
        void onCheckClick(int count);
    }

    // 要删除的商品
    public ArrayList<PriorityModel> getDeleteInfo() {
        ArrayList<PriorityModel> lists    = new ArrayList<>();
        int                      size     = getcount();
        ProductBaseBean          temp     = null;
        int                      priority = -1;
        for (int i = 0; i < size; i++) {
            temp = getItem(i);
            if (!temp.checked) {
                priority++;
                lists.add(new PriorityModel(priority, temp.DOCID));
            }
        }
        return lists;
    }

    // 拖动后的商品顺序
    public ArrayList<PriorityModel> getUpdateInfo() {
        ArrayList<PriorityModel> lists    = new ArrayList<>();
        int                      size     = getcount();
        ProductBaseBean          temp     = null;
        int                      priority = -1;
        for (int i = 0; i < size; i++) {
            priority++;
            temp = getItem(i);
            lists.add(new PriorityModel(priority, temp.DOCID));
        }
        return lists;
    }

    public void reloadAfterDelete() {
        ArrayList temp = new ArrayList();
        for (ProductBaseBean data : mData) {
            if (data.checked) {
                temp.add(data);
            }
        }
        if (temp.size() > 0) {
            mData.removeAll(temp);
        }
        if (HaiUtils.getSize(mData) < 3) {
            removeAllFooterView();
        }
        notifyDataSetChanged();
    }

    public int getcount() {
        return mData == null ? 0 : mData.size();
    }

    public int getCheckedCount() {
        int count = 0;
        for (ProductBaseBean data : mData) {
            if (data.checked) {
                count++;
            }
        }
        return count;
    }

}
