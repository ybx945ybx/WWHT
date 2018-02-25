package com.a55haitao.wwht.data.repository;

import android.text.TextUtils;

import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.UserService;
import com.a55haitao.wwht.data.model.result.CheckThirdAccountResult;
import com.a55haitao.wwht.data.model.result.GetVerifyCodeResult;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.UserInfoResult;
import com.a55haitao.wwht.data.model.annotation.AppLoginType;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.annotation.LoginType;
import com.a55haitao.wwht.data.model.annotation.Sex;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.utils.HaiParamPrepare;
import com.orhanobut.logger.Logger;

import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

/**
 * User仓库
 *
 * @author 陶声
 * @since 2016-10-13
 */

public class UserRepository extends BaseRepository {
    private static final String TAG = "UserRepository";

    private static final String BASE_METHOD_URL = "55haitao_uc.UserService/";

    private static volatile UserRepository sInstance;

    private static UserBean sUserBean;

    private final UserService mUserService;

    private UserRepository() {
        mUserService = RetrofitFactory.createService(UserService.class);
    }

    public static UserRepository getInstance() {
        if (sInstance == null) {
            synchronized (SnsRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取用户信息
     */
    public UserBean getUserInfo() {
        if (sUserBean == null) {
            Realm                  realm  = Realm.getDefaultInstance();
            RealmResults<UserBean> result = realm.where(UserBean.class).findAll();
            if (result.size() != 0) {
                sUserBean = result.get(0);
            } else {
                return null;
            }
        }
        return sUserBean;
    }

    /**
     * 保存用户信息
     */
    public void saveUserInfo(UserBean userBean) {
        sUserBean = userBean;

        // 保存到数据库
        Realm.getDefaultInstance()
                .executeTransactionAsync(realm1 -> {
                    RealmResults<UserBean> result = realm1.where(UserBean.class).findAll();
                    if (result.size() != 0) { // 已经包含，则删除
                        result.deleteAllFromRealm();
                    }
                    realm1.copyToRealmOrUpdate(userBean);
                }, () -> {
                    Logger.t(TAG).d("保存用户信息到数据库成功");
                });
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {
        Realm                  realm  = Realm.getDefaultInstance();
        RealmResults<UserBean> result = realm.where(UserBean.class).findAll();
        if (result.size() != 0) { // 已经包含，则删除
            realm.beginTransaction();
            result.deleteAllFromRealm();
            realm.commitTransaction();
        }
        sUserBean = null;
    }

    /**
     * 登录
     *
     * @param username     用户名
     * @param password     密码
     * @param loginType    登录类型
     * @param appLoginType App登录类型
     * @param regionId     国家码
     * @param src          1 app自然注册,  3 app 0元团;
     */
    public Observable<UserBean> login(String username, String password, @LoginType int loginType, @AppLoginType int appLoginType, String regionId, int src, String activity_id) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "login");
        paramMap.put("username", username);
        paramMap.put("password", password);
        paramMap.put("login_type", loginType);
        paramMap.put("app_login_type", appLoginType);
        paramMap.put("country", regionId);
        paramMap.put("src", src);
        if (!TextUtils.isEmpty(activity_id))
            paramMap.put("activity_id", activity_id);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.login(paramMap));
    }

    /**
     * 验证码登陆
     *
     * @param mobile      手机号
     * @param verifyCode  验证码
     * @param countryCode 国家码
     */
    public Observable<UserBean> checkMcodeLogin(String mobile, String verifyCode, int countryCode, int src, String activity_id) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "check_mcode_login");
        paramMap.put("mobile", mobile);
        paramMap.put("code", verifyCode);
        paramMap.put("country", String.format("+%d", countryCode));
        paramMap.put("src", src);
        if (!TextUtils.isEmpty(activity_id))
            paramMap.put("activity_id", activity_id);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.checkMcodeLogin(paramMap));
    }

    /**
     * 退出登录
     */
    public Observable<CommonDataBean> logout() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "logout");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mUserService.logout(paramMap));
    }

    /**
     * 注册
     */
    public Observable<UserBean> register(String mobile, String password, String verifyCode, int countryCode, int src, String activity_id) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "register");
        paramMap.put("mobile", mobile);
        paramMap.put("password", password);
        paramMap.put("identify_code", verifyCode);
        paramMap.put("country", countryCode);
        paramMap.put("src", src);
        if (!TextUtils.isEmpty(activity_id))
            paramMap.put("activity_id", activity_id);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.register(paramMap));
    }

    /**
     * 获取我的用户信息
     */
    public Observable<UserBean> myInfo() {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "myinfo");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mUserService.myInfo(paramMap));
    }

    /**
     * 获取用户信息
     */
    public Observable<UserInfoResult> userInfo(int userId) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "user_info");
        paramMap.put("user_id", userId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.userInfo(paramMap));
    }

    /**
     * 重置密码
     *
     * @param mobile      手机号
     * @param countryCode 国家码
     * @param newPwd      新密码
     * @param verifyCode  验证码
     */
    public Observable<UserBean> resetPassword(int countryCode, String mobile, String newPwd, String verifyCode) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "reset_password");
        paramMap.put("country", countryCode);
        paramMap.put("mobile", mobile);
        paramMap.put("password", newPwd);
        paramMap.put("identify_code", verifyCode);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.resetPassword(paramMap));
    }

    /**
     * 更新用户信息
     *
     * @param nickname 用户名
     * @param sex      性别
     * @param location 地区
     * @param headImg  头像
     */
    public Observable<CommonDataBean> updateProfile(String nickname, @Sex int sex, String location, String signature, String headImg) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "update_profile");
        paramMap.put("nickname", nickname);
        paramMap.put("sex", sex);
        paramMap.put("location", location);
        paramMap.put("signature", signature);
        if (headImg != null) {
            paramMap.put("head_img", headImg);
        }
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mUserService.updateProfile(paramMap));
    }

    /**
     * 更新用户信息     注册后引导用户修改昵称用
     *
     * @param nickname 用户名
     * @param headImg  头像
     */
    public Observable<CommonDataBean> updateProfile(String nickname, String headImg) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "update_profile");
        paramMap.put("nickname", nickname);
        if (headImg != null) {
            paramMap.put("head_img", headImg);
        }
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mUserService.updateProfile(paramMap));
    }

    /**
     * 检查第三方账号信息
     *
     * @param platform 平台
     * @param openId   open_id
     */
    public Observable<CheckThirdAccountResult> checkThirdAccount(String platform, String openId, String mAccessToken) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "check_third_account");
        paramMap.put("account_provider", platform);
        paramMap.put("mAccessToken", mAccessToken);
        paramMap.put("open_id", openId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.checkThirdAccount(paramMap));
    }

    /**
     * 绑定第三方
     *
     * @param platform    平台
     * @param openId      open_id
     * @param accessToken accessToken
     */
    public Observable<CommonDataBean> bindSSO(String platform, String openId, String accessToken) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "bind_sso");
        paramMap.put("account_provider", platform);
        paramMap.put("open_id", openId);
        paramMap.put("access_token", accessToken);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, paramMap);
        return transform(mUserService.bindSSO(paramMap));
    }

    /**
     * 第三方注册
     *
     * @param countryCode     国家码
     * @param mobile          手机号
     * @param headImg         头像
     * @param openId          open_id
     * @param accessToken     access_token
     * @param nickname        昵称
     * @param accountProvider 平台
     * @param verifyCode      验证码
     */
    public Observable<UserBean> thirdRegister(int countryCode, String mobile, String headImg, String openId, String accessToken, String nickname, String accountProvider, String verifyCode) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "third_register");
        paramMap.put("country", String.format("+%d", countryCode));
        paramMap.put("mobile", mobile);
        paramMap.put("head_img", headImg);
        paramMap.put("open_id", openId);
        paramMap.put("access_token", accessToken);
        paramMap.put("nickname", nickname);
        paramMap.put("password", "");
        paramMap.put("account_provider", accountProvider);
        paramMap.put("identify_code", verifyCode);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.thirdRegister(paramMap));
    }

    /**
     * 第三方登录
     *
     * @param accountProvider 平台
     * @param accessToken     access_token
     * @param openId          open_id
     */
    public Observable<UserBean> thirdLogin(String accountProvider, String accessToken, String openId, int src, String activity_id) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "third_login");
        paramMap.put("account_provider", accountProvider);
        paramMap.put("access_token", accessToken);
        paramMap.put("open_id", openId);
        paramMap.put("src", src);
        if (!TextUtils.isEmpty(activity_id))
            paramMap.put("activity_id", activity_id);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.thirdLogin(paramMap));
    }

    /**
     * 验证码登陆 - 发送验证码
     *
     * @param mobile      手机号
     * @param countryCode 国家码
     */
    public Observable<GetVerifyCodeResult> getVerifyCode(String mobile, int countryCode) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "get_verify_code");
        paramMap.put("mobile", mobile);
        paramMap.put("country", String.format("+%d", countryCode));
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mUserService.getVerifyCode(paramMap));
    }


}
