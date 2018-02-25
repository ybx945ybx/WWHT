package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.OthersHomePagePagerAdapter;
import com.a55haitao.wwht.data.event.HomePageRefreshEvent;
import com.a55haitao.wwht.data.event.UserFollowEvent;
import com.a55haitao.wwht.data.model.annotation.FollowFansFragmentType;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.UserInfoResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.view.AvatarPopupWindow;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 他人主页
 */
public class OthersHomePageActivity extends BaseHasFragmentActivity implements View.OnClickListener {

    @BindView(R.id.msv)                MultipleStatusView msView;       // 多状态布局
    @BindView(R.id.tv_nickname)        HaiTextView        mTvNickname;       // 昵称
    @BindView(R.id.tv_user_title)      HaiTextView        mTvUserTitle;      // user_title
    @BindView(R.id.tv_following_count) HaiTextView        mTvFollowingCount; // 关注数
    @BindView(R.id.tv_follower_count)  HaiTextView        mTvFollowerCount;  // 粉丝数
    @BindView(R.id.tv_like_count)      HaiTextView        mTvLikeCount;      // 点赞数
    @BindView(R.id.img_avatar)         AvatarView         mImgAvatar;        // 头像
    @BindView(R.id.tab)                TabLayout          mTab;              // tab
    @BindView(R.id.vp_content)         ViewPager          mVpContent;        // 内容
    //    @BindView(R.id.swipe)              HaiSwipeRefreshLayout mSwipe;            // 下拉刷新
    @BindView(R.id.appBar)             AppBarLayout       mAppBar;           // AppBarLayout
    @BindView(R.id.chk_follow_user)    CheckBox           mChkFollowUser;    // 关注按钮
    @BindView(R.id.ll_user_info)       LinearLayout       mLlUserInfo;
    @BindView(R.id.tv_title_name)      HaiTextView        mTvTitleName;      // 标题
    @BindView(R.id.tv_signature)       HaiTextView        mTvSignature;      // 签名

    @BindDimen(R.dimen.avatar_my_homepage) int AVATAR_SIZE; // 头像尺寸

    private int               mUserId;  // 他人userId
    private String            mHeadImg; //  头像
    private AvatarPopupWindow mAvatarPopupWindow;
    private String            mCornerUrl;
    private Tracker           mTracker; // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_home_page);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + mUserId;
    }


    private void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getIntExtra("user_id", 0);
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("他人主页");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews(Bundle savedInstanceState) {
        msView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新用户信息
                loadData();
                // 通知ViewPager更新
                EventBus.getDefault().post(new HomePageRefreshEvent(true));

            }
        });
        // ViewPager
        mVpContent.setAdapter(new OthersHomePagePagerAdapter(getSupportFragmentManager(), mUserId));
        mTab.setupWithViewPager(mVpContent);
        mAppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            mTvTitleName.setVisibility(Math.abs(verticalOffset) >= mLlUserInfo.getMeasuredHeight() ?
                    View.VISIBLE : View.INVISIBLE);
        });
    }

    private void loadData() {
        msView.showLoading();
        loadUserInfo();
    }

    /**
     * 读取用户信息
     */
    private void loadUserInfo() {
        UserRepository.getInstance()
                .userInfo(mUserId)
                .compose(bindToLifecycle())
                .subscribe(new DefaultSubscriber<UserInfoResult>() {
                    @Override
                    public void onSuccess(UserInfoResult result) {
                        msView.showContent();
                        // 更新View
                        setUserInfoView(result);
                        mHeadImg = result.head_img;
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    /**
     * 关注用户请求
     */
    private void requestFollowUser(int userId) {
        SnsRepository.getInstance()
                .followUser(userId)
                .compose(bindToLifecycle())
                .subscribe(new DefaultSubscriber<FollowUserResult>() {
                    @Override
                    public void onSuccess(FollowUserResult result) {
                        mChkFollowUser.setChecked(result.is_following);
                        if (result.is_following) {
                            ToastUtils.showToast(mActivity, "关注成功");
                        }
                        EventBus.getDefault().post(new UserFollowEvent());
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        mChkFollowUser.setChecked(!mChkFollowUser.isChecked());
                        return super.onFailed(e);
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 设置个人信息View
     *
     * @param userInfo 用户信息
     */
    private void setUserInfoView(UserInfoResult userInfo) {
        // 关注状态
        mChkFollowUser.setChecked(userInfo.is_following);
        // 头像
        if (userInfo.user_title.size() != 0) {
            mCornerUrl = userInfo.user_title.get(0).getIconUrl();
        }
        mImgAvatar.loadImg(userInfo.head_img, mCornerUrl);
        // 昵称
        mTvNickname.setText(userInfo.nickname);
        mTvTitleName.setText(userInfo.nickname);
        // user_title
        if (userInfo.user_title.size() != 0) {
            mTvUserTitle.setVisibility(View.VISIBLE);
            mTvUserTitle.setText(userInfo.user_title.get(0).getTitle());
        } else {
            mTvUserTitle.setVisibility(View.INVISIBLE);
        }
        // 关注数
        mTvFollowingCount.setText(String.format("%d关注", userInfo.following_count));
        // 粉丝数
        mTvFollowerCount.setText(String.format("%d粉丝", userInfo.follower_count));
        // 赞数
        mTvLikeCount.setText(String.format("%d点赞", userInfo.like_count));
        // 笔记数
        mTab.getTabAt(0).setText(String.format("笔记 · %d", userInfo.post_count));
        // 点赞数
        mTab.getTabAt(1).setText(String.format("草单 · %d", userInfo.comm_counts.getEasyopt_count()));
        // 签名
        String signature = userInfo.signature;
        if (TextUtils.isEmpty(signature)) {
            mTvSignature.setVisibility(View.INVISIBLE);
        } else {
            mTvSignature.setVisibility(View.VISIBLE);
            mTvSignature.setText(signature);
        }
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param userId  用户Id
     */
    public static void actionStart(Context context, int userId) {
        if (userId == HaiUtils.getUserId())
            return;
        Intent intent = new Intent(context, OthersHomePageActivity.class);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
    }

    /**
     * 关注 & 粉丝
     */
    @OnClick({R.id.tv_following_count, R.id.tv_follower_count})
    public void onClick(View view) {
        @FollowFansFragmentType int type = FollowFansFragmentType.FOLLOW;
        if (view.getId() == R.id.tv_follower_count) {
            type = FollowFansFragmentType.FANS;
        }

        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_FansOrFollows, mUserId + "", this, TraceUtils.PageCode_HomePage, type == FollowFansFragmentType.FANS ? TraceUtils.PositionCode_Follower + "" : TraceUtils.PositionCode_Following + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_FansOrFollows, TraceUtils.PageCode_HomePage, type == FollowFansFragmentType.FANS ? TraceUtils.PositionCode_Follower : TraceUtils.PositionCode_Following, "");

        FollowAndFansActivity.toThisActivity(mActivity, type, mUserId);
    }

    /**
     * 关注用户
     */
    @OnClick(R.id.chk_follow_user)
    public void clickFollowUser() {
        if (HaiUtils.needLogin(mActivity)) {
            mChkFollowUser.setChecked(!mChkFollowUser.isChecked());
            return;
        }
        requestFollowUser(mUserId);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_back)
    public void clickBack() {
        finish();
    }

    /**
     * 头像查看大图
     */
    @OnClick(R.id.img_avatar)
    public void showBigAvatar() {
        mAvatarPopupWindow = new AvatarPopupWindow(mActivity, mHeadImg);
        mTvUserTitle.postDelayed(() -> mAvatarPopupWindow.showOrDismiss(mTvUserTitle), 200);
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param userId  用户Id
     */
    public static void toThisActivity(Context context, int userId) {
        Intent intent = new Intent(context, OthersHomePageActivity.class);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param userId  用户Id
     * @param newTask 是否开启新栈
     */
    public static void toThisActivity(Context context, int userId, boolean newTask) {
        Intent intent = new Intent(context, OthersHomePageActivity.class);
        intent.putExtra("user_id", userId);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    /**
     * @return 用户Id
     */
    public int getUserId() {
        return mUserId;
    }
}


