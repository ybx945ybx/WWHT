package com.a55haitao.wwht.ui.activity.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.FlashSaleAdapter;
import com.a55haitao.wwht.data.interfaces.IReponse;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.result.FlashSaleResult;
import com.a55haitao.wwht.data.net.TestSubscriber;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import tom.ybxtracelibrary.YbxTrace;

/**
 * Created by a55 on 2017/3/17.
 * 限时抢购商品列表页面
 */

public class FlashSaleActivity extends BaseNoFragmentActivity{

    private MultipleStatusView    msView;
    private HaiSwipeRefreshLayout mSwipe;
    private RecyclerView          rycvFlashSale;
    private FlashSaleAdapter      mAdapter;
    private Tracker               mTracker;      // GA Tracker

    private int page = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sale);
        ButterKnife.bind(this);

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("限时抢购");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        initViews();
        initEvents();
        initData();

    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void initViews() {
        msView = (MultipleStatusView) findViewById(R.id.msView);
        mSwipe = (HaiSwipeRefreshLayout) findViewById(R.id.swipe);
        rycvFlashSale = (RecyclerView) findViewById(R.id.content_view);
        rycvFlashSale.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FlashSaleAdapter(new ArrayList<>(), this);
        rycvFlashSale.setAdapter(mAdapter);

    }

    private void initEvents() {
        msView.setOnRetryClickListener(v -> {
            page = -1;
            mSwipe.setRefreshing(true);
            getData();
        });
        mSwipe.setOnRefreshListener(() -> {
            page = -1;
            getData();
        });
        rycvFlashSale.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_ProductDetail, ((ProductBaseBean) adapter.getData().get(position)).spuid, mActivity, TraceUtils.PageCode_FlashSale, TraceUtils.PositionCode_FlashSale + "", "");
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Goods_Id, ((ProductBaseBean) adapter.getData().get(position)).spuid);
                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_FlashSale, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                ProductMainActivity.toThisAcivity(FlashSaleActivity.this, ((ProductBaseBean) adapter.getData().get(position)).spuid, ((ProductBaseBean) adapter.getData().get(position)).name);
            }
        });
        mAdapter.setOnLoadMoreListener(() -> getData());

    }

    private void initData() {
        mSwipe.setRefreshing(true);
        getData();
    }

    private void getData() {
        HomeRepository.getInstance()
                .getFlashSaleList(page > 0 ? page + 1 : 1)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new TestSubscriber<>(new IReponse<FlashSaleResult>() {
                    @Override
                    public void onSuccess(FlashSaleResult result) {
                        page = result.page;
                        if (page == 1) {
                            if (result.products != null && result.products.size() > 0) {
                                msView.showContent();
                                mAdapter.setNewData(result.products);
                            } else {
                                msView.showEmpty();
                            }
                        } else {
                            mAdapter.addData(result.products);
                        }

                        if (page == result.allpage) {
                            if (page == 1) {
                                mAdapter.loadMoreEnd(true);
                            } else {
                                mAdapter.loadMoreEnd();
                            }
                        } else {
                            mAdapter.loadMoreComplete();
                                                }
                    }

                    @Override
                    public void onFinish() {
                        mHasLoad = true;
                        mSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        mSwipe.setRefreshing(false);
                    }
                }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
        dismissProgressDialog();

    }

}
