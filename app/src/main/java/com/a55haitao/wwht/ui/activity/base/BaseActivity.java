package com.a55haitao.wwht.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.event.APIErrorEvent;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.exception.HTException;
import com.a55haitao.wwht.data.model.result.RegisterDeviceResult;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.DeviceRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.myaccount.account.LoginMobileActivity;
import com.a55haitao.wwht.ui.view.HaiProgressDialog;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.magicwindow.MLink;
import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.Session;
import retrofit2.adapter.rxjava.HttpException;
import tom.ybxtracelibrary.YbxTrace;

/**
 * Created by 董猛 on 2016/10/8.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    public String TAG = getClass().getSimpleName();
    protected Activity          mActivity;
    private   HaiProgressDialog mHaiProgressDialog;
    protected AlertDialog       mNeedLoginDialog;
    public    boolean           mIsResumed;
    protected int PAGE_SIZE = 20;
    protected boolean mHasLoad;                         // 是否加载过一次

    // 用于页面跟踪的参数
    protected boolean hasCreated;        // 第一次进页面为false统计pageview事件后置为true，返回进入页面时不统计pageview事件
    public    String  pref;              // 前一页面
    public    String  prefh;             // 前一页面的hash
    public    String  purl;              // 当前页面
    public    String  purlh;             // 当前页面的hash

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case HaiConstants.ReponseCode.CODE_UTK_EMPTY:
                case HaiConstants.ReponseCode.CODE_DECODE_USER_TOKEN_ERROR:
                case HaiConstants.ReponseCode.CODE_USER_TOKEN_EXPIRED:
                case HaiConstants.ReponseCode.CODE_LOGINED_ON_OTHER_DEVICE:
                    UserRepository.getInstance().clearUserInfo();
                    EventBus.getDefault().post(new LoginStateChangeEvent(false));
                    showNeedLoginDialog(msg.arg1);
                    break;
                case HaiConstants.ReponseCode.CODE_DECODE_DEVICE_TOKEN_ERROR:       //DeviceToken异常
                    DeviceRepository.getInstance().clearDeviceToken();
                    DeviceRepository.getInstance()
                            .registerDevice()
                            .subscribe(new DefaultSubscriber<RegisterDeviceResult>() {
                                @Override
                                public void onSuccess(RegisterDeviceResult registerDeviceResult) {
                                    DeviceRepository.getInstance().setDeviceToken(registerDeviceResult.deviceToken);
                                    SPUtils.put(mActivity, "_dtk", registerDeviceResult.deviceToken);
                                }

                                @Override
                                public void onFinish() {
                                }
                            });
                    break;
                case 500:
                case 102023:
                    break;
                case 14000:
                    // 下架商品
                    EventBus.getDefault().post(new APIErrorEvent(14000, (String) msg.obj));
                    break;
                case 102054:
                    ToastUtils.showToast(mActivity, "该商品每次限购一件");
                    break;
                case 110:
                    // 兑换优惠券时候，兑换码过期
                    EventBus.getDefault().post(new APIErrorEvent(110, (String) msg.obj));
                    break;
                default:
                    String str = (String) msg.obj;
                    if (!TextUtils.isEmpty(str)) {
                        ToastUtils.showToast(BaseActivity.this, str);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        ActivityCollector.add(this);
        if (DeviceUtils.getScreenWidth() == 0) {
            DeviceUtils.init(this);
        }

        purlh = mActivity.toString().substring(mActivity.toString().indexOf("@") + 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Uri mLink = getIntent().getData();
        if (mLink != null) {
            MLinkAPIFactory.createAPI(mActivity).router(mLink);
        } else {
            MLink.getInstance(this).checkYYB();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session.onResume(this);

        mIsResumed = true;

        // 埋点 pageview事件      还没有全部埋点getActivityTAG为空说明还没埋
        if (!hasCreated && !TextUtils.isEmpty(getActivityTAG())) {
            hasCreated = true;
            BaseActivity frontActivity = ActivityCollector.getSecondActivity();
            if (frontActivity != null) {
                purl = getActivityTAG();
                pref = frontActivity.purl;
                prefh = frontActivity.purlh;
                YbxTrace.getInstance().pageView(this, frontActivity.purl, frontActivity. purlh, getActivityTAG(), purlh, "");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Session.onPause(this);

        mIsResumed = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remove(this);
        if (EventBus.getDefault().isRegistered(this)) {                  //防止内存泄漏
            EventBus.getDefault().unregister(this);
        }
    }

    protected abstract String getActivityTAG();

    public Handler getHandler() {
        return mHandler;
    }

    public void showNeedLoginDialog(int code) {
        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }

        if (mNeedLoginDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert_Self);
            switch (code) {
                case HaiConstants.ReponseCode.CODE_DECODE_USER_TOKEN_ERROR:
                    builder.setTitle("服务器异常");
                    break;
                case HaiConstants.ReponseCode.CODE_USER_TOKEN_EXPIRED:
                    builder.setTitle("账号身份已过期");
                    break;
                case HaiConstants.ReponseCode.CODE_UTK_EMPTY:
                case HaiConstants.ReponseCode.CODE_LOGINED_ON_OTHER_DEVICE:
                    builder.setTitle("账号已在其它设备登录");
                    break;
            }
            builder.setMessage("请重新登录")
                    .setCancelable(false)
                    .setPositiveButton("确定", (dialog, which) -> {
                        Intent intent = new Intent(BaseActivity.this, LoginMobileActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        dialog.dismiss();
                    });
            mNeedLoginDialog = builder.create();
        }
        mNeedLoginDialog.show();
    }

    public void showProgressDialog() {
        showProgressDialog(false);
    }

    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(null, cancelable);
    }

    public void showProgressDialog(String message) {
        showProgressDialog(message, false);
    }

    /**
     * 显示ProgressDialog
     *
     * @param message    显示文字
     * @param cancelable 是否可取消
     */
    public void showProgressDialog(String message, boolean cancelable) {
        if (mHaiProgressDialog == null) {
            mHaiProgressDialog = new HaiProgressDialog(this);
            mHaiProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (!mHaiProgressDialog.isShowing()) {
            mHaiProgressDialog.show();
        }
        mHaiProgressDialog.setMessage(message);
        mHaiProgressDialog.setCancelable(cancelable);
    }


    /**
     * 隐藏ProgressDialog
     */
    public void dismissProgressDialog() {
        if (mHaiProgressDialog == null) return;

        if (mHaiProgressDialog.isShowing()) {
            mHaiProgressDialog.setMessage("正在加载...");
            mHaiProgressDialog.dismiss();
        }
    }

    /**
     * 通用显示错误页
     *
     * @param msv     MultipleStatusView
     * @param e       异常
     * @param hasData 是否加载过数据
     */
    public void showFailView(MultipleStatusView msv, Throwable e, boolean hasData) {
        if ((e instanceof HttpException || e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException)
                && !hasData) {    // 网络问题
            msv.showNoNetwork();
        } else if (e instanceof HTException && !hasData) {
            msv.showError();
        } else if (!hasData) {
            msv.showError();
        }
    }

    /**
     * 商详页显示错误用
     */
    public void showProductFailView(MultipleStatusView msv, Throwable e, boolean hasProduct) {  //    fase 商品不存在，进来就是报14000错误  true 进来成功掉到接口数据
        if (e instanceof HttpException || e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {    // 网络问题
            msv.showNoNetwork();
        } else if (e instanceof HTException && !(((HTException) e).code == 14000)) {
            msv.showError();
        } else {
            msv.showError();
        }
    }

}
