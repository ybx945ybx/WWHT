package com.a55haitao.wwht.data.repository;

import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.VerifyService;
import com.a55haitao.wwht.data.model.annotation.IdentifyCodeType;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.TreeMap;

import rx.Observable;

/**
 * 验证码仓库
 *
 * @author 陶声
 * @since 2016-10-20
 */

public class VerifyRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "55haitao_uc.VerifyService/";

    private static volatile VerifyRepository instance;
    private final           VerifyService    mVerifyService;

    private VerifyRepository() {
        mVerifyService = RetrofitFactory.createService(VerifyService.class);
    }

    public static VerifyRepository getInstance() {
        if (instance == null) {
            synchronized (VerifyRepository.class) {
                if (instance == null) {
                    instance = new VerifyRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 获取验证码
     *
     * @param type   验证码类型
     * @param mobile 手机号
     */
    public Observable<CommonDataBean> getNoLoginVerifyCode(@IdentifyCodeType int type, String mobile) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_nologin_verify_code");
        map.put("identify_type", type);
        map.put("mobile", mobile);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mVerifyService.getNoLoginVerifyCode(map));
    }

    /**
     * 获取验证码
     *
     * @param type        验证码类型
     * @param mobile      手机号
     * @param countryCode 国家区号
     */
    public Observable<CommonDataBean> getNoLoginVerifyCode(@IdentifyCodeType int type, String mobile, int countryCode) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_nologin_verify_code");
        map.put("identify_type", type);
        map.put("mobile", mobile);
        map.put("country", countryCode);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mVerifyService.getNoLoginVerifyCode(map));
    }
}
