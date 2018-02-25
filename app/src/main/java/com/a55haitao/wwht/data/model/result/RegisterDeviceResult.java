package com.a55haitao.wwht.data.model.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 注册设备
 *
 * @author 陶声
 * @since 2016-10-18
 */

public class RegisterDeviceResult implements Serializable {
    @SerializedName("device_token")
    public String deviceToken;

    public boolean success;
}
