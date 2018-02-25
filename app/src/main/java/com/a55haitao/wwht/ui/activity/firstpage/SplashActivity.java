package com.a55haitao.wwht.ui.activity.firstpage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.a55haitao.wwht.BuildConfig;
import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.model.result.AdResult;
import com.a55haitao.wwht.data.model.result.RegisterDeviceResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.DeviceRepository;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.mcxiaoke.packer.helper.PackerNg;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.ButterKnife;
import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.mlink.YYBCallback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import tom.ybxtracelibrary.Entity.TraceCommonBean;
import tom.ybxtracelibrary.YbxTrace;

public class SplashActivity extends BaseActivity {
    @BindString(R.string.key_is_first_enter) String KEY_IS_FIRST_ENTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        //        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
        //            //如果你就放在launcher Activity中话，这里可以直接return了
        //            Intent mainIntent = getIntent();
        //            String action     = mainIntent.getAction();
        //            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
        //                finish();
        //                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
        //            }
        //        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 页面跟踪
        initYbxTrace();

        // 初始化魔窗
        initMW();
        // 魔窗注册
        MLinkAPIFactory.createAPI(this).registerWithAnnotation(this);

        // 友盟统计是否加密
        MobclickAgent.enableEncrypt(!BuildConfig.DEBUG);
        // 禁止Umeng默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);

        // 获取闪屏页广告图
        getAdImage();

        String dtk = String.valueOf(SPUtils.get(this, "_dtk", ""));

        if (TextUtils.isEmpty(dtk)) {
            DeviceRepository.getInstance()
                    .registerDevice()
                    .subscribe(new DefaultSubscriber<RegisterDeviceResult>() {
                        @Override
                        public void onSuccess(RegisterDeviceResult registerDeviceResult) {
                            DeviceRepository.getInstance().setDeviceToken(registerDeviceResult.deviceToken);
                            SPUtils.put(getApplicationContext(), "_dtk", registerDeviceResult.deviceToken);
                        }

                        @Override
                        public void onFinish() {
                        }
                    });

        }

        // 页面跟踪lanuch埋点
        YbxTrace.getInstance().launch(this);

        // 魔窗的跳转逻辑
        if (SplashActivity.this.getIntent().getData() != null) {
            MLinkAPIFactory.createAPI(SplashActivity.this).router(mActivity, SplashActivity.this.getIntent().getData());
            //跳转后结束当前activity
            SplashActivity.this.finish();
        } else {
            //如果需要应用宝跳转，则调用。否则不需要
            MLinkAPIFactory.createAPI(SplashActivity.this).checkYYB(mActivity, new YYBCallback() {
                @Override
                public void onFailed(Context context) {
                    Observable.timer(1, TimeUnit.SECONDS)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .map(s -> (Boolean) SPUtils.get(SplashActivity.this, KEY_IS_FIRST_ENTER, true))
                            .subscribe(isFirstLauch -> {
                                Intent intent = new Intent(SplashActivity.this, isFirstLauch ? GuideActivity.class : AdActivity.class);
                                SplashActivity.this.startActivity(intent);
                                SplashActivity.this.finish();
                            });

                }

                @Override
                public void onSuccess() {
                    SplashActivity.this.finish();
                }
            });

        }

    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initMW() {
        MWConfiguration config = new MWConfiguration(this);
        config.setLogEnable(BuildConfig.DEBUG);
        //获取渠道名
        String channelId = PackerNg.getMarket(mActivity, "normal");
        //设置渠道，非必须（渠道推荐在AndroidManifest.xml内填写）
        config.setChannel(channelId);
        MagicWindowSDK.initSDK(config);
    }

    /**
     * 页面跟踪数据统计
     */
    private void initYbxTrace() {
        TraceCommonBean traceCommonBean = new TraceCommonBean();

        int uid = 0;
        try {
            if (HaiUtils.isLogin()) {
                uid = UserRepository.getInstance().getUserInfo().getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        traceCommonBean.mid = uid + "";
        traceCommonBean.v = BuildConfig.VERSION_NAME;
        traceCommonBean.bid = "gwzg";
        traceCommonBean.iev = Build.BRAND + ","
                + android.os.Build.MODEL + ","
                + android.os.Build.VERSION.SDK_INT + ","
                + android.os.Build.VERSION.RELEASE + ","
                + Build.USER;
        YbxTrace.initTrace(mActivity, traceCommonBean, 1);
        YbxTrace.setUploadSwitch(ServerUrl.ACTIVITYANALY);  // 是否上传的开关

    }

    // 获取开屏页广告图
    private void getAdImage() {
        HomeRepository.getInstance()
                .indexAdvert()
                .subscribe(new DefaultSubscriber<AdResult>(true) {
                    @Override
                    public void onSuccess(AdResult adResult) {
                        if (adResult == null || adResult.advert == null) {
                            HaiApplication.adImage = null;
                            return;
                        }

                        String adImg = adResult.advert.img;
                        String adUri = adResult.advert.uri;
                        SPUtils.put(SplashActivity.this, "adUri", adUri);

                        if (!TextUtils.isEmpty(adImg)) {
                            download(adImg);
                        }

                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }

    private void download(String adImg) {
        new Thread(() -> {
            try {
                byte[] data = getImage(adImg);
                if (data != null) {
                    HaiApplication.adImage = data;// bitmap
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public byte[] getImage(String path) throws Exception {
        URL               url  = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]                buffer    = new byte[1024];
        int                   len       = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Uri mLink = intent.getData();
        if (mLink != null) {
            MLinkAPIFactory.createAPI(mActivity).router(mLink);
        } else {
            MLinkAPIFactory.createAPI(mActivity).checkYYB();
        }
    }
}
