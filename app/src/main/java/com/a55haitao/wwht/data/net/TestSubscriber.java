package com.a55haitao.wwht.data.net;

import android.text.TextUtils;
import android.widget.Toast;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.event.APIErrorEvent;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.exception.HTException;
import com.a55haitao.wwht.data.interfaces.IReponse;
import com.a55haitao.wwht.data.model.result.RegisterDeviceResult;
import com.a55haitao.wwht.data.repository.DeviceRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by 董猛 on 2016/10/25.
 */

public class TestSubscriber<T> extends Subscriber<ResponseBody> {

    private IReponse<T>        mReponse;
    private MultipleStatusView mMultipleStatusView;
    private boolean            closeTips;               //splashActivity界面为true不提示token问题弹登录

    public TestSubscriber(IReponse<T> mReponse) {
        this(mReponse, null);
    }

    public TestSubscriber(IReponse<T> mReponse, MultipleStatusView view) {
        this.mReponse = mReponse;
        if (view != null) {
            this.mMultipleStatusView = view;
        }
    }

    // splashActivity界面不提示token问题弹登录
    public TestSubscriber(IReponse<T> mReponse, boolean closeTips) {
        this.mReponse = mReponse;
        this.closeTips = closeTips;
    }

    @Override
    public void onStart() {
        super.onStart();
        showLoadingView();
    }

    @Override
    public final void onCompleted() {
        mReponse.onFinish();
    }

    @Override
    public final void onError(Throwable e) {
        Logger.d(e);
        showLoadErrorView();
        if (e instanceof HttpException || e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {    // 网络问题
//            if (onFailed(e)) {
                ToastUtils.showToast("网络请求失败，请检查您的网络设置");
//            }
        } else if (e instanceof HTException) {
            switch (((HTException) e).code) {
                case 500:
                    break;
                // 用户Token异常
                case HaiConstants.ReponseCode.CODE_UTK_EMPTY:
                case HaiConstants.ReponseCode.CODE_DECODE_USER_TOKEN_ERROR:
                case HaiConstants.ReponseCode.CODE_USER_TOKEN_EXPIRED:
                case HaiConstants.ReponseCode.CODE_LOGINED_ON_OTHER_DEVICE: {
                    if (!closeTips) {
                        UserRepository.getInstance().clearUserInfo();
                        EventBus.getDefault().post(new LoginStateChangeEvent(false));
                        ActivityCollector.sendExceptionMessage(((HTException) e).code, "");
                    }
                    break;
                }

                //DeviceToken异常
                case HaiConstants.ReponseCode.CODE_DECODE_DEVICE_TOKEN_ERROR: {
                    DeviceRepository.getInstance().clearDeviceToken();
                    DeviceRepository.getInstance()
                            .registerDevice()
                            .subscribe(new DefaultSubscriber<RegisterDeviceResult>() {
                                @Override
                                public void onSuccess(RegisterDeviceResult registerDeviceResult) {
                                    DeviceRepository.getInstance().setDeviceToken(registerDeviceResult.deviceToken);
                                    // SPUtils.put(, "_dtk", registerDeviceResult.deviceToken);
                                }

                                @Override
                                public void onFinish() {
                                }
                            });
                    break;
                }

                case HaiConstants.ReponseCode.CODE_SSO_ALREADY_BINDED:    // 已经绑定第三方账号
                    ToastUtils.showToast(((HTException) e).msg);
                    break;
                case HaiConstants.ReponseCode.CODE_PRODUCT_NOT_FOUND:    // 下架商品
                case HaiConstants.ReponseCode.CODE_INVALIDATE_POST:      // 帖子已失效
                    EventBus.getDefault().post(new APIErrorEvent(((HTException) e).code, ((HTException) e).msg));
                    break;
                //                case 14110:                     // get_activity  数据不存在
                case 102023:                     // get_activity  数据不存在
                    break;
                default:
                    ToastUtils.showToast(((HTException) e).msg);
                    break;
            }
        } else {

            e.printStackTrace();
        }
        mReponse.onError(e);
        mReponse.onFinish();
    }

    @Override
    public void onNext(ResponseBody apiResult) {
        try {
            String     reponseString = apiResult.string();
            JSONObject jsonObject    = new JSONObject(reponseString);
            int        code          = jsonObject.getInt("code");
            if (code != 0) {
                if (!closeTips) {
                    ActivityCollector.sendExceptionMessage(code, jsonObject.getString("msg"));
                    showLoadErrorView();
                }
                return;
            }
            showLoadSuccessView();
            if (!TextUtils.isEmpty(jsonObject.optString("stat"))) {
                JSONObject jsonStat = new JSONObject(jsonObject.getString("stat"));
                HaiApplication.systemTime = jsonStat.optLong("systime");
//                ToastUtils.showToast(HaiApplication.systemTime + "限时抢购");
            }

            Type type = HaiUtils.getParamterized(mReponse);
            if (type instanceof Class<?>) {
                Class<T> classFromType = (Class<T>) type;
                if (classFromType == String.class) {
                    mReponse.onSuccess((T) jsonObject.getString("data").toString());
                } else if (classFromType == JSONObject.class) {
                    mReponse.onSuccess((T) jsonObject.getJSONArray("data"));
                } else if (classFromType == JSONArray.class) {
                    mReponse.onSuccess((T) jsonObject.getJSONArray("data"));
                } else {
                    mReponse.onSuccess(new Gson().fromJson(jsonObject.getString("data"), classFromType));
                }
            } else if (type instanceof ParameterizedType) {
                mReponse.onSuccess(new Gson().fromJson(jsonObject.getString("data"), TypeToken.get(type).getType()));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {            //服务器数据格式异常时，作null处理
            mReponse.onSuccess(null);
        }
    }

    private void showLoadingView() {
        if (hasStatusView()) {
            mMultipleStatusView.showLoading();
        }
    }

    private void showLoadErrorView() {
        if (hasStatusView()) {
            mMultipleStatusView.showError();
        }
    }

    private void showLoadSuccessView() {
        if (hasStatusView()) {
            mMultipleStatusView.showContent();
        }
    }

    private boolean hasStatusView() {
        return !(mMultipleStatusView == null);
    }
}
