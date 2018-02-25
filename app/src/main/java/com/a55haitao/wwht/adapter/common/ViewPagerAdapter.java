package com.a55haitao.wwht.adapter.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import java.util.ArrayList;

/**
 * 通用ViewPagerAdapter
 * Created by a55 on 2017/4/11.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mFragments ;
    private ArrayList<String>  mTitles ;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> mFragments, ArrayList<String> titles){
        super(fm);
        mTitles = titles;
        this.mFragments = mFragments;
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
        return mTitles == null ? null : mTitles.get(position);
    }
}
