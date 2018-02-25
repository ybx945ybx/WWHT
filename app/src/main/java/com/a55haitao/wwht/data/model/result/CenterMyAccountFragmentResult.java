package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.UserBean;

/**
 * 个人中心 网络请求返回实体组合
 *
 * @author 陶声
 * @since 2016-11-17
 */

public class CenterMyAccountFragmentResult {
    public UserBean                       userBean;
    public GetUserStarInfoCountsV12Result getUserStarInfoCountsV12Result;

    public CenterMyAccountFragmentResult() {
    }


    public CenterMyAccountFragmentResult(UserBean userBean, GetUserStarInfoCountsV12Result getUserStarInfoCountsV12Result) {
        this.userBean = userBean;
        this.getUserStarInfoCountsV12Result = getUserStarInfoCountsV12Result;
    }
}
