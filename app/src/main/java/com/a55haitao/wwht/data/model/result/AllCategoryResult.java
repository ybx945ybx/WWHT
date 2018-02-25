package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.CategoryBean;

import java.util.ArrayList;

/**
 * Created by 55haitao on 2016/11/9.
 */

public class AllCategoryResult {
    public String name;
    public int category_id;

    /**
     * 非服务器返还的字段
     * 用于标记ListView Item 项是否被选中
     */
    public boolean isChecked ;

    public ArrayList<CategoryBean> sub;

}
