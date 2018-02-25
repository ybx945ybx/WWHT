package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.common.ViewPagerAdapter;
import com.a55haitao.wwht.data.model.result.TrackInfoResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.fragment.shoppingcart.TransferDetailFragment;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 物流详情页面
 */
public class TransferDetailActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)           DynamicHeaderView  mTitle;     // 标题
    @BindView(R.id.msView)          MultipleStatusView mMultipleStatusView;
    @BindView(R.id.expressNameTxt)  TextView           mExpressNameTxt;
    @BindView(R.id.expressNoTxt)    TextView           mExpressNoTxt;
    @BindView(R.id.expressLayout_1) LinearLayout       expressLayout_1;
    @BindView(R.id.expressLayout_2) LinearLayout       expressLayout_2;
    @BindView(R.id.tabLayout)       TabLayout          mTabLayout;
    @BindView(R.id.viewPager)       ViewPager          mViewPager;

    private String  mOrderId;
    private String  mTrackNumber;
    private Tracker mTracker;       // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_detail);
        ButterKnife.bind(this);

        initVars();
        initViews();
        initdata();

    }

    @Override
    protected String getActivityTAG() {
        return  TAG + "?" + "id=" + mOrderId;
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            mOrderId = intent.getStringExtra("param1");
            mTrackNumber = intent.getStringExtra("param2");
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("物流详情");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews() {
        mMultipleStatusView.setOnRetryClickListener(v -> requestTransferData());
        mTitle.setHeadClickListener(() -> NtalkerUtils.contactNtalker(mActivity));

    }

    private void initdata() {
        requestTransferData();
    }

    private void requestTransferData() {
        mMultipleStatusView.showLoading();
        OrderRepository.getInstance()
                .getTrackInfo(mOrderId, mTrackNumber)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<TrackInfoResult>() {
                    @Override
                    public void onSuccess(TrackInfoResult result) {
                        if (HaiUtils.getSize(result.package_list) == 0) {
                            mMultipleStatusView.showEmpty();
                            return;
                        }

                        TrackInfoResult.PackageListBean packageListBean = result.package_list.get(0);

                        if (packageListBean.track_info == null) {
                            mMultipleStatusView.showEmpty();
                            return;
                        }

                        ArrayList<BaseFragment> mFragments = new ArrayList<>();
                        ArrayList<String>       mTabs      = new ArrayList<>();
                        for (int i = 0; i < result.package_list.size(); i++) {
                            mTabs.add("包裹" + (i + 1));
                            mFragments.add(TransferDetailFragment.instance(result, i));
                        }
                        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTabs));
                        mTabLayout.setupWithViewPager(mViewPager);

                        mMultipleStatusView.showContent();
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mMultipleStatusView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    /**
     * 跳转到本页
     *
     * @param context  context
     * @param orderId  订单号
     * @param packName 包裹名
     */
    public static void toThisActivity(Context context, String orderId, String packName) {
        Intent intent = new Intent(context, TransferDetailActivity.class);
        intent.putExtra("param1", orderId);
        intent.putExtra("param2", packName);
        context.startActivity(intent);
    }
}
