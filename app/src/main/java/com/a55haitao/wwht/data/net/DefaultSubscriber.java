package com.a55haitao.wwht.data.net;

import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.constant.HaiConstants.ReponseCode;
import com.a55haitao.wwht.data.event.APIErrorEvent;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.exception.HTException;
import com.a55haitao.wwht.data.model.result.RegisterDeviceResult;
import com.a55haitao.wwht.data.repository.DeviceRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Subscriber基类
 *
 * @author 陶声
 * @since 2016-10-10
 */
public abstract class DefaultSubscriber<T> extends rx.Subscriber<T> {

    private boolean closeTips;

    public DefaultSubscriber() {
    }

    // splashActivity界面不提示token问题弹登录
    public DefaultSubscriber(boolean closeTips) {
        this.closeTips = closeTips;
    }

    @Override
    public final void onCompleted() {
        onFinish();
    }

    @Override
    public final void onError(Throwable e) {
        Logger.d(e);
        if (e instanceof HttpException || e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {    // 网络问题
            if (onFailed(e)) {
                ToastUtils.showToast("网络请求失败，请检查您的网络设置");
            }
        } else if (e instanceof HTException) {
            switch (((HTException) e).code) {
                case 500:
                    if (onFailed(e)) {
                        ToastUtils.showToast("服务器异常，请稍后再试");
                    }
                    break;
                // UserToken异常
                case ReponseCode.CODE_UTK_EMPTY:
                case ReponseCode.CODE_DECODE_USER_TOKEN_ERROR:
                case ReponseCode.CODE_USER_TOKEN_EXPIRED:
                case ReponseCode.CODE_LOGINED_ON_OTHER_DEVICE:
                    if (!closeTips) {
                        UserRepository.getInstance().clearUserInfo();
                        EventBus.getDefault().post(new LoginStateChangeEvent(false));
                        ActivityCollector.sendExceptionMessage(((HTException) e).code, "");
                    }
                    break;

                // DeviceToken异常
                case ReponseCode.CODE_DECODE_DEVICE_TOKEN_ERROR: {
                    DeviceRepository.getInstance().clearDeviceToken();
                    DeviceRepository.getInstance()
                            .registerDevice()
                            .subscribe(new DefaultSubscriber<RegisterDeviceResult>() {
                                @Override
                                public void onSuccess(RegisterDeviceResult registerDeviceResult) {
                                    DeviceRepository.getInstance().setDeviceToken(registerDeviceResult.deviceToken);
                                }

                                @Override
                                public void onFinish() {
                                }
                            });
                    break;
                }

                case ReponseCode.CODE_SSO_ALREADY_BINDED:    // 已经绑定第三方账号
                    onFailed(e);
                    break;
                case ReponseCode.CODE_PRODUCT_NOT_FOUND:    // 下架商品
                case ReponseCode.CODE_INVALIDATE_POST:      // 帖子已失效
                    EventBus.getDefault().post(new APIErrorEvent(((HTException) e).code, ((HTException) e).msg));
                    break;
                //                case 14110:                     // get_activity  数据不存在
                case 102023:                     // get_activity  数据不存在
                    break;
//                case 102020:                     // 收货地址不存在
//                    break;
                case HaiConstants.ReponseCode.E_PRODUCT_CHeckPRICE_FAILURE:
                    break;
                default:
                    ToastUtils.showToast(((HTException) e).msg);
                    break;
            }
        } else {
            // 其他异常
            if (onFailed(e)) {
                ToastUtils.showToast("服务器异常，请稍后再试");
            }
            e.printStackTrace();
        }

        //        onFailed(e);
        onFinish();
    }

    @Override
    public final void onNext(T t) {
        onSuccess(t);
    }

    /**
     * 网络请求 成功的回调
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 网络请求 失败的回调
     *
     * @param e 异常
     * @return hasData
     */
    public boolean onFailed(Throwable e) {
        return true;
    }

    /**
     * 网络请求 结束的回调
     * Note: 无论成功、失败,该方法一定会执行
     * Note: 当被用作Rx map多个网络请求时的观察者时,onSuccess()会执行多次,而onFailed()、onFinish()只执行一次
     */
    public abstract void onFinish();
}
