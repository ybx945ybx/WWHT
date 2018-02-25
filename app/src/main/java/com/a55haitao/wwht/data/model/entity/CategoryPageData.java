package com.a55haitao.wwht.data.model.entity;

import com.a55haitao.wwht.data.model.result.EasyOptListResult;

/**
 * 分类页面数据集合
 *
 * @author 陶声
 * @since 2017-05-09
 */

public class CategoryPageData {
    public CategoryPageBean  mCategoryPageBean;
    public EasyOptListResult mEasyOptListResult;

    public CategoryPageData() {
    }

    public CategoryPageData(CategoryPageBean categoryPageBean, EasyOptListResult easyOptListResult) {
        mCategoryPageBean = categoryPageBean;
        mEasyOptListResult = easyOptListResult;
    }
}
