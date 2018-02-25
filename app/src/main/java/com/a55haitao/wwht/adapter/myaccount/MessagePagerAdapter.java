package com.a55haitao.wwht.adapter.myaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.myaccount.message.MessageFragmentFactory;


/**
 * 个人中心 - 消息 viewpager adapter
 * Created by 陶声 on 16/6/15.
 */
public class MessagePagerAdapter extends FragmentPagerAdapter {


    public MessagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MessageFragmentFactory.createFragment(position);
    }

    @Override
    public int getCount() {
        return MessageFragmentFactory.TYPE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MessageFragmentFactory.getFragmentName(position);
    }
}
