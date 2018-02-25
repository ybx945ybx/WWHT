package com.a55haitao.wwht.wxapi.pay;

import android.content.Context;
import android.util.Log;

import com.a55haitao.wwht.data.model.entity.WxPayBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Random;
import java.util.TreeMap;

/**
 * Created by Drolmen
 * 微信支付封装类
 */
public class WxApi {
    public  IWXAPI  mWxApi;
    private Context mContext;

    public WxApi(Context context, boolean var) {
        this.mContext = context;
        mWxApi = WXAPIFactory.createWXAPI(mContext, Constants.WX_APP_ID);
    }

    public void regToWx() {
        // 将该app注册到微信
        mWxApi.registerApp(Constants.WX_APP_ID);
    }

    //pay
    public void
    payRequest2(WxPayBean bean) {
        /*if (mWxApi.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
            ToastUtils.showToast(mContext, "请先更新微信应用");
            return;
        }*/
        /*if (!mWxApi.isWXAppInstalled()) {
            ToastUtils.showToast(mContext, "请先安装微信应用");
            return;
        }
        if (!mWxApi.isWXAppSupportAPI()) {
            ToastUtils.showToast(mContext, "请先更新微信应用");
            return;
        }*/
        PayReq request = new PayReq();
        request.appId = bean.appid;
        request.partnerId = bean.partnerid;
        request.prepayId = bean.prepayid;
        request.packageValue = bean.packageX;
        request.nonceStr = bean.noncestr;
        request.timeStamp = String.valueOf(bean.timestamp);
        request.sign = bean.sign;
        //        TreeMap<String, Object> singParam = new TreeMap<>();
        //        singParam.put("appid", loadData.appId);
        //        singParam.put("noncestr", loadData.nonceStr);
        //        singParam.put("package", loadData.packageValue);
        //        singParam.put("partnerid", loadData.partnerId);
        //        singParam.put("prepayid", loadData.prepayId);
        //        singParam.put("timestamp", loadData.timeStamp);
        //        loadData.sign = genAppSign(singParam);

        regToWx();
        mWxApi.sendReq(request);
    }

    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String genAppSign(TreeMap<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        for (String string : params.keySet()) {
            sb.append(string);
            sb.append('=');
            sb.append(params.get(string));
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

}
