package com.a55haitao.wwht.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.ProductType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.SelectedSkuBean;
import com.a55haitao.wwht.data.model.result.OrderDetailResult;
import com.a55haitao.wwht.data.model.result.OrderDetailResult.OrderDetailBean.StorelistBean;
import com.a55haitao.wwht.data.model.result.OrderDetailResult.OrderDetailBean.StorelistBean.ProductListBean;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.RefundPopupWindow;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 董猛 on 16/9/8.
 */
public class OrderDetailHelper {

    private static RefundPopupWindow sPw;

    public static void renderUI(Activity context, LinearLayout parent, OrderDetailResult.OrderDetailBean order) {
        // 订单Id
        String orderId = order.order_id;

        LayoutInflater inflater = LayoutInflater.from(context);

        int size = order.storelist.size();

        for (int i = 0; i < size; i++) {
            StorelistBean store = order.storelist.get(i);
            // 商家信息头
            addSellerInfoView(context, parent, inflater, store, i != 0);
            // 循环添加商品信息
            for (int j = 0, productCount = store.productlist.size(); j < productCount; j++) {
                ProductListBean product = store.productlist.get(j);
                addProductView(context, inflater, parent, product);
            }
            addSellerFeeView(context, inflater, parent, store);
        }
    }

    /**
     * 商家头信息
     */
    private static void addSellerInfoView(Activity context, LinearLayout parent, LayoutInflater inflater, StorelistBean storelistBean, boolean hasTopMargin) {
        LinearLayout llSellerInfo = (LinearLayout) inflater.inflate(R.layout.order_detail_seller_item_layout, parent, false);
        // 超过一个显示marginTop
        HaiUtils.setVisibie(llSellerInfo, R.id.view_margin, hasTopMargin);
        //商家名字
        HaiUtils.setText(llSellerInfo, R.id.sellerNameTxt, String.format("%s官网发货", storelistBean.store_name_en));
        //国籍
        ImageView flagImgView = (ImageView) llSellerInfo.findViewById(R.id.flagImg);

        int flagResourceId = HaiUtils.getFlagResourceId(storelistBean.country.regionName, false);
        if (flagResourceId == -1) {
            String flagUrl = storelistBean.country.flag;
            Glide.with(context)
                    .load(flagUrl)
                    .override(HaiConstants.CompoundSize.PX_20, HaiConstants.CompoundSize.PX_20)
                    .into(flagImgView);
        } else {
            flagImgView.setImageResource(flagResourceId);
        }
        parent.addView(llSellerInfo);
    }

    /**
     * 商家费用信息
     */
    private static void addSellerFeeView(Activity context, LayoutInflater inflater, LinearLayout parent, StorelistBean store) {
        LinearLayout llSellerFee = (LinearLayout) inflater.inflate(R.layout.footer_item_order_detail_seller_fee, parent, false);
        // 活动优惠
        HaiTextView tvActivity = (HaiTextView) llSellerFee.findViewById(R.id.tv_activity);
        if (store.bigStorefullminusAmount == 0) {
            tvActivity.setVisibility(View.GONE);
        } else {
            tvActivity.setVisibility(View.VISIBLE);
            tvActivity.setText(String.format("活动优惠：-\u00A5%d", (int)store.bigStorefullminusAmount));
        }

        // 税费
        StringBuilder sbTax = new StringBuilder("税费：");
        if (store.bigStoreConsumptionaxmount != 0) {
            sbTax.append("海外消费税 \u00A5")
                    .append(PriceUtils.round(store.bigStoreConsumptionaxmount))
                    .append(" + ");
        }
        sbTax.append("关税 \u00A5")
                .append(PriceUtils.round(store.bigStoreTariff));
        HaiUtils.setText(llSellerFee, R.id.tv_tax, sbTax.toString());
        // 运费
        String shippingFee = String.format("运费：官网运费 \u00A5%d + 国际运费 \u00A5%d",
                PriceUtils.round(store.bigStoreShippingFee), // 官网运费
                PriceUtils.round(store.bigStoreTransFee)); // 国际运费
        HaiUtils.setText(llSellerFee, R.id.tv_shipping_fee, shippingFee);

        parent.addView(llSellerFee);
    }

    /**
     * 商品信息
     */
    private static void addProductView(Activity context, LayoutInflater inflater, LinearLayout parent, ProductListBean product) {
        LinearLayout llProduct = (LinearLayout) inflater.inflate(R.layout.item_order_detail_product, parent, false);

        // 商品个数
        HaiUtils.setText(llProduct, R.id.tv_product_count, String.format("X%s", product.count));
        //商品名称
        HaiUtils.setText(llProduct, R.id.tv_product_name, product.product_name);
        //商品价格
        HaiUtils.setText(llProduct, R.id.tv_product_price, String.format(" \u00A5%s", product.bigSkuPrice));
        //商品图片
        UPaiYunLoadManager.loadImage(context,
                product.coverImgUrl,
                UpaiPictureLevel.FOURTH,
                R.id.u_pai_yun_null_holder_tag,
                (ImageView) llProduct.findViewById(R.id.img_product_pic));
        // 规格信息
        List<SelectedSkuBean.SkuValues> skuValues
                = product.selected_sku.skuValues;
        if (skuValues != null && skuValues.size() != 0) {
            LinearLayout llProductInfo = (LinearLayout) llProduct.findViewById(R.id.ll_product_info);
            for (int p = 0; p < skuValues.size(); p++) {
                TextView textView = (TextView) inflater.inflate(R.layout.simple_spec_tv_layout, llProductInfo, false);
                String   value    = skuValues.get(p).value;
                String   specInfo = HaiUtils.transformSpec(skuValues.get(p).name);
                textView.setText(String.format("%s：%s", specInfo, value));
                llProductInfo.addView(textView);
            }
        }
        // 商品状态
        if (TextUtils.isEmpty(product.product_status)) {
            HaiUtils.setVisibie(llProduct, R.id.tv_order_status, false);
        } else {
            // 设置商品状态文本
            HaiUtils.setText(llProduct, R.id.tv_order_status, product.product_status);
            // 设置商品状态文本颜色
            switch (product.product_status_code) {
                case ProductType.PROCUREMENT:
                case ProductType.PROCUREMENT_SUCCESS:
                    // 绿色
                    HaiUtils.setTextColor(llProduct, R.id.tv_order_status, ContextCompat.getColor(context, R.color.product_status_green));
                    break;
                case ProductType.REFUND_SUCCESS:  // 退款成功
                    // 绿色
                    HaiUtils.setTextColor(llProduct, R.id.tv_order_status, ContextCompat.getColor(context, R.color.product_status_orange));
                    TextView text = (HaiTextView) llProduct.findViewById(R.id.tv_order_status);
                    text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    text.setOnClickListener((v) -> showPopupWindow(context, v));
                    break;
                case ProductType.REFUND_ACCEPTED:  // 退款受理中
                case ProductType.REFUND_REQUEST:   // 退款待受理
                    // 红色
                    HaiUtils.setTextColor(llProduct, R.id.tv_order_status, ContextCompat.getColor(context, R.color.product_status_red));
                    break;
                default:
                    // 默认灰色
                    HaiUtils.setTextColor(llProduct, R.id.tv_order_status, ContextCompat.getColor(context, R.color.colorGray333333));
                    break;
            }
        }
        llProduct.setOnClickListener(v -> {
            if (product.selected_sku != null)
                ProductMainActivity.toThisAcivity(context, product.selected_sku.spuid);
        });
        parent.addView(llProduct);
    }

    private static void showPopupWindow(Activity context, View v) {
        if (sPw == null) {
            sPw = new RefundPopupWindow(context);
        }
        sPw.showOrDismiss(v);
    }

}

