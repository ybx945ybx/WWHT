package com.a55haitao.wwht.ui.fragment.myaccount.followfans;

import android.util.SparseArray;

import com.a55haitao.wwht.data.model.annotation.FollowFansFragmentType;

import static com.a55haitao.wwht.data.model.annotation.FollowFansFragmentType.FOLLOW;


/**
 * @author 陶声
 * @Description 关注 & 粉丝 Fragment 工厂
 * @date 2016-08-05
 */
public class FollowFansFragmentFactory {
    public static final int TYPE_COUNT = 2;

    private static SparseArray<FollowFansBaseFragment> fragments = new SparseArray<>();

    /**
     * 创建Fragment
     */
    public static FollowFansBaseFragment createFragment(@FollowFansFragmentType int type) {
       /* FollowFansBaseFragment fragment = fragments.get(type);
        if (fragment == null) {
            switch (type) {
                case FOLLOW:
                    fragment = new FollowFragment();
                    break;
                case FANS:
                    fragment = new FansFragment();
                    break;
            }
            fragments.put(type, fragment);
        }
        return fragment;*/

        return type == FOLLOW ? new FollowFragment() : new FansFragment();
    }

    /**
     * 清除Fragment
     */
    /*public static void clearFragments() {
        if (fragments != null)
            fragments.clear();
    }*/
}
