package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.CheckThirdAccountResult;
import com.a55haitao.wwht.data.model.result.GetVerifyCodeResult;
import com.a55haitao.wwht.data.model.result.UserInfoResult;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * User相关接口
 *
 * @author 陶声
 * @since 2016-10-12
 */

public interface UserService {

    /**
     * 登录
     */
    @POST("m.api")
    Observable<ApiModel<UserBean>> login(@Body Map<String, Object> body);

    /**
     * 验证码登陆
     */
    @POST("m.api")
    Observable<ApiModel<UserBean>> checkMcodeLogin(@Body Map<String, Object> body);

    /**
     * 注册
     */
    @POST("m.api")
    Observable<ApiModel<UserBean>> register(@Body Map<String, Object> body);

    /**
     * 获取我的用户信息
     */
    @POST("m.api")
    Observable<ApiModel<UserBean>> myInfo(@Body Map<String, Object> body);

    /**
     * 获取用户信息
     */
    @POST("m.api")
    Observable<ApiModel<UserInfoResult>> userInfo(@Body Map<String, Object> body);

    /**
     * 退出登录
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> logout(@Body Map<String, Object> body);

    /**
     * 重置密码
     */
    @POST("m.api")
    Observable<ApiModel<UserBean>> resetPassword(@Body Map<String, Object> body);

    /**
     * 更新用户信息
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> updateProfile(@Body Map<String, Object> body);

    /**
     * 检查第三方账号信息
     */
    @POST("m.api")
    Observable<ApiModel<CheckThirdAccountResult>> checkThirdAccount(@Body Map<String, Object> body);

    /**
     * 绑定第三方
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> bindSSO(@Body Map<String, Object> body);

    /**
     * 第三方注册
     */
    @POST("m.api")
    Observable<ApiModel<UserBean>> thirdRegister(@Body Map<String, Object> body);

    /**
     * 第三方登录
     */
    @POST("m.api")
    Observable<ApiModel<UserBean>> thirdLogin(@Body Map<String, Object> body);

    /**
     * 验证码登陆 - 发送验证码
     */
    @POST("m.api")
    Observable<ApiModel<GetVerifyCodeResult>> getVerifyCode(@Body Map<String, Object> body);
}
