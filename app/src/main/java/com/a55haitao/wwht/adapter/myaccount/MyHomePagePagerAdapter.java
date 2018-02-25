package com.a55haitao.wwht.adapter.myaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.myaccount.MyHomePageFragmentFactory;
import com.a55haitao.wwht.ui.fragment.myaccount.SectionPostFragment;

import java.util.ArrayList;

/**
 * 个人主页Fragment Adapter
 *
 * @author 陶声
 * @since 2016-12-07
 */

public class MyHomePagePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private static final int TYPE_COUNT = 2;


    public MyHomePagePagerAdapter(FragmentManager fm, int userId) {
        super(fm);
        mFragments = new ArrayList<>(TYPE_COUNT);
        mFragments.add(SectionPostFragment.newInstance(userId, SectionPostFragment.TYPE_USER_POST));
        mFragments.add(SectionPostFragment.newInstance(userId, SectionPostFragment.TYPE_LIKE_POST));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return MyHomePageFragmentFactory.TYPE_COUNT;
    }
}
