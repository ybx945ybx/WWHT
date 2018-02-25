package com.a55haitao.wwht.adapter.social;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.social.SocialFragmentFactory;

/**
 * 社区ViewPager Adapter
 * {@link com.a55haitao.wwht.ui.fragment.social.CenterSocialFragment 社区Fragment}
 *
 * @author 陶声
 * @since 2016-10-12
 */
public class SocialPagerAdapter extends FragmentPagerAdapter {

    public SocialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SocialFragmentFactory.createFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return SocialFragmentFactory.getFragmentName(position);
    }

    @Override
    public int getCount() {
        return SocialFragmentFactory.TYPE_COUNT;
    }
}
