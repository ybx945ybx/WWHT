package com.a55haitao.wwht.ui.activity.social;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.UserListAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.UserFollowEvent;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.GetPostLikeUserListResult;
import com.a55haitao.wwht.data.model.result.GetPostSpecialLikesResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.SpecialRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全部点赞页
 */
public class LikeListActivity extends BaseNoFragmentActivity {
    @BindView(R.id.content_view) RecyclerView          mRvContent;      // 全部点赞条目
    @BindView(R.id.ib_cancel)    ImageButton           mIbTitleBack;    // 返回
    @BindView(R.id.swipe)        HaiSwipeRefreshLayout mSwipe;          // 下拉刷新
    @BindView(R.id.msv_layout)   MultipleStatusView    mSv;             // 多布局容器

    @BindString(R.string.key_post_id)            String KEY_POST_ID;
    @BindString(R.string.key_special_id)         String KEY_special_ID;
    @BindString(R.string.key_product_special_id) String KEY_PD_special_ID;

    @BindColor(R.color.color_swipe)    int      colorSwipe;   // 下拉刷新颜色
    @BindDrawable(R.mipmap.dot_line)   Drawable DIVIDER_BG;   // 分割线背景
    @BindDimen(R.dimen.avatar_big)     int      AVATAR_SIZE;  // 头像尺寸
    @BindDimen(R.dimen.padding_medium) int      DIVIDER_PADDING;  // 分割线padding

    private int                mPostId;
    private int                mSpecialId;
    private int                mCurrentPage;
    private int                mAllPage;
    private int                productSpecialId;
    private UserListAdapter    mAdapter;
    private List<UserListBean> mUserList;
    private Tracker            mTracker; // GA Tracker

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mPostId = intent.getIntExtra(KEY_POST_ID, -1);
            mSpecialId = intent.getIntExtra(KEY_special_ID, -1);
            productSpecialId = intent.getIntExtra(KEY_PD_special_ID, -1);
        }
        mCurrentPage = 1;
        mAllPage = 1;
        mUserList = new ArrayList<>();
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("点赞列表");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // 注册EventBus
        EventBus.getDefault().register(this);

    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        mSwipe.setColorSchemeColors(colorSwipe);
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        // Adapter
        mAdapter = new UserListAdapter(mUserList);
        mRvContent.setAdapter(mAdapter);
        // Listener
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HotTagDetail, mAdapter.getData().get(position).id + "", LikeListActivity.this, TraceUtils.PageCode_PostLikeList, TraceUtils.PositionCode_User + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_PostLikeList, TraceUtils.PositionCode_User, mAdapter.getData().get(position).id + "");

                OthersHomePageActivity.actionStart(mActivity, mAdapter.getData().get(position).id);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CheckBox chkFollowUser = (CheckBox) view;
                if (HaiUtils.needLogin(mActivity)) {
                    chkFollowUser.setChecked(!chkFollowUser.isChecked());
                } else {
                    requestFollowUser(chkFollowUser, mAdapter.getData().get(position).id, position);
                }
            }
        });

        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                mRvContent.post(() -> {
                    if (mAdapter.getData().size() < PAGE_SIZE) {
                        mAdapter.loadMoreEnd(true);
                    } else if (mCurrentPage < mAllPage) {
                        mCurrentPage++;
                        mSwipe.setEnabled(false);
                        requestLikeListData();
                    }
                });
            });
        });
        // 刷新
        mSwipe.setOnRefreshListener(this::refreshData);
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
    public void loadData() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;
        requestLikeListData();
    }

    /**
     * 请求点赞列表数据
     */
    private void requestLikeListData() {
        if (mPostId != -1) {
            SnsRepository.getInstance()
                    .getPostLikeUserList(mPostId, mCurrentPage)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<GetPostLikeUserListResult>() {
                        @Override
                        public void onSuccess(GetPostLikeUserListResult result) {
                            if (result.count == 0) {
                                mSv.showEmpty();
                                mAdapter.notifyDataSetChanged();
                                mAdapter.loadMoreFail();
                            } else {
                                mSv.showContent();
                                mAllPage = result.allpage;
                                if (mSwipe.isRefreshing()) {
                                    mAdapter.setNewData(result.users);
                                } else {
                                    mAdapter.addData(result.users);
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
        } else if (mSpecialId != -1) {
            SpecialRepository.getInstance()
                    .getPostSpecialLikes(mSpecialId, mCurrentPage)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<GetPostSpecialLikesResult>() {
                        @Override
                        public void onSuccess(GetPostSpecialLikesResult result) {
                            if (result.users.size() == 0) {
                                mSv.showEmpty();
                                mAdapter.notifyDataSetChanged();
                                mAdapter.loadMoreFail();
                            } else {
                                mSv.showContent();
                                mAllPage = result.allpage;
                                if (mSwipe.isRefreshing()) {
                                    mAdapter.setNewData(result.users);
                                } else {
                                    mAdapter.addData(result.users);
                                    if (mUserList.size() > PAGE_SIZE && mUserList.size() >= result.count) {
                                        mAdapter.loadMoreEnd();
                                    } else {
                                        mAdapter.loadMoreComplete();
                                    }
                                }
                            }
                        }

                        @Override
                        public boolean onFailed(Throwable e) {
                            showFailView(mSv, e, mHasLoad);
                            return mHasLoad;
                        }

                        @Override
                        public void onFinish() {
                            mHasLoad = true;
                            mSwipe.setEnabled(true);
                            mSwipe.setRefreshing(false);
                            mAdapter.setEnableLoadMore(true);
                        }
                    });
        } else if (productSpecialId != -1) {
            SpecialRepository.getInstance()
                    .getProductSpecialLikes(productSpecialId, mCurrentPage)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<GetPostSpecialLikesResult>() {
                        @Override
                        public void onSuccess(GetPostSpecialLikesResult result) {
                            if (result.users.size() == 0) {
                                mSv.showEmpty();
                                mAdapter.notifyDataSetChanged();
                                mAdapter.loadMoreFail();
                            } else {
                                mSv.showContent();
                                mAllPage = result.allpage;
                                if (mSwipe.isRefreshing()) {
                                    mAdapter.setNewData(result.users);
                                } else {
                                    mAdapter.addData(result.users);
                                    if (mUserList.size() > PAGE_SIZE && mUserList.size() >= result.count) {
                                        mAdapter.loadMoreEnd();
                                    } else {
                                        mAdapter.loadMoreComplete();
                                    }
                                }
                            }
                        }

                        @Override
                        public boolean onFailed(Throwable e) {
                            showFailView(mSv, e, mHasLoad);
                            return mHasLoad;
                        }

                        @Override
                        public void onFinish() {
                            mHasLoad = true;
                            mSwipe.setEnabled(true);
                            mSwipe.setRefreshing(false);
                            mAdapter.setEnableLoadMore(true);
                        }
                    });
        }
    }

    /**
     * 关注用户
     *
     * @param chkFollowUser 关注按钮
     * @param id            用户id
     */
    private void requestFollowUser(CheckBox chkFollowUser, int id, int position) {
        SnsRepository.getInstance()
                .followUser(id)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
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


    /**
     * 返回
     */
    @OnClick(R.id.ib_cancel)
    public void back() {
        onBackPressed();
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

    /**
     * 登录状态变化
     */
    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        refreshData();
    }

    /**
     * 用户关注状态变化
     */
    @Subscribe
    public void onUserFollowEvent(UserFollowEvent event) {
        refreshData();
    }
}
