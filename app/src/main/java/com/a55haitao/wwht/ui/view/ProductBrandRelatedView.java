package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haotao_Fujie on 16/10/27.
 */

public class ProductBrandRelatedView extends LinearLayout {

    @BindView(R.id.leftImage)         ImageView    leftImage;
    @BindView(R.id.leftTitleTxt)      TextView     leftTitleTxt;
    @BindView(R.id.leftRealPriceTxt)  TextView     leftRealPriceTxt;
    @BindView(R.id.leftMallPriceTxt)  TextView     leftMallPriceTxt;
    @BindView(R.id.rightImage)        ImageView    rightImage;
    @BindView(R.id.rightTitleTxt)     TextView     rightTitleTxt;
    @BindView(R.id.rightRealPriceTxt) TextView     rightRealPriceTxt;
    @BindView(R.id.rightMallPriceTxt) TextView     rightMallPriceTxt;
    @BindView(R.id.rightLin)          LinearLayout rightLin;
    @BindView(R.id.leftLin)           LinearLayout leftLin;

    private ProductBrandRelatedViewInterface mInterface;

    public void setProductBrandRelatedViewInterface(ProductBrandRelatedViewInterface mInterface) {
        this.mInterface = mInterface;
    }

    public ProductBrandRelatedView(Context context) {
        super(context);
        renderUI(context);
    }

    public ProductBrandRelatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        renderUI(context);
    }

    private void renderUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.product_brand_related_view, this);
        ButterKnife.bind(this);
    }

    public void updateUI(ArrayList<ProductBaseBean> pairs) {

        if (pairs == null || pairs.size() == 0)
            return;

        ProductBaseBean leftPair = pairs.get(0);
        UPaiYunLoadManager.loadImage(getContext(), leftPair.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_tiny, leftImage);
        leftTitleTxt.setText(leftPair.name);
        leftRealPriceTxt.setText(PriceUtils.toRMBFromFen(leftPair.realPrice));
        if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(leftPair.realPrice, leftPair.mallPrice)) {
            leftMallPriceTxt.setVisibility(GONE);
            leftRealPriceTxt.setGravity(Gravity.CENTER);
        } else {
            leftMallPriceTxt.setVisibility(VISIBLE);
            leftMallPriceTxt.setText(PriceUtils.toRMBFromFen(leftPair.mallPrice));
            leftMallPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        }
        leftLin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterface != null) {
                    mInterface.tapOnProduct(leftPair.spuid, leftPair.name);
                }
            }
        });

        if (pairs.size() == 2) {
            rightLin.setVisibility(VISIBLE);
            ProductBaseBean rightPair = pairs.get(1);
            UPaiYunLoadManager.loadImage(getContext(), rightPair.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_tiny, rightImage);
            rightTitleTxt.setText(rightPair.name);
            rightRealPriceTxt.setText(PriceUtils.toRMBFromFen(rightPair.realPrice));
            if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(rightPair.realPrice, rightPair.mallPrice)) {
                rightMallPriceTxt.setVisibility(GONE);
                rightRealPriceTxt.setGravity(Gravity.CENTER);
            } else {
                rightMallPriceTxt.setText(PriceUtils.toRMBFromFen(rightPair.mallPrice));
                rightMallPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            rightLin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInterface != null) {
                        mInterface.tapOnProduct(rightPair.spuid, rightPair.name);
                    }
                }
            });
        } else {
            rightLin.setVisibility(GONE);
        }
    }

    public interface ProductBrandRelatedViewInterface {
        void tapOnProduct(String spuid, String name);
    }

}
