package com.a55haitao.wwht.ui.activity.firstpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
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
import com.a55haitao.wwht.ui.fragment.firstpage.FavorableFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官网特卖列表页面
 */
public class FavorableActivity extends BaseHasFragmentActivity {

    @BindView(R.id.msView)    MultipleStatusView mMsView;
//    @BindView(R.id.tabLayout) TabLayout          mTabLayout;
//    @BindView(R.id.viewPager) ViewPager          mViewPager;

    private String         mTabName;
    private int            mPosition;               //  各频道页跳转过来定位到相应tab
    private HomeRepository mReposity;
    private Tracker        mTracker;

    private FavorableFragment favorableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorable);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
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
        mTracker.setScreenName("特卖列表_全部");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews(Bundle savedInstanceState) {
        mMsView.setOnRetryClickListener(v -> getTabs());
    }

    // 获取特卖tabs
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
//                            titles.add(tabBean.tabs.get(i).name);
//                            mFragments.add(FavorableFragment.newInstance(tabBean.tabs.get(i).tab_id));
                            if (mTabName.equals(tabBean.tabs.get(i).name)) {
//                                mPosition = i;

                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                favorableFragment = FavorableFragment.newInstance(tabBean.tabs.get(i).tab_id);
                                ft.add(R.id.content_view, favorableFragment);
                                ft.commit();
                            }
                        }

//                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments, titles);
//                        mViewPager.setAdapter(adapter);
//                        mTabLayout.setupWithViewPager(mViewPager);
//
//                        mViewPager.setCurrentItem(mPosition);
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

    public static void toThisActivity(Activity activity, String tabName, int type) {
        Intent intent = new Intent(activity, FavorableActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("tabName", tabName);
        activity.startActivity(intent);
    }
}
