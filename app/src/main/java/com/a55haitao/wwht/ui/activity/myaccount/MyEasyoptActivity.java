package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.MyEasyoptPagerAdapter;
import com.a55haitao.wwht.data.event.EasyOptCreateCountChangeEvent;
import com.a55haitao.wwht.data.event.EasyOptLikeCountChangeEvent;
import com.a55haitao.wwht.data.model.result.GetUserStarInfoCountsV12Result;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.easyopt.EasyoptCreateActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的草单
 * {@link com.a55haitao.wwht.ui.fragment.easyopt.EasyOptFragment 我创建的草单 & 我收藏的草单 Fragment}
 */
public class MyEasyoptActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)             DynamicHeaderView mTitle;
    @BindView(R.id.tab)               TabLayout         mTab;
    @BindView(R.id.vp_content)        ViewPager         mVpContent;
    @BindView(R.id.activity_my_album) LinearLayout      mActivityMyAlbum;

    private Tracker mTracker;   // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_easyopt);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void initVariables() {
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("我创建的草单");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews(Bundle savedInstanceState) {
        // 新建草单
        mTitle.setHeadClickListener(new DynamicHeaderView.OnHeadViewClickListener() {
            @Override
            public void onRightImgClick() {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_EasyCreat, "", MyEasyoptActivity.this, TraceUtils.PageCode_MyEasy, TraceUtils.PositionCode_AlbumCreate + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_EasyCreat, TraceUtils.PageCode_MyEasy, TraceUtils.PositionCode_AlbumCreate, "");

                EasyoptCreateActivity.toThisActivity(mActivity, null, null);
            }
        });
        // ViewPager
        mVpContent.setAdapter(new MyEasyoptPagerAdapter(getSupportFragmentManager()));
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // GA Tracker
                mTracker.setScreenName(position == 0 ? "我创建的草单" : "我收藏的草单");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTab.setupWithViewPager(mVpContent);
    }

    private void loadData() {
        SnsRepository.getInstance()
                .getUserStarInfoCountsV12()
                .subscribe(new DefaultSubscriber<GetUserStarInfoCountsV12Result>() {
                    @Override
                    public void onSuccess(GetUserStarInfoCountsV12Result result) {
                        setCreatedEasyoptCount(result.easyopt_count);
                        setLikedEasyoptCount(result.easyopt_start_count);
                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }

    /**
     * 跳转到本页面
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, MyEasyoptActivity.class));
    }

    /**
     * 我创建的草单 数目
     *
     * @param count 草单数
     */
    private void setCreatedEasyoptCount(int count) {
        mTab.getTabAt(0).setText(String.format("我创建的草单 · %d", count));
    }

    /**
     * 我收藏的草单数
     *
     * @param count 草单数
     */
    private void setLikedEasyoptCount(int count) {
        mTab.getTabAt(1).setText(String.format("我收藏的草单 · %d", count));
    }

    /**
     * 创建草单书改变
     * @param event
     */
    @Subscribe
    public void onEasyOptLikeCountChangeEvent(EasyOptLikeCountChangeEvent event) {
        setLikedEasyoptCount(event.count);
    }

    /**
     * 收藏草单数改变
     * @param event
     */
    @Subscribe
    public void onEasyOptCreateCountChangeEvent(EasyOptCreateCountChangeEvent event) {
        setCreatedEasyoptCount(event.count);
    }
}
