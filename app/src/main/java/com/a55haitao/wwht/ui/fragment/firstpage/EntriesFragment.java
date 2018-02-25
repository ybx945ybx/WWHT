package com.a55haitao.wwht.ui.fragment.firstpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.EntriesAdapter;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

/**
 * Created by a55 on 2017/4/11.
 */

public class EntriesFragment extends BaseFragment {

    private MultipleStatusView msView;
    private SwipeRefreshLayout swipeView;
    private RecyclerView       mRecyclerView;
    private int                tab_id;
    private int     page      = -1;
    private double  allPage   = -1;
    private boolean isLoading = false;
    private HomeRepository mReposity;
    private EntriesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab_id = getArguments().getInt("tab_id");
        mReposity = HomeRepository.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_entries, container, false);
        msView = (MultipleStatusView) view.findViewById(R.id.msView);
        msView.setOnRetryClickListener(v -> {
            page = -1;
            swipeView.setRefreshing(true);
            requestContent();
        });
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipeView);
        swipeView.setOnRefreshListener(() -> {
            page = -1;
            requestContent();
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.content_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new EntriesAdapter(mActivity, null);
        adapter.setTag(TAG + tab_id);
        adapter.setOnLoadMoreListener(() -> loadMoreData());
        mRecyclerView.setAdapter(adapter);

        swipeView.setRefreshing(true);
        page = -1;
        requestContent();

        return view;

    }

    public static EntriesFragment newInstance(int tab_id) {
        EntriesFragment fragment = new EntriesFragment();
        Bundle          bundle   = new Bundle();
        bundle.putInt("tab_id", tab_id);
        fragment.setArguments(bundle);
        return fragment;
    }


    // 加载更多
    private void loadMoreData() {
        if (hasMore()) {
            mReposity.getEntriesById(page, tab_id)
                    .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                    .subscribe(new DefaultSubscriber<TabEntryBean>() {
                        @Override
                        public void onStart() {
                            isLoading = true;
                        }

                        @Override
                        public void onSuccess(TabEntryBean tabEntryBean) {

                            page = tabEntryBean.page;
                            allPage = tabEntryBean.allpage;
                            adapter.addData(tabEntryBean.entries);
                            if (hasMore()) {
                                adapter.loadMoreComplete();
                            } else {
                                adapter.loadMoreEnd();
                            }
                        }

                        @Override
                        public void onFinish() {
                            isLoading = false;
                        }
                    });
        }
    }

    // 获取接口数据
    public void requestContent() {
//        if (!isLoading) {
            mReposity.getEntriesById(page, tab_id)
                    .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                    .subscribe(new DefaultSubscriber<TabEntryBean>() {
                        @Override
                        public void onStart() {
                            isLoading = true;
                        }

                        @Override
                        public void onSuccess(TabEntryBean tabEntryBean) {
                            if (tabEntryBean.entries != null && tabEntryBean.entries.size() > 0) {
                                msView.showContent();
                                page = tabEntryBean.page;
                                allPage = tabEntryBean.allpage;

                                adapter.setNewData(tabEntryBean.entries);
                                if (hasMore()) {
                                    adapter.loadMoreComplete();
                                } else {
                                    if (tabEntryBean.entries.size() > 1) {
                                        adapter.loadMoreEnd();
                                    } else {
                                        adapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                msView.showEmpty();
                            }
                            mHasData = true;
                        }

                        @Override
                        public void onFinish() {
                            isLoading = false;
                            swipeView.setRefreshing(false);
                        }

                        @Override
                        public boolean onFailed(Throwable e) {
                            showFailView(msView, e, mHasData);
                            return mHasData;
                        }
                    });
//        }

    }

    private boolean hasMore() {
        return page == -1 || page < allPage;
    }

}
