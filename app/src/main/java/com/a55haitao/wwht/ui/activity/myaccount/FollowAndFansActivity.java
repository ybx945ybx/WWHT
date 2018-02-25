package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.FollowFansPagerAdapter;
import com.a55haitao.wwht.data.model.annotation.FollowFansFragmentType;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.a55haitao.wwht.data.model.annotation.FollowFansFragmentType.FANS;
import static com.a55haitao.wwht.data.model.annotation.FollowFansFragmentType.FOLLOW;

/**
 * 关注 & 粉丝 Activity
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.followfans.FollowFragment} 关注页
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.followfans.FansFragment} 粉丝页
 */
public class FollowAndFansActivity extends BaseHasFragmentActivity {
    @BindView(R.id.ib_cancel)                ImageButton mIbTitleBack;                         // 返回按钮
    @BindView(R.id.rdo_followAndFans_follow) RadioButton mRdoFollowAndFansFollow;   // 关注
    @BindView(R.id.rdo_followAndFans_fans)   RadioButton mRdoFollowAndFansFans;       // 粉丝
    @BindView(R.id.rgp_followAndFans_type)   RadioGroup  mRgpFollowAndFansType;        // radioGroup
    @BindView(R.id.vp_followAndFans_content) ViewPager   mVpFollowAndFansContent;     // 内容

    private int mUserId; // 用户Id

    private int     mCurrentType;   // 类型 : 关注/粉丝
    private Tracker mTracker;       // Tracker

    public int getUserId() {
        return mUserId;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_and_fans);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mCurrentType = intent.getIntExtra("type", 0);
            mUserId = intent.getIntExtra("user_id", 0);
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(mCurrentType == FOLLOW ? "关注列表" : "粉丝列表");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        mVpFollowAndFansContent.setAdapter(new FollowFansPagerAdapter(getSupportFragmentManager()));

        mRgpFollowAndFansType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (group.getCheckedRadioButtonId()) {
                case R.id.rdo_followAndFans_follow:
                    mVpFollowAndFansContent.setCurrentItem(0);  // 偷懒写法
                    break;
                case R.id.rdo_followAndFans_fans:
                    mVpFollowAndFansContent.setCurrentItem(1);
                    break;
            }
        });

        // 根据不同的类型动态切换
        switch (mCurrentType) {
            case FOLLOW:
                mRdoFollowAndFansFollow.setChecked(true);
                break;
            case FANS:
                mRdoFollowAndFansFans.setChecked(true);
                break;
            default:
        }

        mVpFollowAndFansContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {  // ViewPager切换
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // GA
                mTracker.setScreenName(position == 0 ? "关注列表" : "粉丝列表");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                // 标题RadioGroup切换
                switch (position) {
                    case FOLLOW:
                        mRdoFollowAndFansFollow.setChecked(true);
                        break;
                    case FANS:
                        mRdoFollowAndFansFans.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 跳转到此页面
     *
     * @param context context
     * @param type    页面类型
     * @param userId  用户Id
     */
    public static void toThisActivity(Context context, @FollowFansFragmentType int type, int userId) {
        Intent intent = new Intent(context, FollowAndFansActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_cancel)
    public void onClick() { // 返回
        onBackPressed();
    }

    /*@Override
    protected void onDestroy() {
        FollowFansFragmentFactory.clearFragments();
        super.onDestroy();
    }*/

}
