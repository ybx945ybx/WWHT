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
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haotao_Fujie on 2016/10/28.
 */

public class ProductEveryoneBuyingView extends LinearLayout {

    @BindView(R.id.contentImage1)
    ImageView    contentImage1;
    @BindView(R.id.contentTitle1)
    TextView     contentTitle1;
    @BindView(R.id.contentSubtitle1)
    TextView     contentSubtitle1;
    @BindView(R.id.contentRealPrice1)
    TextView     contentRealPrice1;
    @BindView(R.id.contentMallPrice1)
    TextView     contentMallPrice1;
    @BindView(R.id.contentView1)
    LinearLayout contentView1;
    @BindView(R.id.contentImage2)
    ImageView    contentImage2;
    @BindView(R.id.contentTitle2)
    TextView     contentTitle2;
    @BindView(R.id.contentSubtitle2)
    TextView     contentSubtitle2;
    @BindView(R.id.contentRealPrice2)
    TextView     contentRealPrice2;
    @BindView(R.id.contentMallPrice2)
    TextView     contentMallPrice2;
    @BindView(R.id.contentView2)
    LinearLayout contentView2;
    @BindView(R.id.contentImage3)
    ImageView    contentImage3;
    @BindView(R.id.contentTitle3)
    TextView     contentTitle3;
    @BindView(R.id.contentSubtitle3)
    TextView     contentSubtitle3;
    @BindView(R.id.contentRealPrice3)
    TextView     contentRealPrice3;
    @BindView(R.id.contentMallPrice3)
    TextView     contentMallPrice3;
    @BindView(R.id.contentView3)
    LinearLayout contentView3;
    @BindView(R.id.contentImage4)
    ImageView    contentImage4;
    @BindView(R.id.contentTitle4)
    TextView     contentTitle4;
    @BindView(R.id.contentSubtitle4)
    TextView     contentSubtitle4;
    @BindView(R.id.contentRealPrice4)
    TextView     contentRealPrice4;
    @BindView(R.id.contentMallPrice4)
    TextView     contentMallPrice4;
    @BindView(R.id.contentView4)
    LinearLayout contentView4;
    @BindView(R.id.contentMallSeller1)
    TextView     contentMallSeller1;
    @BindView(R.id.contentMallSeller2)
    TextView     contentMallSeller2;
    @BindView(R.id.contentMallSeller3)
    TextView     contentMallSeller3;
    @BindView(R.id.contentMallSeller4)
    TextView     contentMallSeller4;
    @BindView(R.id.contentMallCountryImage1)
    ImageView    contentMallCountryImage1;
    @BindView(R.id.contentMallCountryImage2)
    ImageView    contentMallCountryImage2;
    @BindView(R.id.contentMallCountryImage3)
    ImageView    contentMallCountryImage3;
    @BindView(R.id.contentMallCountryImage4)
    ImageView    contentMallCountryImage4;

    private Context mContext;

    private ProductBrandRelatedView.ProductBrandRelatedViewInterface mInterface;

    public void setProductBrandRelatedViewInterface(ProductBrandRelatedView.ProductBrandRelatedViewInterface mInterface) {
        this.mInterface = mInterface;
    }

    public ProductEveryoneBuyingView(Context context) {
        super(context);
        this.mContext = context;
        renderUI(context);
    }

    public ProductEveryoneBuyingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        renderUI(context);
    }

    private void renderUI(Context context) {

        LayoutInflater.from(context).inflate(R.layout.product_everyone_buying_view, this);
        ButterKnife.bind(this);
    }

    public void updateUI(ArrayList<ProductBaseBean> products) {

        if (products == null || products.size() == 0 || products.size() > 4) return;

        ProductBaseBean product1 = null;
        ProductBaseBean product2 = null;
        ProductBaseBean product3 = null;
        ProductBaseBean product4 = null;
        if (products.size() == 1) {
            contentImage2.setVisibility(INVISIBLE);
            contentImage3.setVisibility(INVISIBLE);
            contentImage4.setVisibility(INVISIBLE);
            product1 = products.get(0);
        } else if (products.size() == 2) {
            contentImage3.setVisibility(INVISIBLE);
            contentImage4.setVisibility(INVISIBLE);
            product1 = products.get(0);
            product2 = products.get(1);
        } else if (products.size() == 3) {
            contentImage4.setVisibility(INVISIBLE);
            product1 = products.get(0);
            product2 = products.get(1);
            product3 = products.get(2);
        } else {
            product1 = products.get(0);
            product2 = products.get(1);
            product3 = products.get(2);
            product4 = products.get(3);
        }

        switch (products.size()) {

            case 4: {
                UPaiYunLoadManager.loadImage(getContext(), product4.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_tiny, contentImage4);
                contentTitle4.setText(product4.name);
                contentSubtitle4.setText(product4.brand);
                contentRealPrice4.setText(PriceUtils.toRMBFromFen(product4.realPrice));
                if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(product4.realPrice, product4.mallPrice)) {
                    contentMallPrice4.setVisibility(GONE);
                    contentRealPrice4.setGravity(Gravity.CENTER);
                } else {
                    contentMallPrice4.setVisibility(VISIBLE);
                    contentMallPrice4.setText(PriceUtils.toRMBFromFen(product4.mallPrice));
                    contentMallPrice4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                contentView4.setTag(product4);
                contentView4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterface != null) {
                            ProductBaseBean productBaseBean = (ProductBaseBean) contentView4.getTag();
//                            String spuid = (String) contentView4.getTag();
                            mInterface.tapOnProduct(productBaseBean.DOCID, productBaseBean.name);
                        }
                    }
                });
                setFlag(product4, contentMallCountryImage4);
                contentMallSeller4.setText(product4.getSellerName());
            }

            case 3: {
                UPaiYunLoadManager.loadImage(getContext(), product3.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_tiny, contentImage3);
                contentTitle3.setText(product3.name);
                contentSubtitle3.setText(product3.brand);
                contentRealPrice3.setText(PriceUtils.toRMBFromFen(product3.realPrice));
                if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(product3.realPrice, product3.mallPrice)) {
                    contentMallPrice3.setVisibility(GONE);
                    contentRealPrice3.setGravity(Gravity.CENTER);
                } else {
                    contentMallPrice3.setVisibility(VISIBLE);
                    contentMallPrice3.setText(PriceUtils.toRMBFromFen(product3.mallPrice));
                    contentMallPrice3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                contentView3.setTag(product3);
                contentImage3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterface != null) {
                            ProductBaseBean productBaseBean = (ProductBaseBean) contentView3.getTag();
                            //                            String spuid = (String) contentView4.getTag();
                            mInterface.tapOnProduct(productBaseBean.DOCID, productBaseBean.name);

                        }
                    }
                });
                setFlag(product3, contentMallCountryImage3);
                contentMallSeller3.setText(product3.getSellerName());
            }

            case 2: {
                UPaiYunLoadManager.loadImage(getContext(), product2.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_tiny, contentImage2);
                contentTitle2.setText(product2.name);
                contentSubtitle2.setText(product2.brand);
                contentRealPrice2.setText(PriceUtils.toRMBFromFen(product2.realPrice));
                if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(product2.realPrice, product2.mallPrice)) {
                    contentMallPrice2.setVisibility(GONE);
                    contentRealPrice2.setGravity(Gravity.CENTER);
                } else {
                    contentMallPrice2.setVisibility(VISIBLE);
                    contentMallPrice2.setText(PriceUtils.toRMBFromFen(product2.mallPrice));
                    contentMallPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                contentView2.setTag(product2);
                contentView2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterface != null) {
                            ProductBaseBean productBaseBean = (ProductBaseBean) contentView2.getTag();
                            //                            String spuid = (String) contentView4.getTag();
                            mInterface.tapOnProduct(productBaseBean.DOCID, productBaseBean.name);

                        }
                    }
                });
                setFlag(product2, contentMallCountryImage2);

                //                int resId = HaiUtils.getFlagResourceId(product2.country,false);
                //                if (resId == - 1) {
                //                    UPaiYunLoadManager.load(context, product2.sellerInfo.logo, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, contentMallCountryImage2);
                //                }else {
                //                    contentMallCountryImage2.setImageResource(resId);
                //                }
                contentMallSeller2.setText(product2.getSellerName());
            }

            case 1: {
                UPaiYunLoadManager.loadImage(getContext(), product1.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_tiny, contentImage1);
                contentTitle1.setText(product1.name);
                contentSubtitle1.setText(product1.brand);
                contentRealPrice1.setText(PriceUtils.toRMBFromFen(product1.realPrice));
                if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(product1.realPrice, product1.mallPrice)) {
                    contentMallPrice1.setVisibility(GONE);
                    contentRealPrice1.setGravity(Gravity.CENTER);
                } else {
                    contentMallPrice1.setVisibility(VISIBLE);
                    contentMallPrice1.setText(PriceUtils.toRMBFromFen(product1.mallPrice));
                    contentMallPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                contentView1.setTag(product1);
                contentView1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterface != null) {
                            ProductBaseBean productBaseBean = (ProductBaseBean) contentView1.getTag();
                            //                            String spuid = (String) contentView4.getTag();
                            mInterface.tapOnProduct(productBaseBean.DOCID, productBaseBean.name);

                        }
                    }
                });
                setFlag(product1, contentMallCountryImage1);
                //                int resId = HaiUtils.getFlagResourceId(product1.country,false);
                //                if (resId == - 1) {
                //                    UPaiYunLoadManager.load(context, product1.sellerInfo.logo, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, contentMallCountryImage1);
                //                }else {
                //                    contentMallCountryImage1.setImageResource(resId);
                //                }
                contentMallSeller1.setText(product1.getSellerName());
            }
        }

    }

    private void setFlag(ProductBaseBean product, ImageView imageView) {
        if (product.sellerInfo != null && product.sellerInfo.country != null) {
            int flagResourceId = HaiUtils.getFlagResourceId(product.sellerInfo.country, false);
            if (flagResourceId == -1) {
                int px20 = HaiConstants.CompoundSize.PX_20;
                Glide.with(ActivityCollector.getTopActivity())
                        .load(product.sellerInfo.flag)
                        .override((int) (1.8 * px20), px20)
                        .into(imageView);
            } else {
                imageView.setImageResource(flagResourceId);
            }
        }
    }
}
