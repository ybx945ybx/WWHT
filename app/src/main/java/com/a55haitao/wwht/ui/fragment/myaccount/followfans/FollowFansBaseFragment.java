package com.a55haitao.wwht.ui.fragment.myaccount.followfans;


import android.graphics.drawable.Drawable;
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
import com.a55haitao.wwht.adapter.myaccount.UserListAdapter;
import com.a55haitao.wwht.data.event.UserFollowEvent;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.myaccount.FollowAndFansActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 关注 & 粉丝 Fragment
 * {@link FollowFragment 关注页面}
 * {@link FansFragment 粉丝页面}
 * Created by 陶声 on 16/7/8.
 */
public abstract class FollowFansBaseFragment extends BaseFragment {

    @BindView(R.id.content_view)     RecyclerView       mRvContent;  // 内容
    @BindView(R.id.msv)              MultipleStatusView mSv;         // 多状态布局
    @BindView(R.id.swipe)            SwipeRefreshLayout mSwipe;      // 下拉刷新
    @BindColor(R.color.color_swipe)  int                colorSwipe;   // 下拉刷新颜色
    @BindDrawable(R.mipmap.dot_line) Drawable           DIVIDER_BG;   // 下拉刷新颜色
    @BindDimen(R.dimen.avatar_big)   int                AVATAR_SIZE;  // 头像尺寸

    protected Unbinder           mUnbinder;
    protected int                mCurrentPage;   // 当前页数
    protected int                mAllPage;       // 总页数
    protected int                mUserId;
    protected UserListAdapter    mAdapter;
    protected List<UserListBean> mUserList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_follow_fans, null, false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        mCurrentPage = 1;
        mAllPage = 1;
        mUserId = ((FollowAndFansActivity) mActivity).getUserId();
        mUserList = new ArrayList<>();
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
        mAdapter = new UserListAdapter(mUserList);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 跳转到他人主页

                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HomePage, mAdapter.getData().get(position).id + "", mActivity, TraceUtils.PageCode_FansOrFollows, TraceUtils.PositionCode_User + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_FansOrFollows, TraceUtils.PositionCode_User, mAdapter.getData().get(position).id + "");

                OthersHomePageActivity.actionStart(mActivity, mAdapter.getData().get(position).id);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 关注用户
                switch (view.getId()) {
                    case R.id.chk_follow_user:
                        CheckBox chkFollowUser = (CheckBox) view;
                        if (HaiUtils.needLogin(mActivity)) {
                            chkFollowUser.setChecked(!chkFollowUser.isChecked());
                        } else {
                            requestFollowUser(chkFollowUser, mAdapter.getData().get(position).id, position);
                        }
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
                    loadUserList();
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
    public void loadData() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;
        loadUserList();
    }

    /**
     * 网络请求
     */
    public abstract void loadUserList();

    /**
     * 关注用户
     *
     * @param chkFollowUser 关注按钮
     * @param id            用户id
     */
    private void requestFollowUser(CheckBox chkFollowUser, int id, int position) {
        SnsRepository.getInstance()
                .followUser(id)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<FollowUserResult>() {
                    @Override
                    public void onSuccess(FollowUserResult result) {
                        if (result.is_following) {
                            ToastUtils.showToast(mActivity, "关注成功");
                        }
                        chkFollowUser.setChecked(result.is_following);

                        UserListBean user = mAdapter.getData().get(position);
                        user.is_following = result.is_following;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        chkFollowUser.setChecked(!chkFollowUser.isChecked());
                        return super.onFailed(e);
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Subscribe
    public void onUserFollowEvent(UserFollowEvent event) {
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSwipe.setRefreshing(false);
    }
}
