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
import com.a55haitao.wwht.adapter.firstpage.FavorableAdapter;
import com.a55haitao.wwht.data.interfaces.ILoad;
import com.a55haitao.wwht.data.model.entity.TabFavorableBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import tom.ybxtracelibrary.YbxTrace;

/**
 * Created by a55 on 2017/4/11.
 */

public class FavorableFragment extends BaseFragment implements ILoad {

    private MultipleStatusView msView;
    private SwipeRefreshLayout swipeView;
    private RecyclerView       mListView;
    private int                tab_id;
    private int     page      = -1;
    private double  allPage   = -1;
    private boolean isLoading = false;
    private HomeRepository   mReposity;
    private FavorableAdapter mAdaptrer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab_id = getArguments().getInt("tab_id");
        mReposity = HomeRepository.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_favorable, container, false);
        msView = (MultipleStatusView) view.findViewById(R.id.msView);
        msView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = -1;
                swipeView.setRefreshing(true);
                requestContent();
            }
        });
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipeView);
        mListView = (RecyclerView) view.findViewById(R.id.content_view);
        mAdaptrer = new FavorableAdapter(mActivity, null);
        mAdaptrer.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mListView);
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdaptrer);
        mListView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addAnalysActMatchUri(mActivity, TraceUtils.PageCode_OfficalSale, TraceUtils.PositionCode_OfficialSale, mAdaptrer.getItem(position).uri);
                YbxTrace.getInstance().event(mActivity, ((BaseActivity)mActivity).pref, ((BaseActivity)mActivity).prefh, ((BaseActivity)mActivity).purl, ((BaseActivity)mActivity).purlh, "", TraceUtils.PositionCode_OfficialSale, TraceUtils.Event_Category_Click, "", null, "");

                HaiUriMatchUtils.matchUri(mActivity, mAdaptrer.getItem(position).uri);
            }
        });

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = -1;
                requestContent();
            }
        });
        swipeView.setRefreshing(true);
        page = -1;
        requestContent();
        return view;

    }

    public static FavorableFragment newInstance(int tab_id) {
        FavorableFragment fragment = new FavorableFragment();
        Bundle            bundle   = new Bundle();
        bundle.putInt("tab_id", tab_id);
        fragment.setArguments(bundle);
        return fragment;
    }


    public void requestContent() {
        //        if (hasMore() && !isLoading) {
        mReposity.getFavorableById(page, tab_id)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<TabFavorableBean>() {
                    @Override
                    public void onStart() {
                        isLoading = true;
                    }

                    @Override
                    public void onSuccess(TabFavorableBean bean) {
                        page = bean.page;
                        allPage = bean.allpage;

                        if (bean.favorables != null && bean.favorables.size() > 0) {

                            //                            if (page == 1) {
                            //                            }
                            msView.showContent();

                            //                            if (mAdaptrer == null) {
                            //
                            //                                if (hasMore()) {
                            //                                    mAdaptrer.loadMoreComplete();
                            //                                } else {
                            //                                    if (bean.favorables.size() > 2) {
                            //                                        mAdaptrer.loadMoreEnd();
                            //                                    } else {
                            //                                        mAdaptrer.setEnableLoadMore(false);
                            //                                    }
                            //                                }
                            //                            } else {
                            if (page == 1) {
                                mAdaptrer.setNewData(bean.favorables);
                                if (hasMore()) {
                                    mAdaptrer.loadMoreComplete();
                                } else {
                                    if (bean.favorables.size() > 2) {
                                        mAdaptrer.loadMoreEnd();
                                    } else {
                                        mAdaptrer.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                mAdaptrer.addData(bean.favorables);
                                if (hasMore()) {
                                    mAdaptrer.loadMoreComplete();
                                } else {
                                    mAdaptrer.loadMoreEnd();
                                }
                            }
                            //                                mAdaptrer.notifyDataSetChanged();
                            //                            }
                        } else {
                            if (page == 1) {
                                msView.showEmpty();
                            }
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

    @Override
    public boolean hasMore() {
        return page == -1 || page < allPage;
    }

    @Override
    public void loadMore() {
        if (hasMore())
            requestContent();
    }
}
