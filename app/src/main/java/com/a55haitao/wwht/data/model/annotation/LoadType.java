package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.LoadType.LOAD_INIT;
import static com.a55haitao.wwht.data.model.annotation.LoadType.LOAD_PAGE;
import static com.a55haitao.wwht.data.model.annotation.LoadType.LOAD_REFRESH;

@IntDef({LOAD_INIT, LOAD_PAGE, LOAD_REFRESH})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadType {
    int LOAD_INIT = 100;
    int LOAD_PAGE = 101;
    int LOAD_REFRESH = 102;
}