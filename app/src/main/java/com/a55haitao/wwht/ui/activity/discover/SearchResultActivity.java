package com.a55haitao.wwht.ui.activity.discover;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.SelectedLabelsAdapter;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.adapter.product.ProductRecomAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ProductLikeEvent;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;
import com.a55haitao.wwht.data.interfaces.ILoad;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.ProductNullType;
import com.a55haitao.wwht.data.model.annotation.Sort;
import com.a55haitao.wwht.data.model.annotation.UseSynType;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.repository.SearchResultReposity;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.ShoppingCartActivity;
import com.a55haitao.wwht.ui.view.FullyGridLayoutManager;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.TraceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 展示双列商品列表(分类／搜索结果)
 */

public class SearchResultActivity extends BaseNoFragmentActivity implements ILoad {

    @BindView(R.id.siteViewStub)           ViewStub           mSiteViewStub;
    @BindView(R.id.searchViewStub)         ViewStub           mSearchViewStub;
    @BindView(R.id.tv_result_tips)         HaiTextView        tvResultTips;
    @BindView(R.id.msView)                 MultipleStatusView mMultipleStatusView;
    @BindView(R.id.rycv_product)           RecyclerView       mContentGridView;
    @BindView(R.id.priceCheckTV)           CheckedTextView    mPriceCheckTV;
    @BindView(R.id.priceImgView)           ImageView          mPriceImgView;
    @BindView(R.id.hotRadioBtn)            RadioButton        mHotRadioBtn;         //热门
    @BindView(R.id.saleRadioBtn)           RadioButton        mSaleRadioBtn;        //折扣
    @BindView(R.id.priceLayout)            LinearLayout       mPriceLayout;         //价格
    @BindView(R.id.filterLayout)           LinearLayout       mFilterLayout;        //筛选
    @BindView(R.id.recommendGoodsListView) RecyclerView       mRecommendGridView;
    @BindView(R.id.filterCondictionLayout) LinearLayout       mFilterTagLayout;     //标签
    @BindView(R.id.rycv_selected_labels)   RecyclerView       rycvSelectedLabels;

    private View            titleLayout;
    private ToastPopuWindow mPwToast;

    private SelectedLabelsAdapter mSelectedLabelsAdapter;
    private ArrayList<SearchResultBean.LabelsBean> selectedLabels = new ArrayList<>();
    private SearchResultReposity mReposity;                       //该页面封装后的Model
    private ProductAdapter       mContentAdapter;
    private ProductRecomAdapter  mRecommendAdapter;
    private Tracker              mTracker;                        // GA Tracker

    private double   allPage;                                     // 总页数
    private boolean  isLoading;                                   // 是否正在加载
    private boolean  isSimilar;                                   // 是否shi相似词
    private boolean  isNewData;                                   // 是否是重置关键词新搜索
    private boolean  isInit;                                      // 第一次进来
    private boolean  toTop;
    public  String   keySearch;
    public  String   keyTitle;
    private String[] relateQuery;
    private int      type;
    public int page = -1;                                        // 当前页

    private static final String KEY_PAGE_TYPE          = "type";                                           // 页面类型
    private static final String KEY_SEARCH_WORDS       = "s_w";                                            // 搜索关键词
    private static final String KEY_TITLE              = "title";                                          // 商家、品牌 Title
    private static final String KEY_QUERY_BEAN         = "querybean";                                      // 商家、品牌 查询基础参数
    private static final String HAS_RESULT_HAS_SIMILAR = "系统可以为\"%s\"提供同义词匹配，试一试获取更多结果。";    // 有结果同时配置了同义词
    private static final String MORE_SIMILAR           = "系统已为您显示\"%s\"的同义词结果，返回查看原结果。";      // 查看了更多结果
    private static final String NO_RESULT_HAS_SIMILAR  = "抱歉，没有找到相关商品，已为您显示\"%s\"的结果。";       // 无结果但配置了同义词
    public static final  int    REQUEST_CODE_FILTER    = 555;                                              // requestCode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initData();
    }

    private void initVars() {
        isInit = true;

        // GA 屏幕跟踪
        mTracker = ((HaiApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("搜索_结果");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // 根据intetnt网络请的类型
        Intent intent = getIntent();
        type = intent.getIntExtra(KEY_PAGE_TYPE, PageType.SEARCH);

        if (type == PageType.SEARCH) {        //搜索结果页面
            keySearch = intent.getStringExtra(KEY_SEARCH_WORDS);
            mReposity = SearchResultReposity.newInstance(keySearch);
            // GA 事件
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("电商运营")
                    .setAction("搜索")
                    .setLabel(keySearch)
                    .build());

        } else {                              //商家、品牌主页
            keyTitle = intent.getStringExtra(KEY_TITLE);
            mReposity = SearchResultReposity.newInstance(keyTitle, intent.getParcelableExtra(KEY_QUERY_BEAN), type);
            // GA 事件
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("电商运营")
                    .setAction("热门分类 Click")
                    .setLabel(keyTitle)
                    .build());

        }

    }

    private void initViews() {
        mMultipleStatusView.setOnRetryClickListener(v -> {
            page = -1;
            requestData();
        });
        initTitle();
        initTagLayout();
    }

    private void initData() {
        requestData();
        getShoppingCartBadge();
    }

    /**
     * 关键词改变后搜索数据
     */
    private void requestNewData(String key) {
        if (!isLoading) {
            mMultipleStatusView.showLoading();
            mFilterTagLayout.setVisibility(View.GONE);
            if (selectedLabels != null)
                selectedLabels.clear();
            keySearch = key;
            mReposity.setTopic(key);
            mReposity.setCurrentSort(Sort.HOT);
            mReposity.setGroups(null);
            mReposity.clearFilterPositions();
            mReposity.priceFilter_e = "";
            mReposity.priceFilter_s = "";
            HaiUtils.setText(titleLayout, R.id.searchEdt, key);
            isLoading = true;
            isNewData = true;
            page = -1;
            toTop = true;     //  列表滑至顶部

            searchProduct();
        }
    }

    /**
     * 数据请求
     */
    private void requestData() {
        if (!isLoading) {
            isLoading = true;
            mMultipleStatusView.showLoading();
            searchProduct();
        }
    }

    private void searchProduct() {
        mReposity.doSearch(page, isSimilar ? UseSynType.SYN_USE : UseSynType.SYN_NOMAL)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<SearchResultBean>() {
                    @Override
                    public void onSuccess(SearchResultBean searchResultBean) {
                        //  搜索动作上报
                        searchLogServer(searchResultBean);

                        isSimilar = false;
                        if (HaiUtils.getSize(searchResultBean.products) > 0) {

                            if (type == PageType.SEARCH) {
                                if (searchResultBean.extend != null) {
                                    // 相关词
                                    if (searchResultBean.extend.useRelateQuery) {
                                        relateQuery = searchResultBean.extend.relateQuery;
                                        if (searchResultBean.page <= 1) {
                                            ProductBaseBean productBaseBean = new ProductBaseBean();
                                            productBaseBean.relateQuery = relateQuery;
                                            searchResultBean.products.add(0, productBaseBean);
                                        }
                                    } else {
                                        relateQuery = null;
                                    }

                                    // 同义词
                                    if (isNewData || isInit) {

                                        if (searchResultBean.extend.synonymAvalible) {
                                            setResultTips(0);
                                        } else {
                                            if (searchResultBean.extend.useSynonym) {
                                                setResultTips(1);
                                            }
                                        }
                                    }

                                }
                            }

                            isInit = false;
                            mRecommendGridView.setVisibility(View.GONE);
                            mContentGridView.setVisibility(View.VISIBLE);
                            mMultipleStatusView.showContent();
                            page = searchResultBean.page;
                            allPage = searchResultBean.allpage;

                            if (mContentAdapter == null) {
                                mReposity.setGroups(searchResultBean.group);
                                mContentAdapter = new ProductAdapter(searchResultBean.products, mActivity, mPwToast, mContentGridView, true, type);
                                mContentAdapter.setActivityAnalysListener(new ProductAdapter.ActivityAnalysListener() {
                                    @Override
                                    public void OnActivityAnalys(String spuid) {
                                        // 埋点
                                        HashMap<String, String> kv = new HashMap<String, String>();
                                        kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                                    }
                                });
                                mContentAdapter.setOnLoadMoreListener(() -> loadMoreData(), mContentGridView);
                                mContentAdapter.setOnTagClickListener(tag -> requestNewData(tag));
                                mContentGridView.setLayoutManager(new GridLayoutManager(mActivity, 2));
                                mContentGridView.setAdapter(mContentAdapter);
                                if (hasMore()) {
                                    mContentAdapter.loadMoreComplete();
                                } else {
                                    if (HaiUtils.getSize(searchResultBean.products) > 3) {
                                        mContentAdapter.loadMoreEnd();
                                    } else {
                                        mContentAdapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                if (searchResultBean.page <= 1) {        //非翻页
                                    if (isNewData) {
                                        mReposity.setGroups(searchResultBean.group);
                                        isNewData = false;
                                    }
                                    if (toTop) {   //   回到顶部
                                        ((GridLayoutManager) mContentGridView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                                        toTop = false;
                                    }
                                    mContentAdapter.setNewData(searchResultBean.products);
                                    if (hasMore()) {
                                        mContentAdapter.loadMoreComplete();
                                    } else {
                                        if (HaiUtils.getSize(searchResultBean.products) > 3) {
                                            mContentAdapter.loadMoreEnd();
                                        } else {
                                            mContentAdapter.setEnableLoadMore(false);
                                        }
                                    }
                                } else {                                 //翻页
                                    mContentAdapter.addData(searchResultBean.products);
                                    if (hasMore()) {
                                        mContentAdapter.loadMoreComplete();
                                    } else {
                                        mContentAdapter.loadMoreEnd();
                                    }
                                }
                            }
                        } else {
                            tvResultTips.setVisibility(View.GONE);
                            //获取大家都在买
                            ProductRepository.getInstance()
                                    .getRecommandProduct()
                                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                                    .subscribe(new DefaultSubscriber<SearchResultBean>() {
                                        @Override
                                        public void onSuccess(SearchResultBean searchResultBean) {
                                            mRecommendGridView.setVisibility(View.VISIBLE);
                                            mContentGridView.setVisibility(View.GONE);
                                            mMultipleStatusView.showContent();

                                            FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(SearchResultActivity.this, 2);
                                            fullyGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                                @Override
                                                public int getSpanSize(int position) {
                                                    if (position == 0 || position == 1 || position == (HaiUtils.getSize(searchResultBean.products) + 2)) {
                                                        return 2;
                                                    } else {
                                                        return 1;
                                                    }
                                                }
                                            });
                                            mRecommendGridView.setLayoutManager(fullyGridLayoutManager);
                                            if (mRecommendAdapter == null) {
                                                mRecommendAdapter = new ProductRecomAdapter(SearchResultActivity.this, searchResultBean.products, mPwToast, mRecommendGridView, ProductNullType.PRODUCT_SEARCH, relateQuery);
                                                mRecommendAdapter.setOnTagClickListener(tag -> requestNewData(tag));
                                                mRecommendGridView.setAdapter(mRecommendAdapter);
                                            } else {
                                                mRecommendAdapter.setmList(searchResultBean.products);
                                                mRecommendAdapter.notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onFinish() {
                                            dismissProgressDialog();
                                        }

                                        @Override
                                        public boolean onFailed(Throwable e) {
                                            showFailView(mMultipleStatusView, e, false);
                                            return mHasLoad;
                                        }
                                    });
                            //                            mRecommendLayout.setVisibility(View.VISIBLE);
                            //                            mRecommendGridView.setVisibility(View.VISIBLE);
                            //                            mMultipleStatusView.setVisibility(View.GONE);
                        }
                        mHasLoad = true;
                    }

                    @Override
                    public void onFinish() {
                        isLoading = false;

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mMultipleStatusView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    /**
     * 获取购物车数量
     */
    private void getShoppingCartBadge() {
        if (!HaiUtils.isLogin()) {
            return;
        }
        ProductRepository.getInstance()
                .getShoppingCartListCnt()
                .subscribe(new DefaultSubscriber<ShoppingCartCntBean>() {
                    @Override
                    public void onSuccess(ShoppingCartCntBean shoppingCartCntBean) {
                        setCartBadgeView(shoppingCartCntBean.count);
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }

    private void setResultTips(int type) { //  0是有结果有相似 1是查看更多相似 2是无结果有相似
        tvResultTips.setVisibility(View.VISIBLE);
        String s = "";
        if (type == 0) {
            s = String.format(HAS_RESULT_HAS_SIMILAR, keySearch);
        } else if (type == 1) {
            s = String.format(MORE_SIMILAR, keySearch);
        } else if (type == 2) {
            s = String.format(NO_RESULT_HAS_SIMILAR, keySearch);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);

        // 字体颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ed5137"));

        // 点击事件
        spannableStringBuilder.setSpan(foregroundColorSpan, s.indexOf("\""), s.lastIndexOf("\"") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        if (type == 0) {
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
                    isSimilar = true;
                    requestNewData(mReposity.getTopic());
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(true);
                    ds.setColor(Color.parseColor("#0bb9ff"));
                }
            }, s.indexOf("，") + 1, s.indexOf("，") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (type == 1) {
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
                    isSimilar = false;
                    requestNewData(mReposity.getTopic());

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(true);
                    ds.setColor(Color.parseColor("#0bb9ff"));
                }
            }, s.indexOf("，") + 1, s.indexOf("，") + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvResultTips.setText(spannableStringBuilder);
        tvResultTips.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void searchLogServer(SearchResultBean searchResultBean) {
        mReposity.searchLogServer(page, searchResultBean.count)
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        return super.onFailed(e);
                    }
                });

    }

    /**
     * 加载更多
     */
    public void loadMoreData() {
        if (hasMore() && !isLoading) {
            isLoading = true;
            searchProduct();
        }
    }

    public void initTitle() {
        if (mReposity.isQuery()) {
            titleLayout = mSearchViewStub.inflate();
            titleLayout.setOnClickListener(v -> onBackPressed());
            HaiUtils.setText(titleLayout, R.id.searchEdt, mReposity.getTopic());
        } else {
            mSiteViewStub.inflate();
            ((HaiTextView) ButterKnife.findById(this, R.id.titleTxt)).setText(mReposity.getTitle());
            ButterKnife.findById(this, R.id.backImg).setOnClickListener(v -> onBackPressed());
            ButterKnife.findById(this, R.id.toShopCarImg).setOnClickListener(v -> {
                if (!HaiUtils.needLogin(this)) {
                    // 埋点
                    YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Cart, TraceUtils.Event_Category_Click, "", null, "");

                    startActivity(new Intent(this, ShoppingCartActivity.class));
                    overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
                }
            });
        }
    }

    private void updateBar(boolean... booleans) {
        mHotRadioBtn.setChecked(booleans[0]);
        mSaleRadioBtn.setChecked(booleans[1]);
        mPriceCheckTV.setChecked(booleans[2]);
        if (!booleans[2]) {
            mPriceImgView.setImageLevel(0);
        }
    }

    private void initTagLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rycvSelectedLabels.setLayoutManager(linearLayoutManager);
        mSelectedLabelsAdapter = new SelectedLabelsAdapter(this, selectedLabels);
        rycvSelectedLabels.setAdapter(mSelectedLabelsAdapter);
        rycvSelectedLabels.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 判断该类型是否还有选中，
                SearchResultBean.LabelsBean item = (SearchResultBean.LabelsBean) adapter.getData().get(position);
                if (item.type == 1) {                         //  分类
                    if (!mReposity.upData(0, item.pos)) {
                        mReposity.removeFilterPosition(0);
                    }
                } else if (item.type == 2) {                  //  品牌
                    if (!mReposity.upData(1, item.pos)) {
                        mReposity.removeFilterPosition(1);
                    }
                } else if (item.type == 3) {                  //  商家
                    if (!mReposity.upData(2, item.pos)) {
                        mReposity.removeFilterPosition(2);
                    }
                } else {                                      //  价格
                    mReposity.upData(3, item.pos);
                    mReposity.removeFilterPosition(3);
                }

                updateTagLayout(position);
                page = -1;
                toTop = true;
                requestData();

            }
        });
    }

    private void updateTagLayout(int position) {
        mSelectedLabelsAdapter.getData().remove(position);
        mSelectedLabelsAdapter.notifyDataSetChanged();
        if (HaiUtils.getSize((ArrayList) mSelectedLabelsAdapter.getData()) == 0) {
            mFilterTagLayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.hotRadioBtn, R.id.saleRadioBtn, R.id.priceLayout, R.id.filterLayout})
    public void onBarClick(View view) {
        switch (view.getId()) {
            case R.id.hotRadioBtn:
                mReposity.setCurrentSort(Sort.HOT);
                updateBar(true, false, false);
                page = -1;
                toTop = true;
                requestData();
                break;
            case R.id.saleRadioBtn:
                mReposity.setCurrentSort(Sort.SALE);
                updateBar(false, true, false);
                page = -1;
                toTop = true;
                requestData();
                break;
            case R.id.priceLayout:
                page = -1;
                toTop = true;
                if (mPriceCheckTV.isChecked()) {
                    int level = mPriceImgView.getDrawable().getLevel() % 2 + 1;
                    mPriceImgView.setImageLevel(level);
                    mReposity.setCurrentSort(level == 1 ? Sort.PRICE_ASCEND : Sort.PRICE_DESCEND);
                } else {
                    mReposity.setCurrentSort(Sort.PRICE_ASCEND);
                    mPriceImgView.setImageLevel(1);
                }
                updateBar(false, false, true);
                requestData();
                break;
            case R.id.filterLayout:
                if (mReposity.getGroups() != null) {
                    mReposity.getGroups().add(null);
                    FilterActivity.toThisActivityForResult(this, mReposity.getFilterPositions(), mReposity.getGroupsJson()
                            , REQUEST_CODE_FILTER, mReposity.priceFilter_s, mReposity.priceFilter_e);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        // 接收筛选后返回的数据
        if (requestCode == REQUEST_CODE_FILTER) {
            mReposity.setFilterPositions(data.getIntArrayExtra("ints"));

            String           resultData       = data.getStringExtra("data");
            SearchResultBean searchResultBean = new Gson().fromJson(resultData, SearchResultBean.class);
            mReposity.priceFilter_s = data.getStringExtra("priceFilter_s");
            mReposity.priceFilter_e = data.getStringExtra("priceFilter_e");
            mReposity.upDataGroup(searchResultBean.group);

            mSelectedLabelsAdapter.setNewData(mReposity.getSelected(searchResultBean.group));
            if (HaiUtils.getSize((ArrayList) mSelectedLabelsAdapter.getData()) == 0) {
                mFilterTagLayout.setVisibility(View.GONE);
            } else {
                mFilterTagLayout.setVisibility(View.VISIBLE);
            }
            page = -1;
            toTop = true;     //  列表滑至顶部
            requestData();
        }
    }

    /**
     * @param activity
     * @param searchWords 搜索关键词
     */
    public static void toThisActivity(Activity activity, String searchWords) {
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra(KEY_PAGE_TYPE, PageType.SEARCH);
        intent.putExtra(KEY_SEARCH_WORDS, searchWords);
        activity.startActivity(intent);
    }

    public static void toThisActivityForResult(Activity activity, String searchWords, int request) {
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra(KEY_PAGE_TYPE, PageType.SEARCH);
        intent.putExtra(KEY_SEARCH_WORDS, searchWords);
        activity.startActivityForResult(intent, request);
    }

    /**
     * @param activity
     * @param title     商家或品牌名称
     * @param queryBean QueryBean
     * @param pageType
     */
    public static void toThisActivity(Activity activity, String title, QueryBean queryBean, @PageType int pageType) {
        if (TextUtils.isEmpty(title) || queryBean == null) {
            return;
        }
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra(KEY_PAGE_TYPE, pageType);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_QUERY_BEAN, queryBean);
        activity.startActivity(intent);
    }

    /**
     * 分页判断，是否加载更多
     */
    @Override
    public boolean hasMore() {
        return page == -1 || page < allPage;
    }

    @Override
    public void loadMore() {
        requestData();
    }

    /**
     * 登录状态改变  更新购物车数量
     *
     * @param event
     */
    @Subscribe
    public void onLoginStatusEvent(LoginStateChangeEvent event) {
        if (event.isLogin) {
            getShoppingCartBadge();
        } else {
            setCartBadgeView(0);
        }
    }

    @Subscribe
    public void cartCountChange(ShoppingCartCntChangeEvent event) {
        setCartBadgeView(event.shoppingCartBadgeCnt);
    }

    private void setCartBadgeView(int shoppingCartBadgeCnt) {
        HaiTextView carCountTxt = (HaiTextView) findViewById(R.id.carCountTxt);
        if (carCountTxt == null)
            return;
        if (shoppingCartBadgeCnt > 0) {
            carCountTxt.setVisibility(View.VISIBLE);
            carCountTxt.setText(String.valueOf(shoppingCartBadgeCnt));
        } else {
            carCountTxt.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void changeLikeState(ProductLikeEvent event) {
        if (mRecommendGridView.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < mRecommendAdapter.getmList().size(); i++) {
                if (mRecommendAdapter.getmList().get(i).spuid.equals(event.spuid)) {
                    if (mRecommendAdapter.getmList().get(i).is_star != event.liklikeState) {
                        mRecommendAdapter.getmList().get(i).is_star = event.liklikeState;
                        mRecommendAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        } else if (HaiUtils.getSize(mContentAdapter.getData()) > 0) {
            boolean isLike = event.liklikeState;
            String  spuid  = event.spuid;
            for (int i = 0; i < mContentAdapter.getData().size(); i++) {
                String  prdSpuid = mContentAdapter.getItem(i).spuid;
                boolean prdLike  = mContentAdapter.getItem(i).is_star;
                if (prdSpuid != null && prdSpuid.equals(spuid)) {
                    if (prdLike != isLike) {
                        mContentAdapter.getItem(i).is_star = event.liklikeState;
                        mContentAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    protected String getActivityTAG() {
        if (type == PageType.SEARCH) {        //搜索结果页面
            return TAG + "?" + "keyword=" + keySearch;
        } else {                              //商家、品牌主页
            return TAG + "?" + "title=" + keyTitle;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("searchWords", mReposity.getTopic());
        setResult(RESULT_OK, intent);
        finish();
    }
}
