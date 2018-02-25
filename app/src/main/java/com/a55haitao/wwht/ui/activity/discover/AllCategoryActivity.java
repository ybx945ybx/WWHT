package com.a55haitao.wwht.ui.activity.discover;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.AllCategoryAdapter;
import com.a55haitao.wwht.adapter.category.HotCategoryParentAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.model.result.AllCategoryResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.ShoppingCartActivity;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.PinnedHeaderListView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 全部分类页面
 */
public class AllCategoryActivity extends BaseNoFragmentActivity {

    @BindView(R.id.categoryLeftListView)  ListView             leftListView;
    @BindView(R.id.msv)                   MultipleStatusView   msView;
    @BindView(R.id.categoryRightListView) PinnedHeaderListView rightListView;
    @BindView(R.id.carCountTxt)           TextView             carCountTxt;

    private ArrayList<AllCategoryResult> data;
    private AllCategoryAdapter           leftListAdapter;
    private HotCategoryParentAdapter     rightListAdapter;

    private boolean isUserTouch;                // 标志的滑动状态,true为用户滑动,false为代码控制滑动
    private Tracker mTracker;                   // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initData();

    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void initVars() {
        isUserTouch = false;

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("全部分类");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews() {
        msView.setOnRetryClickListener(v -> loadData());

        rightListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentHeadPositoon = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (!isUserTouch) {
                    return;
                }

                currentHeadPositoon = firstVisibleItem / 2;

                for (int i = 0; i < data.size(); i++) {
                    data.get(i).isChecked = (i == currentHeadPositoon);
                }
                leftListAdapter.notifyDataSetChanged();
                leftListView.setSelection(currentHeadPositoon);
            }
        });

        rightListView.setOnTouchListener((v, event) -> {
            isUserTouch = true;
            return false;
        });

    }

    private void initData() {
        loadData();
        getShoppingCartBadge();
    }

    /**
     * 获取数据
     */
    private void loadData() {
        msView.showLoading();
        ProductRepository.getInstance()
                .getAllCategories()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<ArrayList<AllCategoryResult>>() {
                    @Override
                    public void onSuccess(ArrayList<AllCategoryResult> allCategoryResults) {
                        data = allCategoryResults;
                        setView();
                        msView.showContent();
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
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

    private void setView() {
        if (data != null) {
            data.get(0).isChecked = true;
            leftListAdapter = new AllCategoryAdapter(this, data);
            leftListView.setAdapter(leftListAdapter);

            rightListAdapter = new HotCategoryParentAdapter(this, data);
            rightListView.setAdapter(rightListAdapter);
        }
    }

    @OnClick({R.id.backImg, R.id.toShopCarImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.toShopCarImg:
                if (!HaiUtils.needLogin(this)) {
                    // 埋点
//                    TraceUtils.addClick(TraceUtils.PageCode_Cart, "", this, TraceUtils.PageCode_AllCategory, TraceUtils.PositionCode_Cart + "", "");

                    //                    TraceUtils.addAnalysAct(TraceUtils.PageCode_Cart, TraceUtils.PageCode_AllCategory, TraceUtils.PositionCode_Cart, "");

                    ShoppingCartActivity.toThisAcivity(this);
                }
                break;
        }
    }

    @OnItemClick(R.id.categoryLeftListView)
    public void onItemClick(int position) {

        isUserTouch = false;

        int size = data.size();
        for (int i = 0; i < size; i++) {
            data.get(i).isChecked = i == position;
        }

        leftListAdapter.notifyDataSetChanged();

        rightListView.setSelection(position * 2);

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
        if (shoppingCartBadgeCnt > 0) {
            carCountTxt.setVisibility(View.VISIBLE);
            carCountTxt.setText(String.valueOf(shoppingCartBadgeCnt));
        } else {
            carCountTxt.setVisibility(View.INVISIBLE);
        }
    }

}
