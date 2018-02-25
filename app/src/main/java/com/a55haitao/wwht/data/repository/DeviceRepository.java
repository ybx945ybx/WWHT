package com.a55haitao.wwht.data.repository;

import android.text.TextUtils;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.DeviceService;
import com.a55haitao.wwht.data.model.result.RegisterDeviceResult;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.utils.HaiParamPrepare;
import com.a55haitao.wwht.utils.SPUtils;

import java.util.TreeMap;

import rx.Observable;

/**
 * Device 仓库
 *
 * @author 陶声
 * @since 2016-10-18
 */

public class DeviceRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "55haitao_uc.DeviceService/";
    private static volatile DeviceRepository instance;

    private       String        deviceToken;
    private final DeviceService mDeviceService;

    private DeviceRepository() {
        mDeviceService = RetrofitFactory.createService(DeviceService.class);
    }

    public static DeviceRepository getInstance() {
        if (instance == null) {
            synchronized (DeviceRepository.class) {
                if (instance == null) {
                    instance = new DeviceRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 获取DeviceToken
     */
    public String getDeviceToken() {
        if (TextUtils.isEmpty(deviceToken)) { // 从sp读取
            deviceToken = (String) SPUtils.get(HaiApplication.getContext(), "_dtk", "");
        }
        return deviceToken;
    }

    /**
     * 设置DeviceToken
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        SPUtils.put(HaiApplication.getContext(), "_dtk", deviceToken);
    }

    /**
     * 清除DeviceToken
     */
    public void clearDeviceToken() {
        this.deviceToken = null;
        SPUtils.remove(HaiApplication.getContext(), "_dtk");
    }

    /**
     * 注册设备
     */
    public Observable<RegisterDeviceResult> registerDevice() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "register_device");
        map.put("push_device_token", "");
        map.put("_dtk", "");
        HaiParamPrepare.buildParams(LoginLevels.NONE, map);
        return transform(mDeviceService.registerDevice(map));
    }

    /**
     * 注册极光推送Id
     */
    public Observable<Boolean> updatePushToken(String pushId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "update_push_token");
        map.put("push_device_token", pushId);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mDeviceService.updatePushToken(map));
    }

}
