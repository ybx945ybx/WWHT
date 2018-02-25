package com.a55haitao.wwht.ui.activity.product;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.adapter.common.ViewPagerAdapter;
import com.a55haitao.wwht.adapter.easyopt.AddToEasyoptListAdapter;
import com.a55haitao.wwht.adapter.firstpage.BannerAdapter;
import com.a55haitao.wwht.adapter.product.ProductBrandAdpater;
import com.a55haitao.wwht.adapter.product.ProductEveryoneBuyingAdapter;
import com.a55haitao.wwht.adapter.product.ProductRecomAdapter;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.APIErrorEvent;
import com.a55haitao.wwht.data.event.EasyoptListChangeEvent;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.OrderCreateBackEvent;
import com.a55haitao.wwht.data.event.ProductLikeEvent;
import com.a55haitao.wwht.data.event.ProductRTStockChangeEvent;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;
import com.a55haitao.wwht.data.interfaces.IReponse;
import com.a55haitao.wwht.data.interfaces.OrderModel;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.ProductNullType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.AssuranceBean;
import com.a55haitao.wwht.data.model.entity.CartLogsBean;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.GrantCouponsBean;
import com.a55haitao.wwht.data.model.entity.LikeProductBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.ProductDetailBean;
import com.a55haitao.wwht.data.model.entity.ProductRecommendBean;
import com.a55haitao.wwht.data.model.entity.ProductSearchBean;
import com.a55haitao.wwht.data.model.entity.SellerBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.model.entity.TabBannerBean;
import com.a55haitao.wwht.data.model.result.EasyoptList4AddResult;
import com.a55haitao.wwht.data.model.result.FullcutHasCheckResult;
import com.a55haitao.wwht.data.model.result.RelateSpecialsResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.CartRepository;
import com.a55haitao.wwht.data.repository.ExtendRepository;
import com.a55haitao.wwht.data.repository.PassPortRepository;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.easyopt.EasyoptCreateActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.OrderCreateActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.ShoppingCartActivity;
import com.a55haitao.wwht.ui.activity.social.PostDetailActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.fragment.product.ProductQueryFragment;
import com.a55haitao.wwht.ui.fragment.product.ProductTxtImageFragment;
import com.a55haitao.wwht.ui.view.AvatarPopupWindow;
import com.a55haitao.wwht.ui.view.BadgeView;
import com.a55haitao.wwht.ui.view.DividerItemDecoration;
import com.a55haitao.wwht.ui.view.FullyGridLayoutManager;
import com.a55haitao.wwht.ui.view.HaiCountDownView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ObservableScrollView;
import com.a55haitao.wwht.ui.view.ProductBrandRelatedView;
import com.a55haitao.wwht.ui.view.SpecPopupWindow;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.ui.view.ToastPwAddToEasyopt;
import com.a55haitao.wwht.ui.view.WrapContentHeightViewPager;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.StaticObjectUtil;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.glide.GlideCircleTransform;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.XNSDKListener;
import me.relex.circleindicator.CircleIndicator;
import tom.ybxtracelibrary.YbxTrace;

import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Pray;
import static com.a55haitao.wwht.utils.glide.UPaiYunLoadManager.sDiskCacheStrategy;

@MLinkRouter(keys = {"productDetailKey"})
public class ProductMainActivity extends BaseHasFragmentActivity implements XNSDKListener {

    @BindView(R.id.activity_product_main)   RelativeLayout             activityProductMain;
    @BindView(R.id.msv)                     MultipleStatusView         mSview;
    @BindView(R.id.navBackLin)              LinearLayout               navBackLin;
    @BindView(R.id.img_shopping_cart)       ImageView                  mImgShoppingCart;       // 购物车
    @BindView(R.id.navShareBtn)             ImageView                  navShareBtn;
    @BindView(R.id.navigationLin)           LinearLayout               navigationLin;
    @BindView(R.id.viewline)                View                       viewline;
    @BindView(R.id.tabRadioGroup)           RadioGroup                 tabRadioGroup;
    @BindView(R.id.scrollView)              ObservableScrollView       scrollView;
    @BindView(R.id.llyt_content)            LinearLayout               llytContent;            //
    @BindView(R.id.llyt_product_detail)     LinearLayout               llytProduct;            // 有商品板块
    @BindView(R.id.tabRadioGroup_hide)      RadioGroup                 tabRadioGroupHide;
    @BindView(R.id.secondFragmentViewPager) WrapContentHeightViewPager secondFragmentViewPager;
    @BindView(R.id.llyt_product_recom)      LinearLayout               nullContent;            // 商品不存在
    @BindView(R.id.rycv_recommend)          RecyclerView               rycvRecommend;          // 大家都在买
    @BindView(R.id.bottomBar)               LinearLayout               bottomBar;
    @BindView(R.id.addCartBtn)              HaiTextView                addCartBtn;
    @BindView(R.id.payBtn)                  HaiTextView                payBtn;
    @BindView(R.id.noStockBtn)              Button                     noStockBtn;
    @BindView(R.id.tv_service)              HaiTextView                mTvService;             // 客服

    //  banner
    @BindView(R.id.bannerViewPager)     ViewPager            bannerViewPager;
    @BindView(R.id.bannerIndicator)     CircleIndicator      bannerIndicator;
    @BindView(R.id.likeProductBtn)      CheckBox             likeProductBtn;
    @BindView(R.id.thumbnailScrollView) HorizontalScrollView thumbnailScrollView;
    @BindView(R.id.thumbnailLin)        LinearLayout         thumbnailLin;

    // 限时活动
    @BindView(R.id.llyt_limit_time) LinearLayout     llytLimitTime;
    @BindView(R.id.tv_limit_time)   HaiCountDownView tvTimer;

    // 商品名称 价格 折扣 核价
    @BindView(R.id.productNameTxt)  TextView     productNameTxt;
    @BindView(R.id.currentPriceTxt) TextView     currentPriceTxt;
    @BindView(R.id.mallPriceTxt)    TextView     mallPriceTxt;
    @BindView(R.id.tv_discount)     HaiTextView  tvDiscount;
    @BindView(R.id.productRTLin)    LinearLayout productRTLin;
    @BindView(R.id.productRTImage)  ImageView    productRTImage;

    // 促销语
    @BindView(R.id.tv_sales_promotion) HaiTextView tvSalesPromotion;

    // 满减优惠语
    @BindView(R.id.tv_fullcut) HaiTextView tvFullCut;

    // 官网价格 官网运费
    @BindView(R.id.officalCurrentPriceTxt)  TextView     officalCurrentPriceTxt;
    @BindView(R.id.officalMallPriceTxt)     TextView     officalMallPriceTxt;
    @BindView(R.id.domestictransformfeeTxt) TextView     domestictransformfeeTxt;
    @BindView(R.id.llyt_consumptionfee)     LinearLayout consumptionfeeLin;
    @BindView(R.id.consumptionfeeTxt)       HaiTextView  consumptionfeeTxt;

    // 优惠券
    @BindView(R.id.llyt_coupon)     LinearLayout llytCoupon;
    @BindView(R.id.tv_coupon_title) HaiTextView  tvCouponTitle;
    @BindView(R.id.tv_get_coupon)   HaiTextView  tvGetCoupon;

    // 小编推荐语
    @BindView(R.id.tv_editer_recommend) HaiTextView tvEditerRecommend;

    // assurance
    @BindView(R.id.rycv_assurance) RecyclerView rycvAssurance;

    // 听听大家怎么说（笔记）
    @BindView(R.id.llyt_relate_post) LinearLayout llytRelatePost;
    @BindView(R.id.tv_post_count)    HaiTextView  tvPostCount;
    @BindView(R.id.rycv_relete_post) RecyclerView rycvRelatePost;

    // 官网
    @BindView(R.id.sellerLin)     RelativeLayout sellerLin;
    @BindView(R.id.sellerImage)   ImageView      sellerImage;
    @BindView(R.id.sellerNameTxt) TextView       sellerNameTxt;
    @BindView(R.id.sellerDescTxt) TextView       sellerDescTxt;

    // 品牌
    @BindView(R.id.brandLin)               LinearLayout    brandLin;
    @BindView(R.id.brandImageTxt)          HaiTextView     brandImageTxt;
    @BindView(R.id.brandHeaderLin)         RelativeLayout  brandHeaderLin;
    @BindView(R.id.brandImage)             ImageView       brandImage;
    @BindView(R.id.brandNameTxt)           TextView        brandNameTxt;
    @BindView(R.id.brandCountryTxt)        TextView        brandCountryTxt;
    @BindView(R.id.brandDescTxt)           TextView        brandDescTxt;
    @BindView(R.id.relativeBrandViewPager) ViewPager       relativeBrandViewPager;
    @BindView(R.id.relativeBrandIndicator) CircleIndicator relativeBrandIndicator;

    // 相关专题
    @BindView(R.id.llyt_relate_theme) LinearLayout     llytRelateTheme;
    @BindView(R.id.rycv_relete_theme) RecyclerView     rycvRelateTheme;
    @BindView(R.id.sdv_theme)         SimpleDraweeView sdvRelateTheme;

    // 大家都在买
    @BindView(R.id.everyoneBuyingLin)       LinearLayout    everyoneBuyingLin;
    @BindView(R.id.everyOneBuyingTitle)     HaiTextView     everyOneBuyingTitle;
    @BindView(R.id.everyOneBuyingViewPager) ViewPager       everyOneBuyingViewPager;
    @BindView(R.id.everyOneBuyingIndicator) CircleIndicator everyOneBuyingIndicator;

    // 海淘流程
    @BindView(R.id.iv_hiden_show)        ImageView    ivHidenShow;
    @BindView(R.id.llyt_process_content) LinearLayout llytProcessContent;

    // 购物须知
    @BindView(R.id.mustKnowBtn) TextView mustKnowBtn;

    private BannerAdapter                                    mBannerAdapter;
    private RVBaseAdapter<GrantCouponsBean.GrantCoupon>      mCouponAdapter;
    private RVBaseAdapter<RelateSpecialsResult.PostsBean>    mReletePostAdapter;
    private RVBaseAdapter<RelateSpecialsResult.SpecialsBean> mReleteThemeAdapter;
    private RVBaseAdapter<AssuranceBean>                     mAssuranceAdapter;
    private ProductBrandAdpater                              mProductBrandAdpater;
    private ProductEveryoneBuyingAdapter                     mProductEveryoneBuyingAdapter;
    private ProductRecomAdapter                              productRecomAdapter;

    private ArrayList<GrantCouponsBean.GrantCoupon>      mCouponList;       // 草单
    private ArrayList<RelateSpecialsResult.PostsBean>    mRelatePostList;   // 笔记
    private ArrayList<RelateSpecialsResult.SpecialsBean> mRelateThemeList;  // 专题

    private ProductDetailBean mProductDetailBean;
    private ProductRepository mRepository;

    private ArrayList<BaseFragment> mFragmentList;
    private ProductTxtImageFragment mProductTxtImageFragment;
    private ProductQueryFragment    mProductQueryFragment;

    private BadgeView mBvShoppingCart;                                      // 购物车角标

    // SPU ID
    protected String mSpuid;
    private   String defaultSkuid;
    private   String title;

    // 满减优惠活动
    private String fullcutTitle;
    private String fullcutPromotion;
    private String fid;

    // 规格选择弹出框
    private SpecPopupWindow                         mSpecPopupWindow;
    private Dialog                                  mEasyoptDlg;
    private Dialog                                  mCouponDlg;
    private View                                    mDlgView;
    private View                                    mDlgCouponView;
    private View                                    mDlgHeaderView;
    private RecyclerView                            mRvEasyopt;
    private RecyclerView                            mRycvCoupons;
    private ImageView                               ivCouponClose;
    private List<EasyoptList4AddResult.EasyoptBean> mEasyoptList; // 草单
    private AddToEasyoptListAdapter                 mAdapter;
    private HaiTextView                             mTvWishlistCount;
    private ToastPwAddToEasyopt                     mPwEasyopt;

    private Tracker         mTracker;    // GA Tracker
    private StringBuilder   mSbGAEvent;  // GA事件Label文本
    private String          mSourceName; // 来源Activity名称
    private ToastPopuWindow mPwToast;

    private boolean hasProduct;          // false 商品不存在，进来就是报14000错误  true 进来成功调到接口数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Ntalker.getInstance().setSDKListener(this);
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("商品详情");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        initVars();
        initViews();
        initData();

    }

    private void initVars() {
        mRepository = ProductRepository.getInstance();
        mEasyoptList = new ArrayList<>();
        mCouponList = new ArrayList<>();
        mRelatePostList = new ArrayList<>();
        mRelateThemeList = new ArrayList<>();

        //通过intent方式获取动态参数值
        Intent intent = getIntent();
        if (intent != null) {
            mSpuid = intent.getStringExtra("spuid");
            mSourceName = intent.getStringExtra("source_name");
            title = intent.getStringExtra("name");
        }

    }

    private void initViews() {
        // 购物车角标
        mBvShoppingCart = new BadgeView(mActivity);
        mBvShoppingCart.setTargetView(mImgShoppingCart);
        mBvShoppingCart.setBadgeCount(0);
        mBvShoppingCart.setBadgeMargin(0, DisplayUtils.dp2px(mActivity, -2), DisplayUtils.dp2px(mActivity, -3), 0);

        // 心愿单和优惠券dg
        initDlgView();

        // Nav返回
        navBackLin.setOnClickListener(v -> onBackPressed());

        mSview.setOnRetryClickListener(v -> loadData());

        scrollView.setScrollViewListener((x, y, oldx, oldy) -> {
            if (!hasProduct) {
                return;
            }
            float y1 = tabRadioGroup.getY();
            if (y >= y1) {
                tabRadioGroupHide.setVisibility(View.VISIBLE);
            } else {
                tabRadioGroupHide.setVisibility(View.GONE);
            }
        });

        // assurance
        rycvAssurance.setLayoutManager(new LinearLayoutManager(mActivity));
        mAssuranceAdapter = new RVBaseAdapter<AssuranceBean>(mActivity, new ArrayList<>(), R.layout.product_main_assurance_item) {
            @Override
            public void bindView(RVBaseHolder holder, AssuranceBean assuranceBean) {
                holder.setText(R.id.tv_assurance, assuranceBean.title);
                ((HaiTextView) holder.getView(R.id.tv_assurance)).setTextColor(assuranceBean.getRtype() ? Color.parseColor("#666666") : Color.parseColor("#999999"));
                ((ImageView) holder.getView(R.id.iv_assurance_state)).setImageResource(assuranceBean.getRtype() ? R.mipmap.ic_product_assurance_ok : R.mipmap.ic_product_assurance_ng);
            }
        };
        rycvAssurance.setAdapter(mAssuranceAdapter);

        // 相关笔记（听听大家怎么说）
        rycvRelatePost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mReletePostAdapter = new RVBaseAdapter<RelateSpecialsResult.PostsBean>(this, mRelatePostList, R.layout.relate_post_item) {
            @Override
            public void bindView(RVBaseHolder holder, RelateSpecialsResult.PostsBean post) {
                // 图片
                SimpleDraweeView sdv = holder.getView(R.id.img_pic);
                sdv.setImageURI(UpyUrlManager.getUrl(post.image_url.get(0).url, DisplayUtils.dp2px(mActivity, 150)));
                // 文本
                if (!TextUtils.isEmpty(post.content)) {
                    holder.setText(R.id.tv_desc, post.content);
                }

                holder.itemView.setOnClickListener(v -> {
                    // 埋点
                    //                    TraceUtils.addClick(TraceUtils.PageCode_PostDetail, post.id + "", ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Post + "", "");

                    //                    TraceUtils.addAnalysAct(TraceUtils.PageCode_PostDetail, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Post, post.id + "");

                    Intent intent = new Intent(mActivity, PostDetailActivity.class);
                    intent.putExtra("post_id", post.id);
                    intent.putExtra("wh_rate", post.image_url.get(0).wh_rate);
                    startActivity(intent);

                });

            }
        };
        rycvRelatePost.setAdapter(mReletePostAdapter);

        // 相关专题
        rycvRelateTheme.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mReleteThemeAdapter = new RVBaseAdapter<RelateSpecialsResult.SpecialsBean>(this, mRelateThemeList, R.layout.relate_theme_item) {
            @Override
            public void bindView(RVBaseHolder holder, RelateSpecialsResult.SpecialsBean item) {
                ((SimpleDraweeView) holder.getView(R.id.imgView)).setImageURI(UPaiYunLoadManager.formatImageUrl(item.image, UpaiPictureLevel.SINGLE));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 埋点
                        //                        TraceUtils.addAnalysActMatchUri(ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_OfficialSale, item.uri);
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_OfficialSale, TraceUtils.Event_Category_Click, "", null, "");

                        HaiUriMatchUtils.matchUri(mActivity, item.uri);
                    }
                });
                holder.setVisibility(holder.getLayoutPosition() == getItemCount() - 1 ? View.VISIBLE : View.GONE, R.id.space);

            }
        };
        rycvRelateTheme.setAdapter(mReleteThemeAdapter);

        sdvRelateTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 埋点
                //                TraceUtils.addAnalysActMatchUri(ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_OfficialSale, (String) v.getTag());
                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_OfficialSale, TraceUtils.Event_Category_Click, "", null, "");

                HaiUriMatchUtils.matchUri(mActivity, (String) v.getTag());
            }
        });

    }

    private void initDlgView() {
        // 草单dg
        mDlgView = LayoutInflater.from(this).inflate(R.layout.dlg_add_to_my_easyopt, null);
        mDlgHeaderView = LayoutInflater.from(this).inflate(R.layout.header_dlg_add_to_my_easyopt, null);
        // 取消
        mDlgView.findViewById(R.id.ib_cancel).setOnClickListener(v -> mEasyoptDlg.dismiss());
        // 心愿单单品数
        mTvWishlistCount = (HaiTextView) mDlgHeaderView.findViewById(R.id.tv_wishlist_count);
        // 新建草单
        mDlgHeaderView.findViewById(R.id.rl_create_easyopt).setOnClickListener(v -> EasyoptCreateActivity.toThisActivity(mActivity, mProductDetailBean.coverImgUrl, mSpuid));
        // 添加到心愿单
        mDlgHeaderView.findViewById(R.id.rl_wishlist).setOnClickListener(v -> {
            likeProductBtn.performClick();
            mEasyoptDlg.dismiss();
        });
        // 草单列表
        mRvEasyopt = (RecyclerView) mDlgView.findViewById(R.id.rv_easyopt_list);
        mRvEasyopt.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvEasyopt.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        // 草单Adapter
        mAdapter = new AddToEasyoptListAdapter(mEasyoptList);
        mAdapter.addHeaderView(mDlgHeaderView);
        mRvEasyopt.setAdapter(mAdapter);
        mRvEasyopt.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mEasyoptList.size() >= position + 1 && mEasyoptList.get(position).contain == 0) {
                    requestAddProductToMyEasyoptList(position);
                } else {
                    ToastUtils.showToast(mActivity, "此草单已包含该商品");
                }
            }
        });

        // 优惠券dg
        mDlgCouponView = LayoutInflater.from(this).inflate(R.layout.product_main_coupon_layout, null);
        ivCouponClose = (ImageView) mDlgCouponView.findViewById(R.id.iv_close);
        ivCouponClose.setOnClickListener(v -> mCouponDlg.dismiss());
        mRycvCoupons = (RecyclerView) mDlgCouponView.findViewById(R.id.rycv_coupon_list);
        mRycvCoupons.setLayoutManager(new LinearLayoutManager(mActivity));
        mCouponAdapter = new RVBaseAdapter<GrantCouponsBean.GrantCoupon>(mActivity, mCouponList, R.layout.product_main_coupon_item) {
            @Override
            public void bindView(RVBaseHolder holder, GrantCouponsBean.GrantCoupon grantCoupon) {
                holder.setVisibility(grantCoupon.is_receive == 1 ? View.VISIBLE : View.GONE, R.id.couponStatusImg);
                holder.setVisibility(grantCoupon.is_receive == 1 ? View.INVISIBLE : View.VISIBLE, R.id.llyt_coupon_recived);

                holder.setText(R.id.tv_title, grantCoupon.title);
                holder.setText(R.id.tv_desc, grantCoupon.desc);
                holder.setText(R.id.tv_time, grantCoupon.getTermOfValidity());
                holder.setText(R.id.tv_sub_title, grantCoupon.subtitle);

                holder.getView(R.id.tv_get_coupon).setOnClickListener(v -> {
                    if (HaiUtils.needLogin(mActivity)) {
                        return;
                    }
                    PassPortRepository.getInstance()
                            .receiveCoupon(String.valueOf(grantCoupon.id))
                            .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                            .subscribe(new DefaultSubscriber<Object>() {
                                @Override
                                public void onSuccess(Object o) {
                                    //                                    mPwToast = ToastPopuWindow.makeText(mActivity, "领取成功", AlertViewType.AlertViewType_OK).parentView(mDlgCouponView);
                                    //                                    mPwToast.show();
                                    Toast.makeText(mActivity, "领取成功", Toast.LENGTH_SHORT).show();
                                    grantCoupon.is_receive = 1;
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFinish() {

                                }
                            });
                });
            }
        };
        mRycvCoupons.setAdapter(mCouponAdapter);
    }

    private void initData() {
        mSview.showLoading();
        loadData();
        //        getShoppingCartBadge(false);
    }

    // 获取数据
    private void loadData() {
        ProductRepository.getInstance()
                .getProductDetail(mSpuid, true)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new com.a55haitao.wwht.data.net.TestSubscriber<>(new IReponse<ProductDetailBean>() {
                    @Override
                    public void onSuccess(ProductDetailBean productDetailBean) {
                        hasProduct = true;
                        defaultSkuid = productDetailBean.defaultSkuid;
                        mProductDetailBean = productDetailBean;
                        initializeProductData();

                        // 开始初始化界面/核价
                        renderUI();

                        // 检查满减优惠
                        checkHasFullcut();

                        // 获取购物车数量
                        getShoppingCartBadge(false);

                        // 获取优惠券并渲染ui
                        getGrantCoupons();

                        // 获取相关笔记相关专题并渲染ui
                        getRelateSpecials();

                        // 获取大家都在买并渲染ui
                        getRecommendProduct();

                        // 获取品牌商家并渲染ui
                        if (productDetailBean.brandInfo.query != null && !TextUtils.isEmpty(productDetailBean.brandInfo.query.brand)) {
                            getSearchProduct(productDetailBean.brandInfo);
                        } else {
                            brandLin.setVisibility(View.GONE);
                        }

                        // 统计事件
                        doStatistics(productDetailBean);

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showProductFailView(mSview, e, hasProduct);
                    }
                }));

    }

    // 检查是否有满减优惠活动
    private void checkHasFullcut() {
        ExtendRepository.getInstance()
                .getFullcutHasCheck(mSpuid)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<FullcutHasCheckResult>() {
                    @Override
                    public void onSuccess(FullcutHasCheckResult fullcutHasCheckResult) {
                        if (fullcutHasCheckResult != null) {
                            fullcutTitle = fullcutHasCheckResult.title;
                            fullcutPromotion = fullcutHasCheckResult.promotion_msg;
                            fid = fullcutHasCheckResult.id + "";

                            String s = fullcutPromotion + " 前往活动";

                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);

                            // 点击事件
                            spannableStringBuilder.setSpan(new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
                                    ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
                                    gotoFullcutList();
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    ds.setUnderlineText(true);
                                    ds.setColor(Color.parseColor("#999999"));
                                }
                            }, s.length() - 4, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                            tvFullCut.setText(spannableStringBuilder);
                            tvFullCut.setMovementMethod(LinkMovementMethod.getInstance());

                            tvFullCut.setVisibility(View.VISIBLE);

                        } else {
                            tvFullCut.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    // 前往满减优惠活动商品列表
    private void gotoFullcutList() {
        Intent intent = new Intent(mActivity, ProductsListActivity.class);
        intent.putExtra("title", fullcutTitle);
        intent.putExtra("fid", fid);
        mActivity.startActivity(intent);
    }

    private void getProductRT(boolean needDel) {
        // 核价
        if (needDel) {
            productRTLin.setVisibility(View.VISIBLE);
            mProductDetailBean.setInProductRTing(true);
            Animation          circleAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.anim_roation_orage_cirlce);
            LinearInterpolator linear          = new LinearInterpolator();
            circleAnimation.setInterpolator(linear);
            if (circleAnimation != null) {
                productRTImage.startAnimation(circleAnimation);
            }
            ProductRepository.getInstance()
                    .getProductRT(mSpuid)
                    .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                    .subscribe(new DefaultSubscriber<ProductDetailBean>() {
                        @Override
                        public void onSuccess(ProductDetailBean productDetailBean) {
                            if (needDel) {
                                mProductDetailBean = null;
                                mProductDetailBean = productDetailBean;
                                mProductDetailBean.isUpdate = true;

                                initializeProductData();
                                EventBus.getDefault().post(new ProductRTStockChangeEvent(!productDetailBean.isSoldOut()));
                            }

                        }

                        @Override
                        public void onFinish() {
                            if (needDel) {
                                // 停止核价动画
                                productRTImage.clearAnimation();
                                productRTLin.setVisibility(View.GONE);
                                mProductDetailBean.setInProductRTing(false);
                            }
                        }
                    });
        } else {
            productRTLin.setVisibility(View.GONE);
            mProductDetailBean.setInProductRTing(false);
            ProductRepository.getInstance()
                    .getProductAsyncCheck(defaultSkuid)
                    .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                    .subscribe(new DefaultSubscriber<Object>() {

                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        }

    }

    private void getGrantCoupons() {
        mRepository.getGrantCoupons(mSpuid)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<GrantCouponsBean>() {
                    @Override
                    public void onSuccess(GrantCouponsBean grantCouponsBean) {
                        // 优惠券
                        if (HaiUtils.getSize(grantCouponsBean.couponq_list) > 0) {
                            for (GrantCouponsBean.GrantCoupon grantCoupon : grantCouponsBean.couponq_list) {
                                grantCoupon.is_receive = grantCoupon.canReceive() ? 0 : 1;
                            }
                            llytCoupon.setVisibility(View.VISIBLE);
                            tvCouponTitle.setText(grantCouponsBean.couponq_list.get(0).title);
                            creatCouponDialog(grantCouponsBean.couponq_list);
                            tvGetCoupon.setOnClickListener(v -> showCouponDialog());
                        } else {
                            llytCoupon.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private void creatCouponDialog(ArrayList<GrantCouponsBean.GrantCoupon> couponq_list) {
        if (mCouponDlg == null) {
            mCouponDlg = new Dialog(this);
            mCouponDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mCouponDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            int dlgWidth  = DisplayUtils.getScreenWidth(mActivity);
            int dlgHeight = DisplayUtils.getScreenHeight(mActivity) * 3 / 5;
            mCouponDlg.setContentView(mDlgCouponView, new LinearLayout.LayoutParams(dlgWidth, dlgHeight));
            Window dialogWindow = mCouponDlg.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);

        }
        mCouponAdapter.changeDatas(couponq_list);

    }

    /**
     * 弹出领取优惠券
     */
    private void showCouponDialog() {
        mCouponDlg.show();
    }

    private void getRelateSpecials() {
        mRepository.getRelateSpecial(mSpuid)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<RelateSpecialsResult>() {
                    @Override
                    public void onSuccess(RelateSpecialsResult relateSpecialsResult) {
                        // 相关笔记
                        if (HaiUtils.getSize(relateSpecialsResult.posts) > 0) {
                            llytRelatePost.setVisibility(View.VISIBLE);
                            tvPostCount.setText("听听大家怎么说（" + relateSpecialsResult.posts.size() + "）");
                            mReletePostAdapter.changeDatas(relateSpecialsResult.posts);
                        } else {
                            llytRelatePost.setVisibility(View.GONE);
                        }
                        // 相关专题
                        if (HaiUtils.getSize(relateSpecialsResult.specials) > 0) {
                            llytRelateTheme.setVisibility(View.VISIBLE);
                            if (HaiUtils.getSize(relateSpecialsResult.specials) == 1) {
                                rycvRelateTheme.setVisibility(View.GONE);
                                sdvRelateTheme.setVisibility(View.VISIBLE);
                                sdvRelateTheme.setImageURI(UPaiYunLoadManager.formatImageUrl(relateSpecialsResult.specials.get(0).image, UpaiPictureLevel.SINGLE));
                                sdvRelateTheme.setTag(relateSpecialsResult.specials.get(0).uri);
                            } else {
                                rycvRelateTheme.setVisibility(View.VISIBLE);
                                sdvRelateTheme.setVisibility(View.GONE);
                                mReleteThemeAdapter.changeDatas(relateSpecialsResult.specials);
                            }
                        } else {
                            llytRelateTheme.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private void getRecommendProduct() {
        mRepository.getRecommendProduct(mSpuid)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<ArrayList<ProductBaseBean>>() {
                    @Override
                    public void onSuccess(ArrayList<ProductBaseBean> productBaseBeens) {
                        // 大家都在买
                        if (HaiUtils.getSize(productBaseBeens) > 0) {
                            everyoneBuyingLin.setVisibility(View.VISIBLE);
                            mProductEveryoneBuyingAdapter = new ProductEveryoneBuyingAdapter(mActivity, productBaseBeens);
                            everyOneBuyingViewPager.setAdapter(mProductEveryoneBuyingAdapter);
                            everyOneBuyingIndicator.setViewPager(everyOneBuyingViewPager);
                            mProductEveryoneBuyingAdapter.registerDataSetObserver(everyOneBuyingIndicator.getDataSetObserver());
                            mProductEveryoneBuyingAdapter.setClickListener(new ProductBrandRelatedView.ProductBrandRelatedViewInterface() {
                                @Override
                                public void tapOnProduct(String spuid, String name) {
                                    // 埋点
                                    //                                    TraceUtils.addClick(TraceUtils.PageCode_PostDetail, spuid, ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_EveryBuyingProduct + "", "");
                                    HashMap<String, String> kv = new HashMap<String, String>();
                                    kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                                    YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                                    //                                    TraceUtils.addAnalysAct(TraceUtils.PageCode_ProductDetail, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_EveryBuyingProduct, spuid);

                                    ProductMainActivity.toThisAcivity(mActivity, spuid, name);
                                }
                            });

                        } else {
                            everyoneBuyingLin.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private void getSearchProduct(ProductDetailBean.BrandBaseDesModel brandInfo) {
        mRepository.getSearchProduct(mSpuid, brandInfo)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<ProductSearchBean>() {
                    @Override
                    public void onSuccess(ProductSearchBean productSearchBean) {
                        // 品牌信息 && 品牌商品
                        if (HaiUtils.getSize(productSearchBean.products) >= 2) {

                            String logo = mProductDetailBean.brandInfo.getLogoUrl();
                            if (TextUtils.isEmpty(logo)) {
                                brandImageTxt.setVisibility(View.VISIBLE);
                                brandImage.setVisibility(View.GONE);

                                String logoName = "";
                                if (!TextUtils.isEmpty(mProductDetailBean.brandInfo.nameen)) {
                                    logoName = mProductDetailBean.brandInfo.nameen;
                                } else if (!TextUtils.isEmpty(mProductDetailBean.brandInfo.namecn)) {
                                    logoName = mProductDetailBean.brandInfo.namecn;
                                } else {
                                    logoName = mProductDetailBean.brandInfo.name;
                                }

                                HaiUtils.setBrandOrSellerImg(brandImageTxt, logoName, 3);
                            } else {
                                brandImageTxt.setVisibility(View.GONE);
                                brandImage.setVisibility(View.VISIBLE);
                                UPaiYunLoadManager.loadImage(mActivity, logo, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, brandImage, false);
                            }
                            brandNameTxt.setText(mProductDetailBean.brandInfo.getBrandWholeName());
                            brandCountryTxt.setText(mProductDetailBean.brandInfo.country);
                            brandDescTxt.setText(mProductDetailBean.brandInfo.desc);

                            mProductBrandAdpater = new ProductBrandAdpater(mActivity, productSearchBean.products);
                            relativeBrandViewPager.setAdapter(mProductBrandAdpater);
                            relativeBrandIndicator.setViewPager(relativeBrandViewPager);
                            mProductBrandAdpater.registerDataSetObserver(relativeBrandIndicator.getDataSetObserver());

                            mProductBrandAdpater.setClickListener(new ProductBrandRelatedView.ProductBrandRelatedViewInterface() {
                                @Override
                                public void tapOnProduct(String spuid, String name) {
                                    // 埋点
                                    //                                    TraceUtils.addClick(TraceUtils.PageCode_PostDetail, spuid, ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_BrandProduct + "", "");
                                    HashMap<String, String> kv = new HashMap<String, String>();
                                    kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                                    YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                                    //                                    TraceUtils.addAnalysAct(TraceUtils.PageCode_ProductDetail, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_BrandProduct, spuid);

                                    ProductMainActivity.toThisAcivity(mActivity, spuid, name);
                                }
                            });

                            brandHeaderLin.setOnClickListener(v -> {
                                String                              title     = null;
                                ProductDetailBean.BrandBaseDesModel brandInfo = mProductDetailBean.brandInfo;
                                if (!TextUtils.isEmpty(brandInfo.nameen)) {
                                    title = brandInfo.nameen;
                                } else if (!TextUtils.isEmpty(brandInfo.namecn)) {
                                    title = brandInfo.namecn;
                                } else {
                                    title = brandInfo.name;
                                }

                                // 埋点
                                //                                TraceUtils.addClick(TraceUtils.PageCode_BrandOrSiteDetail, title, ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Brand + "", "");
                                //                                HashMap<String, String> kv = new HashMap<String, String>();
                                //                                kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Brand, TraceUtils.Event_Category_Click, "", null, "");

                                //                                TraceUtils.addAnalysAct(TraceUtils.PageCode_BrandOrSiteDetail, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Brand, title);

                                SiteActivity.toThisActivity(mActivity, title, PageType.BRAND, brandInfo.logo1, brandInfo.logo3, brandInfo.img_cover);
                            });

                        } else {
                            brandLin.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 获取购物车数量
     * 发生了加入购物车的动作后 needsendevent为true
     */
    private void getShoppingCartBadge(boolean needSendEvent) {
        if (!HaiUtils.isLogin()) {
            return;
        }
        ProductRepository.getInstance()
                .getShoppingCartListCnt()
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<ShoppingCartCntBean>() {
                    @Override
                    public void onSuccess(ShoppingCartCntBean shoppingCartCntBean) {
                        setShoppingCartCount(shoppingCartCntBean.count);
                        if (needSendEvent) {
                            EventBus.getDefault().post(new ShoppingCartCntChangeEvent(shoppingCartCntBean.count));
                        }
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }

    private void doStatistics(ProductDetailBean productDetailBean) {
        // TalkingData 查看商品
        TalkingDataAppCpa.onViewItem(productDetailBean.spuid,
                productDetailBean.category.getCategoryInfo(),
                productDetailBean.name,
                (int) (productDetailBean.realPrice * 100));

        // GA 查看商品事件
        mSbGAEvent = new StringBuilder();
        mSbGAEvent.append(productDetailBean.spuid)
                .append(",").append(productDetailBean.name)
                .append(",").append(productDetailBean.brandInfo.getBrandWholeName());

        if (productDetailBean.sellerInfo != null && !TextUtils.isEmpty(productDetailBean.sellerInfo.nameen)) {
            mSbGAEvent.append(",").append(productDetailBean.sellerInfo.nameen);
        }
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("交易相关")
                .setAction("查看商品")
                .setLabel(mSbGAEvent.toString())
                .build());
        // 限时抢购 click && 单品推荐 click
        if (!TextUtils.isEmpty(mSourceName)) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("电商运营")
                    .setAction(mSourceName + " Click")
                    .setLabel(mSbGAEvent.toString())
                    .build());
        }

    }

    // 选择规格弹窗
    private void createSpecPopuView(boolean fromBuyingButton) {
        if (mSpecPopupWindow == null) {
            mSpecPopupWindow = new SpecPopupWindow(ProductMainActivity.this, activityProductMain.getWidth(), activityProductMain.getHeight(), mProductDetailBean);
            mSpecPopupWindow.setSpecListener(new SpecPopupWindow.SpecPopuWindowInterface() {

                @Override
                public void onClickPopuOKFromBuying(String skuid) {
                    if (skuid != null && skuid.length() > 0) {
                        // 埋点上报
                        cartLogs(skuid, 1);

                        // 埋点
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Order_Spuid, mSpuid);
                        kv.put(TraceUtils.Event_Kv_Order_Skuid, skuid);
                        kv.put(TraceUtils.Event_Kv_Order_Count, "1");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Goods_Buy, kv, "");

                        mSpecPopupWindow.showOrDismiss(bottomBar);
                        // 直接下单购买
                        OrderModel.CartType cartType = new OrderModel.CartType(mSpuid, skuid, "1");
                        OrderCreateActivity.toThisActivity(ProductMainActivity.this, cartType, 0);
                    } else {
                        mPwToast = ToastPopuWindow.makeText(ProductMainActivity.this, "请选择规格", AlertViewType.AlertViewType_Warning).parentView(bottomBar);
                        mPwToast.show();
                    }
                }

                @Override
                public void onClickPopuOKFromAddingCart(String skuid) {
                    if (skuid != null && skuid.length() > 0) {
                        // 埋点上报
                        cartLogs(skuid, 0);

                        mSpecPopupWindow.showOrDismiss(bottomBar);
                        // 加入购物车
                        showProgressDialog("加入购物车...");
                        CartRepository.getInstance()
                                .addCart(mSpuid, skuid, 1)
                                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                                    @Override
                                    public void onSuccess(CommonDataBean result) {
                                        getShoppingCartBadge(true);
                                        mPwToast = ToastPopuWindow.makeText(mActivity, "加入购物车成功", AlertViewType.AlertViewType_OK).parentView(bottomBar);
                                        mPwToast.show();

                                        // 埋点
                                        HashMap<String, String> kv = new HashMap<String, String>();
                                        kv.put(TraceUtils.Event_Kv_Order_Spuid, mSpuid);
                                        kv.put(TraceUtils.Event_Kv_Order_Skuid, skuid);
                                        kv.put(TraceUtils.Event_Kv_Cartid, result.cart_id);
                                        kv.put(TraceUtils.Event_Kv_Order_Count, "1");
                                        kv.put(TraceUtils.Event_Kv_Code, "0");
                                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Cart, kv, "");

                                    }

                                    @Override
                                    public void onFinish() {
                                        dismissProgressDialog();
                                    }

                                    @Override
                                    public boolean onFailed(Throwable e) {
                                        // 埋点
                                        HashMap<String, String> kv = new HashMap<String, String>();
                                        kv.put(TraceUtils.Event_Kv_Order_Spuid, mSpuid);
                                        kv.put(TraceUtils.Event_Kv_Order_Skuid, skuid);
                                        kv.put(TraceUtils.Event_Kv_Code, "1");
                                        kv.put(TraceUtils.Event_Kv_Order_Count, "1");
                                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Cart, kv, "");

                                        return super.onFailed(e);
                                    }
                                });
                    } else {
                        mPwToast = ToastPopuWindow.makeText(ProductMainActivity.this, "请选择规格", AlertViewType.AlertViewType_Warning).parentView(bottomBar);
                        mPwToast.show();
                    }
                }

                @Override
                public void onClickSpecCompareInfo() {
                    String key = StaticObjectUtil.sizeForKey("" + mProductDetailBean.cateId);
                    String url = "http://plugin.55haitao.com/#/ruler/" + key;
                    H5Activity.toThisActivity(mActivity, url, "尺码对照");
                }

                @Override
                public void onClickCover(String cover) {
                    AvatarPopupWindow avatarPopupWindow = new AvatarPopupWindow(mActivity, cover);
                    bottomBar.postDelayed(() -> avatarPopupWindow.showOrDismiss(bottomBar), 200);
                }
            });
        }
        mSpecPopupWindow.setFromBuyButton(fromBuyingButton);
    }

    public void renderUI() {
        if (mProductDetailBean == null) {
            addCartBtn.setVisibility(View.GONE);
            payBtn.setVisibility(View.GONE);
            noStockBtn.setVisibility(View.VISIBLE);
            return;
        }

        // 购物车
        mImgShoppingCart.setOnClickListener(v -> {
            if (!HaiUtils.needLogin(mActivity)) {
                // 埋点
                //                TraceUtils.addClick(TraceUtils.PageCode_Cart, "", ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Cart + "", "");
                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Cart, TraceUtils.Event_Category_Click, "", null, "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_Cart, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Cart, "");

                ShoppingCartActivity.toThisAcivity(mActivity);
            }
        });

        // 分享
        navShareBtn.setOnClickListener(v -> ShareUtils.showShareBoard(mActivity, mProductDetailBean.share.title, mProductDetailBean.share.desc, mProductDetailBean.share.url, mProductDetailBean.share.icon, false));

        if (mProductDetailBean != null && !mProductDetailBean.isSoldOut()) {
            if (!mProductDetailBean.isUpdate) {
                getProductRT(false);
            }

            addCartBtn.setVisibility(View.VISIBLE);
            payBtn.setVisibility(View.VISIBLE);
            noStockBtn.setVisibility(View.GONE);

            // 加入购物车
            addCartBtn.setOnClickListener(v -> {
                if (HaiUtils.needLogin(mActivity)) return;

                if (mProductDetailBean._isSingleSpecAndValue) {   //  无属性商品直接加入购物车
                    String skuid = TextUtils.isEmpty(mProductDetailBean.skuid) ? mProductDetailBean.defaultSkuid : mProductDetailBean.skuid;
                    if (skuid != null && skuid.length() > 0) {
                        // 埋点上报
                        cartLogs(skuid, 0);

                        showProgressDialog("加入购物车...");

                        CartRepository.getInstance()
                                .addCart(mSpuid, skuid, 1)
                                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                                    @Override
                                    public void onSuccess(CommonDataBean commonDataBean) {
                                        getShoppingCartBadge(true);
                                        mPwToast = ToastPopuWindow.makeText(mActivity, "加入购物车成功", AlertViewType.AlertViewType_OK).parentView(bottomBar);
                                        mPwToast.show();

                                        // 埋点
                                        HashMap<String, String> kv = new HashMap<String, String>();
                                        kv.put(TraceUtils.Event_Kv_Order_Spuid, mSpuid);
                                        kv.put(TraceUtils.Event_Kv_Order_Skuid, skuid);
                                        kv.put(TraceUtils.Event_Kv_Cartid, commonDataBean.cart_id);
                                        kv.put(TraceUtils.Event_Kv_Order_Count, "1");
                                        kv.put(TraceUtils.Event_Kv_Code, "0");
                                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Cart, kv, "");

                                    }

                                    @Override
                                    public void onFinish() {
                                        dismissProgressDialog();
                                    }

                                    @Override
                                    public boolean onFailed(Throwable e) {
                                        // 埋点
                                        HashMap<String, String> kv = new HashMap<String, String>();
                                        kv.put(TraceUtils.Event_Kv_Order_Spuid, mSpuid);
                                        kv.put(TraceUtils.Event_Kv_Order_Skuid, skuid);
                                        kv.put(TraceUtils.Event_Kv_Code, "1");
                                        kv.put(TraceUtils.Event_Kv_Order_Count, "1");
                                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Cart, kv, "");

                                        return super.onFailed(e);
                                    }
                                });

                    }
                } else {
                    createSpecPopuView(false);
                    mSpecPopupWindow.showOrDismiss(bottomBar);
                }
                // GA 事件
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("交易相关")
                        .setAction("加入购物车")
                        .setLabel(mSbGAEvent.toString())
                        .build());
            });

            // 立即购买
            payBtn.setOnClickListener(v -> {
                if (HaiUtils.needLogin(mActivity)) return;

                if (mProductDetailBean._isSingleSpecAndValue) {      // 无属性商品直接下单购买
                    String skuid = TextUtils.isEmpty(mProductDetailBean.skuid) ? mProductDetailBean.defaultSkuid : mProductDetailBean.skuid;

                    if (skuid != null && skuid.length() > 0) {
                        // 埋点上报
                        cartLogs(skuid, 1);

                        // 埋点
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Order_Spuid, mSpuid);
                        kv.put(TraceUtils.Event_Kv_Order_Skuid, skuid);
                        kv.put(TraceUtils.Event_Kv_Order_Count, "1");
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Goods_Buy, kv, "");

                        OrderModel.CartType cartType = new OrderModel.CartType(mSpuid, skuid, "1");
                        OrderCreateActivity.toThisActivity(ProductMainActivity.this, cartType, 0);

                    } else {
                        mPwToast = ToastPopuWindow.makeText(ProductMainActivity.this, "请选择规格", AlertViewType.AlertViewType_Warning).parentView(bottomBar);
                        mPwToast.show();
                    }
                } else {
                    createSpecPopuView(true);
                    mSpecPopupWindow.showOrDismiss(bottomBar);
                }

                // GA 事件
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("交易相关")
                        .setAction("立即购买")
                        .setLabel(mSbGAEvent.toString())
                        .build());
            });
        } else {
            if (!mProductDetailBean.isUpdate) {
                getProductRT(true);
            }
            addCartBtn.setVisibility(View.GONE);
            payBtn.setVisibility(View.GONE);
            noStockBtn.setVisibility(View.VISIBLE);
        }

        //ViewPager 滑动处理
        //        bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
        //
        //            private float currentDownX;
        //            private float currentDownY;
        //
        //            @Override
        //            public boolean onTouch(View view, MotionEvent motionEvent) {
        //
        //                boolean result = false;
        //
        //                int action = motionEvent.getAction();
        //
        //                if (action == MotionEvent.ACTION_DOWN) {
        //                    currentDownX = motionEvent.getX();
        //                    currentDownY = motionEvent.getY();
        //                    view.getParent().requestDisallowInterceptTouchEvent(true);
        //                    result = true;
        //                } else if (action == MotionEvent.ACTION_MOVE) {
        //                    float x = motionEvent.getX();
        //                    float y = motionEvent.getY();
        //
        //                    float k = (x - currentDownX) / (y - currentDownY);
        //
        //                    if (Math.abs(k) > 0.58) {        //临界值设置为 1/√3
        //                        //                        Log.d("MainActivity", "判断为--->左右滑动");
        //                    } else {
        //                        //                        Log.d("MainActivity", "判断为--->上下滑动");
        //                        view.getParent().requestDisallowInterceptTouchEvent(false);
        //                    }
        //
        //                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
        //                    view.getParent().requestDisallowInterceptTouchEvent(false);
        //                }
        //
        //                return result;
        //            }
        //        });

        // 商品Banner
        List<TabBannerBean> tabDatas = mProductDetailBean.generateTabBannerBean();
        mBannerAdapter = new BannerAdapter(mActivity, tabDatas.size() > 6 ? tabDatas.subList(0, 6) : tabDatas);
        bannerViewPager.setAdapter(mBannerAdapter);
        bannerIndicator.setViewPager(bannerViewPager);
        mBannerAdapter.registerDataSetObserver(bannerIndicator.getDataSetObserver());
        // 到最后一张继续向左滑动快速查看图文详情效果
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currPosition = 0; // 当前滑动到了哪一页
            boolean canJump = false;
            boolean canLeft = true;
            boolean isObjAnmatitor = true;
            boolean isObjAnmatitor2 = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == (mBannerAdapter.getDataCount() - 1)) {
                    if (positionOffset > 0.2) {
                        canJump = true;
                        if (mBannerAdapter.arrowImage != null && mBannerAdapter.slideText != null) {
                            if (isObjAnmatitor) {
                                isObjAnmatitor = false;
                                ObjectAnimator animator = ObjectAnimator.ofFloat(mBannerAdapter.arrowImage, "rotation", 0f, 180f);
                                animator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mBannerAdapter.slideText.setText("释放查看图文详情");
                                        isObjAnmatitor2 = true;
                                    }
                                });
                                animator.setDuration(100).start();
                            }
                        }
                    } else if (positionOffset <= 0.2 && positionOffset > 0) {
                        canJump = false;
                        if (mBannerAdapter.arrowImage != null && mBannerAdapter.slideText != null) {
                            if (isObjAnmatitor2) {
                                isObjAnmatitor2 = false;
                                ObjectAnimator animator = ObjectAnimator.ofFloat(mBannerAdapter.arrowImage, "rotation", 180f, 360f);
                                animator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mBannerAdapter.slideText.setText("滑动查看图文详情");
                                        isObjAnmatitor = true;
                                    }
                                });
                                animator.setDuration(100).start();
                            }
                        }
                    }
                    canLeft = false;
                } else {
                    canLeft = true;
                    //                    if (positionOffset > 0.1 && canSet) {
                    //                        canSet = false;
                    //                        bannerViewPager.setCurrentItem(position == currPosition ? position + 1 : position);
                    ////                        if (currPosition == position) {   // 往左滑 从第i页滑向第i+1页，目的页面是i+1。参数变化：position开始一直是i
                    ////                            new Handler().post(() -> { // 在handler里调用setCurrentItem才有效
                    ////                                bannerViewPager.setCurrentItem(position + 1);
                    ////                            });
                    ////                        } else {//  从第i页滑向第i-1页，目的页面是i-1。参数变化：position一直为i-1
                    ////                            new Handler().post(() -> { // 在handler里调用setCurrentItem才有效
                    ////                                bannerViewPager.setCurrentItem(position + 1);
                    ////                            });
                    ////                        }
                    //                    }
                }

            }

            @Override
            public void onPageSelected(int position) {
                currPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (currPosition == (mBannerAdapter.getDataCount() - 1) && !canLeft) {
                    if (state == ViewPager.SCROLL_STATE_SETTLING) {
                        if (canJump) {
                            scrollView.scrollTo(0, (int) tabRadioGroup.getY());
                        }
                        new Handler().post(() -> { // 在handler里调用setCurrentItem才有效
                            bannerViewPager.setCurrentItem(mBannerAdapter.getDataCount() - 1);
                        });
                    }
                }

            }
        });

        // 心愿单(点赞商品)
        likeProductBtn.setChecked(mProductDetailBean.is_star);
        likeProductBtn.setOnClickListener(v -> {

            CheckBox buttonView = (CheckBox) v;

            if (HaiUtils.needLogin(mActivity)) {
                buttonView.setChecked(false);
                return;
            }

            likeProductBtn.setEnabled(false);

            // GA 事件
            StringBuilder sbGAEvent = new StringBuilder();
            sbGAEvent.append(mSpuid)
                    .append(",").append(mProductDetailBean.getNameFormatWithBrandName())
                    .append(",").append(mProductDetailBean.brandInfo.getBrandWholeName())
                    .append(",").append(mProductDetailBean.sellerInfo.nameen);

            Tracker tracker = ((HaiApplication) mActivity.getApplication()).getDefaultTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("电商运营")
                    .setAction("加入心愿单")
                    .setLabel(sbGAEvent.toString())
                    .build());

            // 点赞商品请求
            ProductRepository.getInstance()
                    .likeProduct(mSpuid)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<LikeProductBean>() {
                        @Override
                        public void onSuccess(LikeProductBean likeProductBean) {
                            if (buttonView.isChecked() == true) {
                                mPwToast = ToastPopuWindow.makeText(mActivity, "已加入心愿单", AlterViewType_Pray).parentView(buttonView);
                                mPwToast.show();
                                EventBus.getDefault().post(new EasyoptListChangeEvent(true));

                            } else {
                                EventBus.getDefault().post(new EasyoptListChangeEvent(false));
                            }

                            EventBus.getDefault().post(new ProductLikeEvent(buttonView.isChecked(), mSpuid));
                        }

                        @Override
                        public void onFinish() {
                            likeProductBtn.setEnabled(true);
                        }
                    });

        });

        // 促销限时  促销语
        long systemtime = HaiApplication.systemTime / 1000;
        if (mProductDetailBean.promotionCode != null && mProductDetailBean.promotionCode.end > systemtime) {
            llytLimitTime.setVisibility(View.VISIBLE);
            float[] time = HaiTimeUtils.counterTimeToDay(String.valueOf(systemtime), String.valueOf(mProductDetailBean.promotionCode.end));
            tvTimer.setTime((int) time[3], (int) time[2], (int) time[1], (int) time[0]);
            tvTimer.start();
            if (TextUtils.isEmpty(mProductDetailBean.promotionCode.content)) {
                tvSalesPromotion.setVisibility(View.GONE);
            } else {
                tvSalesPromotion.setText(mProductDetailBean.promotionCode.content);
                tvSalesPromotion.setVisibility(View.VISIBLE);
            }
        } else {
            llytLimitTime.setVisibility(View.GONE);
            tvSalesPromotion.setVisibility(View.GONE);
        }

        // Title
        productNameTxt.setText(mProductDetailBean.getNameFormatWithBrandName());

        // Price
        // 人民币现价
        currentPriceTxt.setText(PriceUtils.toRMBFromFen(mProductDetailBean.realPrice));
        // 人民币原价／折扣标签
        if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(mProductDetailBean.realPrice, mProductDetailBean.mallPrice)) {
            mallPriceTxt.setVisibility(View.GONE);
        } else {
            mallPriceTxt.setVisibility(View.VISIBLE);
            mallPriceTxt.setText(PriceUtils.toRMBFromFen(mProductDetailBean.mallPrice));
            mallPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(PriceUtils.FloatKeepOneFloor(mProductDetailBean.realPrice * 10 / mProductDetailBean.mallPrice) + "折");
        }

        // 官网标注的外币现价
        officalCurrentPriceTxt.setText(PriceUtils.toAnyCurrencyFromFen(mProductDetailBean.unit, mProductDetailBean.realPriceOrg, true));
        // 官网标注的外币原价
        if (PriceUtils.currentPriceGreaterOrEqualThanMallPrice(mProductDetailBean.realPriceOrg, mProductDetailBean.mallPriceOrg)) {
            officalMallPriceTxt.setVisibility(View.GONE);
        } else {
            officalCurrentPriceTxt.setVisibility(View.VISIBLE);
            officalMallPriceTxt.setText(PriceUtils.toAnyCurrencyFromFen(mProductDetailBean.unit, mProductDetailBean.mallPriceOrg, true));
            officalMallPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // 国内运费
        domestictransformfeeTxt.setText(mProductDetailBean.displayDomesticShippingFee());
        // 消费税
        if (mProductDetailBean.fees.consume == 0) {
            consumptionfeeLin.setVisibility(View.GONE);
        } else {
            consumptionfeeLin.setVisibility(View.VISIBLE);
            consumptionfeeTxt.setText(mProductDetailBean.displayConsumptionFee());
        }

        if (mProductDetailBean.skuInfo.style == null) {
            thumbnailScrollView.setVisibility(View.GONE);
        } else {
            thumbnailScrollView.setVisibility(View.VISIBLE);

            thumbnailLin.removeAllViews();
            if (mProductDetailBean.skuInfo.style.skustylelist != null) {

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(thumbnailLin.getLayoutParams());
                if (mProductDetailBean.skuInfo.style.skustylelist.size() * DisplayUtils.dp2px(mActivity, 50) < DisplayUtils.getScreenWidth(mActivity)) {
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                } else {
                    layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                }
                thumbnailLin.setLayoutParams(layoutParams);
            }

            for (ProductDetailBean.StyleListData styleListData : mProductDetailBean.skuInfo.style.skustylelist) {
                thumbnailLin.setVisibility(View.VISIBLE);
                CheckableImageButton thumbButton = (CheckableImageButton) LayoutInflater.from(mActivity).inflate(R.layout.button_thumbnail, thumbnailLin, false);
                thumbnailLin.addView(thumbButton);
                UPaiYunLoadManager.loadImage(mActivity, styleListData.imgCover, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, thumbButton);
                thumbButton.setOnClickListener(mListener);
            }

            // 默认设置第一个选中
            CheckableImageButton firstThumbButton = (CheckableImageButton) thumbnailLin.getChildAt(0);
            firstThumbButton.setChecked(true);

        }

        // 编辑说
        if (mProductDetailBean.editor != null && !TextUtils.isEmpty(mProductDetailBean.editor.text)) {
            tvEditerRecommend.setVisibility(View.VISIBLE);
            tvEditerRecommend.setText(mProductDetailBean.editor.text);
        } else {
            tvEditerRecommend.setVisibility(View.GONE);
        }

        // 商家信息
        if (mProductDetailBean.sellerInfo != null) {
            if (HaiUtils.getSize(mProductDetailBean.sellerInfo.stag) > 0) {
                rycvAssurance.setVisibility(View.VISIBLE);
                mAssuranceAdapter.changeDatas(mProductDetailBean.sellerInfo.stag);
            } else {
                rycvAssurance.setVisibility(View.GONE);
            }

            setFlag(mProductDetailBean, sellerImage);
            sellerNameTxt.setText(mProductDetailBean.sellerInfo.nameen + "官网发货");

            if (!TextUtils.isEmpty(mProductDetailBean.sellerInfo.desc)) {
                sellerDescTxt.setText(mProductDetailBean.sellerInfo.desc);
            } else {
                sellerDescTxt.setVisibility(View.GONE);
            }
        } else {
            sellerLin.setVisibility(View.GONE);
            findViewById(R.id.viewline).setVisibility(View.GONE);
        }

        // 小编推荐语和商家配置信息都不存在
        if (tvEditerRecommend.getVisibility() == View.GONE && (rycvAssurance.getVisibility() == View.GONE || sellerLin.getVisibility() == View.GONE)) {
            findViewById(R.id.view_bottom).setVisibility(View.GONE);
        }

        sellerLin.setOnClickListener(v -> {
            String                      title      = null;
            SellerBean.SellerDetailBean sellerInfo = mProductDetailBean.sellerInfo;
            if (!TextUtils.isEmpty(sellerInfo.nameen)) {
                title = sellerInfo.nameen;
            } else if (!TextUtils.isEmpty(sellerInfo.namecn)) {
                title = sellerInfo.namecn;
            } else {
                title = sellerInfo.name;
            }

            // 埋点
            //            TraceUtils.addClick(TraceUtils.PageCode_BrandOrSiteDetail, title, ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Store + "", "");
            YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Store, TraceUtils.Event_Category_Click, "", null, "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_BrandOrSiteDetail, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_Store, title);

            SiteActivity.toThisActivity(mActivity, title, PageType.SELLER, sellerInfo.logo1, sellerInfo.logo3, sellerInfo.img_cover);
        });

        // 购物须知
        mustKnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 埋点
                //                TraceUtils.addClick(TraceUtils.PageCode_H5, ServerUrl.HELP_INTRODUCE_URL, ProductMainActivity.this, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_MustKnow + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_H5, TraceUtils.PageCode_ProductDetail, TraceUtils.PositionCode_MustKnow, "");
                H5Activity.toThisActivity(mActivity, ServerUrl.HELP_INTRODUCE_URL, "购物须知");
            }
        });

        // 海淘流程
        ivHidenShow.setOnClickListener(v -> {
            if (llytProcessContent.getVisibility() == View.VISIBLE) {
                llytProcessContent.setVisibility(View.GONE);
                ivHidenShow.setImageResource(R.mipmap.ic_expand);
            } else {
                llytProcessContent.setVisibility(View.VISIBLE);
                ivHidenShow.setImageResource(R.mipmap.ic_hiden);
            }
        });

        //第二页
        tabRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.tuwenTab) {
                secondFragmentViewPager.setCurrentItem(0);
                ((RadioButton) tabRadioGroupHide.getChildAt(0)).setChecked(true);
            } else if (checkedId == R.id.queryTab) {
                secondFragmentViewPager.setCurrentItem(1);
                ((RadioButton) tabRadioGroupHide.getChildAt(1)).setChecked(true);
            }
        });
        tabRadioGroupHide.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.tuwenTab_hide) {
                ((RadioButton) tabRadioGroup.getChildAt(0)).setChecked(true);
            } else if (checkedId == R.id.queryTab_hide) {
                ((RadioButton) tabRadioGroup.getChildAt(1)).setChecked(true);
            }
        });

        mFragmentList = new ArrayList<>();
        mProductTxtImageFragment = ProductTxtImageFragment.newInstance(mProductDetailBean.content.getProductDesc(), mProductDetailBean.pageId, mProductDetailBean.coverImgList);
        mProductQueryFragment = ProductQueryFragment.newInstance(new Gson().toJson(mProductDetailBean.faq));
        mFragmentList.add(mProductTxtImageFragment);
        mFragmentList.add(mProductQueryFragment);

        secondFragmentViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, null));

        secondFragmentViewPager.setCurrentItem(0);

        secondFragmentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) tabRadioGroup.getChildAt(position)).setChecked(true);
                ((RadioButton) tabRadioGroupHide.getChildAt(position)).setChecked(true);
                View                      view         = secondFragmentViewPager.getChildAt(position);
                int                       height       = view.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) secondFragmentViewPager.getLayoutParams();
                layoutParams.height = height;
                secondFragmentViewPager.setLayoutParams(layoutParams);
                if (tabRadioGroupHide.getVisibility() == View.VISIBLE) {
                    scrollView.scrollTo(0, (int) tabRadioGroup.getY());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSview.showContent();

    }

    /**
     * 加入购物车埋点上报
     *
     * @param skuid
     * @param buyType
     */
    private void cartLogs(String skuid, int buyType) {
        CartLogsBean cartLogsBean = new CartLogsBean();
        cartLogsBean.buyType = buyType;
        cartLogsBean.skuid = skuid;
        cartLogsBean.timestamp = System.currentTimeMillis();
        String data = new Gson().toJson(cartLogsBean);
        mRepository.cartLogs(data)
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private void setFlag(ProductDetailBean product, ImageView imageView) {
        if (product.sellerInfo != null && product.sellerInfo.country != null) {
            int flagResourceId = HaiUtils.getFlagResourceId(product.sellerInfo.country, true);
            if (flagResourceId == -1) {
                UPaiYunLoadManager.loadImage(mActivity, mProductDetailBean.sellerInfo.flag, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, sellerImage, true);
            } else {
                Glide.with(this)
                        .load(flagResourceId)
                        .transform(new GlideCircleTransform(mActivity))
                        .diskCacheStrategy(sDiskCacheStrategy)
                        .dontAnimate().into(imageView);
            }
        }
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int selectedIndex = 0;
            for (int i = 0; i < thumbnailLin.getChildCount(); i++) {
                CheckableImageButton thumbButton = (CheckableImageButton) thumbnailLin.getChildAt(i);
                if (thumbButton.equals(v)) {
                    selectedIndex = i;
                    if (thumbButton.isChecked() == true) return;
                } else {
                    thumbButton.setChecked(false);
                }
            }
            CheckableImageButton checkImageButton = (CheckableImageButton) v;
            ((CheckableImageButton) v).setChecked(true);

            // fresh banner
            ArrayList<TabBannerBean> tabDatas = mProductDetailBean.skuInfo.style.generateTabBannerBean(selectedIndex);
            mBannerAdapter.setmBanners(tabDatas);
            bannerViewPager.setCurrentItem(0);

            Toast.makeText(mActivity, mProductDetailBean.skuInfo.style.getStyleValue(selectedIndex), Toast.LENGTH_SHORT).show();

        }
    };

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        if (event.isLogin) {
            // 刷新优惠券
            ProductRepository.getInstance()
                    .getGrantCoupons(mSpuid)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<GrantCouponsBean>() {
                        @Override
                        public void onSuccess(GrantCouponsBean grantCouponsBean) {
                            if (HaiUtils.getSize(grantCouponsBean.couponq_list) > 0) {
                                for (GrantCouponsBean.GrantCoupon grantCoupon : grantCouponsBean.couponq_list) {
                                    grantCoupon.is_receive = grantCoupon.canReceive() ? 0 : 1;
                                }
                                if (mCouponDlg == null) {
                                    creatCouponDialog(grantCouponsBean.couponq_list);
                                } else {
                                    mCouponAdapter.changeDatas(grantCouponsBean.couponq_list);
                                }
                            }

                        }

                        @Override
                        public void onFinish() {

                        }
                    });

            // 点赞状态
            ProductRepository.getInstance()
                    .getProductDetail(mSpuid)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<ProductDetailBean>() {
                        @Override
                        public void onSuccess(ProductDetailBean productDetailBean) {
                            likeProductBtn.setChecked(productDetailBean.is_star);

                        }

                        @Override
                        public void onFinish() {

                        }
                    });

            getShoppingCartBadge(false);

        }
    }

    /**
     * 接收草单改变事件
     */
    @Subscribe
    public void onEasyoptListChangeEvent(EasyoptListChangeEvent event) {
        requestMyEasyoptList();
    }

    /**
     * 订单生成页面返回后刷新商详数据
     *
     * @param event
     */
    @Subscribe
    public void onOrderCreateBackEvent(OrderCreateBackEvent event) {
        Logger.d("订单生成页面返回后刷新商详数据");
        loadData();
    }

    @Subscribe
    public void changeLikeState(ProductLikeEvent event) {
        if (rycvRecommend.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < productRecomAdapter.getmList().size(); i++) {
                if (productRecomAdapter.getmList().get(i).spuid.equals(event.spuid)) {
                    if (productRecomAdapter.getmList().get(i).is_star != event.liklikeState) {
                        productRecomAdapter.getmList().get(i).is_star = event.liklikeState;
                        productRecomAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    @Subscribe
    public void onProductRTStockChanged(ProductRTStockChangeEvent event) {
        if (event.hasStock == false) {
            addCartBtn.setVisibility(View.GONE);
            payBtn.setVisibility(View.GONE);
            noStockBtn.setVisibility(View.VISIBLE);
        } else {
            renderUI();
        }
    }

    @Subscribe
    public void onShoppingCartCntChanged(ShoppingCartCntChangeEvent event) {
        setShoppingCartCount(event.shoppingCartBadgeCnt);
    }

    @Subscribe
    public void onAPIErrorEvent(APIErrorEvent event) {
        if (event.code == 14000) {
            if (hasProduct) {      //  核价后报14000
                addCartBtn.setVisibility(View.GONE);
                payBtn.setVisibility(View.GONE);
                noStockBtn.setVisibility(View.VISIBLE);
                noStockBtn.setText("商品已下架");

                mPwToast = ToastPopuWindow.makeText(mActivity, "该商品已抢完，下次快一点哦", AlertViewType.AlterViewType_Add_Cart).parentView(bottomBar);
                mPwToast.show();

            } else {              //   进来就报14000
                getProductRecom();
            }
        }
    }

    //  进来就报14000情况下，单独获取大家都在买
    private void getProductRecom() {
        //        if (TextUtils.isEmpty(title)) {
        //            setRecommedView(null);
        //        } else {
        ProductRepository.getInstance()
                .productRecom(TextUtils.isEmpty(title) ? "" : title)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<ProductRecommendBean>() {
                    @Override
                    public void onSuccess(ProductRecommendBean productRecommendBean) {
                        setRecommedView(productRecommendBean.products);
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showProductFailView(mSview, e, hasProduct);
                        return super.onFailed(e);
                    }
                });
        //        }
    }

    //   商品下架 大家都在买的状态界面
    private void setRecommedView(ArrayList<ProductBaseBean> products) {
        llytProduct.setVisibility(View.GONE);
        nullContent.setVisibility(View.VISIBLE);
        bottomBar.setVisibility(View.GONE);
        llytContent.setPadding(0, 0, 0, 0);
        mSview.showContent();
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(ProductMainActivity.this, 2);
        fullyGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == 1 || position == (HaiUtils.getSize(products) + 2)) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rycvRecommend.setLayoutManager(fullyGridLayoutManager);
        productRecomAdapter = new ProductRecomAdapter(ProductMainActivity.this, products, mPwToast, rycvRecommend, ProductNullType.PRODUCT_MAIN);
        rycvRecommend.setAdapter(productRecomAdapter);

    }

    // 初始化规格变量
    ArrayList<ProductDetailBean.AssistSpecItem> specs                = new ArrayList<>();
    ArrayList<String>                           defalutSelectedStyle = new ArrayList<>();
    ArrayList<String>                           defaultValues        = new ArrayList<>();
    private static final int SkuType_NoAttribute      = 0;  // 商品无属性
    private static final int SkuType_AttributeWOStyle = 1;  // 商品有属性（没有Style）
    private static final int SkuType_AttributeWStyle  = 2;  // 商品有属性（有Style）
    private int skutype;
    public HashMap<String, ArrayList<String>> cache_allSpecs      = new HashMap<>();
    public HashMap<String, String>            cache_selectedSpecs = new HashMap<>();

    public void initializeProductData() {
        // 清空数据
        specs.clear();
        defalutSelectedStyle.clear();
        defaultValues.clear();
        cache_allSpecs.clear();
        cache_selectedSpecs.clear();
        // 验证数据一致性
        checkConsistency(true);
        // 处理规格数据
        if (mProductDetailBean.skuInfo.style == null) {
            if (mProductDetailBean.skuInfo.select != null && mProductDetailBean.skuInfo.select.size() > 0) {
                skutype = SkuType_AttributeWOStyle;                // 商品有属性无style
            } else {
                mProductDetailBean._isSingleSpecAndValue = true;
                return;
            }
        } else {
            if (mProductDetailBean.skuInfo.select == null || mProductDetailBean.skuInfo.select.size() == 0) {
                mProductDetailBean._isSingleSpecAndValue = true;
                return;
            } else {
                skutype = SkuType_AttributeWStyle;// 商品有属性带style
            }
        }
        initializeSpeces();
        if (specs == null || specs.size() == 0) {
            return;
        }
        setSpecs(specs, defalutSelectedStyle, defaultValues);
        setAllSpecs();


        mProductDetailBean.specs = specs;
        mProductDetailBean.cache_allSpecs = cache_allSpecs;
        mProductDetailBean.cache_selectedSpecs = cache_selectedSpecs;
    }

    private void setAllSpecs() {
        // 设置所有的specs
        for (String spec : cache_allSpecs.keySet()) {
            ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < specs.size(); i++) {
                for (int j = 0; j < specs.get(i).specs.size(); j++) {
                    if (spec.equals(specs.get(i).specs.get(j))) {
                        values.add(specs.get(i).values.get(j));
                    }
                }
            }
            //去除重复
            if (HaiUtils.getSize(values) > 1) {
                for (int i = 0; i < values.size() - 1; i++) {
                    for (int j = values.size() - 1; j > i; j--) {
                        if (values.get(j).equals(values.get(i))) {
                            values.remove(j);
                        }
                    }
                }
            }
            cache_allSpecs.put(spec, values);
        }
    }

    private void initializeSpeces() {
        boolean hasDefaultSelece = false;
        for (ProductDetailBean.SelectData selectData : mProductDetailBean.skuInfo.select) {
            ProductDetailBean.AssistSpecItem item = new ProductDetailBean.AssistSpecItem();
            item.specs = new ArrayList<>();
            item.values = new ArrayList<>();
            if (skutype == SkuType_AttributeWStyle) {
                item.specs.add(mProductDetailBean.skuInfo.style.name);
                item.values.add(selectData.style);
            }
            for (ProductDetailBean.SkuSelectListData skuSelectListData : selectData.skuselectlist) {
                if (skutype == SkuType_AttributeWStyle) {
                    // 有些商品会出现skuselectlist的select与style的名字一样
                    if (mProductDetailBean.skuInfo.style.name.equals(skuSelectListData.select)) {
                        skuSelectListData.select = "_" + skuSelectListData.select;
                    }
                }
                item.specs.add(skuSelectListData.select);
                item.values.add(skuSelectListData.value);
            }
            item.stock = selectData.inStock;
            item.skuid = selectData.skuid;
            specs.add(item);

            if (mProductDetailBean.defaultSkuid.equals(selectData.skuid) && !selectData.isSoldOut()) {
                // 默认选中项
                defalutSelectedStyle.addAll(item.specs);
                defaultValues.addAll(item.values);
                hasDefaultSelece = true;
            }

            // 设置cache_allSpecs的key
            if (selectData.equals(mProductDetailBean.skuInfo.select.get(0))) {
                for (int i = item.specs.size() - 1; i >= 0; i--) {    // 反向取 因为第一个put进去的在最底部
                    cache_allSpecs.put(item.specs.get(i), new ArrayList<>());
                }
            }

        }
        // 默认skuid已售完  选第一个没售完的为默认选择
        if (!hasDefaultSelece) {
            for (ProductDetailBean.SelectData selectData : mProductDetailBean.skuInfo.select) {
                if (!selectData.isSoldOut()) {
                    ArrayList<String> specs  = new ArrayList<>();
                    ArrayList<String> values = new ArrayList<>();
                    if (skutype == SkuType_AttributeWStyle) {
                        specs.add(mProductDetailBean.skuInfo.style.name);
                        values.add(selectData.style);
                    }
                    for (ProductDetailBean.SkuSelectListData skuSelectListData : selectData.skuselectlist) {
                        specs.add(skuSelectListData.select);
                        values.add(skuSelectListData.value);
                    }
                    defalutSelectedStyle.addAll(specs);
                    defaultValues.addAll(values);
                    return;

                }
            }
        }
    }

    // 判断商品规格是否一致并修复
    private boolean checkConsistency(boolean fix) {
        boolean isConsistency = true;
        int     max           = 0;
        if (mProductDetailBean.skuInfo != null && mProductDetailBean.skuInfo.select != null && mProductDetailBean.skuInfo.select.size() > 0) {
            max = mProductDetailBean.skuInfo.select.get(0).skuselectlist.size();
            for (ProductDetailBean.SelectData selectData : mProductDetailBean.skuInfo.select) {
                if (HaiUtils.getSize(selectData.skuselectlist) > max) {
                    max = HaiUtils.getSize(selectData.skuselectlist);
                    isConsistency = false;
                } else if (HaiUtils.getSize(selectData.skuselectlist) < max) {
                    isConsistency = false;
                }
            }
            //  如果不一致 修复  移除坏数据
            if (!isConsistency && fix) {
                for (int backIndex = HaiUtils.getSize(mProductDetailBean.skuInfo.select) - 1; backIndex >= 0; backIndex--) {
                    ProductDetailBean.SelectData selectData = mProductDetailBean.skuInfo.select.get(backIndex);
                    if (HaiUtils.getSize(selectData.skuselectlist) != max) {
                        mProductDetailBean.skuInfo.select.remove(backIndex);
                    }
                }
            }

        }
        return isConsistency;

    }

    /**
     * 初始化产品 - 转义
     * eg: 传入参数specs: [{ "specs":["Color", "Size"], "values":["Black","5"], YES, "11111111"}, { "specs":["Color", "Size"], "values":["Black","6"], YES, "22222222"}]
     */
    private void setSpecs(ArrayList<ProductDetailBean.AssistSpecItem> specs, ArrayList<String> styleNames, ArrayList<String> values) {

        ProductDetailBean.AssistSpecItem item = specs.get(0);
        // 清空本地的spec
        cache_selectedSpecs.clear();
        for (String spec : item.specs) {
            // 空字符串代表未选中spec
            cache_selectedSpecs.put(spec, "");
        }
        // 将默认的选中规格设置为已选
        if (styleNames != null && values != null && styleNames.size() > 0 && values.size() > 0) {
            for (int i = 0; i < styleNames.size(); i++) {
                cache_selectedSpecs.put(styleNames.get(i), values.get(i));
            }
        }

    }

    /**
     * 跳转到本页面
     */
    public static void toThisAcivity(Context context, String spuid) {
        if (TextUtils.isEmpty(spuid)) {
            return;
        }
        Intent intent = new Intent(context, ProductMainActivity.class);
        intent.putExtra("spuid", spuid);
        context.startActivity(intent);
    }

    /**
     * 跳转到本页面带商品名
     */
    public static void toThisAcivity(Context context, String spuid, String name) {
        if (TextUtils.isEmpty(spuid)) {
            return;
        }
        Intent intent = new Intent(context, ProductMainActivity.class);
        intent.putExtra("spuid", spuid);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    /**
     * 跳转到本页面  推送
     */
    public static void toThisAcivity(Context context, String spuid, boolean newTask) {
        if (TextUtils.isEmpty(spuid)) {
            return;
        }
        Intent intent = new Intent(context, ProductMainActivity.class);
        intent.putExtra("spuid", spuid);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到本页面 Ga统计
     */
    public static void toThisAcivity(Context context, String spuid, String sourceName, String name) {
        if (TextUtils.isEmpty(spuid)) {
            return;
        }
        Intent intent = new Intent(context, ProductMainActivity.class);
        intent.putExtra("spuid", spuid);
        intent.putExtra("source_name", sourceName);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    /**
     * 客服
     */
    @OnClick(R.id.tv_service)
    public void clickService() {
        // GA 事件
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("联系客服")
                .setAction("在线客服")
                .setLabel("商品详情")
                .build());
        NtalkerUtils.contactNtalkeWithProduct(mActivity, mSpuid);
    }

    /**
     * 设置购物车角标数
     *
     * @param count 商品个数
     */
    public void setShoppingCartCount(int count) {
        if (mBvShoppingCart == null || count < 0)
            return;
        mBvShoppingCart.setBadgeCount(count);
    }

    /**
     * 草单
     */
    @OnClick(R.id.tv_easyopt)
    public void clickEasyopt() {
        if (HaiUtils.needLogin(mActivity))
            return;

        if (mEasyoptDlg == null) {
            mEasyoptDlg = new Dialog(this);
            mEasyoptDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mEasyoptDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            int dlgWidth  = DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 80);
            int dlgHeight = DisplayUtils.dp2px(mActivity, 400);
            mEasyoptDlg.setContentView(mDlgView, new LinearLayout.LayoutParams(dlgWidth, dlgHeight));
            // 请求草单列表数据
            mEasyoptDlg.show();
            requestMyEasyoptList();
        } else {
            mEasyoptDlg.show();
        }
    }

    /**
     * 添加商品到我的草单 - 网络请求
     */
    private void requestAddProductToMyEasyoptList(int position) {
        showProgressDialog();
        // GA 事件
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("电商运营")
                .setAction("加入草单")
                .setLabel(mSbGAEvent.toString())
                .build());

        int    easyoptId   = mEasyoptList.get(position).easyopt_id;
        String easyoptLogo = mEasyoptList.get(position).img_cover;
        String easyoptName = mEasyoptList.get(position).name;

        SnsRepository.getInstance()
                .easyoptAdd(mSpuid, easyoptId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object obj) {
                        mEasyoptList.get(position).contain = 1;
                        mEasyoptList.get(position).product_count++;
                        mAdapter.notifyDataSetChanged();
                        mEasyoptDlg.dismiss();
                        mPwEasyopt = ToastPwAddToEasyopt.makeText(mActivity, easyoptId, UpyUrlManager.getUrl(easyoptLogo, DisplayUtils.dp2px(mActivity, 30)), easyoptName).parentView(bottomBar);
                        mPwEasyopt.show();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 获取我的草单列表 - 网络请求
     */
    public void requestMyEasyoptList() {
        SnsRepository.getInstance()
                .easyoptList4Add(mSpuid)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<EasyoptList4AddResult>() {
                    @Override
                    public void onSuccess(EasyoptList4AddResult result) {
                        mEasyoptList.clear();
                        mAdapter.addData(result.easyopt);
                        mTvWishlistCount.setText(String.format("商品 %d", result.starproduct.count));
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
        if (mPwEasyopt != null && mPwEasyopt.isShowing())
            mPwEasyopt.dismiss();

        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShareUtils.dismissShareBoard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productRTImage.clearAnimation();
        UMShareAPI.get(this).release();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
        if (mPwEasyopt != null && mPwEasyopt.isShowing())
            mPwEasyopt.dismiss();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "spuid=" + mSpuid + (TextUtils.isEmpty(title) ? "" : "<A>" + "name=" + title);
    }

    //  以下是小能客服回掉接口
    @Override
    public void onChatMsg(boolean b, String s, String s1, String s2, long l, boolean b1) {

    }

    @Override
    public void onUnReadMsg(String s, String s1, String s2, int i) {

    }

    @Override
    public void onClickMatchedStr(String s, String s1) {

    }

    @Override
    public void onClickUrlorEmailorNumber(int i, String s) {

    }

    @Override
    public void onClickShowGoods(int i, int i1, String s, String s1, String s2, String s3, String s4, String s5) {

    }

    @Override
    public void onError(int i) {

    }

    // 图文详情和常见问题
    //    class ProductSecondPagerAdapter extends FragmentPagerAdapter {
    //        private ArrayList<BaseFragment> mFragments;
    //
    //        public ProductSecondPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> mFragments) {
    //            super(fm);
    //            this.mFragments = mFragments;
    //        }
    //
    //        @Override
    //        public Fragment getItem(int position) {
    //            return mFragments.get(position);
    //        }
    //
    //        @Override
    //        public int getCount() {
    //            return mFragments == null ? 0 : mFragments.size();
    //        }
    //
    //    }

    // 预估国际运费
    //        shippingfeeTxt.setText(mProductDetailBean.displayInternatialShippingFee());
    //        productShippingFeeBtn.setOnClickListener(v -> {
    //            ShippingFeePopuWindow shippingFeePopuWindow = new ShippingFeePopuWindow(mActivity);
    //            shippingFeePopuWindow.showOrDismiss(productShippingFeeBtn);
    //        });
    //                // 关税
    //                int tariffValue = (int) mProductDetailBean.fees.tariff;
    //                tariffTxt.setText(mProductDetailBean.displayTariffFee());
    //                if (tariffValue < 0) {
    //                    productRuleBtn.setVisibility(View.GONE);
    //                } else {
    //                    productRuleBtn.setVisibility(View.VISIBLE);
    //                }
    // Rule
    //        productRuleBtn.setOnClickListener(v -> {
    //
    //            String tariffRate   = null;
    //            String subsidy      = null;
    //            int    tariffValue1 = (int) mProductDetailBean.fees.tariff;
    //            if (mProductDetailBean.tariffNotice != null) {
    //                subsidy = mProductDetailBean.tariffNotice.notice;
    //                tariffRate = mProductDetailBean.tariffNotice.desc;
    //            }
    //
    //            TariffPopuWindow tariffPopuWindow = new TariffPopuWindow(mActivity, tariffRate, subsidy, tariffValue1);
    //            tariffPopuWindow.setTariffPopuWindowInterface(() -> H5Activity.toThisActivity(mActivity, mProductDetailBean.tariffNotice.url, "税率表"));
    //            tariffPopuWindow.showOrDismiss(productRuleBtn);
    //        });

    // Style Selection

}
