package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel.SINGLE;
import static com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel.TWICE;
import static com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel.TRIBBLE;
import static com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel.FOURTH;

/**
 * Created by Haotao_Fujie on 16/10/13.
 */

@IntDef({SINGLE, TWICE, TRIBBLE, FOURTH})
@Retention(RetentionPolicy.SOURCE)

public @interface UpaiPictureLevel {

    int SINGLE = 1;

    int TWICE = 2;

    int TRIBBLE = 3;

    int FOURTH = 4;

}
