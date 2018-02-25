package com.a55haitao.wwht.data.net.api;


import com.a55haitao.wwht.data.model.result.RegisterDeviceResult;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Device相关接口
 *
 * @author 陶声
 * @since 2016-10-12
 */

public interface DeviceService {
    /**
     * 注册设备
     */
    @POST("m.api")
    Observable<ApiModel<RegisterDeviceResult>> registerDevice(@Body Map<String, Object> body);

    /**
     * 注册极光推送
     */
    @POST("m.api")
    Observable<ApiModel<Boolean>> updatePushToken(@Body Map<String, Object> body);
}
