package com.a55haitao.wwht.data.model.result;

/**
 * 他人主页 网络请求返回实体包装
 *
 * @author 陶声
 * @since 2016-11-17
 */

public class OthersAccountActivityResult {
    public UserInfoResult        userInfo;
    public GetUserPostListResult result;

    public OthersAccountActivityResult() {
    }

    public OthersAccountActivityResult(UserInfoResult userInfo, GetUserPostListResult result) {
        this.userInfo = userInfo;
        this.result = result;
    }
}
