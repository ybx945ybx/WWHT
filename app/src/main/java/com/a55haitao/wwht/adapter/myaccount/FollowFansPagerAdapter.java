package com.a55haitao.wwht.adapter.myaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a55haitao.wwht.ui.fragment.myaccount.followfans.FollowFansFragmentFactory;


/**
 * {@link com.a55haitao.wwht.ui.activity.myaccount.FollowAndFansActivity} 关注和粉丝页面
 *
 * @author 陶声
 * @date 2016-08-05
 * @Description 关注 & 粉丝 ViewPager Adapter
 */
public class FollowFansPagerAdapter extends FragmentPagerAdapter {

    public FollowFansPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FollowFansFragmentFactory.createFragment(position);
    }

    @Override
    public int getCount() {
        return FollowFansFragmentFactory.TYPE_COUNT;
    }
}
