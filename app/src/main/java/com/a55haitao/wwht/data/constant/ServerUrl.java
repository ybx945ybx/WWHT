package com.a55haitao.wwht.data.constant;

/**
 * Created by 董猛 on 2016/10/8.
 */

import android.os.Environment;

import com.a55haitao.wwht.BuildConfig;

/**
 * ServerUrl URL
 */
public class ServerUrl {

    public static final boolean BUGLYABLE     = false;  // bugly开关,发版本时设为true
    public static final boolean ACTIVITYANALY = false;   // 页面跟踪开关,发版本时设为true
    public static final int     DEV           = 0;      // 开发
    public static final int     TEST          = 1;      // 测试
    public static final int     PROD          = 2;      // 生产

    public static int ENV_TYPE = PROD; // 环境

    static {

        if (ENV_TYPE == DEV) {     //DEV 开发环境
            SERVER_URL = "http://api.dev.55haitao.com/";

            H5_BASE_URL = "http://h5.dev.55haitao.com/";

            H5_Domain = ".dev.55haitao.com";

            Alipay_Notify_URL = "http://113.204.216.142:1888/mall_app_alipay_notify";
            Wxpay_Notify_URL = "http://113.204.216.142:1888/mall_app_wechat_notify";

            PRODUCT_INTRODUCE_URL = "http://share.missbaijie.com/baijie_h5/notice3.html";
            VERSION_INTRODUCE_URL = "http://share.missbaijie.com/baijie_h5/version.html";
            PUSH_GUIDE_URL = "http://m.dev.55shantao.com/baijie_h5/notification.html";
            HELP_INTRODUCE_URL = "http://plugin.dev.55haitao.com/#/readme";
            INVITE_USER_BASEURL = "http://h5.dev.55haitao.com/discount?user_id=";

            USER_AGREEMENT_URL = "http://h5.test.55haitao.com/agreement.html";   // 用户协议
            PRIVACY_POLICY_URL = "http://h5.test.55haitao.com/privacy.html";     // 隐私声明
            Commission_URL = "http://plugin.dev.55haitao.com/#/my_commission";
            Commission_Detail_URL = "http://plugin.dev.55haitao.com/#/commission_detail";
            Coupons_URL = "http://plugin.dev.55haitao.com/#/coupons";
            CART_URL = "http://plugin.dev.55haitao.com/#/cart/home";
            CACHE_URL = "http://plugin.dev.55haitao.com/#/home";
            TAXEXPLAIN_URL = "http://plugin.dev.55haitao.com/#/readme/tax?_k=bhp25s";
            MF_URL_PRODUCT = "http://plugin.dev.55haitao.com/#/product/%= ";

            Mall_Get_Coupon_Url = "http://h5.dev.55haitao.com/activity/coupon?utm_source=icon";
        } else if (ENV_TYPE == TEST) {   //测试环境TEST

            SERVER_URL = "http://api.test.55haitao.com/";

            H5_BASE_URL = "http://h5.test.55haitao.com/";

            H5_Domain = ".dev.55haitao.com";

            Alipay_Notify_URL = "http://113.204.216.142:1888/mall_app_alipay_notify";
            Wxpay_Notify_URL = "http://113.204.216.142:1888/mall_app_wechat_notify";

            PRODUCT_INTRODUCE_URL = "http://share.missbaijie.com/baijie_h5/notice3.html";
            VERSION_INTRODUCE_URL = "http://share.missbaijie.com/baijie_h5/version.html";
            PUSH_GUIDE_URL = "http://m.test.55shantao.com/baijie_h5/notification.html";
            HELP_INTRODUCE_URL = "http://plugin.test.55haitao.com/#/readme";
            INVITE_USER_BASEURL = "http://h5.test.55haitao.com/discount?user_id=";

            USER_AGREEMENT_URL = "http://h5.test.55haitao.com/agreement.html";   // 用户协议
            PRIVACY_POLICY_URL = "http://h5.test.55haitao.com/privacy.html";   // 隐私声明
            Commission_URL = "http://plugin.test.55haitao.com/#/my_commission";
            Commission_Detail_URL = "http://plugin.test.55haitao.com/#/commission_detail";
            Coupons_URL = "http://plugin.test.55haitao.com/#/coupons";
            CART_URL = "http://plugin.test.55haitao.com/#/cart/home";
            CACHE_URL = "http://plugin.test.55haitao.com/#/home";
            TAXEXPLAIN_URL = "http://plugin.test.55haitao.com/#/readme/tax?_k=bhp25s";
            MF_URL_PRODUCT = "http://plugin.test.55haitao.com/#/product/";

            Mall_Get_Coupon_Url = "http://h5.test.55haitao.com/activity/coupon?utm_source=icon";

        } else {  //生产环境PROD
            SERVER_URL = "http://api.55haitao.com/";

            H5_BASE_URL = "http://h5.55haitao.com/";

            H5_Domain = ".55haitao.com";

            Alipay_Notify_URL = "http://open.55shantao.com/mall_app_alipay_notify";
            Wxpay_Notify_URL = "http://open.55shantao.com/mall_app_wechat_notify";

            PRODUCT_INTRODUCE_URL = "http://share.missbaijie.com/baijie_h5/notice3.html";
            VERSION_INTRODUCE_URL = "http://share.missbaijie.com/baijie_h5/version.html";
            PUSH_GUIDE_URL = "http://share.missbaijie.com/baijie_h5/notification.html";
            HELP_INTRODUCE_URL = "http://plugin.55haitao.com/#/readme";
            INVITE_USER_BASEURL = "http://h5.55haitao.com/discount?user_id=";

            USER_AGREEMENT_URL = "http://h5.55haitao.com/agreement.html";   // 用户协议
            PRIVACY_POLICY_URL = "http://h5.55haitao.com/privacy.html";   // 隐私声明
            Commission_URL = "http://plugin.55haitao.com/#/my_commission";
            Commission_Detail_URL = "http://plugin.55haitao.com/#/commission_detail";
            Coupons_URL = "http://plugin.55haitao.com/#/coupons";
            CART_URL = "http://plugin.55haitao.com/#/cart/home";
            CACHE_URL = "http://plugin.55haitao.com/#/home";
            TAXEXPLAIN_URL = "http://plugin.55haitao.com/#/readme/tax?_k=bhp25s";
            MF_URL_PRODUCT = "http://plugin.55haitao.com/#/product/";

            Mall_Get_Coupon_Url = "http://h5.55haitao.com/activity/coupon?utm_source=icon";
        }

    }

    public static final String SERVER_URL;                  // 服务器
    public final static String PRODUCT_INTRODUCE_URL;       // 服务说明
    public final static String VERSION_INTRODUCE_URL;       // 版本说明
    public final static String PUSH_GUIDE_URL;              // 打开PUSH的设置说明
    public final static String HELP_INTRODUCE_URL;          // 常见问题
    public final static String INVITE_USER_BASEURL;         // 分享应用/邀请好友url

    public static final String USER_AGREEMENT_URL;          // 用户协议
    public static final String PRIVACY_POLICY_URL;          // 隐私声明
    public final static String Commission_URL;              // 我的佣金
    public final static String Commission_Detail_URL;       // 我的佣金明细
    public final static String Coupons_URL;                 // 我的优惠券
    public final static String CART_URL;                    // 我的购物车
    public final static String CACHE_URL;                   // 缓存页面
    public final static String TAXEXPLAIN_URL;              // 关税说明
    public final static String MF_URL_PRODUCT;              //拼接URL,(spuid)

    public final static String Mall_Get_Coupon_Url;         // 领取包税优惠劵
    public static final String Alipay_Notify_URL;           //支付宝回调地址
    public static final String Wxpay_Notify_URL;            //支付宝回调地址

    public static final String SERV_VERSION = BuildConfig.VERSION_NAME;        //服务器版本
    public static final String H5_BASE_URL;                 // ServerUrl BASE URL
    public static final String H5_Domain;

    // 分享应用 small_icon
    public static final String SHARE_APP_URL     = H5_BASE_URL + "discount?user_id=";
    // 分享应用 icon
    public static final String SHARE_APPICON_URL = H5_BASE_URL + "localImagePaths/icon_55haitao_app.png";
    // 售后政策 small_icon
    public static final String SALE_AFTER_URL    = "http://h5.55haitao.com/aftersales.html";
    // 又拍云上传地址
    public static final String UPYUN_BASE        = "http://st-prod.b0.upaiyun.com";

    // 广告页启动图存储位置
    public static final String ALBUM_PATH = Environment
            .getExternalStorageDirectory() + "/wwht/appStart/";

    public static final String PIC_PATH    = "/.wwht/image/";

}
