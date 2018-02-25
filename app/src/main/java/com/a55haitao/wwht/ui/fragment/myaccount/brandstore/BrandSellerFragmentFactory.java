package com.a55haitao.wwht.ui.fragment.myaccount.brandstore;


import android.util.SparseArray;

import com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType;

import static com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType.BRAND;
import static com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType.SELLER;

/**
 * 关注的品牌 & 关注的商家 Fragment 工厂
 * <p>
 * Created by 陶声 on 16/7/8.
 */
public class BrandSellerFragmentFactory {
    public static final int TYPE_COUNT = 2;

    private static SparseArray<BrandSellerBaseFragment> mFragments = new SparseArray<>();

    /**
     * 创建Fragment
     */
    public static BrandSellerBaseFragment createFragment(@BrandSellerFragmentType int type) {
        BrandSellerBaseFragment fragment = mFragments.get(type);
        if (fragment == null) {
            switch (type) {
                case BRAND:
                    fragment = new BrandFragment();
                    break;
                case SELLER:
                    fragment = new SellerFragment();
                    break;
            }
            mFragments.put(type, fragment);
        }
        return fragment;
    }

    /**
     * 清除Fragment
     */
    public static void clearFragments() {
        if (mFragments != null)
            mFragments.clear();
    }
}
