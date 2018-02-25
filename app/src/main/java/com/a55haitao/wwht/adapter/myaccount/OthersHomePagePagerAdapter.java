package com.a55haitao.wwht.adapter.myaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.easyopt.EasyOptFragment;
import com.a55haitao.wwht.ui.fragment.myaccount.SectionPostFragment;

import java.util.ArrayList;

/**
 * 他人主页Fragment Adapter
 *
 * @author 陶声
 * @since 2016-12-07
 */

public class OthersHomePagePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private static final int TYPE_COUNT = 2;

    /**
     * @param fm     FragmentManager
     * @param userId 他人Id
     */
    public OthersHomePagePagerAdapter(FragmentManager fm, int userId) {
        super(fm);
        mFragments = new ArrayList<>(TYPE_COUNT);
        mFragments.add(SectionPostFragment.newInstance(userId, SectionPostFragment.TYPE_USER_POST));
        mFragments.add(EasyOptFragment.newInstanceForOwer(userId));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return TYPE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "笔记" : "草单";
    }
}
