package com.a55haitao.wwht.ui.fragment.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.TabViewPagerAdapter;
import com.a55haitao.wwht.data.model.entity.TabBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.discover.SearchWordsActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.DisableViewPage;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 首页根Fragment
 */
public class CenterFirstPageFragment extends BaseFragment {

    @BindView(R.id.tabLayout) TabLayout          mTabLayout;
    @BindView(R.id.viewPager) DisableViewPage    mViewPager;
    @BindView(R.id.msView)    MultipleStatusView mMsView;

    private Unbinder       mUnbinder;
    private LayoutInflater mInflater;
    private boolean        initCustomTabView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) mActivity).purl = getClass().getSimpleName();
        ((BaseActivity) mActivity).purlh = this.toString().substring(this.toString().indexOf("{") + 1, this.toString().indexOf("{") + 8);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center_first_page, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mMsView.setOnRetryClickListener(v -> getTab());
        mInflater = LayoutInflater.from(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTab();
    }

    private void getTab() {
        mMsView.showLoading();
        requestHomeTabs();
    }

    private void requestHomeTabs() {
        HomeRepository.getInstance()
                .homeTabs()
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<TabBean>() {
                    @Override
                    public void onSuccess(TabBean tabBean) {
                        mMsView.showContent();
                        TabBean.Tab tab = new TabBean.Tab();
                        tab.name = "热门";
                        tabBean.tabs.add(0, tab);

                        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getChildFragmentManager(), tabBean.tabs);
                        mViewPager.setAdapter(adapter);
                        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageSelected(int position) {
                                if (!TextUtils.isEmpty(tabBean.tabs.get(position).uri)) {
                                    mViewPager.setPagingEnabled(false);
                                } else {
                                    mViewPager.setPagingEnabled(true);
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });
                        mTabLayout.setupWithViewPager(mViewPager);
                        mViewPager.setOffscreenPageLimit(20);

                        for (int i = 0; i < tabBean.tabs.size(); i++) {
                            TabLayout.Tab tabItem = mTabLayout.getTabAt(i);
                            tabItem.setCustomView(getTabView(i, tabBean.tabs.get(i).name));
                        }
                        mHasData = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mMsView, e, mHasData);
                        return mHasData;
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    private View getTabView(int position, String title) {
        View     view     = mInflater.inflate(R.layout.tab_view_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_title);
        textView.setText(title);
        if (position == 0 & !initCustomTabView) {
            view.setSelected(true);
            initCustomTabView = true;
        }
        return view;
    }

    @OnClick(R.id.searchLayout)
    public void onTitleClick() {
        // 埋点
        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_SearchBar, TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Hot_Search);
        // 跳转
        startActivity(new Intent(getContext(), SearchWordsActivity.class));
        getActivity().overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}