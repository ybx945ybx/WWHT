package com.a55haitao.wwht.adapter.firstpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.FirstPageViewType;
import com.a55haitao.wwht.data.model.annotation.StatementType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.DailyProductBean;
import com.a55haitao.wwht.data.model.entity.FirstPageBodyBean;
import com.a55haitao.wwht.data.model.entity.HotAdBean;
import com.a55haitao.wwht.data.model.entity.HotFavorableBean;
import com.a55haitao.wwht.data.model.entity.MallStatementBean;
import com.a55haitao.wwht.data.model.entity.NewsFlashBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.TabBannerBean;
import com.a55haitao.wwht.data.model.result.NewsFlashResult;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OtherRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.easyopt.RecommendEasyOptActivity;
import com.a55haitao.wwht.ui.activity.firstpage.EntriesActivity;
import com.a55haitao.wwht.ui.activity.firstpage.FavorableActivity;
import com.a55haitao.wwht.ui.activity.firstpage.NewsFlashActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.activity.social.HotTagActivity;
import com.a55haitao.wwht.ui.view.CountDownTimerView;
import com.a55haitao.wwht.ui.view.HaiImageView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.PagerContainer;
import com.a55haitao.wwht.ui.view.StrikeThruTextView;
import com.a55haitao.wwht.ui.view.VerticalMarqueeView;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
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
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.upyun.library.common.UploadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 首页热门adapter
 * Created by a55 on 2017/9/22.
 */

public class FirstPageHotAdapter extends BaseQuickAdapter<FirstPageBodyBean, BaseViewHolder> {

    private Activity            mActivity;
    private LayoutInflater      mInflater;
    private ConvenientBanner    mConvenientBanner;
    private VerticalMarqueeView marqueeView;
    private PagerContainer      pagerContainer;

    private ArrayList<TabBannerBean> tabBannerDatas;
    //    private ArrayList<ProductBaseBean> flashSaeList;
    private CoverFlowAdapter         coverFlowAdapter;

    private long    sysTime;                                    // 服务器时间
    private int     flashSaleWidth;
    private int     flashSaleHeigh;
    private int     flashSaleGoods;
    private String  mTabName;                                   // 所属Tab名
    private Tracker mTracker;

    public FirstPageHotAdapter(Activity baseActivity, ArrayList<FirstPageBodyBean> data) {
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
                // 四个icon
                .registerItemType(FirstPageViewType.MallStatement, R.layout.first_page_mall_statement_layout)
                // 优惠快报
                .registerItemType(FirstPageViewType.NewsFlash, R.layout.center_first_newsflash_layout)
                // 广告图
                .registerItemType(FirstPageViewType.Ads, R.layout.first_page_ad_img_layout)
                // 限时抢购
                .registerItemType(FirstPageViewType.FlashSale, R.layout.center_first_flash_sale_layout)
                // 通用标题
                .registerItemType(FirstPageViewType.SubTitle, R.layout.first_page_sub_title_layout)
                // 官网特卖
                .registerItemType(FirstPageViewType.Image_Flavor_Hot, R.layout.first_page_favorable_img_layout)
                // 官网特卖底部查看全部
                .registerItemType(FirstPageViewType.Flavor_Hot_Foot_All, R.layout.flavor_hot_foot_all)
                // 超值单品
                .registerItemType(FirstPageViewType.Daily_Product, R.layout.item_daily_product);

        mActivity = baseActivity;
        mInflater = LayoutInflater.from(baseActivity);
        // GA Tracker
        HaiApplication application = (HaiApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();
        initSize();

    }

    // 初始化限时抢购尺寸
    private void initSize() {
        flashSaleWidth = (DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 22)) * 25 / 34;
        flashSaleHeigh = flashSaleWidth / 2;
        flashSaleGoods = flashSaleHeigh - DisplayUtils.dp2px(mActivity, 30);
    }

    public void setSysTime() {
        sysTime = HaiApplication.systemTime / 1000;
    }

    @Override
    protected void convert(BaseViewHolder holder, FirstPageBodyBean item) {
        switch (holder.getItemViewType()) {
            case FirstPageViewType.Banner:
                setBannerView(holder, item);
                break;
            case FirstPageViewType.Ads:              // 活动图
                setAdView(holder, item);
                break;
            case FirstPageViewType.MallStatement:    // 4个icon
                setMallStatementView(holder, item);
                break;
            case FirstPageViewType.SubTitle:         // 标题
                setSubTitleView(holder, item);
                break;
            case FirstPageViewType.FlashSale:        // 限时抢购
                setFlashSaleView(holder, item);
                break;
            case FirstPageViewType.NewsFlash:        // 优惠快报
                setNewsFlashView(holder, item);
                break;
            case FirstPageViewType.Image_Flavor_Hot: // 官网特卖
                setFlavorView(holder, item);
                break;
            case FirstPageViewType.Flavor_Hot_Foot_All: // 官网特卖底部查看更多
                setFlavorFootView(holder, item);
                break;
            case FirstPageViewType.Daily_Product:    // 超值单品
                setDailyProductView(holder, item);
                break;

        }
    }

    private void setBannerView(BaseViewHolder holder, FirstPageBodyBean item) {
        Logger.d("setBannerView");
        tabBannerDatas = (ArrayList<TabBannerBean>) item.getData();
        if (mConvenientBanner == null) {
            mConvenientBanner = holder.getView(R.id.banner);
            mConvenientBanner
                    .setPages(() -> new LocalImageHolderView(), tabBannerDatas)
                    .setPageIndicator(new int[]{R.mipmap.ic_banner_show_indicator_normal, R.mipmap.ic_banner_show_indicator_selected})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnItemClickListener(position1 -> {
                        TabBannerBean bean = tabBannerDatas.get(position1);
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
        } else {
            mConvenientBanner.notifyDataSetChanged();
        }
    }

    private void setAdView(BaseViewHolder holder, FirstPageBodyBean item) {
        HaiImageView              ivAd = holder.getView(R.id.iv_ad);
        LinearLayout.LayoutParams lp   = (LinearLayout.LayoutParams) ivAd.getLayoutParams();
        lp.width = DisplayUtils.getScreenWidth(mActivity);
        lp.height = lp.width * 3 / 8;
        ivAd.setLayoutParams(lp);

        ViewGroup.LayoutParams lp1 = holder.itemView.getLayoutParams();
        lp1.height = lp.height + DisplayUtils.dp2px(mActivity, 12);
        holder.itemView.setLayoutParams(lp1);

        HotAdBean hotAdBean = null;
        if (item.getData() instanceof ArrayList) {
            hotAdBean = (HotAdBean) ((ArrayList) item.getData()).get(0);
        } else if (item.getData() instanceof HotAdBean) {
            hotAdBean = (HotAdBean) item.getData();
        }

        UPaiYunLoadManager.loadImage(mActivity,
                hotAdBean.image,
                UpaiPictureLevel.SINGLE,
                R.id.u_pai_yun_null_holder_tag, ivAd);

        final String adUri = hotAdBean.uri;
        holder.itemView.setOnClickListener(v -> {
            // 埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_HotAd, TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Hot_Ads);
            // 跳转
            HaiUriMatchUtils.matchUri(mActivity, adUri);
        });

    }

    private void setMallStatementView(BaseViewHolder holder, FirstPageBodyBean item) {
        ArrayList<MallStatementBean> mallStatementBean = (ArrayList<MallStatementBean>) item.getData();
        LinearLayout                 rootLin           = holder.getView(R.id.statementRootLin);
        if (rootLin.getChildCount() == 0)
            for (int i = 0; i < mallStatementBean.size(); i++) {
                LinearLayout itemLin = (LinearLayout) mInflater.inflate(R.layout.first_page_statement_view, rootLin, false);
                ImageView    image   = (ImageView) itemLin.getChildAt(0);
                HaiTextView  txtView = (HaiTextView) itemLin.getChildAt(1);

                MallStatementBean data = mallStatementBean.get(i);
                itemLin.setTag(R.id.tag_for_statement_type, data.type);
                itemLin.setTag(R.id.tag_for_statement_url, data.url);
                itemLin.setTag(R.id.tag_for_statement_title, data.title);
                itemLin.setTag(R.id.tag_for_statement_position, i);
                UPaiYunLoadManager.loadImage(mActivity, data.icon, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, image);
                txtView.setText(data.title);
                if (!TextUtils.isEmpty(data.background)) {
                    txtView.setTextColor(Color.parseColor("#" + data.background));
                }
                itemLin.setOnClickListener(v -> onStatemnetClick((int) v.getTag(R.id.tag_for_statement_position), (int) v.getTag(R.id.tag_for_statement_type), (String) v.getTag(R.id.tag_for_statement_url), (String) v.getTag(R.id.tag_for_statement_title)));
                rootLin.addView(itemLin);

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
        } else if (text.equals("精 选 合 集") || text.equals("今 日 推 荐") || text.equals("热 卖 单 品")) {
            bottomLine.setVisibility(View.INVISIBLE);
            moreTxt.setVisibility(View.VISIBLE);
        } else if (text.equals("推 荐 草 单") || text.equals("官 网 特 卖") || text.equals("限 时 抢 购") || text.equals("超 值 单 品")) {
            bottomLine.setVisibility(View.INVISIBLE);
            moreTxt.setVisibility(View.INVISIBLE);
        }
        moreTxt.setTag(text);
        titleTxtView.setText(text);
    }

    private void setFlashSaleView(BaseViewHolder holder, FirstPageBodyBean item) {
        ArrayList<ProductBaseBean> flashSaeList = (ArrayList) item.getData();
        if (pagerContainer == null) {
            pagerContainer = holder.getView(R.id.flash_sale_container);
            ViewPager                pager        = pagerContainer.getViewPager();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) pager.getLayoutParams();
            layoutParams.width = flashSaleWidth;
            layoutParams.height = layoutParams.width / 2;
            pager.setLayoutParams(layoutParams);
            pager.setClipChildren(false);
            coverFlowAdapter = new CoverFlowAdapter(flashSaeList);
            pager.setAdapter(new CoverFlowAdapter(flashSaeList));
            pager.setCurrentItem(flashSaeList.size() * 100 / 2, false);
            pager.setOffscreenPageLimit(2);
        } else {
            coverFlowAdapter.flashSaeList = flashSaeList;
            coverFlowAdapter.notifyDataSetChanged();
        }
    }

    private void setNewsFlashView(BaseViewHolder holder, FirstPageBodyBean item) {
        LinearLayout llytNewsFlash = holder.getView(R.id.llyt_newsflash);
        if (marqueeView == null) {
            marqueeView = holder.getView(R.id.tv_newsflash);
        }
        HaiTextView tvMore = holder.getView(R.id.tv_more_newsflash);

        llytNewsFlash.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, NewsFlashActivity.class);
            mActivity.startActivity(intent);
        });
        tvMore.setOnClickListener(v -> {
            // 埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_ExpressMore
                    , TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Hot_Letter);
            // 跳转
            Intent intent = new Intent(mActivity, NewsFlashActivity.class);
            mActivity.startActivity(intent);
        });

        // 垂直跑马灯
        ArrayList<NewsFlashBean> letters = ((NewsFlashResult) item.getData()).letters;
        List<String>             info    = new ArrayList<>();
        if (letters != null && letters.size() > 0) {
            for (int i = 0; i < letters.size(); i++) {
                info.add(letters.get(i).title);
            }
        }
        marqueeView.startWithList(info);

        marqueeView.setOnItemClickListener((position12, textView) -> {
            // 埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Express
                    , TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Hot_Letter);
            // 跳转
            HaiUriMatchUtils.matchUri(mActivity, letters.get(position12).url);

            OtherRepository.getInstance()
                    .updateLettersCount(letters.get(position12).id)
                    .compose(RxLifecycleAndroid.bindActivity(ActivityCollector.getTopActivity().lifecycle()))
                    .subscribe(new DefaultSubscriber<Object>() {
                        @Override
                        public void onSuccess(Object result) {

                        }

                        @Override
                        public void onFinish() {
                        }
                    });

        });
        marqueeView.startFlipping();

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

    private void setFlavorFootView(BaseViewHolder holder, FirstPageBodyBean item) {
        holder.itemView.setOnClickListener(v -> {
            // 埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_OfficialSaleMore
                    , TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Hot_OfficalSale);
            // 跳转
            FavorableActivity.toThisActivity(mActivity, mTabName, 0);

        });
    }


    private void setDailyProductView(BaseViewHolder holder, FirstPageBodyBean item) {
        DailyProductBean dailyProductBean = (DailyProductBean) item.getData();

        // 商品图
        UPaiYunLoadManager.loadImage(mActivity, dailyProductBean.img, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_small, holder.getView(R.id.img_pic));
        HaiTextView tvTag = holder.getView(R.id.tv_tag);
        if (!TextUtils.isEmpty(dailyProductBean.tag)) {
            tvTag.setVisibility(View.VISIBLE);
            tvTag.setText(dailyProductBean.tag);
            if (!TextUtils.isEmpty(dailyProductBean.tag_color)) {
                tvTag.setBackgroundColor(Color.parseColor("#" + dailyProductBean.tag_color));
            }
        } else {
            tvTag.setVisibility(View.GONE);
        }

        HaiTextView tvPriceName = holder.getView(R.id.tv_price_name);
        if (!TextUtils.isEmpty(dailyProductBean.price_name)) {
            tvPriceName.setVisibility(View.VISIBLE);
            tvPriceName.setText(dailyProductBean.price_name);
            if (!TextUtils.isEmpty(dailyProductBean.price_color)) {
                tvPriceName.setTextColor(Color.parseColor("#" + dailyProductBean.price_color));
            }
        } else {
            tvPriceName.setVisibility(View.GONE);
        }

        // 国旗
        if (dailyProductBean.sellerInfo != null && dailyProductBean.sellerInfo.country != null) {
            int flagId = HaiUtils.getFlagResourceId(dailyProductBean.sellerInfo.country, false);
            if (flagId == -1) {
                int px20 = HaiConstants.CompoundSize.PX_20;

                Glide.with(mActivity)
                        .load(dailyProductBean.sellerInfo.flag)
                        .override((int) (1.8 * px20), px20)
                        .placeholder(R.mipmap.ic_default_square_small)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into((ImageView) holder.getView(R.id.img_country));
            } else {
                Glide.with(mActivity)
                        .load(flagId)
                        .placeholder(R.mipmap.ic_default_square_small)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into((ImageView) holder.getView(R.id.img_country));
            }
        }
        holder.setText(R.id.tv_brand_name, dailyProductBean.title); // 标题
        holder.setText(R.id.tv_desc, dailyProductBean.name);      // 副标题
        holder.setText(R.id.tv_seller, dailyProductBean.sellerInfo != null ? dailyProductBean.sellerInfo.name + "官网发货" : "官网发货");
        holder.setText(R.id.tv_real_price, String.format("%s%d", ApiUrl.RMB_UNICODE, (int) (dailyProductBean.realPrice / 100))); // 现价

        TextView tvDiscount = holder.getView(R.id.tvDiscount);
        if (Math.abs(dailyProductBean.realPrice - dailyProductBean.mallPrice) < 0.0001) { // 原价现价相同
            holder.setVisible(R.id.tv_mall_price, false);
            tvDiscount.setVisibility(View.GONE);

        } else {
            holder.setVisible(R.id.tv_mall_price, true);
            holder.setText(R.id.tv_mall_price, String.format("%s%d", ApiUrl.RMB_UNICODE, (int) (dailyProductBean.mallPrice / 100))); // 原价
            // 原价删除线
            ((HaiTextView) holder.getView(R.id.tv_mall_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(PriceUtils.FloatKeepOneFloor(dailyProductBean.realPrice * 10 / dailyProductBean.mallPrice) + "折");

        }
        holder.setVisible(R.id.iv_soldout, HaiUtils.isSoldOut(dailyProductBean.inStock, dailyProductBean.stock));

        holder.itemView.setOnClickListener(v -> ProductMainActivity.toThisAcivity(mActivity, dailyProductBean.spuid, dailyProductBean.name));

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

    // 限时抢购画廊效果
    private class CoverFlowAdapter extends PagerAdapter {
        private ArrayList<ProductBaseBean> flashSaeList;

        public CoverFlowAdapter(ArrayList<ProductBaseBean> flashSaeList) {
            this.flashSaeList = flashSaeList;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            final View                  view        = mInflater.inflate(R.layout.flash_sale_pager_itme, null);
            RelativeLayout              rlytContent = (RelativeLayout) view.findViewById(R.id.rlyt_content);
            RelativeLayout.LayoutParams lp          = (RelativeLayout.LayoutParams) rlytContent.getLayoutParams();
            lp.width = flashSaleWidth;
            lp.height = lp.width / 2;
            rlytContent.setLayoutParams(lp);

            final int                   nPosition = position % flashSaeList.size();
            ImageView                   ivCover   = (ImageView) view.findViewById(R.id.iv_goods);
            RelativeLayout.LayoutParams ivlp      = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
            ivlp.width = flashSaleGoods;
            ivlp.height = flashSaleGoods;
            ivCover.setLayoutParams(ivlp);
            UPaiYunLoadManager.loadImage(mActivity, flashSaeList.get(nPosition).coverImgUrl, UpaiPictureLevel.TRIBBLE, R.mipmap.ic_default_square_small, ivCover);
            HaiTextView        tvBrand   = (HaiTextView) view.findViewById(R.id.tv_brand);
            HaiTextView        tvTitle   = (HaiTextView) view.findViewById(R.id.tv_title);
            StrikeThruTextView stvPrice  = (StrikeThruTextView) view.findViewById(R.id.tv_market_price);
            HaiTextView        tvPrice   = (HaiTextView) view.findViewById(R.id.tv_time_limit_price);
            RelativeLayout     rlytTimer = (RelativeLayout) view.findViewById(R.id.rlyt_timer);
            CountDownTimerView tvTime    = (CountDownTimerView) view.findViewById(R.id.count_down_timer);

            if (flashSaeList.get(nPosition).end_time == 0) {
                rlytTimer.setVisibility(View.INVISIBLE);
            } else {
                rlytTimer.setVisibility(View.VISIBLE);
                if (flashSaeList.get(nPosition).end_time - sysTime > 0) {
                    float[] time = HaiTimeUtils.counterTimeLong(String.valueOf(sysTime), String.valueOf(flashSaeList.get(nPosition).end_time));
                    tvTime.setTime((int) time[2], (int) time[1], (int) time[0]);
                    tvTime.start();
                } else {
                    tvTime.setTime(0, 0, 0);
                }
            }

            tvBrand.setText(flashSaeList.get(nPosition).brand);
            tvTitle.setText(flashSaeList.get(nPosition).name);
            stvPrice.setText(PriceUtils.toRMBFromFen(flashSaeList.get(nPosition).mallPrice));
            tvPrice.setText(PriceUtils.toRMBFromFen(flashSaeList.get(nPosition).realPrice));
            view.setOnClickListener(v -> {
                // 埋点
                HashMap<String, String> kv = new HashMap<>();
                kv.put(TraceUtils.Event_Kv_Goods_Id, flashSaeList.get(nPosition).spuid);
                YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_FlashSale
                        , TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, TraceUtils.Chid_Hot_FlashSale);
                // 跳转
                ProductMainActivity.toThisAcivity(mActivity, flashSaeList.get(nPosition).spuid, "限时抢购", flashSaeList.get(nPosition).name);
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return flashSaeList.size() * 100;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    public void onStatemnetClick(int position, int type, String uri, String title) {
        // 埋点
        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Icon, TraceUtils.Event_Category_Click, "", null, getIconChid(position));
        // 跳转
        switch (type) {
            case StatementType.StatementType_Fashion: {
                EntriesActivity.toThisActivity(mActivity, mTabName);
                break;
            }
            case StatementType.StatementType_Hot: {
                FavorableActivity.toThisActivity(mActivity, mTabName, 0);
                break;
            }
            case StatementType.StatementType_Tag: {
                Intent intent = new Intent(mActivity, HotTagActivity.class);
                mActivity.startActivity(intent);
                break;
            }
            case StatementType.StatementType_H5: {
                H5Activity.toThisActivity(mActivity, uri, title);
                break;
            }
            case StatementType.StatementType_EO: {
                mActivity.startActivity(new Intent(mActivity, RecommendEasyOptActivity.class));
            }
        }

        // GA 事件
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("电商运营")
                .setAction("4icon Click")
                .setLabel(uri + "," + title)
                .build());

    }

    private String getIconChid(int position) {
        switch (position) {
            case 0:
                return TraceUtils.Chid_Hot_Icon_One;
            case 1:
                return TraceUtils.Chid_Hot_Icon_Two;
            case 2:
                return TraceUtils.Chid_Hot_Icon_Three;
            case 3:
                return TraceUtils.Chid_Hot_Icon_Four;
        }
        return null;
    }

    public void stopMarqueeView() {
        if (marqueeView != null && marqueeView.isFlipping())
            marqueeView.stopFlipping();
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
