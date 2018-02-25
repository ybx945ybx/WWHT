package com.a55haitao.wwht.ui.activity.myaccount.settings;

import android.app.DownloadManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a55haitao.wwht.BuildConfig;
import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.EnforceUpdateReciever;
import com.a55haitao.wwht.data.model.entity.EnforceUpdateBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 * Created by 陶声 on 16/6/14.
 */
public class AboutUsActivity extends BaseNoFragmentActivity {

    @BindView(R.id.tv_version)        TextView       mTvVersion;         // 版本号
    @BindView(R.id.rl_user_agreement) RelativeLayout mRlUserAgreement;   // 隐私声明
    @BindView(R.id.rl_privacy_policy) RelativeLayout mRlPrivacyPolicy;   // 用户协议

    private Tracker               mTracker;                    // GA Tracker
    private boolean               mIsRegister;                 // 是否注册了广播
    private EnforceUpdateReciever mReceiver;

    private long clickTime = 0;                                // 第一次点击的时间
    private int  clickNum  = 0;                                // 点击次数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("关于我们");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
    }

    /**
     * 获取数据
     */
    public void loadData() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            mTvVersion.setText("55海淘 v" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 页面跳转
     */
    @OnClick({R.id.rl_user_agreement, R.id.rl_privacy_policy, R.id.llyt_secret})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user_agreement:
                // 埋点
                //                TraceUtils.addClick(TraceUtils.PageCode_H5, "", this, TraceUtils.PageCode_AboutUs, TraceUtils.PositionCode_UserLicense + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_H5, TraceUtils.PageCode_AboutUs, TraceUtils.PositionCode_UserLicense, "");

                H5Activity.toThisActivity(this, ServerUrl.USER_AGREEMENT_URL, "用户协议");
                break;
            case R.id.rl_privacy_policy:
                // 埋点
                //                TraceUtils.addClick(TraceUtils.PageCode_H5, "", this, TraceUtils.PageCode_AboutUs, TraceUtils.PositionCode_PrivacyStatement + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_H5, TraceUtils.PageCode_AboutUs, TraceUtils.PositionCode_PrivacyStatement, "");

                H5Activity.toThisActivity(this, ServerUrl.PRIVACY_POLICY_URL, "隐私声明");
                break;
            case R.id.llyt_secret:    //  开启隐藏入口

                if (System.currentTimeMillis() - clickTime > 2000) {
                    clickTime = System.currentTimeMillis();
                    clickNum = 0;
                } else {
                    clickNum++;
                    clickTime = System.currentTimeMillis();
                    if (clickNum > 5) {
                        clickNum = 0;
                        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                        String url = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                        H5Activity.toThisActivity(mActivity, url, "");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查更新
     */
    @OnClick(R.id.rl_settings_checkVersion)
    public void chechVersion() {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mReceiver = new EnforceUpdateReciever(this, String.valueOf(System.currentTimeMillis()));
        registerReceiver(mReceiver, intentFilter);
        mIsRegister = true;
        examAppVersion();
    }

    private void examAppVersion() {
        showProgressDialog("正在检查更新");
        SnsRepository.getInstance()
                .latestAppVer()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<EnforceUpdateBean>() {
                    @Override
                    public void onSuccess(EnforceUpdateBean result) {
                        //检查是否需要强制升级
                        showEnforceUpdateDialog(result);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private void showEnforceUpdateDialog(EnforceUpdateBean bean) {
        String title       = String.format("发现新版本v%s", bean.now_ver_num);
        Uri    uri         = Uri.parse(bean.download_link);
        int    versionCode = Integer.valueOf(bean.low_ver_desc);

        int newVersionCode = -1;
        try {
            newVersionCode = Integer.valueOf(bean.now_ver_desc);
        } catch (NumberFormatException e) {
            newVersionCode = -1;
        }

        //检查是否需要弹窗
        if (bean.now_ver_num != null && bean.now_ver_num.equals(BuildConfig.VERSION_NAME)) {
            Toast.makeText(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            return;
        } else if (newVersionCode != -1 && BuildConfig.VERSION_CODE >= newVersionCode) {
            Toast.makeText(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AboutUsActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert_Self).setTitle(title).setMessage(bean.change_desc);
        builder.setPositiveButton("立刻更新", (dialog, which) -> {
            dialog.dismiss();
            dowmAPKFile(uri);

        });
        builder.setCancelable(false);
        if (BuildConfig.VERSION_CODE >= versionCode) {      //不需要强制升级
            builder.setNegativeButton("稍后再说", (dialog, which) -> {
                dialog.dismiss();
            }).setCancelable(true);
        }
        builder.create().show();
    }

    private void dowmAPKFile(Uri uri) {
        //开启下载
        DownloadManager.Request request = null;
        try {
            request = new DownloadManager.Request(uri);
            //            loadData.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mReceiver.getFileName());
            request.setDestinationInExternalFilesDir(this, null, mReceiver.getFileName());
            request.setTitle("下载").setDescription("正在下载55海淘");
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
        } catch (IllegalArgumentException e) {      //捕捉Uri参数异常，low_ver_desc异常
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsRegister) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

}
