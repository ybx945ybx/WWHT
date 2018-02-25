package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import com.a55haitao.wwht.ui.activity.myaccount.BrandAndSellerActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link BrandAndSellerActivity} 关注的品牌和商家页
 *
 * @author 陶声
 * @since 2016-08-29
 */
@IntDef({BrandSellerFragmentType.BRAND,
        BrandSellerFragmentType.SELLER})
@Retention(RetentionPolicy.SOURCE)

public @interface BrandSellerFragmentType {
    int BRAND = 0;   // 我关注的品牌

    int SELLER = 1;  // 我关注的商家
}
