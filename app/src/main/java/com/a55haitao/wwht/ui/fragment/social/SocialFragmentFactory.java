package com.a55haitao.wwht.ui.fragment.social;

import android.util.SparseArray;

import com.a55haitao.wwht.data.model.annotation.SocialFragmentType;

import static com.a55haitao.wwht.data.model.annotation.SocialFragmentType.FOLLOW;
import static com.a55haitao.wwht.data.model.annotation.SocialFragmentType.POST;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-10-12
 */

public class SocialFragmentFactory {
    public static final int TYPE_COUNT = 2;

    private static SparseArray<SocialBaseFragment> fragments = new SparseArray<>();

    /**
     * 创建Fragment
     */
    public static SocialBaseFragment createFragment(@SocialFragmentType int type) {
        SocialBaseFragment fragment = fragments.get(type);
        if (fragment == null) {
            switch (type) {
                case POST:
                    fragment = new SocialPostFragment();
                    break;
                case FOLLOW:
                    fragment = new FollowFragment();
                    break;
            }
            fragments.put(type, fragment);
        }
        return fragment;
    }

    /**
     * 获取Fragment名
     */
    public static String getFragmentName(@SocialFragmentType int type) {
        String name = null;
        switch (type) {
            case POST:
                name = "精选";
                break;
            case FOLLOW:
                name = "关注";
                break;
        }
        return name;
    }

    /**
     * 清除
     */
    public static void clearFragments() {
        if (fragments != null) {
            fragments.clear();
        }
    }
}
