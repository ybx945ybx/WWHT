package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.MessagePagerAdapter;
import com.a55haitao.wwht.data.model.annotation.MessageFragmentType;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.myaccount.message.MessageFragmentFactory;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心 - 消息
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.message.MessageFragment 消息Fragment}
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.message.NotificationFragment 通知Fragment}
 */
public class MessageActivity extends BaseHasFragmentActivity {
    @BindView(R.id.ib_cancel)                    ImageButton mIbTitleBack;                             // 返回
    @BindView(R.id.tab_messageActivity_titleTab) TabLayout   mTabMessageActivityTitleTab; // tab
    @BindView(R.id.vp_messageActivity_content)   ViewPager   mVpMessageActivityContent;     // 内容

    @BindString(R.string.key_user_id) String KEY_USER_ID;

    private int     mUserId;    // user_id
    private Tracker mTracker;   // tracker

    public int getUserId() {
        return mUserId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
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
        mTracker.setScreenName("消息列表");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        mVpMessageActivityContent.setAdapter(new MessagePagerAdapter(getSupportFragmentManager()));
        mTabMessageActivityTitleTab.setupWithViewPager(mVpMessageActivityContent);
        mVpMessageActivityContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private MessageFragmentType[] mTypes;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // GA Tracker
                mTracker.setScreenName(position == 0 ? "消息列表" : "通知列表");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 返回上一页面
     */
    @OnClick(R.id.ib_cancel)
    public void onClick() {
        onBackPressed();
    }


    @Override
    protected void onDestroy() {
        MessageFragmentFactory.clearFragments();
        super.onDestroy();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
