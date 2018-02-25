package com.a55haitao.wwht.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.a55haitao.wwht.data.model.entity.OrderLogsBean;
import com.a55haitao.wwht.data.model.entity.WxPayBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.PayResultActivity;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.wxapi.pay.AliPayResult;
import com.a55haitao.wwht.wxapi.pay.Constants;
import com.a55haitao.wwht.wxapi.pay.WxApi;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 支付结果页面
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private String mOrderId;
    private String serialNumber;
    private int    mAmount;
    private WxApi  api;
    private final String CANCEL_PAY = "用户取消支付";
    private boolean           mIsWxPay;
    private ArrayList<String> skulist;

    /**
     * Handler 处理支付宝支付调结果
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    AliPayResult payResult = new AliPayResult((String) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        paySuccess(false);
                    } else {
                        payFailed(CANCEL_PAY);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        api = new WxApi(this, false);
        boolean b = api.mWxApi.handleIntent(intent, this);

        if (b)
            return;

        mOrderId = intent.getStringExtra("PARAM1");
        mAmount = intent.getIntExtra("PARAM5", -1);
        mIsWxPay = intent.getBooleanExtra("PARAM3", true);

        skulist = intent.getStringArrayListExtra("PARAM6");
        serialNumber = intent.getStringExtra("PARAM7");

        //判断是否需要支付
        if (!intent.getBooleanExtra("PARAM4", true)) {
            paySuccess(true);
            return;
        }

        String payData = intent.getStringExtra("PARAM2");

        if (mIsWxPay) {
            showProgressDialog("正在请求中...", true);
            Observable.timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> api.payRequest2(new Gson().fromJson(payData, WxPayBean.class)));
        } else {
            //支付宝支付
            Observable.just(payData)
                    .observeOn(Schedulers.newThread())
                    .subscribe(aLong -> {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(WXPayEntryActivity.this);
                        // 调用支付接口，获取支付结果
                        String  result = alipay.pay(aLong, true);
                        Message msg    = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    });
        }

    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + mOrderId;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.mWxApi.handleIntent(intent, this);
    }

    private void paySuccess(boolean isNoNeedPay) {
        // 付款成功 埋点上报
        OrderLogsBean orderLogsBean = new OrderLogsBean();
        orderLogsBean.infoType = 0;
        orderLogsBean.paystatus = 2;
        orderLogsBean.paytimestamp = System.currentTimeMillis();
        orderLogsBean.ordertimestamp = 0;
        orderLogsBean.orderid = mOrderId;
        orderLogsBean.skulist = skulist;
        orderLogsBean.serialNumber = serialNumber;
        orderLogsBean.source = "";
        if (isNoNeedPay) {
            orderLogsBean.payType = 2;
        } else {
            orderLogsBean.payType = mIsWxPay ? 0 : 1;
        }
        String data = new Gson().toJson(orderLogsBean);
        orderLogs(data);

        dismissProgressDialog();
        TalkingDataAppCpa.onOrderPaySucc(String.valueOf(UserRepository.getInstance().getUserInfo().getId()), mOrderId,
                mAmount * 100, "CNY", mIsWxPay ? "微信" : "支付宝");
        PayResultActivity.toThisActivity(mActivity, mOrderId, true);
        finish();

    }

    private void payFailed(String reason) {
        // 付款失败 埋点上报
        OrderLogsBean orderLogsBean = new OrderLogsBean();
        orderLogsBean.infoType = 0;
        orderLogsBean.paystatus = 1;
        orderLogsBean.paytimestamp = System.currentTimeMillis();
        orderLogsBean.ordertimestamp = 0;
        orderLogsBean.orderid = mOrderId;
        orderLogsBean.skulist = skulist;
        orderLogsBean.serialNumber = serialNumber;
        orderLogsBean.source = "";
        orderLogsBean.payType = mIsWxPay ? 0 : 1;
        String data = new Gson().toJson(orderLogsBean);
        orderLogs(data);

        PayResultActivity.toThisActivity(mActivity, mOrderId, false);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        dismissProgressDialog();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {            //支付成功
                paySuccess(false);
            } else if (resp.errCode == -2) {    //支付失败
                payFailed(CANCEL_PAY);
            } else if (resp.errCode == -1) {
                payFailed(CANCEL_PAY);
            }

        }
    }

    public static void toThisActivity(Activity activity, String serialNumber, String orderId, String payData, ArrayList<String> skulist, int amount, boolean isWxPay, boolean needPay) {

        //检测是否安装微信
        if (isWxPay && !WXAPIFactory.createWXAPI(activity, Constants.WX_APP_ID).isWXAppInstalled()) {
            ToastUtils.showToast(activity, "请安装微信客户端");
            return;
        }

        if (WXAPIFactory.createWXAPI(activity, Constants.WX_APP_ID).getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
            ToastUtils.showToast(activity, "请先更新微信应用");
            return;
        }

        Intent intent = new Intent(activity, WXPayEntryActivity.class);
        intent.putExtra("PARAM1", orderId);
        intent.putExtra("PARAM2", payData);
        intent.putExtra("PARAM3", isWxPay);
        intent.putExtra("PARAM4", needPay);
        intent.putExtra("PARAM5", amount);
        intent.putStringArrayListExtra("PARAM6", skulist);
        intent.putExtra("PARAM7", serialNumber);
        activity.startActivity(intent);
    }

    public static void toThisActivity(Activity activity, String orderId, String payData, int amount, boolean isWxPay, boolean needPay) {

        //检测是否安装微信
        if (isWxPay && !WXAPIFactory.createWXAPI(activity, Constants.WX_APP_ID).isWXAppInstalled()) {
            ToastUtils.showToast(activity, "请安装微信客户端");
            return;
        }

        if (WXAPIFactory.createWXAPI(activity, Constants.WX_APP_ID).getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
            ToastUtils.showToast(activity, "请先更新微信应用");
            return;
        }

        Intent intent = new Intent(activity, WXPayEntryActivity.class);
        intent.putExtra("PARAM1", orderId);
        intent.putExtra("PARAM2", payData);
        intent.putExtra("PARAM3", isWxPay);
        intent.putExtra("PARAM4", needPay);
        intent.putExtra("PARAM5", amount);
        activity.startActivity(intent);
    }

    private void orderLogs(String data) {
        ProductRepository.getInstance()
                .orderLogs(data)
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

}
