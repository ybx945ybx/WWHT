package com.a55haitao.wwht.adapter.myaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.easyopt.EasyOptFragment;
import com.a55haitao.wwht.utils.HaiUtils;

import java.util.ArrayList;

/**
 * 我的草单 FragmentAdapter
 *
 * @author 陶声
 * @since 2017-01-09
 */

public class MyEasyoptPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<EasyOptFragment> mFragments;

    public MyEasyoptPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragments.add(EasyOptFragment.newInstanceForOwer(HaiUtils.getUserId()));
        mFragments.add(EasyOptFragment.newInstanceForLiked());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "我创建的草单" : "我收藏的草单";
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
