package com.a55haitao.wwht.ui.fragment.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.PostListAdapter;
import com.a55haitao.wwht.data.event.HomePageRefreshEvent;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.PostChangeEvent;
import com.a55haitao.wwht.data.event.UserInfoChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.data.model.result.GetUserPostListResult;
import com.a55haitao.wwht.data.model.result.LikePostResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.activity.social.PostDetailActivity;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.varunest.sparkbutton.SparkButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;


/**
 * 个人中心 - 晒物Fragment
 *
 * @author 陶声
 * @since 2016-20-12
 */

public class SectionPostFragment extends MyAccountBaseFragment {
    @BindView(R.id.content_view) RecyclerView       mRvContent;   // 内容
    @BindView(R.id.msv)          MultipleStatusView mSv;

    public static final int TYPE_USER_POST = 1;
    public static final int TYPE_LIKE_POST = 2;

    private int             mCurrentPage;
    private int             mAllPage;
    private PostListAdapter mAdapter;
    private int             mUserId; // 用户Id

    private List<PostBean>  mPostList;
    private int             mType;
    private ToastPopuWindow mPwToast;

    public static SectionPostFragment newInstance(int userId, int type) {
        SectionPostFragment fragment = new SectionPostFragment();
        Bundle              bundle   = new Bundle();
        bundle.putInt("user_id", userId);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_account_tabs;
    }

    @Override
    public void initVariables() {
        mAllPage = 1;
        mCurrentPage = 1;
        mPostList = new ArrayList<>();
        // user_id
        mUserId = getArguments().getInt("user_id");
        mType = getArguments().getInt("type", TYPE_USER_POST);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initViews(View v, Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        // adapter
        mAdapter = new PostListAdapter(mPostList, mFragment);
        mRvContent.setAdapter(mAdapter);
        // listener
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_PostDetail, mAdapter.getData().get(position).post_id + "", mActivity, TraceUtils.PageCode_HomePage, TraceUtils.PositionCode_Post + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_PostDetail, TraceUtils.PageCode_HomePage, TraceUtils.PositionCode_Post, mAdapter.getData().get(position).post_id + "");

                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mAdapter.getData().get(position).post_id);
                intent.putExtra("wh_rate", mAdapter.getData().get(position).images.get(0).wh_rate);
                startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PostBean post = mAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.sb_like: // 点赞
                        SparkButton sb = (SparkButton) view;
                        TextView tvLikeCount = (TextView) ((ViewGroup) sb.getParent()).findViewById(R.id.tv_like_count);
                        if (HaiUtils.needLogin(mActivity)) {
                            sb.setChecked(post.is_liked);
                        } else {
                            requestLikePost(post.post_id, sb, tvLikeCount, position);
                        }
                        break;
                    case R.id.img_avatar:
                    case R.id.tv_nickname:
                        if (mActivity instanceof OthersHomePageActivity && post.owner.id == ((OthersHomePageActivity) mActivity).getUserId())
                            return;

                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_HomePage, post.owner.id + "", mActivity, TraceUtils.PageCode_HomePage, TraceUtils.PositionCode_User + "", "");

                        //                        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_HomePage, TraceUtils.PositionCode_User, post.owner.id + "");

                        OthersHomePageActivity.toThisActivity(mActivity, post.owner.id);
                    default:
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
                    loadPostList();
                }
            });
        });
        // 重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

    @Override
    public void loadData() {
        mSv.showLoading();
        mCurrentPage = 1;
        loadPostList();
    }

    /**
     * 获取晒物列表
     */
    private void loadPostList() {
        Observable<GetUserPostListResult> postListObservable;

        if (mType == TYPE_USER_POST) {
            postListObservable = SnsRepository.getInstance().getUserPostList(mUserId, mCurrentPage);
        } else {
            postListObservable = SnsRepository.getInstance().getLikePostList(mCurrentPage);
        }

        postListObservable
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<GetUserPostListResult>() {
                    @Override
                    public void onSuccess(GetUserPostListResult result) {
                        if (result.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mIsRefreshing) {
                                mAdapter.setNewData(result.post_list);
                            } else {
                                mAdapter.addData(result.post_list);
                                if (mPostList.size() > PAGE_SIZE && mPostList.size() >= result.count) {
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
                        //                        mSv.showError();
                        showFailView(mSv, e, mHasData);
                        return mHasData;
                    }

                    @Override
                    public void onFinish() {
                        mIsRefreshing = false;
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }

    /**
     * 点赞请求
     *
     * @param postId      帖子Id
     * @param sb          点赞按钮
     * @param tvLikeCount 点赞数
     */
    private void requestLikePost(int postId, SparkButton sb, TextView tvLikeCount, int position) {
        SnsRepository.getInstance()
                .likePost(postId)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<LikePostResult>() {
                    @Override
                    public void onSuccess(LikePostResult result) {
                        if (result.is_like_now) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, result.membership_point, AlterPointViewType.AlterPointViewType_Like).parentView(mRvContent);
                            mPwToast.show();
                        }
                        sb.setChecked(result.is_like_now);
                        tvLikeCount.setText(String.valueOf(result.like_count));

                        PostBean post = mAdapter.getData().get(position);
                        post.like_count = result.like_count;
                        post.is_liked = result.is_like_now;

                        EventBus.getDefault().post(new PostChangeEvent());
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Subscribe
    public void onHomePageRefreshEvent(HomePageRefreshEvent event) {
        mIsRefreshing = true;
        refreshData();
    }

    @Subscribe
    public void onPostChangeEvent(PostChangeEvent event) {
        if (!getUserVisibleHint()) {
            mIsRefreshing = true;
            refreshData();
        }
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        if (HaiUtils.isLogin())
            loadData();
    }

    @Subscribe
    public void onUserInfoChangeEvent(UserInfoChangeEvent event) {
        //        loadData();
        mIsRefreshing = true;
        refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }
}
