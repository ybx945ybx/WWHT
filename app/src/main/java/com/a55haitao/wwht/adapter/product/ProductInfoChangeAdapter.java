package com.a55haitao.wwht.adapter.product;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.CompoundButton;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.shoppingcart.ShoppingCartExpandableListAdapter;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/11/16.
 */

public class ProductInfoChangeAdapter extends BaseQuickAdapter<OrderCommitBean.FailedExtData, BaseViewHolder> {

    private Activity mActivity;

    public ProductInfoChangeAdapter(ArrayList<OrderCommitBean.FailedExtData> data, Activity activity) {
        super(R.layout.product_change_item, data);
        this.mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderCommitBean.FailedExtData cartdata) {
        if ("invalid".equals(cartdata.price_status)) {
            // 无效
            holder.setVisible(R.id.itemCheckBox, false);
            holder.setVisible(R.id.itemInvalid, true);

            holder.setVisible(R.id.itemOldPriceTxtStrike, false);
            holder.setVisible(R.id.itemOldPriceTxt, false);
            holder.setVisible(R.id.tv_arrow, false);
            holder.setVisible(R.id.itemNowPriceTxt, false);
        } else {
            // 有效
            holder.setVisible(R.id.itemCheckBox, true);
            holder.setVisible(R.id.itemInvalid, false);
            String oldPrice = PriceUtils.toRMBFromYuan(cartdata.oldprice);
            if (!TextUtils.isEmpty(cartdata.nowprice)) {
                holder.setText(R.id.itemOldPriceTxtStrike, oldPrice);
                holder.setVisible(R.id.itemOldPriceTxtStrike, true);
                holder.setVisible(R.id.itemOldPriceTxt, false);
                holder.setVisible(R.id.tv_arrow, true);
                holder.setVisible(R.id.itemNowPriceTxt, true);
                holder.setText(R.id.itemNowPriceTxt, PriceUtils.toRMBFromYuan(cartdata.nowprice));

            } else {
                holder.setText(R.id.itemOldPriceTxt, oldPrice);
                holder.setVisible(R.id.itemOldPriceTxtStrike, false);
                holder.setVisible(R.id.itemOldPriceTxt, true);
                holder.setVisible(R.id.tv_arrow, false);
                holder.setVisible(R.id.itemNowPriceTxt, false);

            }
        }
        boolean productSelected = cartdata.getProductSelected();
        holder.setChecked(R.id.itemCheckBox, productSelected);
        //        holder.itemCheckBox.setChecked(productSelected);
        UPaiYunLoadManager.loadImage(mActivity, cartdata.cover, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.getView(R.id.itemImage));
        holder.setText(R.id.itemTitleTxt, cartdata.title);

        holder.getView(R.id.itemCheckBox).setOnClickListener(v -> {
            if (v instanceof CompoundButton) {
                CompoundButton compoundButton = (CompoundButton) v;
                boolean        isChecked      = compoundButton.isChecked();
                cartdata.setIs_select(isChecked);
            }
        });

    }
}
