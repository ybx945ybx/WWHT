package com.a55haitao.wwht.ui.activity.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.HotTagListAdapter;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.data.model.result.HotTagListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门标签列表
 */
public class HotTagActivity extends BaseActivity {

    @BindView(R.id.content_view) RecyclerView          mRvContent;  // 内容
    @BindView(R.id.msv_layout)   MultipleStatusView    mSv;         // 多状态布局
    @BindView(R.id.swipe)        HaiSwipeRefreshLayout mSwipe;      // 刷新

    @BindColor(R.color.color_swipe) int colorSwipe;   // 下拉刷新颜色

    private int                            mCurrentPage;
    private int                            mAllPage;
    private HotTagListAdapter              mAdapter;
    private List<HotTagListResult.RetBean> mTagListData;
    private Tracker                        mTracker;        // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_tag);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    private void initVariables() {
        mCurrentPage = 1;
        mAllPage = 1;
        mTagListData = new ArrayList<>();
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("热门标签列表");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity));
        //        mRvContent.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        // Adapter
        mAdapter = new HotTagListAdapter(mActivity, mTagListData);
        mRvContent.setAdapter(mAdapter);
        // Listener
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 跳转到标签帖子页面
                TagBean tag = mAdapter.getData().get(position).tag;

                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HotTagDetail, tag.id + "", HotTagActivity.this, TraceUtils.PageCode_HotTagList, TraceUtils.PositionCode_Tag + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HotTagDetail, TraceUtils.PageCode_HotTagList, TraceUtils.PositionCode_Tag, tag.id + "");

                TagDetailActivity.toThisActivity(mActivity, tag.id, tag.name, tag.is_hot);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int subPos = 0;
                switch (view.getId()) {
                    case R.id.img_pic1:
                        break;
                    case R.id.img_pic2:
                        subPos = 1;
                        break;
                    case R.id.img_pic3:
                        subPos = 2;
                        break;
                }
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_PostDetail, mAdapter.getData().get(position).posts.get(subPos).post_id + "", HotTagActivity.this, TraceUtils.PageCode_HotTagList, TraceUtils.PositionCode_Post + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_PostDetail, TraceUtils.PageCode_HotTagList, TraceUtils.PositionCode_Post, mAdapter.getData().get(position).posts.get(subPos).post_id + "");

                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mAdapter.getData().get(position).posts.get(subPos).post_id);
                startActivity(intent);
            }
        });
        // 下拉刷新颜色
        mSwipe.setColorSchemeColors(colorSwipe);
        // 刷新
        mSwipe.setOnRefreshListener(this::refreshData);
        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                mRvContent.post(() -> {
                    if (mAdapter.getData().size() < PAGE_SIZE) {
                        mAdapter.loadMoreEnd(true);
                    } else if (mCurrentPage < mAllPage) {
                        mCurrentPage++;
                        mSwipe.setEnabled(false);
                        requestHotTagList();
                    }
                });
            });
        });
        // 重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;
        requestHotTagList();
    }

    /**
     * 热门标签列表网络请求
     */
    private void requestHotTagList() {
        SnsRepository.getInstance()
                .getPostHotTagList(mCurrentPage)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<HotTagListResult>() {
                    @Override
                    public void onSuccess(HotTagListResult result) {
                        if (result.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.ret);
                            } else {
                                mAdapter.addData(result.ret);
                                if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= result.count) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
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

    @Override
    public void onStop() {
        mSwipe.setRefreshing(false);
        super.onStop();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}


