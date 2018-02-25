package com.a55haitao.wwht.ui.activity.social;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.HotUserListAdapter;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.model.entity.ShareBean;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.GetHotUserListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 社区 - 添加好友页面
 * <p>
 * Created by 陶声 on 16/7/26.
 */
public class AddFriendsActivity extends BaseNoFragmentActivity implements View.OnClickListener {
    //    @BindView(R.id.title) DynamicHeaderView mTitle;                     // 标题
    //    @BindView(R.id.ll_invite_contacts) LinearLayout mLlInviteContacts;  // 邀请通讯录好友
    //    @BindView(R.id.ll_invite_weibo)    LinearLayout mLlInviteWeibo;        // 邀微博好友
    //    @BindView(R.id.ll_invite_wechat)   LinearLayout mLlInviteWechat;      // 邀微信好友
    //    @BindView(R.id.ll_invite_qq)       LinearLayout mLlInviteQq;              // 邀请QQ好友
    @BindView(R.id.ll_search)  LinearLayout mLlSearch;                   // 搜索框
    @BindView(R.id.rv_content) RecyclerView mRvContent;                   // 推荐用户列表

    @BindString(R.string.key_user_id)              String KEY_USER_ID;
    @BindString(R.string.settings_share_app_title) String SETTINGS_SHARE_APP_TITLE;
    @BindString(R.string.settings_share_app_text)  String SETTINGS_SHARE_APP_TEXT;

    @BindDimen(R.dimen.avatar_big)     int AVATAR_SIZE; // 头像大小
    @BindDimen(R.dimen.padding_medium) int DIVIDER_PADDING; // Divider边距

    private static final int READ_CONTACTS_REQUEST_CODE = 1;

    private int                                  mUserId;    // 用户Id
    private HotUserListAdapter                   mAdapter;
    private List<GetHotUserListResult.UsersBean> mUserListData;
    private ShareBean                            mShareBeanData;
    private Tracker                              mTracker;      // GA Tracker

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
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
            mUserId = intent.getIntExtra(KEY_USER_ID, 0);
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("社区_添加好友");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mUserListData = new ArrayList<>();

    }

    /**
     * 初始化UI
     */
    public void initViews(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new HotUserListAdapter(mUserListData, calcPicWidth(), AVATAR_SIZE);

        LayoutInflater inflater   = LayoutInflater.from(mActivity);
        View           headerView = inflater.inflate(R.layout.header_activity_add_friends, null, false);

        // 通讯录好友
        headerView.findViewById(R.id.ll_invite_contacts)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddFriendsActivity.this.inviteContactsWrapper();
                    }
                });
        // 微博好友
        headerView.findViewById(R.id.ll_invite_weibo)
                .setOnClickListener(this);
        // 微信好友
        headerView.findViewById(R.id.ll_invite_wechat)
                .setOnClickListener(this);
        // qq好友
        headerView.findViewById(R.id.ll_invite_qq)
                .setOnClickListener(this);

        mAdapter.addHeaderView(headerView);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HomePage, mAdapter.getData().get(position).id + "", AddFriendsActivity.this, TraceUtils.PageCode_AddFriends, TraceUtils.PositionCode_User + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_AddFriends, TraceUtils.PositionCode_User, mAdapter.getData().get(position).id + "");

                OthersHomePageActivity.actionStart(mActivity, mAdapter.getData().get(position).id);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mUserListData == null)
                    return;

                switch (view.getId()) {
                    case R.id.chk_follow_user: // 关注用户
                        CheckBox chkFollowUser = (CheckBox) view;
                        if (HaiUtils.needLogin(mActivity)) {
                            chkFollowUser.setChecked(!chkFollowUser.isChecked());
                        } else {
                            requestFollowUser(chkFollowUser, mAdapter.getData().get(position).id);
                        }
                        break;
                }
            }
        });


        mLlSearch.setOnClickListener(v ->
                jumpSearchFriendsActivity()
        );
    }

    /**
     * 计算帖子图片宽度
     */
    private int calcPicWidth() {
        return (DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 10) * 5) / 4;
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SnsRepository.getInstance()
                .getHotUserList()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetHotUserListResult>() {
                    @Override
                    public void onSuccess(GetHotUserListResult result) {
                        //                        mUserListData = result.users;
                        //                        setHotUserListView(mUserListData);

                        mAdapter.setNewData(result.users);
                        mAdapter.loadMoreEnd();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 填充UI
     *
     * @param users
     */
    private void setHotUserListView(List<GetHotUserListResult.UsersBean> users) {
        mAdapter.addData(users);
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
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
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
     * 跳转到搜索好友列表页面
     */
    private void jumpSearchFriendsActivity() {
        startActivity(new Intent(mActivity, SearchFriendsActivity.class));
    }

    /**
     * 邀请第三方好友
     */
    @Override
    public void onClick(View v) {
        SHARE_MEDIA platform = null;
        switch (v.getId()) {
            case R.id.ll_invite_weibo:  // 微博
                platform = SHARE_MEDIA.SINA;
                break;
            case R.id.ll_invite_wechat: // 微信
                platform = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.ll_invite_qq:     // QQ
                platform = SHARE_MEDIA.QQ;
                break;
            default:
                break;
        }

        if ((platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) && !UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
            ToastUtils.showToast(mActivity, "请安装微信客户端");
            return;
        }

        if (platform == SHARE_MEDIA.QQ && !UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.QQ)) {
            ToastUtils.showToast(mActivity, "请安装QQ客户端");
            return;
        }

        if (platform == SHARE_MEDIA.SINA && !UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.SINA)) {
            ToastUtils.showToast(mActivity, "请安装微博客户端");
            return;
        }

        if (mShareBeanData == null) {
            showProgressDialog();
            SHARE_MEDIA finalPlatform = platform;
            SnsRepository.getInstance()
                    .getShareAppInfo()
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<ShareBean>() {
                        @Override
                        public void onSuccess(ShareBean shareBean) {
                            mShareBeanData = shareBean;
                            doShare(finalPlatform, mShareBeanData);
                        }

                        @Override
                        public boolean onFailed(Throwable e) {
                            doShare(finalPlatform);
                            //                            return super.onFailed(e);
                            return false;
                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    });
        } else {
            doShare(platform, mShareBeanData);

        }
    }

    /**
     * 分享
     *
     * @param platform 平台
     */
    private void doShare(SHARE_MEDIA platform) {
        // 分享
        ShareUtils.doShare(platform,
                mActivity,
                SETTINGS_SHARE_APP_TITLE,
                SETTINGS_SHARE_APP_TEXT,
                ServerUrl.SHARE_APP_URL + HaiUtils.getUserId(),
                ServerUrl.SHARE_APPICON_URL,
                false);
    }

    /**
     * 分享
     *
     * @param platform 平台
     */
    private void doShare(SHARE_MEDIA platform, ShareBean shareBean) {
        // 分享
        ShareUtils.doShare(platform,
                mActivity,
                shareBean.title,
                shareBean.desc,
                shareBean.url,
                shareBean.icon,
                false);
    }

    /**
     * 请求权限回调
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    inviteContactsWrapper();
                } else {
                    ToastUtils.showToast(mActivity, "请先打开通讯录权限");
                }
                break;
            }
        }
    }

    private void inviteContactsWrapper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_REQUEST_CODE);
        } else {
            inviteContacts();
        }
    }

    /**
     * 通讯录好友
     */
    public void inviteContacts() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_ContactsFriend, "", this, TraceUtils.PageCode_AddFriends, TraceUtils.PositionCode_SearchContact + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_ContactsFriend, TraceUtils.PageCode_AddFriends, TraceUtils.PositionCode_SearchContact, "");

        Intent intent = new Intent(mActivity, ContactsFriendsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        dismissProgressDialog();
//    }
}
