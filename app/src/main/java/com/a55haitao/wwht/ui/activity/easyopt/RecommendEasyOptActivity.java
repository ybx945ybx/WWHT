package com.a55haitao.wwht.ui.activity.easyopt;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.common.ViewPagerAdapter;
import com.a55haitao.wwht.data.model.entity.EasyOptTagBean;
import com.a55haitao.wwht.data.model.result.EasyOptTagResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.fragment.easyopt.EasyOptFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现 - 精选草单
 */
public class RecommendEasyOptActivity extends BaseHasFragmentActivity {

    @BindView(R.id.tabLayout) TabLayout          mTabLayout;
    @BindView(R.id.viewPager) ViewPager          mViewPager;
    @BindView(R.id.msView)    MultipleStatusView msView;

    private Tracker mTracker;   // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_easy_opt);

        ButterKnife.bind(this);
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("精选草单");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        msView.setOnRetryClickListener(v -> getTag());

        getTag();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void getTag() {
        msView.showLoading();

        SnsRepository.getInstance()
                .easyoptTagList()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<EasyOptTagResult>() {
                    @Override
                    public void onSuccess(EasyOptTagResult result) {
                        msView.showContent();
                        ArrayList<String>       mPageTitles     = new ArrayList<>();
                        ArrayList<BaseFragment> mFragments = new ArrayList<>();
                        for (EasyOptTagBean bean : result.taglist) {
                            EasyOptFragment fragment = EasyOptFragment.newInstanceForTag(bean);
                            mFragments.add(fragment);
                            mPageTitles.add(bean.name);
                        }
                        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mPageTitles));
                        mTabLayout.setupWithViewPager(mViewPager);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
//                        mHasLoad = true;
                    }
                });

    }
}
