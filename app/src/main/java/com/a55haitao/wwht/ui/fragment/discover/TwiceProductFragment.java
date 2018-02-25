package com.a55haitao.wwht.ui.fragment.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.SelectedLabelsAdapter;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.event.ProductLikeEvent;
import com.a55haitao.wwht.data.interfaces.ILoad;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.Sort;
import com.a55haitao.wwht.data.model.annotation.UseSynType;
import com.a55haitao.wwht.data.model.entity.LikeProductBean;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.data.model.entity.SearchSpecialBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.repository.SearchResultReposity;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.discover.FilterActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tom.ybxtracelibrary.YbxTrace;

import static android.app.Activity.RESULT_OK;
import static com.a55haitao.wwht.data.model.annotation.AlertViewType.AlterViewType_Pray;
import static com.a55haitao.wwht.ui.activity.discover.SearchResultActivity.REQUEST_CODE_FILTER;

/**
 * Create by drolmen
 */
public class TwiceProductFragment extends BaseFragment implements ILoad {

    @BindView(R.id.priceCheckTV)           CheckedTextView    mPriceCheckTV;
    @BindView(R.id.priceImgView)           ImageView          mPriceImgView;
    @BindView(R.id.hotRadioBtn)            RadioButton        mHotRadioBtn;         //热门
    @BindView(R.id.saleRadioBtn)           RadioButton        mSaleRadioBtn;        //折扣
    @BindView(R.id.priceLayout)            LinearLayout       mPriceLayout;         //价格
    @BindView(R.id.filterLayout)           LinearLayout       mFilterLayout;        //筛选
    @BindView(R.id.filterCondictionLayout) LinearLayout       mFilterTagLayout;     //标签
    @BindView(R.id.rycv_selected_labels)   RecyclerView       rycvSelectedLabels;
    @BindView(R.id.content_view)           RecyclerView       mRvContent;
    @BindView(R.id.msv)                    MultipleStatusView mSv;

    private SelectedLabelsAdapter mSelectedLabelsAdapter;
    private ArrayList<SearchResultBean.LabelsBean> selectedLabels = new ArrayList<>();
    private boolean              resetData;                                             //分类已选标签改变后重置数据
    private SearchResultReposity mReposity;                  //该页面封装后的Model
    private boolean              isLoading;                             //是否正在加载
    private ProductAdapter       mContentAdapter;
    private int page = -1;                  //当前页
    private double          allPage;            //总页数
    private boolean         toTop;
    private ToastPopuWindow mPwToast;
    private Unbinder        mUnbinder;

    private String mTag;

    public TwiceProductFragment() {
    }

    public static TwiceProductFragment newInstance(Bundle bundle) {
        TwiceProductFragment fragment = new TwiceProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_twice_product, container, false);
        mUnbinder = ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(this);
        initTagLayout();
        return inflate;
    }

    // 品牌和商家
    public void setReposityInfo(String name, int type, QueryBean queryBean) {
        mReposity = SearchResultReposity.newInstance(name, queryBean, type);
    }

    // 专题搜索
    public void setReposityInfo(String name, int type, SearchSpecialBean.SearchSpecialQueryBean queryBean) {
        mReposity = SearchResultReposity.newInstance(name, type, queryBean);
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

    /**
     * 数据请求
     */
    public void requestData() {
        if (hasMore() && !isLoading) {
            isLoading = true;
            //            showProgressDialog("正在加载", true);
            mSv.showLoading();
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
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<SearchResultBean>() {
                    @Override
                    public void onSuccess(SearchResultBean searchResultBean) {
                        //  搜索成功后动作上报
                        searchLogServer(searchResultBean);

                        mSv.showContent();
                        page = searchResultBean.page;
                        allPage = searchResultBean.allpage;

                        if (mContentAdapter == null) {
                            mReposity.setGroups(searchResultBean.group);
                            mContentAdapter = new ProductAdapter(searchResultBean.products, mActivity, mPwToast, mRvContent);
                            if (mReposity.getmPageType() == PageType.CATEGORY) {
                                mContentAdapter.setActivityAnalysListener(new ProductAdapter.ActivityAnalysListener() {
                                    @Override
                                    public void OnActivityAnalys(String spuid) {
                                        // 埋点
//                                        TraceUtils.addClick(TraceUtils.PageCode_ProductDetail, spuid, mActivity, TraceUtils.PageCode_BrandOrSiteDetail, TraceUtils.PositionCode_Product + "", "");
                                        HashMap<String, String> kv = new HashMap<String, String>();
                                        kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                                        YbxTrace.getInstance().event(mActivity, ((BaseActivity)mActivity).pref, ((BaseActivity)mActivity).prefh, ((BaseActivity)mActivity).purl, ((BaseActivity)mActivity).purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                                        //                                        TraceUtils.addAnalysAct(TraceUtils.PageCode_ProductDetail, TraceUtils.PageCode_BrandOrSiteDetail, TraceUtils.PositionCode_Product, spuid);

                                    }
                                });
                            }
                            mContentAdapter.setOnLoadMoreListener(() -> loadMoreData());
                            mRvContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            mRvContent.setAdapter(mContentAdapter);
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
                                if (toTop) {   //   回到顶部
                                    ((GridLayoutManager) mRvContent.getLayoutManager()).scrollToPositionWithOffset(0, 0);
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
                    }

                    @Override
                    public void onFinish() {
                        isLoading = false;
                        dismissProgressDialog();
                        //                        ((SiteActivity)mActivity).finishLoading();
                    }
                });
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

    private void productLike(String spuid, int position) {
        if (HaiUtils.needLogin(mActivity)) {
            return;
        }
        ProductRepository.getInstance()
                .likeProduct(spuid)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<LikeProductBean>() {
                    @Override
                    public void onSuccess(LikeProductBean likeProductBean) {
                        boolean checked = mContentAdapter.getItem(position).is_star;
                        if (!checked) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, "已加入心愿单", AlterViewType_Pray).parentView(mRvContent);
                            mPwToast.show();
                        }
                        mContentAdapter.getItem(position).is_star = !checked;
                        mContentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {
                    }
                });
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rycvSelectedLabels.setLayoutManager(linearLayoutManager);
        mSelectedLabelsAdapter = new SelectedLabelsAdapter(mActivity, selectedLabels);
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
                } else if (item.type == 2) {                   //  品牌
                    if (!mReposity.upData(1, item.pos)) {
                        mReposity.removeFilterPosition(1);
                    }
                } else if (item.type == 3) {                   //  商家
                    if (!mReposity.upData(2, item.pos)) {
                        mReposity.removeFilterPosition(2);
                    }
                } else {                                     //  价格
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

    @Override
    public boolean hasMore() {
        return page == -1 || page < allPage;
    }

    @Override
    public void loadMore() {
        requestData();
    }

    @OnClick({R.id.hotRadioBtn, R.id.saleRadioBtn, R.id.priceLayout, R.id.filterLayout})
    public void onBarClick(View view) {
        switch (view.getId()) {
            case R.id.hotRadioBtn:
                mReposity.setCurrentSort(Sort.HOT);
                updateBar(true, false, false);
                page = -1;
                requestData();
                break;
            case R.id.saleRadioBtn:
                mReposity.setCurrentSort(Sort.SALE);
                updateBar(false, true, false);
                page = -1;
                requestData();
                break;
            case R.id.priceLayout:
                page = -1;
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
    public void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            toTop = true;
            requestData();

        }
    }

    @Subscribe
    public void changeLikeState(ProductLikeEvent event) {
        if (HaiUtils.getSize(mContentAdapter.getData()) > 0) {
            for (int i = 0; i < mContentAdapter.getData().size(); i++) {
                if (mContentAdapter.getData().get(i).spuid.equals(event.spuid)) {
                    if (mContentAdapter.getData().get(i).is_star != event.liklikeState) {
                        mContentAdapter.getData().get(i).is_star = event.liklikeState;
                        mContentAdapter.notifyItemChanged(i);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
