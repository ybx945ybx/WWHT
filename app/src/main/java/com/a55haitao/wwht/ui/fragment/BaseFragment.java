package com.a55haitao.wwht.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.exception.HTException;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.ToastUtils;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Fragment基类
 */

public class BaseFragment extends RxFragment {
    protected String   TAG;
    protected Activity mActivity;
    protected Fragment mFragment;
    protected int PAGE_SIZE = 20;
    protected boolean mHasData;         // 是否成功加载过数据

    public BaseFragment() {
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mFragment = this;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isResumed()) {
            onVisibilityChangedToUser(isVisibleToUser);
        }
    }

    //    当Fragment对用户的可见性发作了改动的时分就会回调此办法
    public void onVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (TAG != null) {
                MobclickAgent.onPageStart(TAG);
            }
        } else {
            if (TAG != null) {
                MobclickAgent.onPageEnd(TAG);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onVisibilityChangedToUser(true);
        }
    }

    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onVisibilityChangedToUser(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public final <T extends View> T findViewById(View parent, int id) {
        return (T) parent.findViewById(id);
    }

    // Show and Dismiss Dialog
    public void showProgressDialog() {
        showProgressDialog(StringConstans.TOAST_LOADING, false);
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
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showProgressDialog(message, cancelable);
        }
    }

    /**
     * 隐藏ProgressDialog
     */
    public void dismissProgressDialog() {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).dismissProgressDialog();
        }
    }

    /**
     * 错误提示
     *
     * @param e 异常
     */
    public void showFailView(Throwable e) {
        if (e instanceof HttpException || e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {    // 网络问题
            ToastUtils.showToast("网络请求失败，请检查您的网络设置");
        } else if (e instanceof HTException) {
            ToastUtils.showToast(((HTException) e).msg);
        } else {
            ToastUtils.showToast("服务器异常，请稍后再试");
        }
    }

    /**
     * 显示错误页
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
}
