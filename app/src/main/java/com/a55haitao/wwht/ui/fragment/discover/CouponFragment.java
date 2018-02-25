package com.a55haitao.wwht.ui.fragment.discover;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.CouponListAdapter;
import com.a55haitao.wwht.data.event.CouponCashEvent;
import com.a55haitao.wwht.data.model.entity.CouponBean;
import com.a55haitao.wwht.data.model.entity.CouponListBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.PassPortRepository;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 优惠券Fragment
 */
public class CouponFragment extends BaseFragment {

    //    @BindView(R.id.content_view) ListView              mListView;
    @BindView(R.id.content_view) RecyclerView          mRvContent;
    @BindView(R.id.msv)          MultipleStatusView    mSv;
    @BindView(R.id.swipe)        HaiSwipeRefreshLayout mSwipe;

    private Unbinder              mUnbinder;
    private int                   mType;
    private CouponListAdapter     mAdapter;
    private ArrayList<CouponBean> mCouponList;

    public static CouponFragment newInstance(int type) {
        CouponFragment f = new CouponFragment();
        Bundle         b = new Bundle();
        b.putInt("type", type);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coupon, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initVars();
        initViews(savedInstanceState);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void initVars() {
        mType = getArguments().getInt("type");
        mCouponList = new ArrayList<>();
    }

    private void initViews(Bundle savedInstanceState) {
        mSv.setOnRetryClickListener(v -> loadData());
        mSwipe.setOnRefreshListener(this::loadData);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRvContent.setLayoutManager(layoutManager);
        // adapter
        mAdapter = new CouponListAdapter(mCouponList);
        mRvContent.setAdapter(mAdapter);
    }

    @Subscribe
    public void reload(CouponCashEvent event) {
        loadData();
    }

    public void loadData() {
        /*SnsRepository.getInstance()
                .getCoupons(mType)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new TestSubscriber<>(new IReponse<CouponListBean>() {
                    @Override
                    public void onSuccess(CouponListBean bean) {
                        mHasLoad = true;
                        if (HaiUtils.getSize(bean.couponq_list) == 0) {
                            mSv.showEmpty();
                        } else {
                            mSv.showContent();
                            mListView.setAdapter(new CouponAdapter(getContext(), bean.couponq_list));
                        }
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                    }
                }));*/
        mSwipe.setRefreshing(true);
        PassPortRepository.getInstance()
                .getCouponList(mType)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<CouponListBean>() {
                    @Override
                    public void onSuccess(CouponListBean result) {
                        mHasData = true;
                        if (HaiUtils.getSize(result.couponq_list) == 0) {
                            mSv.showEmpty();
                        } else {
                            mSv.showContent();
                            //                            mListView.setAdapter(new CouponAdapter(getContext(), result.couponq_list));
                            mAdapter.setNewData(result.couponq_list);
                        }
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setRefreshing(false);
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
        mHasData = false;
        mUnbinder.unbind();
    }
}
