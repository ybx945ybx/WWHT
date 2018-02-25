package com.a55haitao.wwht.ui.fragment.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.FollowingActionAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.CommentActivityType;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.GetFollowingActionListResult;
import com.a55haitao.wwht.data.model.result.GetFollowingActionListResult.ActionListBean;
import com.a55haitao.wwht.data.model.result.LikePostResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.activity.social.CommentListActivity;
import com.a55haitao.wwht.ui.activity.social.PostDetailActivity;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;
import com.varunest.sparkbutton.SparkButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社区 - 关注 Fragment
 *
 * @author 陶声
 * @since 2016-10-12
 */

public class FollowFragment extends SocialBaseFragment {

    @BindView(R.id.content_view)    RecyclerView       mRvContent; // 内容
    @BindView(R.id.swipe)           SwipeRefreshLayout mSwipe;
    @BindView(R.id.ib_login)        ImageButton        mIbLogin;
    @BindView(R.id.msv_layout)      MultipleStatusView mSv;
    @BindView(R.id.page_need_login) LinearLayout       mPageNeedLogin;

    private FollowingActionAdapter mAdapter;
    private int                    mAllPage;
    private int                    mCurrentPage;
    private List<ActionListBean>   mActionList;
    private ToastPopuWindow        mPwToast;

    @Override
    protected int getLayout() {
        return R.layout.fragment_follow;
    }

    @Override
    public void initVariables() {
        mAllPage = 1;
        mCurrentPage = 1;
        mActionList = new ArrayList<>();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews(View v, Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new FollowingActionAdapter(mActionList, mFragment);
        mRvContent.setAdapter(mAdapter);

        bindListener();
    }

    private void bindListener() {
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_PostDetail, mAdapter.getData().get(position).data.post.post_id + "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Post + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_PostDetail, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Post, mAdapter.getData().get(position).data.post.post_id + "");

                // 跳转到帖子详情
                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mAdapter.getData().get(position).data.post.post_id);
                intent.putExtra("wh_rate", mAdapter.getData().get(position).data.post.images.get(0).wh_rate);
                startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PostBean post = mAdapter.getData().get(position).data.post;
                switch (view.getId()) {
                    case R.id.img_avatar: // 用户主页
                    case R.id.tv_nickname:
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_HomePage, post.owner.id + "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_User + "", "");

//                        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_Social, TraceUtils.PositionCode_User, post.owner.id + "");

                        OthersHomePageActivity.actionStart(mActivity, post.owner.id);
                        break;
                    case R.id.chk_follow_user: // 关注用户
                        CheckBox chkFollowUser = (CheckBox) view;
                        if (HaiUtils.needLogin(mActivity)) {
                            chkFollowUser.setChecked(!chkFollowUser.isChecked());
                        } else {
                            requestFollowUser(chkFollowUser, post.owner.id);
                        }
                        break;
                    case R.id.ll_tag_container: // 标签容器
                        break;
                    case R.id.sb_like: // 点赞
                        SparkButton sb = (SparkButton) view;
                        TextView tvLikeCount = (TextView) ((ViewGroup) sb.getParent()).findViewById(R.id.tv_like_count);
                        if (HaiUtils.needLogin(mActivity)) {
                            sb.setChecked(post.is_liked);
                        } else {
                            requestLikePost(post.post_id, sb, tvLikeCount);
                        }
                        break;
                    case R.id.ib_comment: // 评论
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_CommentsList, post.post_id + "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_ViewComments + "", "");

                        //                        TraceUtils.addAnalysAct(TraceUtils.PageCode_CommentsList, TraceUtils.PageCode_Social, TraceUtils.PositionCode_ViewComments, "");

                        CommentListActivity.toThisActivity(mActivity, CommentActivityType.POST, post.post_id, post.owner.id == HaiUtils.getUserId());
                        break;
                    case R.id.ib_share: // 分享
                        ShareUtils.showShareBoard(mActivity,
                                post.share.title,
                                TextUtils.isEmpty(post.share.desc) ? "来自55海淘官网直购社区" : post.share.desc,
                                post.share.url,
                                post.share.icon,
                                true);
                        break;
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
                    mSwipe.setEnabled(false);
                    requestContent();
                }
            });
        });
        // 刷新
        mSwipe.setOnRefreshListener(this::refreshData);
        // 重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    private void refreshData() {
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
//    }

    /**
     * 根据登录状态切换布局
     */
    private void switchPage() {
        mSwipe.setVisibility(HaiUtils.isLogin() ? View.VISIBLE : View.GONE);
        mPageNeedLogin.setVisibility(HaiUtils.isLogin() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void loadData() {
        switchPage();
        if (HaiUtils.isLogin()) {
            mSwipe.setRefreshing(true);
            mCurrentPage = 1;
            requestContent();
        }
    }

    /**
     * 网络请求
     */
    private void requestContent() {
        SnsRepository.getInstance()
                .getFollowingActionList(mCurrentPage)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<GetFollowingActionListResult>() {
                    @Override
                    public void onSuccess(GetFollowingActionListResult result) {
                        if (result.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.action_list);
                            } else {
                                mAdapter.addData(result.action_list);
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

    /**
     * 点赞请求
     *
     * @param postId      帖子Id
     * @param sb          点赞按钮
     * @param tvLikeCount 点赞数
     */
    private void requestLikePost(int postId, SparkButton sb, TextView tvLikeCount) {
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
                        tvLikeCount.setText(String.format("赞 %d", result.like_count));
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 关注用户
     *
     * @param chkFollowUser 关注按钮
     * @param id            用户id
     */
    private void requestFollowUser(CheckBox chkFollowUser, int id) {
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
     * 登录
     */
    @OnClick(R.id.ib_login)
    public void onClick() {
        HaiUtils.needLogin(mActivity);
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
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
