package com.a55haitao.wwht.adapter.myaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.myaccount.brandstore.BrandSellerFragmentFactory;

/**
 * 个人中心 商家&品牌 FragmentPagerAdapter
 *
 * @author 陶声
 * @since 2016-10-14
 */

public class BrandStorePagerAdapter extends FragmentPagerAdapter {
    public BrandStorePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return BrandSellerFragmentFactory.createFragment(position);
    }

    @Override
    public int getCount() {
        return BrandSellerFragmentFactory.TYPE_COUNT;
    }
}
