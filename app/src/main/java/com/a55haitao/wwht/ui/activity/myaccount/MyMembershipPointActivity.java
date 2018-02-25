package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.MembershipPointAdapter;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.GetMembershipPointHistoryResult;
import com.a55haitao.wwht.data.model.result.GetMembershipPointHistoryResult.MembershipPointBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a55haitao.wwht.R.id.msv;

/**
 * 我的积分
 */
public class MyMembershipPointActivity extends BaseNoFragmentActivity {

    @BindView(R.id.content_view) RecyclerView       mRvContent;
    @BindView(R.id.title)        DynamicHeaderView  mTitle;
    @BindView(msv)               MultipleStatusView mSv;

    private int                            mCurrentPage;
    private int                            mAllPage;
    private MembershipPointAdapter         mAdapter;
    private AvatarView                     mAvatar;
    private ImageView                      mImgCancel;
    private TextView                       mTvMembershipPoint;
    private TextView                       mTvMembershipPointRule;
    private PopupWindow                    mPopWindow;
    private Tracker                        mTracker;    // GA Tracker
    private ArrayList<MembershipPointBean> mMembershipPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_membership_point);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    private void initVariables() {
        mCurrentPage = 1;
        mAllPage = 1;
        mMembershipPointList = new ArrayList<>();
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("我的积分");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new MembershipPointAdapter(mMembershipPointList);
        // 头布局
        LayoutInflater inflater    = LayoutInflater.from(mActivity);
        View           headerView  = inflater.inflate(R.layout.header_activity_my_membership_point, null, false);
        View           dividerView = inflater.inflate(R.layout.bg_divider, null, false);
        mAvatar = (AvatarView) headerView.findViewById(R.id.img_avatar);
        mTvMembershipPoint = (HaiTextView) headerView.findViewById(R.id.tv_membership_point);
        mTvMembershipPointRule = (HaiTextView) headerView.findViewById(R.id.tv_membership_point_rule);
        // 头像
        String   cornerUrl = null;
        UserBean userInfo  = UserRepository.getInstance().getUserInfo();
        if (userInfo.getUserTitleList().size() != 0) {
            cornerUrl = userInfo.getUserTitleList().get(0).getIconUrl();
        }
        mAvatar.loadImg(userInfo.getHeadImg(), cornerUrl);
        // 积分
        mTvMembershipPoint.setText(String.valueOf(userInfo.getMembershipPoint()));
        mTvMembershipPointRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRuleView(inflater);
            }
        });

        mAdapter.addHeaderView(headerView);
        mAdapter.addHeaderView(dividerView);

        mRvContent.setAdapter(mAdapter);

        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                if (mAdapter.getData().size() < PAGE_SIZE) {
                    mAdapter.loadMoreEnd(true);
                } else if (mCurrentPage < mAllPage) {
                    mCurrentPage++;
                    requestMembershipPointList();
                }
            });
        });
        // 重试
        mSv.setOnRetryClickListener(v -> loadData());
    }

    private void loadData() {
        mCurrentPage = 1;
        mSv.showLoading();
        requestMembershipPointList();
    }

    /**
     * 积分列表 - 网络请求
     */
    private void requestMembershipPointList() {
        SnsRepository.getInstance()
                .getMembershipPointHistory(mCurrentPage)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetMembershipPointHistoryResult>() {
                    @Override
                    public void onSuccess(GetMembershipPointHistoryResult result) {
                        mSv.showContent();
                        mAdapter.addData(result.datas);

                        if (result.count == 0) {
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mCurrentPage == 1) {
                                mAdapter.setNewData(result.datas);
                            } else {
                                mAdapter.addData(result.datas);
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
     * 积分规则弹窗
     */
    private void showRuleView(LayoutInflater inflater) {
        View contentView = inflater.inflate(R.layout.membership_point_rule, null, false);
        if (mPopWindow == null) {
            mPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false);
        }
        showPopupWindow();

        mImgCancel = (ImageView) contentView.findViewById(R.id.img_cancel);
        mImgCancel.setOnClickListener(v -> {
            dismissPopupWindow();
        });
    }


    /**
     * 显示积分规则
     */
    private void showPopupWindow() {
        mPopWindow.showAsDropDown(mTitle);
        mTitle.setHeadTitle("积分规则");
        mTitle.hideLeftButton();
    }

    /**
     * 隐藏积分规则
     */
    private void dismissPopupWindow() {
        mPopWindow.dismiss();
        mTitle.setHeadTitle("我的积分");
        mTitle.showLeftButton();
    }

    /**
     * 跳转到此页面
     *
     * @param context context
     */
    public static void toThisActivity(Context context) {
        toThisActivity(context, false);
    }

    /**
     * 跳转到此页面
     *
     * @param context context
     * @param newTask 开启新栈
     */
    public static void toThisActivity(Context context, boolean newTask) {
        Intent intent = new Intent(context, MyMembershipPointActivity.class);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopWindow != null && mPopWindow.isShowing()) {
            dismissPopupWindow();

        }
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {
        if (mPopWindow != null && mPopWindow.isShowing()) {
            dismissPopupWindow();
        } else {
            super.onBackPressed();
        }
    }

}
