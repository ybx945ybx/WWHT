package com.a55haitao.wwht.ui.activity.firstpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.common.ViewPagerAdapter;
import com.a55haitao.wwht.data.model.entity.TabBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.fragment.firstpage.EntriesFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

/**
 * 精选合集页面
 */
public class EntriesActivity extends BaseHasFragmentActivity {
    private MultipleStatusView mMsView;
    private TabLayout          mTabLayout;
    private ViewPager          mViewPager;
    private HomeRepository     mReposity;
    private int                mPosition;               //  各频道页跳转过来定位到相应tab
    private String             mTabName;
    private Tracker            mTracker;                // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        initVars();
        initViews();
        getTabs();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void initVars() {
        mTabName = getIntent().getStringExtra("tabName");
        mReposity = HomeRepository.getInstance();
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("合集列表_" + mTabName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews() {
        mMsView = (MultipleStatusView) findViewById(R.id.msView);
        mMsView.setOnRetryClickListener(v -> getTabs());
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void getTabs() {
        mMsView.showLoading();
        mReposity.getTabs()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray jsonArray) {
                        mMsView.showContent();
                        TabBean tabBean = new TabBean();
                        tabBean.tabs = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<TabBean.Tab>>() {
                        }.getType());
                        ArrayList<String>       titles     = new ArrayList<>();
                        ArrayList<BaseFragment> mFragments = new ArrayList<>();

                        for (int i = 0; i < tabBean.tabs.size(); i++) {
                            titles.add(tabBean.tabs.get(i).name);
                            mFragments.add(EntriesFragment.newInstance(tabBean.tabs.get(i).tab_id));
                            if (mTabName.equals(tabBean.tabs.get(i).name)) {
                                mPosition = i;
                            }
                        }

                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments, titles);
                        mViewPager.setAdapter(adapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                        mViewPager.setCurrentItem(mPosition);
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mMsView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    public static void toThisActivity(Activity activity, String tabName) {
        Intent intent = new Intent(activity, EntriesActivity.class);
        intent.putExtra("tabName", tabName);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
    }

}
