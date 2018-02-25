package com.a55haitao.wwht.adapter.product;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.ProductNullType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.LikeProductBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.RealmHistoryBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Pray;

/**
 * 搜索无结果，商品已下架后 带头部提示的商品列表适配器
 * Created by a55 on 2017/5/12.
 */

public class ProductRecomAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private ArrayList<ProductBaseBean> mList;
    private Context                    mContext;
    private LayoutInflater             mLayoutInflater;
    private ToastPopuWindow            mPwToast;
    private RecyclerView               parentView;
    private int                        type;
    private OnTagClickListener         onTagClickListener;
    private String[]                   relateQuery;
    private static int TYPE_NULL_IMAGE = 1;
    private static int TYPE_TEXT       = 2;
    private static int TYPE_GOODS      = 3;
    private static int TYPE_END        = 4;

    public ProductRecomAdapter(Context mContext, ArrayList<ProductBaseBean> mList, ToastPopuWindow mPwToast, RecyclerView parentView, int type) {
        this.mList = mList;
        this.mContext = mContext;
        this.mPwToast = mPwToast;
        this.parentView = parentView;
        this.type = type;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public ProductRecomAdapter(Context mContext, ArrayList<ProductBaseBean> mList, ToastPopuWindow mPwToast, RecyclerView parentView, int type, String[] relateQuery) {
        this.mList = mList;
        this.mContext = mContext;
        this.mPwToast = mPwToast;
        this.parentView = parentView;
        this.type = type;
        this.relateQuery = relateQuery;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<ProductBaseBean> getmList() {
        return mList;
    }

    public void setmList(ArrayList<ProductBaseBean> mList) {
        this.mList = mList;
    }

    public String[] getRelateQuery() {
        return relateQuery;
    }

    public void setRelateQuery(String[] relateQuery) {
        this.relateQuery = relateQuery;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NULL_IMAGE) {
            return new ProductNUllImgViewHolder(mLayoutInflater.inflate(R.layout.product_recom_null, parent, false));
        } else if (viewType == TYPE_TEXT) {
            return new ProductTextViewHolder(mLayoutInflater.inflate(R.layout.adapter_text, parent, false));
        } else if (viewType == TYPE_GOODS) {
            return new ProductRecomViewHolder(mLayoutInflater.inflate(R.layout.item_product_list, parent, false));
        } else if (viewType == TYPE_END) {
            return new ProductEndViewHolder(mLayoutInflater.inflate(R.layout.footer_for_twice_prod_layout, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder helper, int position) {
        if (getItemViewType(position) == TYPE_GOODS) {      //  商品
            ProductBaseBean item = mList.get(position - 2);
            // 商品图
            UPaiYunLoadManager.loadImage(mContext, item.coverImgUrl, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_small, helper.getView(R.id.img_pic));
            // 国旗
            if (item.sellerInfo != null && item.sellerInfo.country != null) {
                int flagId = HaiUtils.getFlagResourceId(item.sellerInfo.country, false);
                if (flagId == -1) {
                    int px20 = HaiConstants.CompoundSize.PX_20;

                    Glide.with(mContext)
                            .load(item.sellerInfo.flag)
                            .override((int) (1.8 * px20), px20)
                            .placeholder(R.mipmap.ic_default_square_small)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .dontAnimate()
                            .into((ImageView) helper.getView(R.id.img_country));
                } else {
                    Glide.with(mContext)
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
                tvDiscount.setText(PriceUtils.FloatKeepOneFloor(item.realPrice*10/item.mallPrice) + "折");

            }

            helper.setVisible(R.id.iv_soldout, HaiUtils.isSoldOut(item.inStock, item.stock) ? true : false);

            helper.setChecked(R.id.like_button, item.is_star);
            helper.getView(R.id.like_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (HaiUtils.needLogin(mContext)) {
                        return;
                    }
                    ProductRepository.getInstance()
                            .likeProduct(item.spuid)
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
                }
            });

            helper.itemView.setOnClickListener(v -> ProductMainActivity.toThisAcivity(mContext, item.spuid, item.name));
        } else if (getItemViewType(position) == TYPE_END) {
            if (HaiUtils.getSize(mList) > 0) {
                helper.setVisible(R.id.loadedLayout, true);
            } else {
                helper.itemView.setVisibility(View.GONE);
                ViewGroup.LayoutParams lp = helper.itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                helper.itemView.setLayoutParams(lp);
            }
        } else if (getItemViewType(position) == TYPE_NULL_IMAGE) {
            if (type == ProductNullType.PRODUCT_MAIN) {

            } else if (type == ProductNullType.PRODUCT_SEARCH) {
                helper.setText(R.id.tv_null, "没有搜索到相关商品T T");

                if (HaiUtils.getSize(relateQuery) > 0) {
                    helper.setVisible(R.id.llyt_recom_search, true);
                    ((TagFlowLayout) helper.getView(R.id.tflyt_recom)).setAdapter(new TagAdapter<String>(relateQuery) {
                        @Override
                        public View getView(FlowLayout parent, int position, String key) {
                            TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.hot_words_layout, parent, false);
                            tv.setText(key);
                            return tv;
                        }
                    });
                    ((TagFlowLayout) helper.getView(R.id.tflyt_recom)).setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            if (onTagClickListener != null)
                                onTagClickListener.onTagClick(relateQuery[position]);
                            return false;
                        }
                    });
                } else {
                    helper.setVisible(R.id.llyt_recom_search, false);
                }

            }
        } else if (getItemViewType(position) == TYPE_TEXT) {
            if (HaiUtils.getSize(mList) > 0) {
//                helper.setVisible(R.id.loadedLayout, true);
            } else {
                helper.itemView.setVisibility(View.GONE);
                ViewGroup.LayoutParams lp = helper.itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                helper.itemView.setLayoutParams(lp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 3 : 3 + mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_NULL_IMAGE;
        } else if (position == 1) {
            return TYPE_TEXT;
        } else if (position == (getItemCount() - 1)) {
            return TYPE_END;
        } else {
            return TYPE_GOODS;
        }
    }

    public interface OnTagClickListener {
        void onTagClick(String tag);
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    class ProductRecomViewHolder extends BaseViewHolder {

        public ProductRecomViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductNUllImgViewHolder extends BaseViewHolder {

        public ProductNUllImgViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductTextViewHolder extends BaseViewHolder {

        public ProductTextViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductEndViewHolder extends BaseViewHolder {

        public ProductEndViewHolder(View itemView) {
            super(itemView);
        }
    }
}
