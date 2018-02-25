package com.a55haitao.wwht.adapter.myaccount.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.myaccount.order.OrderFragmentFactory;


/**
 * 我的订单 ViewPager adapter
 * <p>
 * Created by 陶声 on 16/7/22.
 */
public class OrderPagerAdapter extends FragmentPagerAdapter {
    public OrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OrderFragmentFactory.createFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return OrderFragmentFactory.getFragmentName(position);
    }

    @Override
    public int getCount() {
        return OrderFragmentFactory.TYPE_COUNT;
    }
}
