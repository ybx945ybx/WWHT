package com.a55haitao.wwht;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.model.annotation.PostFragmentLayoutType;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.GlideImageLoader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mcxiaoke.packer.helper.PackerNg;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.magicwindow.Session;
import cn.xiaoneng.uiapi.Ntalker;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmSchema;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 全局Application
 *
 * @author 陶声
 * @since 2016-10-10
 */
public class HaiApplication extends MultiDexApplication {

    private static Context context;
    private static int appCount = 0;
    @PostFragmentLayoutType
    public static int layoutType;

    public static long   systemTime;
    public static byte[] adImage;           // 闪频页广告图

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        layoutType = PostFragmentLayoutType.DOUBLE;

        // 初始化Realm
        initRealm();
        // 初始化LeakCanary
        //        initLeakCanary();
        // 初始化bugly
        initHotFix();
        initBugly();
        // 魔窗
        initMW();
        // 初始化Logger
        Logger.init("wwht").logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        // 获取渠道名
        String channelId = PackerNg.getMarket(context, "normal");
        // 初始化友盟
        initUmeng(channelId);
        // 初始化talkingData
        initTalkingData(channelId);
        // 极光推送
        initJPush();
        // 初始化Stetho
        Stetho.initializeWithDefaults(this);
        // 初始化小能客服
        initNtalker();
        //  初始化字体文件
        HaiTextView.init(this);
        //  初始化fresco
        Fresco.initialize(this);
        initGalleryFinal();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 魔窗
     */
    private void initMW() {

        // mLink Session
        Session.setAutoSession(this);

    }

    /**
     * 初始化阿里HotFix
     */
    private void initHotFix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setAesKey(null)
                .setEnableDebug(BuildConfig.DEBUG)
                .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                    // 补丁加载回调通知
                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                        // 表明补丁加载成功
                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                        // 建议: 用户可以监听进入后台事件, 然后应用自杀
                        if (!HaiUtils.isForeground()) {
                            // 魔窗Session特殊处理
                            Session.onKillProcess();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                        // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                        SophixManager.getInstance().cleanPatches();
                    } else {
                        // 其它错误信息, 查看PatchStatus类说明
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    private void initNtalker() {
        Ntalker.getInstance().initSDK(context, "kf_9566", "10194A81-D54C-4C04-81CE-019B88C24307");
    }

    //    private void initLeakCanary() {
    //        if (LeakCanary.isInAnalyzerProcess(this)) {
    //            // This process is dedicated to LeakCanary for heap analysis.
    //            // You should not init your app in this process.
    //            return;
    //        }
    //        LeakCanary.install(this);
    //    }

    private void initBugly() {
        if (ServerUrl.BUGLYABLE) {
            CrashReport.initCrashReport(getApplicationContext(), "4c6197f619", false);
        }
    }

    private void initRealm() {
        // Realm初始化
        Realm.init(context);
        // Realm迁移
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2) // 版本号从0开始
                .migration((realm, oldVersion, newVersion) -> {
                    RealmSchema schema = realm.getSchema();

                    if (oldVersion < 2) {
                        if (!schema.contains("DeletedOrderListId")) {
                            schema.create("DeletedOrderListId")
                                    .addField("orderId", String.class, FieldAttribute.PRIMARY_KEY);
                        }
                        if (oldVersion == 0) {
                            if (!schema.contains("CommCountsBean")) {
                                schema.create("CommCountsBean")
                                        .addField("ready_to_receive_counts", int.class)
                                        .addField("commission_counts", int.class)
                                        .addField("coupon_counts", int.class)
                                        .addField("ready_to_deliver_counts", int.class)
                                        .addField("star_post_counts", int.class)
                                        .addField("ready_to_pay_counts", int.class)
                                        .addField("star_product_counts", int.class)
                                        .addField("like_counts", int.class)
                                        .addField("easyopt_start_count", int.class)
                                        .addField("post_counts", int.class)
                                        .addField("easyopt_count", int.class)
                                        .addField("star_special_counts", int.class)
                                        .addField("membership_point", int.class)
                                        .addField("follower_counts", int.class)
                                        .addField("following_counts", int.class);

                                schema.get("UserBean")
                                        .addRealmObjectField("commCounts", schema.get("CommCountsBean"));
                            }
                        }
                        oldVersion = 2;
                    }
                })
                .build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * 初始化talkingData
     *
     * @param umengChannelId 友盟渠道Id
     */
    private void initTalkingData(String umengChannelId) {
        // 初始化 appid / umengChannelId
        TalkingDataAppCpa.init(this, "EB0B5099E861488786FB760396627738", HaiUtils.getTalkingDataChannelId(umengChannelId));
        // 关闭talkingData日志
        TalkingDataAppCpa.setVerboseLogDisable();
    }

    /**
     * 初始化友盟
     *
     * @param channelId 渠道Id
     */
    private void initUmeng(String channelId) {
        //        Config.DEBUG = true;
        // 友盟appkey channelId
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(context, "5783795967e58e32890002d8", channelId));
        UMShareAPI.get(this);
        //微信 appid appsecret
        //        PlatformConfig.setWeixin("wxa2694e98318804c8", "20a27d1f391fe16698e053011499f91d");
        PlatformConfig.setWeixin("wxb3e5117a5073813d", "9cad2a14179412bf210d8f6e3dac9ef7");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("3743628679", "1a8954cca888d97acc0c0a8538c866f4", "http://sns.whalecloud.com/sina2/callback");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105730568", "EKlhBRz9v3EASKXx");
    }

    public static int getAppCount() {
        return appCount;
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.requestPermission(this);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(getApplicationContext());

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.ic_status_bar1;
        JPushInterface.setDefaultPushNotificationBuilder(builder);
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    /**
     * 初始化Gallery
     */
    private void initGalleryFinal() {
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0xFF, 0xFF, 0xFF))
                .setTitleBarTextColor(Color.BLACK)
                .setTitleBarIconColor(Color.rgb(0xCC, 0xCC, 0xCC))
                .setIconRotate(R.mipmap.ic_rotate)
                .setFabNormalColor(Color.rgb(0xFF, 0xCC, 0x40))
                .setFabPressedColor(Color.rgb(0xEC, 0xB4, 0x1C))
                .setCropControlColor(Color.WHITE)
                .setCheckNornalColor(Color.WHITE)
                .setIconBack(R.mipmap.ic_arrow_left_grey)
                .setIconFolderArrow(R.mipmap.ic_arrow_down_small)
                .setIconCrop(R.mipmap.ic_crop_img)
                .setIconCamera(R.mipmap.ic_take_photo_gallery)
                .setCheckSelectedColor(Color.BLACK)
                .setEditPhotoBgTexture(new ColorDrawable(Color.rgb(0x33, 0x33, 0x33)))
                .build();

        FunctionConfig sFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setForceCrop(true)
                .setForceCropEdit(false)
                .build();

        ImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(sFunctionConfig)
                .setNoAnimcation(true)
                .setTakePhotoFolder(new File(Environment.getExternalStorageDirectory() + "/wwht/" + "WuWuHaiTao/"))
                .build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * GA
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setAppOptOut(ServerUrl.ENV_TYPE != ServerUrl.PROD);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    /**
     * app进入后台上传统计数据到服务器 成功返回后清除本地数据
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            YbxTrace.getInstance().uploadErrorCache(context);
        }

    }

}
