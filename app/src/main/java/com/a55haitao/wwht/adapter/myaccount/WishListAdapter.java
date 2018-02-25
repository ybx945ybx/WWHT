package com.a55haitao.wwht.adapter.myaccount;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.easyopt.EasyOptDragAdapter;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.result.GetUserStarProductsResult.ProductBean;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心 - 心愿单 - Fragment
 *
 * @author 陶声
 * @since 2016-11-15
 */

public class WishListAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolder> {

    private     Activity                              mActivity;
    private     boolean                               isEdit;
    private EasyOptDragAdapter.CheckClickListener checkClickListener;

    public WishListAdapter(List<ProductBean> data, Activity activity) {
        super(R.layout.item_fragment_wishlist, data);
        mActivity = activity;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setCheckClickListener(EasyOptDragAdapter.CheckClickListener checkClickListener) {
        this.checkClickListener = checkClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBean item) {
        // 商品图
        UPaiYunLoadManager.loadImage(mActivity, item.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_small, helper.getView(R.id.img_pic));
        // 国旗
        if (item.sellerInfo != null && item.sellerInfo.country != null) {
            int flagId = HaiUtils.getFlagResourceId(item.sellerInfo.country, false);
            if (flagId == -1) {
                int px20 = HaiConstants.CompoundSize.PX_20;

                Glide.with(mActivity)
                        .load(item.sellerInfo.flag)
                        .override((int) (1.8 * px20), px20)
                        .placeholder(R.mipmap.ic_default_square_small)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into((ImageView) helper.getView(R.id.img_country));
            } else {
                Glide.with(mActivity)
                        .load(flagId)
                        .placeholder(R.mipmap.ic_default_square_small)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into((ImageView) helper.getView(R.id.img_country));
            }
        }
        helper.setText(R.id.tv_brand_name, item.brand) // 品牌名
                .setText(R.id.tv_desc, item.name) // 商品名
                .setText(R.id.tv_seller, item.sellerInfo != null ? item.sellerInfo.nameen + "官网发货" : "官网发货")
                .setText(R.id.tv_real_price, String.format("%s%d", ApiUrl.RMB_UNICODE, (int) (item.realPrice / 100))); // 现价

        TextView tvDiscount = helper.getView(R.id.tvDiscount);
        if (Math.abs(item.realPrice - item.mallPrice) < 0.0001) { // 原价现价相同
            helper.setVisible(R.id.tv_mall_price, false);
            tvDiscount.setVisibility(View.GONE);

        } else {
            helper.setVisible(R.id.tv_mall_price, true)
                    .setText(R.id.tv_mall_price, String.format("%s%d", ApiUrl.RMB_UNICODE, (int) (item.mallPrice / 100))); // 原价
            // 原价删除线
            ((HaiTextView) helper.getView(R.id.tv_mall_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(PriceUtils.FloatKeepOneFloor(item.realPrice * 10 / item.mallPrice) + "折");

        }

        helper.setVisible(R.id.iv_soldout, HaiUtils.isSoldOut(item.inStock, item.stock) ? true : false);

        //  编辑勾选按钮
        helper.setVisible(R.id.checkedTxt, isEdit ? true : false);
        CheckedTextView mCheckedTextView = helper.getView(R.id.checkedTxt);
        mCheckedTextView.setChecked(item.checked);
        mCheckedTextView.setOnClickListener(v -> {
            mCheckedTextView.toggle();
            item.checked = mCheckedTextView.isChecked();
            if (checkClickListener != null) {
                checkClickListener.onCheckClick(getCheckedCount());
            }
        });

        helper.itemView.setOnClickListener(v -> {
            // 跳转到商品详情页
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_ProductDetail, item.spuid, mActivity, TraceUtils.PageCode_MyWhisList, TraceUtils.PositionCode_Product + "", "");

            ProductMainActivity.toThisAcivity(mActivity, item.spuid, item.name);

        });
    }

    // 要删除的商品
    public ArrayList<String> getDeleteInfo() {
        ArrayList<String> lists = new ArrayList<>();
        for (ProductBean bean : mData) {
            if (bean.checked) {
                lists.add(bean.spuid);
            }
        }
        return lists;
    }

    public int getcount() {
        return mData == null ? 0 : mData.size();
    }

    public int getCheckedCount() {
        int count = 0;
        for (ProductBean data : mData) {
            if (data.checked) {
                count++;
            }
        }
        return count;
    }
}
