package com.a55haitao.wwht.ui.fragment.discover;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.easyopt.EasyOptAdapter;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.CategoryPageBean;
import com.a55haitao.wwht.data.model.entity.CategoryPageData;
import com.a55haitao.wwht.data.model.entity.EasyOptBean;
import com.a55haitao.wwht.data.model.result.EasyOptListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.discover.SearchWordsActivity;
import com.a55haitao.wwht.ui.activity.easyopt.RecommendEasyOptActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.layout.CategoryCell;
import com.a55haitao.wwht.ui.layout.ImageSiteCell;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;

/**
 * 主页 - 分类Fragment
 */
public class CenterCategoryFragment extends BaseFragment {

    @BindView(R.id.recyclerView)   RecyclerView       mEORecycelrView;
    @BindView(R.id.msv)            MultipleStatusView mSv;
    @BindView(R.id.categoryLayout) CategoryCell       mCategoryCell;
    @BindView(R.id.brandLayout)    ImageSiteCell      mBrandCell;
    @BindView(R.id.sellerLayout)   ImageSiteCell      mSellerCell;
    @BindView(R.id.moreTxt)        ImageView          ivMore;
    @BindView(R.id.contentLayout)  LinearLayout       mContentLayout;
    @BindView(R.id.easyOptLayout)  LinearLayout       mEasyOptLayout;

    //    @BindView(R.id.swipe) SwipeRefreshLayout mSwipeRefreshLayout;

    private EasyOptListResult mOptListResult;
    private Unbinder          mUnbinder;

    private CategoryPageBean mCategoryPageBean;
    private Tracker          mTracker;  // GA Tracker

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_center_category, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initVars();
        initViews(v, savedInstanceState);
        return v;
    }

    private void initVars() {
        // GA Tracker
        HaiApplication application = (HaiApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("发现");
    }

    private void initViews(View v, Bundle savedInstanceState) {
        ivMore.setOnClickListener(v12 -> startActivity(new Intent(mActivity, RecommendEasyOptActivity.class)));
        mSv.setOnRetryClickListener(v1 -> requestData());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData();
    }

    public void requestFinish() {
        if (mOptListResult != null && HaiUtils.getSize(mOptListResult.easyopts) > 0) {
            if (mEORecycelrView.getAdapter() == null) {
                EasyOptAdapter      adapter       = new EasyOptAdapter(getContext(), mOptListResult.easyopts);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                mEORecycelrView.setLayoutManager(layoutManager);
                mEORecycelrView.setAdapter(adapter);
                new PagerSnapHelper().attachToRecyclerView(mEORecycelrView);
                layoutManager.scrollToPositionWithOffset(adapter.getItemRealCount() << 10, (int) (DeviceUtils.getDensity() * 65 / 2));
            } else {
                EasyOptAdapter adapter = (EasyOptAdapter) mEORecycelrView.getAdapter();
                adapter.changeDatas(mOptListResult.easyopts);
            }
            mEasyOptLayout.setVisibility(View.VISIBLE);

        } else {
            mEasyOptLayout.setVisibility(View.GONE);
        }

        if (mCategoryPageBean != null) {
            if (HaiUtils.getSize(mCategoryPageBean.hot_categories) > 0) {
                mCategoryCell.setVisibility(View.VISIBLE);
                mCategoryCell.setData(mCategoryPageBean.hot_categories);
            } else {
                mCategoryCell.setVisibility(View.GONE);
            }
            if (HaiUtils.getSize(mCategoryPageBean.hot_brands) > 0) {
                mBrandCell.setVisibility(View.VISIBLE);
                mBrandCell.setData(mCategoryPageBean.hot_brands, PageType.BRAND);
            } else {
                mBrandCell.setVisibility(View.GONE);
            }
            if (HaiUtils.getSize(mCategoryPageBean.hot_sellers) > 0) {
                mSellerCell.setVisibility(View.VISIBLE);
                mSellerCell.setData(mCategoryPageBean.hot_sellers, PageType.SELLER);
            } else {
                mSellerCell.setVisibility(View.GONE);
            }
            mContentLayout.setVisibility(View.VISIBLE);

        } else {
            mContentLayout.setVisibility(View.GONE);
        }

    }

    private void requestData() {
        mSv.showLoading();
        Observable.zip(ProductRepository.getInstance().getHotHome(),
                SnsRepository.getInstance().easyoptSelect(),
                CategoryPageData::new)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<CategoryPageData>() {
                    @Override
                    public void onSuccess(CategoryPageData categoryPageData) {
                        mSv.showContent();
                        // 分类 商家 品牌
                        mCategoryPageBean = categoryPageData.mCategoryPageBean;
                        // 精选草单
                        for (EasyOptBean easyopt : categoryPageData.mEasyOptListResult.easyopts) {
                            if (HaiUtils.getSize(easyopt.products) > 3) {
                                easyopt.products = new ArrayList<>(easyopt.products.subList(0, 3));
                            }
                        }
                        mOptListResult = categoryPageData.mEasyOptListResult;
                        mHasData = true;
                    }

                    @Override
                    public void onFinish() {
                        requestFinish();
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasData);
                        return mHasData;
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.searchLayout)
    public void onClick(View view) {
        startActivity(new Intent(getContext(), SearchWordsActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
