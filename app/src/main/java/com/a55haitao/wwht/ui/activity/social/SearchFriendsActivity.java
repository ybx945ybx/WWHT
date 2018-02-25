package com.a55haitao.wwht.ui.activity.social;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.UserListAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.UserFollowEvent;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.SearchUserResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static com.a55haitao.wwht.R.id.msv;

/**
 * 社区 - 搜索好友页面
 */
public class SearchFriendsActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)            DynamicHeaderView  mTitle;     // 标题
    @BindView(R.id.et_search)        EditText           mEtSearch;  // 搜索框EditText
    @BindView(R.id.content_view)     RecyclerView       mRvContent; // 内容
    @BindDrawable(R.mipmap.dot_line) Drawable           DIVIDER_BG; // 内容
    @BindView(R.id.ib_cancel)        ImageButton        mIbCancel;  // 清除输入
    @BindView(msv)                   MultipleStatusView mSv;        // 多状态布局容器

    private InputMethodManager      mImm;
    private int                     mAllPage;
    private int                     mCurrentPage;
    private UserListAdapter         mAdapter;
    //    private SearchUserResult        mSearchResultData;
    private String                  mName;
    private Subscription            mSubscription;
    private ArrayList<UserListBean> mUserList;

    /**
     * 清除输入
     */
    @OnClick(R.id.ib_cancel)
    public void clickClearInput() {
        mEtSearch.setText("");
        mIbCancel.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
    }

    protected int getLayout() {
        return R.layout.activity_search_friends;
    }

    public void initVars() {
        mAllPage = 1;
        mCurrentPage = 1;
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mUserList = new ArrayList<>();
        EventBus.getDefault().register(this);

    }

    public void initViews(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new UserListAdapter(mUserList);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HomePage, mAdapter.getData().get(position).id + "", mActivity, TraceUtils.PageCode_AddFriends, TraceUtils.PositionCode_User + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_AddFriends, TraceUtils.PositionCode_User, mAdapter.getData().get(position).id + "");

                OthersHomePageActivity.actionStart(mActivity, mAdapter.getData().get(position).id);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.chk_follow_user: // 关注用户
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

        mEtSearch.setHint("输入您想查找的用户昵称......");
        mEtSearch.requestFocus();

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //                mCurrentPage = 1;
                //                requestFriendsList();
                refreshData();
                mIbCancel.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
            }
        });

        // 焦点监听
        mEtSearch.setOnFocusChangeListener((v, hasFocus) -> {
            mIbCancel.setVisibility(TextUtils.isEmpty(mEtSearch.getText().toString()) && hasFocus ? View.INVISIBLE : View.VISIBLE);
        });

        // 输入法回车键触发搜索
        mEtSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                mName = mEtSearch.getText().toString().trim();
                //                mCurrentPage = 1;
                //                requestFriendsList();
                refreshData();
            }
            return false;
        });

        // 触摸RecyclerView时隐藏输入法
        mRvContent.setOnTouchListener((v, event) -> {
            mImm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
            return false;
        });
        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                if (mAdapter.getData().size() < PAGE_SIZE) {
                    mAdapter.loadMoreEnd(true);
                } else if (mCurrentPage < mAllPage) {
                    mCurrentPage++;
                    requestFriendsList();
                }
            });
        });
        // 重试
        mSv.setOnRetryClickListener(v -> refreshData());
    }

    private void refreshData() {
        String name = mEtSearch.getText().toString();
        if (TextUtils.isEmpty(name))
            return;
        mSv.showLoading();
        mAdapter.setEnableLoadMore(false);
        mCurrentPage = 1;
        requestFriendsList();
    }

    /**
     * 好友列表 - 网络请求
     */
    private void requestFriendsList() {
        // 取消之前的请求
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            Logger.t(TAG).d("取消请求");
        }

        mSubscription = SnsRepository.getInstance()
                .searchUser(mEtSearch.getText().toString(), mCurrentPage)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<SearchUserResult>() {
                    @Override
                    public void onSuccess(SearchUserResult result) {
                        //                        mSearchResultData = result;
                        //                        mAdapter.setNewData(result.userlist);

                        if (result.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mCurrentPage == 1) {
                                mAdapter.setNewData(result.userlist);
                            } else {
                                mAdapter.addData(result.userlist);
                                if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= result.count) {
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
                        //                        mHasLoad = true;
                    }
                });
    }

    /**
     * 关注用户
     *
     * @param chkFollowUser 关注按钮
     * @param id            用户id
     */
    private void requestFollowUser(CheckBox chkFollowUser, int id, int position) {

        // 重新请求
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

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    @Subscribe
    public void onUserFollowEvent(UserFollowEvent event) {
        refreshData();
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        refreshData();
    }
}
