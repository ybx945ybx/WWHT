package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.StatementType.StatementType_Fashion;
import static com.a55haitao.wwht.data.model.annotation.StatementType.StatementType_H5;
import static com.a55haitao.wwht.data.model.annotation.StatementType.StatementType_Hot;
import static com.a55haitao.wwht.data.model.annotation.StatementType.StatementType_Null;
import static com.a55haitao.wwht.data.model.annotation.StatementType.StatementType_Tag;

/**
 * Created by Haotao_Fujie on 2016/11/11.
 */

@IntDef({StatementType_Null,StatementType_Hot,StatementType_Fashion,StatementType_Tag,StatementType_H5})
@Retention(RetentionPolicy.SOURCE)

public @interface StatementType {
    int StatementType_Null = 0;
    int StatementType_Hot = 1;            // 官网特卖 - 列表
    int StatementType_Fashion = 2;        // 潮流风尚 - 列表
    int StatementType_Tag = 3;            // 热门标签 - 列表
    int StatementType_H5 = 4;             // H5链接
    int StatementType_EO = 5;             // 草单列表

}
