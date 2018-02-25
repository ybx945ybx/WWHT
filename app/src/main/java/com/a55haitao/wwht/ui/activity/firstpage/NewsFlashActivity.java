package com.a55haitao.wwht.ui.activity.firstpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.NewsFlashAdapter;
import com.a55haitao.wwht.data.model.entity.NewsFlashBean;
import com.a55haitao.wwht.data.model.result.NewsFlashResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OtherRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;
import java.util.HashMap;

import tom.ybxtracelibrary.YbxTrace;

/**
 * Created by a55 on 2017/3/17.
 * 优惠快报列表页面
 */

public class NewsFlashActivity extends BaseNoFragmentActivity implements View.OnClickListener {

    private ImageView             ivBack;
    private HaiSwipeRefreshLayout swrfyt;
    private MultipleStatusView    msView;
    private RecyclerView          rycvNewsFlash;
    private NewsFlashAdapter      mAdapter;
    private int page = -1;
    private Tracker mTracker;   // GA Tarcker

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_flash);

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("优惠快报");
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
        ivBack = (ImageView) findViewById(R.id.headerLeftImg);
        swrfyt = (HaiSwipeRefreshLayout) findViewById(R.id.swipe);
        msView = (MultipleStatusView) findViewById(R.id.msView);
        rycvNewsFlash = (RecyclerView) findViewById(R.id.content_view);
        rycvNewsFlash.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsFlashAdapter(new ArrayList<>(), this);
        rycvNewsFlash.setAdapter(mAdapter);
    }

    private void initEvents() {
        ivBack.setOnClickListener(this);
        swrfyt.setOnRefreshListener(() -> {
            page = -1;
            getData();
        });
        msView.setOnRetryClickListener(v -> {
            page = -1;
            swrfyt.setRefreshing(true);
            getData();
        });
        rycvNewsFlash.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addAnalysActMatchUri(mActivity, TraceUtils.PageCode_Express, TraceUtils.PositionCode_Express, ((NewsFlashBean) adapter.getData().get(position)).url);
                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Express, TraceUtils.Event_Category_Click, "", null, "");

                HaiUriMatchUtils.matchUri(NewsFlashActivity.this, ((NewsFlashBean) adapter.getData().get(position)).url);

                OtherRepository.getInstance()
                        .updateLettersCount(((NewsFlashBean) adapter.getData().get(position)).id)
                        .subscribe(new DefaultSubscriber<Object>() {
                            @Override
                            public void onSuccess(Object result) {

                            }

                            @Override
                            public void onFinish() {
                            }
                        });

                // 点击后更新浏览次数
                rycvNewsFlash.postDelayed(() -> {
                    ((NewsFlashBean) adapter.getData().get(position)).pv_count++;
                    mAdapter.notifyDataSetChanged();
                }, 1000);
            }
        });
        mAdapter.setOnLoadMoreListener(() -> getData());

    }

    private void initData() {
        swrfyt.setRefreshing(true);
        getData();
    }

    // 获取优惠快报列表
    private void getData() {
        OtherRepository.getInstance()
                .getLetters(page > 0 ? page + 1 : 1)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<NewsFlashResult>() {
                    @Override
                    public void onSuccess(NewsFlashResult newsFlashResult) {
                        page = newsFlashResult.page;
                        if (page == 1) {
                            if (newsFlashResult.letters != null && newsFlashResult.letters.size() > 0) {
                                msView.showContent();
                                mAdapter.setNewData(newsFlashResult.letters);
                            } else {
                                msView.showEmpty();
                            }
                        } else {
                            mAdapter.addData(newsFlashResult.letters);
                        }

                        if (page == newsFlashResult.allpage) {
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
                        swrfyt.setRefreshing(false);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
        }
    }

}
