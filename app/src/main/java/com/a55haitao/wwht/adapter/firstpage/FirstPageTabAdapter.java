package com.a55haitao.wwht.adapter.firstpage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.model.annotation.FirstPageViewType;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.CollectSpecialsBean;
import com.a55haitao.wwht.data.model.entity.FirstPageBodyBean;
import com.a55haitao.wwht.data.model.entity.HotFavorableBean;
import com.a55haitao.wwht.data.model.entity.SellerBean;
import com.a55haitao.wwht.data.model.entity.TabBannerBean;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.data.model.result.CollectSpecialsResult;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.firstpage.EntriesActivity;
import com.a55haitao.wwht.ui.view.FullyGridLayoutManager;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 首页子频道页adapter
 * Created by a55 on 2017/9/22.
 */

public class FirstPageTabAdapter extends BaseQuickAdapter<FirstPageBodyBean, BaseViewHolder> {

    private Activity         mActivity;
    private ConvenientBanner mConvenientBanner;
    private ProductAdapter   prodTwiceAdapter;

    private String  mTabName;                                   // 所属Tab名
    private Tracker mTracker;

    public FirstPageTabAdapter(Activity baseActivity, ArrayList<FirstPageBodyBean> data) {
        super(data);

        setMultiTypeDelegate(new MultiTypeDelegate<FirstPageBodyBean>() {
            @Override
            protected int getItemType(FirstPageBodyBean bean) {
                return bean.getViewType();
            }
        });

        getMultiTypeDelegate()
                //  广告页
                .registerItemType(FirstPageViewType.Banner, R.layout.first_page_banner_layout)
                // 通用标题
                .registerItemType(FirstPageViewType.SubTitle, R.layout.first_page_sub_title_layout)
                // 合集推荐
                .registerItemType(FirstPageViewType.Collect_Specials, R.layout.first_page_collect_specials_recyclerview)
                // 人气品牌
                .registerItemType(FirstPageViewType.Recycler_Brand, R.layout.first_page_recycler_for_brand_layout)
                // 官网特卖
                .registerItemType(FirstPageViewType.Image_Flavor, R.layout.first_page_favorable_img_layout)
                // 精选合集大图
                .registerItemType(FirstPageViewType.Image_Entry, R.layout.first_page_entry_img_layout)
                // 精选合集横向列表
                .registerItemType(FirstPageViewType.Recycler_Product_200, R.layout.first_page_recycler_layout)
                // 单品推荐
                .registerItemType(FirstPageViewType.Product_Twice, R.layout.first_page_null_list_view);

        mActivity = baseActivity;
        // GA Tracker
        HaiApplication application = (HaiApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    protected void convert(BaseViewHolder holder, FirstPageBodyBean item) {
        switch (holder.getItemViewType()) {
            case FirstPageViewType.Banner:
                setBannerView(holder, item);
                break;
            case FirstPageViewType.Collect_Specials:        // 合集推荐
                setCollectSpecialView(holder, item);
                break;
            case FirstPageViewType.Recycler_Brand:          // 人气品牌
                setBrandView(holder, item);
                break;
            case FirstPageViewType.SubTitle:                // 标题
                setSubTitleView(holder, item);
                break;
            case FirstPageViewType.Image_Flavor:            // 官网特卖
                setFlavorView(holder, item);
                break;
            case FirstPageViewType.Image_Entry:             // 精选合集大图
                setImageEntryView(holder, item);
                break;
            case FirstPageViewType.Recycler_Product_200:    // 精选合集横向列表
                setRecyclerProductView(holder, item);
                break;
            case FirstPageViewType.Product_Twice:           // 单品推荐
                setProductTwiceView(holder, item);
                break;

        }
    }

    private void setBannerView(BaseViewHolder holder, FirstPageBodyBean item) {
        if (mConvenientBanner == null) {
            mConvenientBanner = holder.getView(R.id.banner);
            ArrayList<TabBannerBean> datas = (ArrayList<TabBannerBean>) item.getData();
            mConvenientBanner
                    .setPages(() -> new LocalImageHolderView(), datas)
                    .setPageIndicator(new int[]{R.mipmap.ic_banner_show_indicator_normal, R.mipmap.ic_banner_show_indicator_selected})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnItemClickListener(position1 -> {
                        TabBannerBean bean = datas.get(position1);
                        // 埋点
                        HashMap<String, String> kv = new HashMap<>();
                        kv.put(TraceUtils.Event_Kv_Banner_Id, position1 + "");
                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Banner, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Banner, kv, TraceUtils.Chid_Hot_Banner + position1);
                        // 跳转
                        HaiUriMatchUtils.matchUri(mActivity, bean.uri);

                        // GA 事件
                        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                                .setCategory("电商运营")
                                .setAction("Banner Click");
                        if (!TextUtils.isEmpty(mTabName)) {
                            StringBuilder sbLabel = new StringBuilder(mTabName);
                            sbLabel.append(",").append(bean.uri);
                            if (!TextUtils.isEmpty(bean.name)) {
                                sbLabel.append(",").append(bean.name);
                            }
                            eventBuilder.setLabel(sbLabel.toString());
                        }
                        mTracker.send(eventBuilder.setValue(holder.getLayoutPosition() + 1).build());

                    });
            mConvenientBanner.startTurning(3000);
        }
    }

    private void setCollectSpecialView(BaseViewHolder holder, FirstPageBodyBean item) {
        RecyclerView rycvCollectSpecials = holder.getView(R.id.rycv_collect_special);
        rycvCollectSpecials.setLayoutManager(new FullyGridLayoutManager(mActivity, 2));
        CollectSpecialsResult collectSpecialsResult = (CollectSpecialsResult) item.getData();
        RVBaseAdapter<CollectSpecialsBean> collectSpecialAdapter = new RVBaseAdapter<CollectSpecialsBean>(mActivity, collectSpecialsResult.data, R.layout.collect_special_item) {
            @Override
            public void bindView(RVBaseHolder holder, CollectSpecialsBean item) {
                // 商品图
                UPaiYunLoadManager.loadImage(mActivity, item.image, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_small, holder.getView(R.id.iv_img));
                holder.setText(R.id.tv_title, item.title_main);
                holder.setText(R.id.tv_subtitle, item.title_second);

                holder.itemView.setOnClickListener(v -> HaiUriMatchUtils.matchUri(mActivity, item.uri));
            }
        };
        rycvCollectSpecials.setAdapter(collectSpecialAdapter);
    }

    private void setBrandView(BaseViewHolder holder, FirstPageBodyBean item) {
        RecyclerView brandRecyView = (RecyclerView) holder.itemView;
        if (brandRecyView.getAdapter() == null) {
            LinearLayoutManager brandLayoutMgr = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
            brandRecyView.setLayoutManager(brandLayoutMgr);
            RVBaseLoopAdapter<SellerBean.SellerBaseBean> adapter = new RVBaseLoopAdapter<SellerBean.SellerBaseBean>(mActivity, (ArrayList) item.getData(), R.layout.item_img_brand_cover) {
                @Override
                public void bindView(RVBaseHolder holder, SellerBean.SellerBaseBean sellerDescBaseBean) {
                    UPaiYunLoadManager.loadImage(mActivity, sellerDescBaseBean.img_cover, UpaiPictureLevel.TWICE, R.id.u_pai_yun_null_holder_tag, holder.getView(R.id.bigCoverImg));
                    UPaiYunLoadManager.loadImage(mActivity, sellerDescBaseBean.logo3, UpaiPictureLevel.TWICE, R.mipmap.ic_default_400_240, holder.getView(R.id.logoImg));
                    holder.itemView.setTag(R.id.tag_for_img, holder.getAdapterPosition() % getItemRealCount());
                    holder.itemView.setOnClickListener(v -> {
                        // 埋点
                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_HotBrand
                                , TraceUtils.Event_Category_Click, "", null, TraceUtils.getFirstTabChidHead(mTabName) + "HB-" + sellerDescBaseBean.name);
                        // 跳转
                        SiteActivity.toThisActivity(mActivity, sellerDescBaseBean.name,
                                PageType.BRAND, sellerDescBaseBean.logo1, sellerDescBaseBean.logo3, sellerDescBaseBean.img_cover);
                    });
                }
            };
            brandRecyView.setAdapter(adapter);
            int   visiblePosition = adapter.getItemRealCount() << 10;
            float offset          = (DeviceUtils.getScreenWidth() - DeviceUtils.getDensity() * 200) / 2;
            brandLayoutMgr.scrollToPositionWithOffset(visiblePosition, (int) offset);
            new PagerSnapHelper().attachToRecyclerView(brandRecyView);      //设置画廊模式
        }
    }

    private void setImageEntryView(BaseViewHolder holder, FirstPageBodyBean item) {
        TabEntryBean.EntriesBean entriesBean = (TabEntryBean.EntriesBean) item.getData();
        UPaiYunLoadManager.loadImage(mActivity, entriesBean.image, UpaiPictureLevel.SINGLE, R.mipmap.ic_default_square_large, holder.getView(R.id.mainImg));
        if (!TextUtils.isEmpty(entriesBean.small_icon)) {
            UPaiYunLoadManager.loadImage(mActivity, entriesBean.small_icon, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, holder.getView(R.id.smallCoverImg));
        }
        holder.getView(R.id.iv_triangle).setVisibility(View.GONE);
        holder.getView(R.id.viewline).setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> {
            // 埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_SpecialCollection
                    , TraceUtils.Event_Category_Click, "", null, TraceUtils.getFirstTabChidHead(mTabName) + "JX");
            // 跳转
            HaiUriMatchUtils.matchUri(mActivity, entriesBean.uri);

        });

    }

    private void setRecyclerProductView(BaseViewHolder holder, FirstPageBodyBean item) {
        RecyclerView        pd200RecyclerView = (RecyclerView) holder.itemView;
        String              uri               = ((TabEntryBean.EntriesBean) getItem(holder.getLayoutPosition() - 1).getData()).uri;
        HorizontalPdAdapter adapter           = (HorizontalPdAdapter) pd200RecyclerView.getAdapter();
        if (adapter == null) {
            pd200RecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
            adapter = new HorizontalPdAdapter(mActivity, (ArrayList) item.getData(), R.layout.item_product_brief_200_layout);
            pd200RecyclerView.setAdapter(adapter);
        } else {
            adapter.changeDatas((ArrayList) item.getData());
        }
        adapter.setType(1);
        adapter.mUri = uri;

    }

    private void setProductTwiceView(BaseViewHolder holder, FirstPageBodyBean item) {
        RecyclerView rycv = (RecyclerView) holder.itemView;
        rycv.setLayoutManager(new FullyGridLayoutManager(mActivity, 2));
        if (rycv.getAdapter() == null) {
            prodTwiceAdapter = new ProductAdapter((ArrayList) item.getData(), mActivity, null, rycv);
            prodTwiceAdapter.setActivityAnalysListener(spuid -> {
                // 埋点
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, TraceUtils.getFirstTabChidHead(mTabName) + "NP");

            });
            rycv.setAdapter(prodTwiceAdapter);
        }
    }

    private void setSubTitleView(BaseViewHolder holder, FirstPageBodyBean item) {
        TextView titleTxtView = holder.getView(R.id.titleTxt);
        String   text         = (String) item.getData();
        View     bottomLine   = holder.getView(R.id.bottomLine);
        View     moreTxt      = holder.getView(R.id.moreTxt);
        if (text.equals("人 气 品 牌")) {
            bottomLine.setVisibility(View.GONE);
            moreTxt.setVisibility(View.INVISIBLE);
        } else if (text.equals("单 品 推 荐") || text.equals("热 门 分 类")) {
            bottomLine.setVisibility(View.VISIBLE);
            moreTxt.setVisibility(View.INVISIBLE);
        } else if (text.equals("精 选 合 集")) {
            bottomLine.setVisibility(View.INVISIBLE);
            moreTxt.setVisibility(View.VISIBLE);
            moreTxt.setOnClickListener(v -> {
                // 埋点
                YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_SpecialCollectionMore
                        , TraceUtils.Event_Category_Click, "", null, TraceUtils.getFirstTabChidHead(mTabName) + "JX");
                // 跳转
                EntriesActivity.toThisActivity(mActivity, mTabName);

            });
        } else if (text.equals("推 荐 草 单") || text.equals("官 网 特 卖") || text.equals("限 时 抢 购") || text.equals("超 值 单 品")) {
            bottomLine.setVisibility(View.INVISIBLE);
            moreTxt.setVisibility(View.INVISIBLE);
        }
        titleTxtView.setText(text);
    }

    private void setFlavorView(BaseViewHolder holder, FirstPageBodyBean item) {
        HotFavorableBean.SpecialsBean.SpecialItem specialItem = ((HotFavorableBean.SpecialsBean.SpecialItem) item.getData());
        Glide.with(mActivity)
                .load(UPaiYunLoadManager.formatImageUrl(specialItem.image, UpaiPictureLevel.SINGLE))
                .transform(new GlideRoundTransform(mActivity, 8))
                .placeholder(R.mipmap.ic_default_400_240)
                .dontAnimate()
                .diskCacheStrategy(UPaiYunLoadManager.sDiskCacheStrategy)
                .into((ImageView) holder.getView(R.id.coverImg));
        if (!TextUtils.isEmpty(specialItem.small_icon)) {
            UPaiYunLoadManager.loadImage(mActivity, specialItem.small_icon, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, holder.getView(R.id.smallCoverImg));
        }
        holder.getView(R.id.coverImg).setOnClickListener(v -> {
            // 埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_OfficialSale
                    , TraceUtils.Event_Category_Click, "", null, mTabName.equals("热门") ? TraceUtils.Chid_Hot_OfficalSale : TraceUtils.getFirstTabChidHead(mTabName) + "OS");
            // 跳转
            HaiUriMatchUtils.matchUri(mActivity, specialItem.uri);

        });

    }

    // banner图
    class LocalImageHolderView implements Holder<TabBannerBean> {

        private ImageView mCoverImg;
        private ImageView mSmallImg;

        @Override
        public View createView(Context context) {

            RelativeLayout inflate = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.first_page_img_layout_for_banner, null);
            mCoverImg = ButterKnife.findById(inflate, R.id.coverImg);
            mSmallImg = ButterKnife.findById(inflate, R.id.smallCoverImg);

            return inflate;
        }

        @Override
        public void UpdateUI(Context context, int position, TabBannerBean data) {
            Glide.with(mActivity)
                    .load(UpyUrlManager.getUrl(data.image, DisplayUtils.getScreenWidth(mActivity)))
                    .placeholder(R.mipmap.ic_default_400_240)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(mCoverImg);
            if (!TextUtils.isEmpty(data.small_icon)) {
                UPaiYunLoadManager.loadImage(mActivity, data.small_icon, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, mSmallImg);
            }
        }
    }

    public void changeLikeState(String spuid, boolean likeState) {
        if (prodTwiceAdapter == null) {
            return;
        }
        if (HaiUtils.getSize(prodTwiceAdapter.getData()) > 0) {
            for (int i = 0; i < prodTwiceAdapter.getData().size(); i++) {
                if (prodTwiceAdapter.getItem(i).spuid.equals(spuid)) {
                    if (prodTwiceAdapter.getItem(i).is_star != likeState) {
                        prodTwiceAdapter.getItem(i).is_star = likeState;
                        prodTwiceAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    public void pauseTurning() {
        if (mConvenientBanner != null && mConvenientBanner.isTurning()) {
            mConvenientBanner.stopTurning();
        }
    }

    public void starTurning() {
        if (mConvenientBanner != null && !mConvenientBanner.isTurning()) {
            mConvenientBanner.startTurning(3000);
        }
    }

    public void setTabName(String tabName) {
        mTabName = tabName;
    }

}
