package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.order.OrderPagerAdapter;
import com.a55haitao.wwht.data.model.annotation.OrderFragmentType;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.myaccount.order.OrderFragmentFactory;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderFragmentFactory Fragment工厂}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderAllFragment 全部订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderUnPayFragment 未付款订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderUnReceiveFragment 未收货订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderCompleteFragment 已完成订单}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.order.OrderUnDeliverFragment 未发货订单}
 */
public class OrderListActivity extends BaseHasFragmentActivity {
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    @BindView(R.id.title)     DynamicHeaderView mTitle; // 标题
    @BindView(R.id.tab_order) TabLayout         mTabOrder;  // 订单状态tab
    @BindView(R.id.vp_order)  ViewPager         mVpOrder;    // 订单列表ViewPager

    @BindString(R.string.key_type) String KEY_TYPE;

    private String  mCurrentType;
    private Tracker mTracker;       // GA Tracker

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mCurrentType = intent.getStringExtra(KEY_TYPE);
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("订单_" + mCurrentType);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private int getCurrentPosition() {
        int pos = 0;
        switch (mCurrentType) {
            case OrderFragmentType.ALL:
                pos = 0;
                break;
            case OrderFragmentType.UNPAY:
                pos = 1;
                break;
            case OrderFragmentType.UNDELIVER:
                pos = 2;
                break;
            case OrderFragmentType.UNRECEIVE:
                pos = 3;
                break;
            case OrderFragmentType.COMPLETE:
                pos = 4;
                break;
        }
        return pos;
    }

    /**
     * 获取当前页名称
     *
     * @param position 当前页位置
     * @return screenName
     */
    private String getScreenName(int position) {
        String name = "订单_";
        switch (position) {
            case 0:
                name += "全部";
                break;
            case 1:
                name += "待付款";
                break;
            case 2:
                name += "待发货";
                break;
            case 3:
                name += "待收货";
                break;
            case 4:
                name += "已完成";
                break;
        }
        return name;
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        mTitle.setHeadClickListener(() -> {
            // GA 事件
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("联系客服")
                    .setAction("在线客服")
                    .setLabel("订单列表")
                    .build());
            NtalkerUtils.contactNtalker(mActivity);
        });

        mVpOrder.setAdapter(new OrderPagerAdapter(getSupportFragmentManager()));
        mTabOrder.setupWithViewPager(mVpOrder);
        //        mVpOrder.setOffscreenPageLimit(3);
        mVpOrder.setCurrentItem(getCurrentPosition());

        mVpOrder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTracker.setScreenName(getScreenName(position));
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param type    订单类型
     */
    public static void actionStart(Context context, @OrderFragmentType String type) {
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OrderFragmentFactory.clearFragments();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
