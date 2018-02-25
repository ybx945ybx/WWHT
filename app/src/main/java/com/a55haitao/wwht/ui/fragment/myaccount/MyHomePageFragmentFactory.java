package com.a55haitao.wwht.ui.fragment.myaccount;

import android.util.SparseArray;

import com.a55haitao.wwht.data.model.annotation.MyHomePageFragmentType;
import com.a55haitao.wwht.utils.HaiUtils;

/**
 * 个人中心Fragment工厂
 *
 * @author 陶声
 * @since 2016-10-12
 */

public class MyHomePageFragmentFactory {

    public static final int TYPE_COUNT = 2;

    private static SparseArray<MyHomePageBaseFragment> fragments = new SparseArray<>();

    public static MyHomePageBaseFragment createFragment(@MyHomePageFragmentType int type) {
        MyHomePageBaseFragment fragment = fragments.get(type);
        if (fragment == null) {
            switch (type) {
                case MyHomePageFragmentType.POST:
                    fragment = SectionPostFragment.newInstance(HaiUtils.getUserId(), SectionPostFragment.TYPE_USER_POST);
                    break;
                case MyHomePageFragmentType.LIKE:
                    fragment = SectionPostFragment.newInstance(HaiUtils.getUserId(), SectionPostFragment.TYPE_LIKE_POST);
                    break;
            }
            fragments.put(type, fragment);
        }
        return fragment;
    }

   /* public static String getFragmentName(@MyHomePageFragmentType int type) {
        String result = "";
        switch (type) {
            case MyHomePageFragmentType.POST:
                result = "笔记";
                break;
            case MyHomePageFragmentType.LIKE:
                //                result = String.format("点赞 · %d", UserRepository.getInstance().getUserInfo().getLikeCount());
                result = "点赞";
                break;
        }
        return result;
    }*/

    public static void clearFragments() {
        if (fragments != null) {
            fragments.clear();
        }
    }
}
