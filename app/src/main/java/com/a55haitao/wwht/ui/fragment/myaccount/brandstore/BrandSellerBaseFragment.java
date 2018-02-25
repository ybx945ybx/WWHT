package com.a55haitao.wwht.ui.fragment.myaccount.brandstore;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.common.BrandSellerListAdapter;
import com.a55haitao.wwht.data.event.BrandStoreFollowEvent;
import com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.result.GetFollowBrandStoreResult;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.myaccount.BrandAndSellerActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 关注 & 粉丝 Fragment
 * {@link BrandFragment 品牌}
 * {@link SellerFragment 商家}
 * Created by 陶声 on 16/7/8.
 */
public abstract class BrandSellerBaseFragment extends BaseFragment {
    @BindView(R.id.content_view) RecyclerView       mRvContent;  // 内容
    @BindView(R.id.msv)          MultipleStatusView mSv;         // 多状态布局
    @BindView(R.id.swipe_layout) SwipeRefreshLayout mSwipe;      // 下拉刷新

    @BindColor(R.color.color_swipe)            int colorSwipe;           // 下拉刷新颜色
    //    @BindDrawable(R.mipmap.dot_line)           Drawable DIVIDER_BG;           // 分割线
    @BindDimen(R.dimen.brand_store_logo_width) int LOGO_SIZE;           // logo尺寸

    protected Unbinder                                 mUnbinder;
    protected int                                      mCurrentPage;   // 当前页数
    protected int                                      mAllPage;       // 总页数
    protected BrandSellerListAdapter                   mAdapter;
    protected int                                      mUserId;
    protected List<GetFollowBrandStoreResult.DataBean> mBrandSellerList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brand_seller, null, false);
        mUnbinder = ButterKnife.bind(this, v);
        initVariables();
        initViews(v, savedInstanceState);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }


    /**
     * 初始化变量
     */
    public void initVariables() {
        mCurrentPage = 1;
        mAllPage = 1;
        mUserId = ((BrandAndSellerActivity) mActivity).getUserId();
        mBrandSellerList = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 初始化布局
     */
    protected void initViews(View v, Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        // adapter
        mAdapter = new BrandSellerListAdapter(mBrandSellerList, BrandSellerFragmentType.BRAND, LOGO_SIZE);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 跳转到品牌/商家主页

                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_BrandOrSiteDetail, mAdapter.getData().get(position).name, mActivity, TraceUtils.PageCode_MyFollowBrandSite, getPageType() == BrandSellerFragmentType.BRAND ? TraceUtils.PositionCode_Brand + "" : TraceUtils.PositionCode_Store + "", "");
//                YbxTrace.getInstance().event(mActivity, TAG + tab_id, "", TraceUtils.PositionCode_OfficialSale, TraceUtils.Event_Category_Click, "", null, "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_BrandOrSiteDetail, TraceUtils.PageCode_MyFollowBrandSite, getPageType() == BrandSellerFragmentType.BRAND ? TraceUtils.PositionCode_Brand : TraceUtils.PositionCode_Store, mAdapter.getData().get(position).name);

                SiteActivity.toThisActivity(mActivity, mAdapter.getData().get(position).name, getPageType());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 关注品牌/商家
                switch (view.getId()) {
                    case R.id.chk_follow_brand_store:
                        CheckBox chkFollowBrandStore = (CheckBox) view;

                        if (HaiUtils.needLogin(mActivity)) {
                            chkFollowBrandStore.setChecked(!chkFollowBrandStore.isChecked());
                            return;
                        }
                        // 关注品牌/商家请求
                        requestFollowBrandStore(chkFollowBrandStore, mAdapter.getData().get(position).nameen);
                        break;
                }
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
                    requestBrandStoreList();
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

    @PageType
    protected abstract int getPageType();

    /**
     * 关注请求
     *
     * @param chkFollowBrandStore checkbox
     * @param nameen              英文名
     */
    protected abstract void requestFollowBrandStore(CheckBox chkFollowBrandStore, String nameen);

    /**
     * 加载数据
     */
    protected void loadData() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;
        requestBrandStoreList();
    }

    /**
     * 网络请求
     */
    protected abstract void requestBrandStoreList();

    @Subscribe
    public void onBrandStoreFollowEvent(BrandStoreFollowEvent event) {
        mAdapter.setEnableLoadMore(false);
        mSwipe.post(() -> {
            mSwipe.setRefreshing(true);
            loadData();
        });
    }


    @Override
    public void onStop() {
        mSwipe.setRefreshing(false);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
