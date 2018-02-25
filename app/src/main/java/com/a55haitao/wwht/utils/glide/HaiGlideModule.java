package com.a55haitao.wwht.utils.glide;

import android.content.Context;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * class description here
 *
 * @author 陶声
 * @since 2017-03-15
 */

public class HaiGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
