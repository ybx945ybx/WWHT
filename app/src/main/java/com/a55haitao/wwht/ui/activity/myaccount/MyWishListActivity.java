package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.WishListAdapter;
import com.a55haitao.wwht.data.event.ProductLikeEvent;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.result.GetUserStarProductsResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.view.GridSpacingItemDecoration;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWishListActivity extends BaseHasFragmentActivity {

    @BindView(R.id.content_view)    RecyclerView          mRvContent; // 心愿单列表
    @BindView(R.id.headRightTxt)    HaiTextView           tvRight;    // 编辑按钮
    @BindView(R.id.swipe)           HaiSwipeRefreshLayout mSwipe;     // 下拉刷新
    @BindView(R.id.msv)             MultipleStatusView    mSv;        // 多状态布局
    @BindView(R.id.deleteBottomTxt) TextView              mDeleteBottomTxt;

    @BindDimen(R.dimen.margin_medium) int mRvSpacing;

    private ToastPopuWindow mPwToast;

    private int                                         mAllPage;
    private int                                         mCurrentPage;
    private WishListAdapter                             mAdapter;
    private List<GetUserStarProductsResult.ProductBean> mProductListData;
    private Tracker                                     mTracker;           // GA Tracker

    private boolean isEdit;                                     //  批量删除编辑状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_list);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    private void initVariables() {
        mAllPage = 1;
        mCurrentPage = 1;
        mProductListData = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("我的心愿单");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRvContent.addItemDecoration(new GridSpacingItemDecoration(2, mRvSpacing, true));
        mAdapter = new WishListAdapter(mProductListData, mActivity);
        mAdapter.setCheckClickListener(count -> mDeleteBottomTxt.setText(String.format("删除%d个商品", count)));
        mRvContent.setAdapter(mAdapter);

        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            if (hasMore()) {
                mCurrentPage++;
                requestProductList();
            }
        });
        // 下拉刷新
        mSwipe.setOnRefreshListener(() -> {
            mAdapter.setEnableLoadMore(false);
            loadData();
        });
        // 错误重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    private boolean hasMore() {
        return mCurrentPage == -1 || mCurrentPage < mAllPage;

    }

    private void loadData() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;
        requestProductList();
    }

    private void requestProductList() {
        ProductRepository.getInstance()
                .getUserStarProducts(mCurrentPage)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetUserStarProductsResult>() {
                    @Override
                    public void onSuccess(GetUserStarProductsResult result) {
                        mCurrentPage = result.page;
                        mAllPage = result.allpage;

                        if (result.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.datas);
                                if (hasMore()) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    if (HaiUtils.getSize(result.datas) > 3) {
                                        mAdapter.loadMoreEnd();
                                    } else {
                                        mAdapter.loadMoreEnd(true);
                                    }
                                }

                                mDeleteBottomTxt.setText("删除0个商品");
                            } else {
                                mAdapter.addData(result.datas);
                                if (hasMore()) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            }
                        }
                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setEnabled(true);
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, MyWishListActivity.class));
    }

    @OnClick({R.id.headerLeftImg, R.id.headRightTxt, R.id.deleteBottomTxt})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
            case R.id.headRightTxt:

                if (isEdit) {
                    tvRight.setText("编辑");
                    mDeleteBottomTxt.setVisibility(View.GONE);

                } else {
                    tvRight.setText("完成");
                    mDeleteBottomTxt.setVisibility(View.VISIBLE);

                }
                isEdit = !isEdit;
                mAdapter.setEdit(isEdit);
                mAdapter.notifyDataSetChanged();

                break;
            case R.id.deleteBottomTxt:
                ArrayList<String> spuidList = mAdapter.getDeleteInfo();
                if (HaiUtils.getSize(spuidList) > 0) {
                    String spuids = new Gson().toJson(spuidList);
                    cancelStarProds(spuids);
                } else {
                    mPwToast = ToastPopuWindow.makeText(mActivity, "请选择需要取消关注的商品", AlertViewType.AlertViewType_Warning).parentView(mRvContent);
                    mPwToast.show();
                    break;
                }

                isEdit = !isEdit;
                tvRight.setText("编辑");

                mDeleteBottomTxt.setVisibility(View.GONE);
                break;
        }
    }

    private void cancelStarProds(String spuids) {
        ProductRepository.getInstance()
                .cancelStarProds(spuids)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        mAdapter.setEdit(false);
                        loadData();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSwipe.setRefreshing(false);
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
        return TAG;
    }

    @Subscribe
    public void onProductLikeEvent(ProductLikeEvent event) {
        mAdapter.setEnableLoadMore(false);
        mSwipe.post(() -> {
            mSwipe.setRefreshing(true);
            loadData();
        });
    }
}
