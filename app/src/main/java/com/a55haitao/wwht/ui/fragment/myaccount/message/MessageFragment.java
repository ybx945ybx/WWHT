package com.a55haitao.wwht.ui.fragment.myaccount.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.MessageListAdapter;
import com.a55haitao.wwht.data.model.entity.MessageBean;
import com.a55haitao.wwht.data.model.result.MessageListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.MessageRedirectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;

/**
 * 消息 - 消息Fragment
 * <p>
 * Created by 陶声 on 16/7/13.
 */
public class MessageFragment extends MessageBaseFragment {
    @BindView(R.id.swipe)        SwipeRefreshLayout mSwipe;      // 下拉刷新
    @BindView(R.id.msv)          MultipleStatusView mSv;         // 多状态布局
    @BindView(R.id.content_view) RecyclerView       mRvContent;  // 内容

    @BindColor(R.color.color_swipe) int colorSwipe;    // 下拉刷新颜色

    private int                mCurrentPage;
    private int                mAllPage;
    private MessageListAdapter mAdapter;
    private List<MessageBean>  mNotifications;

    protected int getLayout() {
        return R.layout.fragment_message;
    }

    public void initVars() {
        mCurrentPage = 1;
        mAllPage = 1;
        mNotifications = new ArrayList<>();
    }

    protected void initViews(View v, Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRvContent.setLayoutManager(layoutManager);
        // adapter
        mAdapter = new MessageListAdapter(mNotifications, mFragment);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 根据消息类型跳转
                MessageBean item = mAdapter.getData().get(position);
                MessageRedirectUtils.redirect(mActivity, item, false);
            }
        });

        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                if (mAdapter.getData().size() < PAGE_SIZE) {
                    mAdapter.loadMoreEnd(true);
                } else if (mCurrentPage < mAllPage) {
                    mCurrentPage++;
                    mSwipe.setEnabled(false);
                    loadNotificationList();
                }
            });
        });
        // 刷新
        mSwipe.setOnRefreshListener(() -> {
            mAdapter.setEnableLoadMore(false);
            loadData();
        });
        // 失败重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    /**
     * 加载数据
     */
    @Override
    public void loadData() {
        mCurrentPage = 1;
        mSwipe.setRefreshing(true);
        loadNotificationList();
    }

    /**
     * 网络请求
     */
    private void loadNotificationList() {
        SnsRepository.getInstance()
                .getMessageList(mCurrentPage)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<MessageListResult>() {
                    @Override
                    public void onSuccess(MessageListResult result) {
                        if (result.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.notifications);
                            } else {
                                mAdapter.addData(result.notifications);
                                if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= result.count) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        }
                        mHasData = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasData);
                        return mHasData;
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
        super.onStop();
        mSwipe.setRefreshing(false);
    }
}
