package com.a55haitao.wwht.ui.activity.discover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.AllBrandOrSellerAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.model.result.AllBrandOrSellerBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.ShoppingCartActivity;
import com.a55haitao.wwht.ui.view.EasyRecyclerViewSidebar;
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

/**
 * 全部商家／品牌列表页面
 */

public class AllBrandOrSellerActivity extends BaseNoFragmentActivity implements EasyRecyclerViewSidebar.OnTouchSectionListener {

    @BindView(R.id.pinnerListView)      PinnedHeaderListView    listView;           // ListView
    @BindView(R.id.msv)                 MultipleStatusView      msView;             // ListView
    @BindView(R.id.titleTxt)            TextView                titleTxt;           // Title
    @BindView(R.id.section_sidebar)     EasyRecyclerViewSidebar slidebar;           // 侧边栏
    @BindView(R.id.section_floating_tv) TextView                slideFloatingTxt;   // 悬浮提示框
    @BindView(R.id.carCountTxt)         TextView                carCountTxt;        // 购物车数量

    private AllBrandOrSellerAdapter         adapter;  //适配器
    private ArrayList<AllBrandOrSellerBean> mData;    //数据
    private int                             type;
    private Tracker                         mTracker; // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_brand_or_seller);
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
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", -1);
        }

        if (type == PageType.BRAND) {
            titleTxt.setText("全部品牌");
        } else if (type == PageType.SELLER) {
            titleTxt.setText("全部商家");
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(type == PageType.BRAND ? "全部品牌" : "全部商家");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews() {
        slidebar.setFloatView(slideFloatingTxt);
        slidebar.setOnTouchSectionListener(this);

        msView.setOnRetryClickListener(v -> {
            requestData(type);
        });
    }

    private void initData() {
        requestData(type);
        getShoppingCartBadge();
    }

    /**
     * 获取数据
     *
     * @param type
     */
    private void requestData(int type) {
        msView.showLoading();
        ProductRepository.getInstance()
                .getAllBrandSeller(type)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<ArrayList<AllBrandOrSellerBean>>() {
                    @Override
                    public void onSuccess(ArrayList<AllBrandOrSellerBean> result) {
                        mData = result;

                        ArrayList<EasyRecyclerViewSidebar.EasySection> list = new ArrayList<>();
                        for (AllBrandOrSellerBean bean : mData) {
                            if (bean.index != null)
                                list.add(new EasyRecyclerViewSidebar.EasySection(bean.index));
                        }
                        slidebar.setSections(list);

                        adapter = new AllBrandOrSellerAdapter(mActivity, mData, type == PageType.BRAND);
                        listView.setAdapter(adapter);

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

    @OnClick(R.id.toShopCarImg)
    public void click(View view) {
        if (!HaiUtils.needLogin(this)) {
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_Cart, "", this, TraceUtils.PageCode_AllBrandOrSite, TraceUtils.PositionCode_Cart + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_Cart, TraceUtils.PageCode_AllBrandOrSite, TraceUtils.PositionCode_Cart, "");

            startActivity(new Intent(this, ShoppingCartActivity.class));
            overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
        }
    }

    @OnClick(R.id.backImg)
    public void backClick() {
        onBackPressed();
    }

    @Subscribe
    public void cartCountChange(ShoppingCartCntChangeEvent event) {
        setCartBadgeView(event.shoppingCartBadgeCnt);
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

    private void setCartBadgeView(int shoppingCartBadgeCnt) {
        if (shoppingCartBadgeCnt > 0) {
            carCountTxt.setVisibility(View.VISIBLE);
            carCountTxt.setText(String.valueOf(shoppingCartBadgeCnt));
        } else {
            carCountTxt.setVisibility(View.INVISIBLE);
        }
    }

    public static void toThisActivity(Activity activity, int type) {
        Intent intent = new Intent(activity, AllBrandOrSellerActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    public void onTouchLetterSection(int sectionIndex, EasyRecyclerViewSidebar.EasySection letterSection) {
        slideFloatingTxt.setText(letterSection.letter);

        //计算正确的位置
        int itemId = (int) adapter.getItemId(sectionIndex, 0);
        listView.setSelection(itemId);

    }

}
