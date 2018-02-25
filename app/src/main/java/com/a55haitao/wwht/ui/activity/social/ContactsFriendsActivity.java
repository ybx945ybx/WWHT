package com.a55haitao.wwht.ui.activity.social;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.UserListAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.UserFollowEvent;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.GetUserListByMobilesResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
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

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 社区 - 添加好友 - 通讯录好友页面
 * <p>
 * Created by 陶声 on 16/7/26.
 */
public class ContactsFriendsActivity extends BaseNoFragmentActivity {
    @BindView(R.id.title)        DynamicHeaderView     mTitle;    // 标题
    @BindView(R.id.content_view) RecyclerView          mRvContent;// 好友列表
    @BindView(R.id.swipe)        HaiSwipeRefreshLayout mSwipe;    // 下拉刷新
    @BindView(R.id.msv_layout)   MultipleStatusView    mSv;       // 多布局容器

    @BindColor(R.color.color_swipe)  int      colorSwipe;   // 下拉刷新颜色
    @BindDrawable(R.mipmap.dot_line) Drawable DIVIDER_BG;   // 下拉刷新颜色

    private   Cursor                     mCursor;
    protected UserListAdapter            mAdapter;
    private   GetUserListByMobilesResult mUserListResultData;
    private   ArrayList<UserListBean>    mUserList;
    private   String                     mContactsListStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_friends);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
        loadData();
    }

    private void initVars() {
        mUserList = new ArrayList<>();
        EventBus.getDefault().register(this);

    }

    /**
     * 初始化UI
     */
    public void initViews(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new UserListAdapter(mUserList);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 跳转到他人主页

                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HomePage, mAdapter.getData().get(position).id + "", ContactsFriendsActivity.this, TraceUtils.PageCode_ContactsFriend, TraceUtils.PositionCode_User + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_ContactsFriend, TraceUtils.PositionCode_User, mAdapter.getData().get(position).id + "");

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
        // 下拉刷新
        mSwipe.setColorSchemeColors(colorSwipe);
        mSwipe.setOnRefreshListener(this::requestContactsFriendsList);
        // 重试
        mSv.setOnRetryClickListener(v -> requestContactsFriendsList());
    }

    /**
     * 加载数据
     */
    public void loadData() {
        new Thread(this::readContacts).start();
    }

    /**
     * 读取手机联系人
     */
    private void readContacts() {
        ContentResolver cr         = getContentResolver();
        String[]        projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        mCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if (mCursor == null || mCursor.getCount() == 0) {
            mSwipe.setRefreshing(false);
            runOnUiThread(() -> mSv.showEmpty());
        } else {
            ArrayList<String> contactsList = new ArrayList<>();
            while (mCursor.moveToNext()) {
                // 取得电话号码
                String phoneNumber = mCursor.getString(0);
                if (!TextUtils.isEmpty(phoneNumber)) {
                    phoneNumber = phoneNumber.replace("-", "");
                    phoneNumber = phoneNumber.replace(" ", "");
                    contactsList.add("\"" + phoneNumber + "\"");
                }
            }
            runOnUiThread(() -> {
                mContactsListStr = contactsList.toString();
                requestContactsFriendsList();
            });
        }
        mCursor.close();
    }

    /**
     * 获取联系人好友列表请求
     */
    private void requestContactsFriendsList() {
        mSwipe.setRefreshing(true);
        SnsRepository.getInstance()
                .getUserListByMobiles(mContactsListStr)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetUserListByMobilesResult>() {
                    @Override
                    public void onSuccess(GetUserListByMobilesResult result) {
                        if (result.users.size() == 0) {
                            mSv.showEmpty();
                        } else {
                            mSv.showContent();
                            mAdapter.setNewData(result.users);
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
                        mSwipe.setRefreshing(false);
                    }
                });
    }

    /**
     * 关注用户
     *
     * @param chkFollowUser 关注按钮
     * @param id            用户id
     * @param position      位置
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

    @Override
    protected void onDestroy() {
        if (!mCursor.isClosed())
            mCursor.close();
        super.onDestroy();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        requestContactsFriendsList();
    }

    @Subscribe
    public void onUserFollowEvent(UserFollowEvent event) {
        requestContactsFriendsList();
    }
}
