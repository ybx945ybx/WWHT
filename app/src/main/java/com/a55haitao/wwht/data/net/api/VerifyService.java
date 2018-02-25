package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 验证码相关Api
 *
 * @author 陶声
 * @since 2016-10-12
 */

public interface VerifyService {
    /**
     * 获取验证码
     */
    @POST("m.api")
    Observable<ApiModel<CommonDataBean>> getNoLoginVerifyCode(@Body Map<String, Object> body);
}
