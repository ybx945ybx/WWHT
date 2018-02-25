package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drolmen on 2016/12/9.
 */

/**
 * 列表展示商品处理
 */
public class ProductLinearLayout extends LinearLayout {


    @Nullable @BindView(R.id.pdMainImg) ImageView mPdMainImg;

    @Nullable @BindView(R.id.pdBrandTxt) HaiTextView mPdBrandTxt;
    @BindView(R.id.pdNameTxt) HaiTextView mPdNameTxt;
    @BindView(R.id.realPriceTxt) HaiTextView mRealPriceTxt;
    @BindView(R.id.mallPriceTxt) StrikeThruTextView mMallPriceTxt;

    private boolean hasInit;

    private ProductBaseBean productBean;

    public ProductLinearLayout(Context context) {
        super(context);
    }

    public ProductLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(ProductBaseBean bean) {
        if (!hasInit) {
            init();
        }
        this.productBean = bean;
        updateView();
    }

    private void init() {
        ButterKnife.bind(this);
        hasInit = true;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductMainActivity.toThisAcivity(ProductLinearLayout.this.getContext(), productBean.spuid, productBean.name);
            }
        });
    }

    private void updateView() {
        setImage();

        if (mPdBrandTxt != null) {
            mPdBrandTxt.setText(productBean.brand);
            mPdBrandTxt.setVisibility(TextUtils.isEmpty(productBean.brand) ? GONE : VISIBLE);
        }

        mPdNameTxt.setText(productBean.name);
        mPdNameTxt.setVisibility(TextUtils.isEmpty(productBean.name) ? GONE : VISIBLE);

        mRealPriceTxt.setText(PriceUtils.toRMBFromFen(productBean.realPrice));
        mMallPriceTxt.setText(PriceUtils.toRMBFromFen(productBean.mallPrice));
        mMallPriceTxt.setVisibility(productBean.realPrice == productBean.mallPrice ? GONE : VISIBLE);
    }

    /**
     * 设置商品图片
     */
    private void setImage() {
        UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(), productBean.coverImgUrl, UpaiPictureLevel.TRIBBLE, R.mipmap.ic_default_square_small, mPdMainImg);
    }
}
