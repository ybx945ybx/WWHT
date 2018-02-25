package com.a55haitao.wwht.adapter.firstpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.a55haitao.wwht.data.model.entity.TabBean;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.fragment.firstpage.FirstPageH5Fragment;
import com.a55haitao.wwht.ui.fragment.firstpage.FirstpageHotFragment;
import com.a55haitao.wwht.ui.fragment.firstpage.FirstpageTabFragment;

import java.util.ArrayList;

/**
 * Created by 董猛 on 2016/10/10.
 */

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mFragments;
    private ArrayList<TabBean.Tab>  mTabs;

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabViewPagerAdapter(FragmentManager fm, ArrayList<TabBean.Tab> tabs) {
        super(fm);
        mTabs = new ArrayList<>(tabs);
        mFragments = new ArrayList<>(tabs.size());
        for (TabBean.Tab tab : mTabs) {
            if (!TextUtils.isEmpty(tab.uri)) {
                mFragments.add(FirstPageH5Fragment.newInstance(tab.uri));
            } else {
                if (tab.name.equals("热门")){
                    mFragments.add(FirstpageHotFragment.newInstance(tab.name));
                }else {
                    mFragments.add(FirstpageTabFragment.newInstance(tab.name));

                }
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).name;
    }
}
