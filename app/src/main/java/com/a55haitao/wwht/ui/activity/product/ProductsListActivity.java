package com.a55haitao.wwht.ui.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.SelectedLabelsAdapter;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.adapter.product.ProductSimpleAdapter;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.Sort;
import com.a55haitao.wwht.data.model.annotation.UseSynType;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.data.model.result.FullcutDetailResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ExtendRepository;
import com.a55haitao.wwht.data.repository.SearchResultReposity;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.discover.FilterActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.HaiUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动商品列表
 * Created by a55 on 2017/7/10.
 */

public class ProductsListActivity extends BaseActivity {

    @BindView(R.id.head)                   DynamicHeaderView  headView;
    @BindView(R.id.msv)                    MultipleStatusView Msv;
    @BindView(R.id.content_view)           RecyclerView       rycvProducts;
    @BindView(R.id.priceCheckTV)           CheckedTextView    mPriceCheckTV;
    @BindView(R.id.priceImgView)           ImageView          mPriceImgView;
    @BindView(R.id.hotRadioBtn)            RadioButton        mHotRadioBtn;         //热门
    @BindView(R.id.saleRadioBtn)           RadioButton        mSaleRadioBtn;        //折扣
    @BindView(R.id.priceLayout)            LinearLayout       mPriceLayout;         //价格
    @BindView(R.id.filterLayout)           LinearLayout       mFilterLayout;        //筛选
    @BindView(R.id.llyt_filter)            LinearLayout       llytFilter;
    @BindView(R.id.filterCondictionLayout) LinearLayout       mFilterTagLayout;     //标签
    @BindView(R.id.rycv_selected_labels)   RecyclerView       rycvSelectedLabels;
    @BindView(R.id.tv_null)                HaiTextView        tvNullSearch;

    private HaiTextView tvActivityTips;

    private SelectedLabelsAdapter mSelectedLabelsAdapter;
    private ArrayList<SearchResultBean.LabelsBean> selectedLabels = new ArrayList<>();
    private SearchResultReposity mReposity;                       //该页面封装后的Model

    private int                  type;      // 0单品1商家
    private String               title;
    private String               fid;
    private ProductSimpleAdapter mSimpleAdapter;
    private ProductAdapter       mAdapter;
    private ToastPopuWindow      mPwToast;

    private double allPage;
    public int page = -1;                                        // 当前页
    private boolean isInit;                                      // 第一次进来
    private boolean toTop;
    private boolean isLoading;                                   // 是否正在加载

    private static final int REQUEST_CODE_FILTER = 100;                                              // requestCode


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        initVars();
        initViews();
        initEvents();
        getdata();
    }

    private void initVars() {
        isInit = true;

        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            fid = intent.getStringExtra("fid");
        }
    }

    private void initViews() {
        // 标题
        headView.setHeadTitle(title);
        headView.setHeaderRightHidden();

        // 活动提示语
        tvActivityTips = (HaiTextView) LayoutInflater.from(this).inflate(R.layout.activity_tips, null);

        //        // 商品列表
        //        rycvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        //        mAdapter = new ProductSimpleAdapter(null, this, mPwToast, rycvProducts, false);
        //        mAdapter.addHeaderView(tvActivityTips);
        //        rycvProducts.setAdapter(mAdapter);
        //
        //        // 已选择的筛选标签
        initTagLayout();

    }

    private void initEvents() {
        Msv.setOnRetryClickListener(v -> getdata());

    }

    private void getdata() {
        Msv.showLoading();
        ExtendRepository.getInstance()
                .getFullcutDetail(1, fid)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<FullcutDetailResult>() {

                    @Override
                    public void onSuccess(FullcutDetailResult fullcutDetailResult) {
                        String activityTips = "";
                        ArrayList<ShoppingCartBean.CutData> cutlist = fullcutDetailResult.f_cutlist;
                        //冒泡算法  对优惠阶梯按从小到大的方式排序
                        for(int i = cutlist.size() - 1; i>0; i--){//让比较的范围不停的减掉最后一个单元
                            for(int j = 1; j <= i; j++){
                                if(cutlist.get(j - 1).full > cutlist.get(j).full){//让2个数之间大的数排后面
                                    ShoppingCartBean.CutData tmp = cutlist.get(j-1);
                                    cutlist.set(j-1, cutlist.get(j));
                                    cutlist.set(j, tmp);
                                }

                            }
                        }
                        for (int i = 0; i < cutlist.size(); i++){
                            if (i == 0){
                                activityTips = cutlist.get(i).title;
                            }else {
                                activityTips = activityTips + "," + cutlist.get(i).title;
                            }
                        }

                        tvActivityTips.setText("限时促销：以下商品参与“" + activityTips + "”活动");
                        type = fullcutDetailResult.f_manage.ltype;
                        if (type == 0) {  // 0单品1商家
                            Msv.showContent();
                            page = fullcutDetailResult.pageinfo.page;
                            allPage = fullcutDetailResult.pageinfo.allpage;
                            //                            mAdapter.setNewData(fullcutDetailResult.select_prods.prods);
                            // 商品列表
                            rycvProducts.setLayoutManager(new GridLayoutManager(ProductsListActivity.this, 2));
                            mSimpleAdapter = new ProductSimpleAdapter(null, ProductsListActivity.this, mPwToast, rycvProducts, false);
                            mSimpleAdapter.addHeaderView(tvActivityTips);
                            rycvProducts.setAdapter(mSimpleAdapter);
                            mSimpleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    loadMoreDataSimple();
                                }
                            });

                            mSimpleAdapter.setNewData(fullcutDetailResult.select_prods.prods);
                            if (hasMore()) {
                                mSimpleAdapter.loadMoreComplete();
                            } else {
                                if (HaiUtils.getSize(fullcutDetailResult.select_prods.prods) > 3) {
                                    mSimpleAdapter.loadMoreEnd();
                                } else {
                                    mSimpleAdapter.setEnableLoadMore(false);
                                }
                            }

                        } else {
                            // 商品列表
                            rycvProducts.setLayoutManager(new GridLayoutManager(ProductsListActivity.this, 2));
                            mAdapter = new ProductAdapter(null, ProductsListActivity.this, mPwToast, rycvProducts, false);
                            mAdapter.addHeaderView(tvActivityTips);
                            rycvProducts.setAdapter(mAdapter);

                            mAdapter.setOnLoadMoreListener(() -> loadMoreData());
                            QueryBean queryBean  = new QueryBean();
                            boolean   isAllBrand = false;
                            if (HaiUtils.getSize(fullcutDetailResult.select) > 0) {
                                queryBean.seller = fullcutDetailResult.select.get(0).seller;
                                String brand = "";
                                for (int i = 0; i < fullcutDetailResult.select.size(); i++) {
                                    if (i == 0) {
                                        brand = fullcutDetailResult.select.get(i).brand;
                                    } else {
                                        brand = brand + "@@" + fullcutDetailResult.select.get(i).brand;
                                    }

                                }
                                if ("*".equals(brand)) {
                                    brand = "";
                                    isAllBrand = true;
                                }
                                queryBean.brand = brand;
                            }
                            mReposity = SearchResultReposity.newInstance(queryBean, PageType.FULLCUT, isAllBrand);
                            requestData();

                        }
                    }

                    @Override
                    public void onFinish() {
                        //                        if (type == 0) {  // 0单品1商家
                        //                            mHasLoad = true;
                        //                        }
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(Msv, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    // 单品活动加载更多
    private void loadMoreDataSimple() {
        ExtendRepository.getInstance()
                .getFullcutDetail(page + 1, fid)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<FullcutDetailResult>() {

                    @Override
                    public void onSuccess(FullcutDetailResult fullcutDetailResult) {

                        page = fullcutDetailResult.pageinfo.page;
                        allPage = fullcutDetailResult.pageinfo.allpage;

                        mSimpleAdapter.addData(fullcutDetailResult.select_prods.prods);
                        if (hasMore()) {
                            mSimpleAdapter.loadMoreComplete();
                        } else {
                            mSimpleAdapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
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

    private void updateBar(boolean... booleans) {
        mHotRadioBtn.setChecked(booleans[0]);
        mSaleRadioBtn.setChecked(booleans[1]);
        mPriceCheckTV.setChecked(booleans[2]);
        if (!booleans[2]) {
            mPriceImgView.setImageLevel(0);
        }
    }

    /**
     * 数据请求
     */
    private void requestData() {
        if (!isLoading) {
            isLoading = true;
            tvNullSearch.setVisibility(View.GONE);
            Msv.setVisibility(View.VISIBLE);
            Msv.showLoading();
            searchProduct();
        }
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

    private void searchProduct() {
        mReposity.doSearch(page, UseSynType.SYN_NOMAL)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<SearchResultBean>() {
                    @Override
                    public void onSuccess(SearchResultBean searchResultBean) {

                        if (HaiUtils.getSize(searchResultBean.products) > 0) {
                            Msv.setVisibility(View.VISIBLE);
                            tvNullSearch.setVisibility(View.GONE);

                            Msv.showContent();
                            page = searchResultBean.page;
                            allPage = searchResultBean.allpage;

                            if (isInit) {
                                llytFilter.setVisibility(View.VISIBLE);
                                mReposity.setGroups(searchResultBean.group);
                                isInit = false;
                            }

                            if (searchResultBean.page <= 1) {        //非翻页

                                if (toTop) {   //   回到顶部
                                    ((GridLayoutManager) rycvProducts.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                                    toTop = false;
                                }
                                mAdapter.setNewData(searchResultBean.products);
                                if (hasMore()) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    if (HaiUtils.getSize(searchResultBean.products) > 3) {
                                        mAdapter.loadMoreEnd();
                                    } else {
                                        mAdapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {                                 //翻页
                                mAdapter.addData(searchResultBean.products);
                                if (hasMore()) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            }
                        } else {
                            Msv.setVisibility(View.GONE);
                            tvNullSearch.setVisibility(View.VISIBLE);

                        }
                        mHasLoad = true;
                    }

                    @Override
                    public void onFinish() {
                        isLoading = false;

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(Msv, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    private boolean hasMore() {
        return page == -1 || page < allPage;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + fid;
    }
}
