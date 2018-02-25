package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 社区Fragment类型
 * <p>
 * Created by 陶声 on 16/6/22.
 */
@IntDef({SocialFragmentType.POST,
        SocialFragmentType.FOLLOW})
@Retention(RetentionPolicy.SOURCE)

public @interface SocialFragmentType {

    int POST = 0;   // 精选

    int FOLLOW = 1; // 关注
}
