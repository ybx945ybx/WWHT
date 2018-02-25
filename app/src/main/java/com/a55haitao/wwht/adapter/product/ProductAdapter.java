package com.a55haitao.wwht.adapter.product;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.LikeProductBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.discover.SearchResultActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Pray;

/**
 * 通用商品列表适配器
 * Created by a55 on 2017/5/14.
 */

public class ProductAdapter extends BaseQuickAdapter<ProductBaseBean, BaseViewHolder> {

    private Activity                               mActivity;
    private ToastPopuWindow                        mPwToast;
    private RecyclerView                           parentView;
    private boolean                                showHotwords;            // 是否有相关热搜
    private int                                    type;
    private ProductRecomAdapter.OnTagClickListener onTagClickListener;
    private ActivityAnalysListener                 activityAnalysListener;  //  点击是否统计转换率
    private boolean showLike;

    public ProductAdapter(ArrayList<ProductBaseBean> data, Activity activity, ToastPopuWindow mPwToast, RecyclerView parentView) {
        super(R.layout.item_product_list, data);
        mActivity = activity;
        this.mPwToast = mPwToast;
        this.parentView = parentView;
        this.showLike = true;
    }

    public ProductAdapter(ArrayList<ProductBaseBean> data, Activity activity, ToastPopuWindow mPwToast, RecyclerView parentView, boolean showLike) {
        super(R.layout.item_product_list, data);
        mActivity = activity;
        this.mPwToast = mPwToast;
        this.parentView = parentView;
        this.showLike = showLike;
    }

    public ProductAdapter(ArrayList<ProductBaseBean> data, Activity activity, ToastPopuWindow mPwToast, RecyclerView parentView, boolean showHotwords, int type) {
        super(R.layout.item_product_list, data);
        mActivity = activity;
        this.mPwToast = mPwToast;
        this.parentView = parentView;
        this.showHotwords = showHotwords;
        this.type = type;
        this.showLike = true;

    }

    public void setOnTagClickListener(ProductRecomAdapter.OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBaseBean item) {
        if (showHotwords) {
            if (helper.getLayoutPosition() == 0 && HaiUtils.getSize(item.relateQuery) > 0) {
                helper.setVisible(R.id.rlyt_product, false)
                        .setVisible(R.id.llyt_hot_words, true);
                setHotWordsView(helper, item);
            } else {
                helper.setVisible(R.id.rlyt_product, true)
                        .setVisible(R.id.llyt_hot_words, false);
                setProductView(helper, item);
            }
        } else {
            setProductView(helper, item);
        }
    }

    // 设置相关热搜视图
    private void setHotWordsView(BaseViewHolder helper, ProductBaseBean item) {
        LinearLayout llytHotWords = (LinearLayout) helper.getView(R.id.llyt_hot_words_contain);
        int          size         = HaiUtils.getSize(item.relateQuery) > 5 ? 5 : HaiUtils.getSize(item.relateQuery);
        for (int i = 0; i < size; i++) {
            TextView tv = (TextView) llytHotWords.getChildAt(i);
            tv.setVisibility(View.VISIBLE);
            tv.setText(item.relateQuery[i]);
            tv.setTag(item.relateQuery[i]);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null)
                        onTagClickListener.onTagClick((String) v.getTag());

                }
            });
        }

    }

    // 设置商品视图
    private void setProductView(BaseViewHolder helper, ProductBaseBean item) {
        // 商品图
        UPaiYunLoadManager.loadImage(mActivity, item.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_small, helper.getView(R.id.img_pic));
        // 角标
        if (!TextUtils.isEmpty(item.promotionTag)){
            helper.setVisible(R.id.iv_promotionTag, true);
            ((SimpleDraweeView)helper.getView(R.id.iv_promotionTag)).setImageURI(UpyUrlManager.getUrl(item.promotionTag, DisplayUtils.dp2px(mActivity, 24)));
        }else {
            helper.setVisible(R.id.iv_promotionTag, false);
        }
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

        helper.setVisible(R.id.iv_soldout, HaiUtils.isSoldOut(item.inStock, item.stock))
                .setVisible(R.id.like_button, showLike)
                .setChecked(R.id.like_button, item.is_star);

        helper.getView(R.id.like_button).setOnClickListener(v -> {
            if (HaiUtils.needLogin(mContext)) {
                return;
            }
            ProductRepository.getInstance()
                    .likeProduct(TextUtils.isEmpty(item.spuid) ? item.DOCID : item.spuid)
                    .compose(RxLifecycleAndroid.bindActivity(((BaseActivity) mContext).lifecycle()))
                    .subscribe(new DefaultSubscriber<LikeProductBean>() {
                        @Override
                        public void onSuccess(LikeProductBean likeProductBean) {
                            boolean checked = item.is_star;
                            if (!checked) {
                                mPwToast = ToastPopuWindow.makeText((BaseActivity) mContext, "已加入心愿单", AlterViewType_Pray).parentView(parentView);
                                mPwToast.show();
                            }
                            item.is_star = !checked;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFinish() {
                        }
                    });
        });

        helper.getView(R.id.rlyt_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == PageType.SEARCH) {    // 搜索结果页面点击商品时打点上报信息
                    ProductRepository.getInstance()
                            .clickLogServer(((SearchResultActivity) mContext).keySearch, item.spuid
                                    , ((SearchResultActivity) mContext).page
                                    , helper.getLayoutPosition() - (((SearchResultActivity) mContext).page * 20 - 20))
                            .subscribe(new DefaultSubscriber<Object>() {
                                @Override
                                public void onSuccess(Object o) {

                                }

                                @Override
                                public boolean onFailed(Throwable e) {
                                    return super.onFailed(e);
                                }

                                @Override
                                public void onFinish() {

                                }
                            });
                }

                if (activityAnalysListener != null) {
                    activityAnalysListener.OnActivityAnalys(item.spuid);
                }

                ProductMainActivity.toThisAcivity(mContext, TextUtils.isEmpty(item.spuid) ? item.DOCID : item.spuid, item.name);

            }
        });

    }

    public interface ActivityAnalysListener {
        void OnActivityAnalys(String spuid);
    }

    public void setActivityAnalysListener(ActivityAnalysListener activityAnalysListener) {
        this.activityAnalysListener = activityAnalysListener;
    }
}
