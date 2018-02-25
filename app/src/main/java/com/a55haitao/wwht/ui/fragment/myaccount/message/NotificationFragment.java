package com.a55haitao.wwht.ui.fragment.myaccount.message;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.NotificationListAdapter;
import com.a55haitao.wwht.data.model.entity.MessageBean;
import com.a55haitao.wwht.data.model.result.NotificationListResult;
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
import butterknife.BindDrawable;
import butterknife.BindView;

/**
 * 消息 - 通知Fragment
 * <p>
 * Created by 陶声 on 16/7/13.
 */
public class NotificationFragment extends MessageBaseFragment {
    @BindView(R.id.content_view) RecyclerView       mRvContent;  // 内容
    @BindView(R.id.msv)          MultipleStatusView mSv;         // 多状态布局
    @BindView(R.id.swipe)        SwipeRefreshLayout mSwipe;      // 下拉刷新

    @BindColor(R.color.color_swipe)  int      colorSwipe;      // 下拉刷新颜色
    @BindDrawable(R.mipmap.dot_line) Drawable DIVIDER_BG;      // 分割线

    private int                     mCurrentPage;
    private int                     mAllPage;
    private NotificationListAdapter mAdapter;           // 消息列表Adapter
    private List<MessageBean>       mNotificationList;  // 消息列表

    @Override
    protected int getLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void initVars() {
        mAllPage = 1;
        mNotificationList = new ArrayList<>();
    }

    @Override
    protected void initViews(View v, Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        // adapter
        mAdapter = new NotificationListAdapter(mNotificationList);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 根据消息类型跳转页面
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
                    requestGetNotificationList();
                }
            });
        });
        // 刷新
        mSwipe.setOnRefreshListener(() -> {
            mAdapter.setEnableLoadMore(false);
            loadData();
        });
        // 重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    /**
     * 加载数据
     */
    @Override
    public void loadData() {
        mCurrentPage = 1;
        mSwipe.setRefreshing(true);
        requestGetNotificationList();
    }

    /**
     * 网络请求
     */
    private void requestGetNotificationList() {
        SnsRepository.getInstance()
                .getNotificationList(mCurrentPage)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<NotificationListResult>() {
                    @Override
                    public void onSuccess(NotificationListResult result) {
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
                                if (mNotificationList.size() > PAGE_SIZE && mNotificationList.size() >= result.count) {
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
