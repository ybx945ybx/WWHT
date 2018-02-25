package com.a55haitao.wwht.data.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 55haitao on 2016/11/7.
 */

public class WxPayBean {
    /**
     * package : Sign=WXPay
     * timestamp : 1478488573
     * sign : BC6C93893ADB50AA030176219D972E85
     * partnerid : 1285483601
     * appid : wxa2694e98318804c8
     * prepayid : wx20161107111613673c3968940406614475
     * noncestr : OMgyYfcmBcYjNMLWHBnl
     */

    @SerializedName("package")
    public String packageX;
    public long timestamp;
    public String sign;
    public String partnerid;
    public String appid;
    public String prepayid;
    public String noncestr;
}
