package com.a55haitao.wwht.ui.activity.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.a55haitao.wwht.BuildConfig;
import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ShareImageEvent;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.annotation.NavBarType;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.data.model.jsobject.BaseJSObject;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType0;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType1;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType11;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType12;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType13;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType14;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType15;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType16;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType2;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType3;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType4;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType5;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType6;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType7;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType8;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.discover.CouponListActivity;
import com.a55haitao.wwht.ui.activity.discover.SearchResultActivity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.easyopt.EasyOptDetailActivity;
import com.a55haitao.wwht.ui.activity.firstpage.EntriesSpecialActivity;
import com.a55haitao.wwht.ui.activity.firstpage.FavorableSpecialActivity;
import com.a55haitao.wwht.ui.activity.myaccount.account.LoginMobileActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.activity.social.SocialSpecialActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.FileUtilsTmp;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.HashMap;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import tom.ybxtracelibrary.YbxTrace;

import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Service;

/**
 * Created by Haotao_Fujie on 16/10/9.
 * H5
 */
@MLinkRouter(keys = {"webPageKey"})
public class H5Activity extends BaseNoFragmentActivity {

    private static final String ERR_URL                = "file:///android_asset/error.html";
    private static final String URL                    = "file:///android_asset/test.html";
    private static final String AppModel               = "AppModel";
    private static final int    FILECHOOSER_RESULTCODE = 1001;
    private WebView           webView;
    private DynamicHeaderView headerView;
    private ImageView         ivClose;
    private ProgressBar       progressBar;
    private boolean           mGAScreenNameSent;

    public String tag = "H5Activity";
    private Context              mContext;
    private String               url;
    private Tracker              mTracker;            // GA Tracker
    private ValueCallback<Uri>   mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageArr;
    private ToastPopuWindow      mPwToast;
    private String               callbackStr;         // 回调js方法名

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource();
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "url=" + url;
    }

    private void clearWebViewResource() {
        if (webView != null) {
            webView.removeAllViews();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.setTag(null);
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init UI
        mContext = this;
        url = getIntent().getStringExtra("url");
//        url = "http://zhigou.dev.55haitao.com/h5/special/2017-black-friday-preheat";

        String title    = getIntent().getStringExtra("title");
        String subUrl   = getIntent().getStringExtra("subUrl");
        String subTitle = getIntent().getStringExtra("subTitle");

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        if (!TextUtils.isEmpty(title)) {
            mTracker.setScreenName(title);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mGAScreenNameSent = true;
        }

        // 初始化ContentView
        setContentView(R.layout.activity_h5);

        EventBus.getDefault().register(this);

        // Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.h5ProgressBar);

        // 直接关闭按钮
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> finish());

        // Header View
        headerView = (DynamicHeaderView) findViewById(R.id.title);
        headerView.setHeadTitle(title);
        if (!TextUtils.isEmpty(subTitle) && !TextUtils.isEmpty(subUrl)) {
            headerView.setRightText(subTitle);
            headerView.setRighTextColor(ContextCompat.getColor(mContext, R.color.colorOrange));
            headerView.setHeadClickListener(() -> H5Activity.toThisActivity(H5Activity.this, subUrl, subTitle));
        } else {
            headerView.setHeadClickListener(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("callRightButton()", value -> {
                    });
                } else {
                    webView.loadUrl("javascript:callRightButton()");
                }
            });
        }

        // WebView
        LinearLayout rootLin = (LinearLayout) findViewById(R.id.h5RootLin);
        // New WebView
        webView = new WebView(mActivity);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootLin.addView(webView);

        // 调试用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        // 初始化设置
        // Init WebView Setting
        WebSettings webSettings = webView.getSettings();
        // 使用硬件加速
        webSettings.setBlockNetworkImage(true);
        // 支持js打开一个窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置编码
        webSettings.setDefaultTextEncodingName("utf-8");
        // 支持js
        webSettings.setJavaScriptEnabled(true);
        //加载网络上的图片资源
        webSettings.setBlockNetworkImage(false);
        // 设置useragent
        String ua = webView.getSettings().getUserAgentString();
        //        webSettings.setUserAgentString(ua + "@@@zg_Android");
        webSettings.setUserAgentString(ua + "@@@zg_Android" + "___" + BuildConfig.VERSION_NAME);

        // 设置 Cookie
        setCookie();

        // 设置js交互
        webView.addJavascriptInterface(new JSHook(new Gson()), AppModel);

        // 获取HTML Title
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("plugin.55haitao.com")) {
                    return;
                }
                headerView.setHeadTitle(title);
                if (!mGAScreenNameSent) {
                    mTracker.setScreenName(title);
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (progressBar.getVisibility() == View.INVISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }

                if (webView != null) {
                    if (webView.canGoBack()) {
                        ivClose.setVisibility(View.VISIBLE);
                    } else {
                        ivClose.setVisibility(View.GONE);
                    }
                }

                super.onProgressChanged(view, newProgress);
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Logger.d("in openFile Uri Callback has accept Type" + acceptType + "has capture" + capture);
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
                i.setType(type);
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            //Android 5.0+
            @Override
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                if (mUploadMessageArr != null)
                    return false;
                mUploadMessageArr = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "文件选择"),
                        FILECHOOSER_RESULTCODE);
                return true;
            }

        };
        webView.setWebChromeClient(wvcc);

        // 设置 WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newUrl) {

                if (newUrl.contains("wvjbscheme://")) {
                    return false;
                }

                // 0院团登录态拦截
                if (newUrl.equals("http://zhigou.dev.55haitao.com/h5/sign")) {
                    UserRepository.getInstance().clearUserInfo();
                    CookieManager.getInstance().removeAllCookie();
                    showNeedLoginDialog(HaiConstants.ReponseCode.CODE_LOGINED_ON_OTHER_DEVICE);
                    return true;
                }

                // 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                view.loadUrl(newUrl);
                ivClose.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Logger.d("onReceivedError---" + error.toString());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Logger.d("onReceivedSslError---" + error.toString());

            }
        });

        // 加载网页
        if (url == null || TextUtils.isEmpty(url)) {
            // 加载失败的页面           small_icon = ERR_URL;
        }
        webView.loadUrl(url);

    }

    public static void toThisActivity(Context context, String url, String title) {
        toThisActivity(context, url, title, false);
    }

    /**
     * 开新栈
     */
    public static void toThisActivity(Context context, String url, String title, boolean newTask) {
        Intent intent = new Intent(context, H5Activity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    public static void toThisActivity(Context context, String url, String title, String subUrl, String subTitle) {
        toThisActivity(context, url, title, subUrl, subTitle, false);
    }

    /**
     * 开新栈
     */
    public static void toThisActivity(Context context, String url, String title, String subUrl, String subTitle, boolean newTask) {
        Intent intent = new Intent(context, H5Activity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("subUrl", subUrl);
        intent.putExtra("subTitle", subTitle);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        if (event.isLogin) {
            setCookie();
            webView.reload();
        }
    }

    /**
     * 黑五预热分享完单张图片成功后
     * @param event
     */
    @Subscribe
    public void onShareImageEvent(ShareImageEvent event) {
        //        Toast.makeText(mActivity, "分享完成回调js", Toast.LENGTH_SHORT).show();
        webView.loadUrl("javascript:" + callbackStr);

    }

    public class JSHook<T> {

        private Gson gson;

        public JSHook(Gson gson) {
            this.gson = gson;
        }

        @JavascriptInterface
        public void javaMethod(String p) {
            Log.d(tag, "JSHook.JavaMethod() called! + " + p);
        }

        @JavascriptInterface
        public void showAndroid() {
            final String info = "来自手机内的内容！！！";
            H5Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:show('" + info + "')");
                }
            });
        }

        @JavascriptInterface
        public void postMessage(String param) {
            Logger.d("postMessage-------->" + param);
            if (gson == null) {
                return;
            }
            try {
                BaseJSObject jsobj = gson.fromJson(param, BaseJSObject.class);
                switch (jsobj.type) {
                    case 0: {
                        // 商详主页
                        JSObjectType0 data = gson.fromJson(param, JSObjectType0.class);

                        // 埋点
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Goods_Id, data.spuid);
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                        ProductMainActivity.toThisAcivity(mActivity, data.spuid);
                        break;
                    }
                    case 1: {
                        // 商家和品牌主页
                        JSObjectType1 data = gson.fromJson(param, JSObjectType1.class);
                        //                        QueryBean     queryBean = new QueryBean();
                        int pageType = PageType.BRAND;
                        if (data.data.type == 0) {
                            // 品牌
                            pageType = PageType.BRAND;
                            //                            queryBean.brand = data.data.name;
                        } else if (data.data.type == 1) {
                            // 商家
                            pageType = PageType.SELLER;
                            //                            queryBean.seller = data.data.name;
                        } else {
                            mPwToast = ToastPopuWindow.makeText(mActivity, "商家和品牌类型不正确", AlertViewType.AlertViewType_Warning);
                            mPwToast.show();
                        }

                        // 埋点
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Brand, TraceUtils.Event_Category_Click, "", null, "");

                        SiteActivity.toThisActivity(mActivity, data.data.name, pageType);
                        break;
                    }
                    case 2: {
                        // 我的优惠券列表
                        startActivity(new Intent(mActivity, CouponListActivity.class));
                        break;
                    }
                    case 3: {
                        // 控制title的名称
                        JSObjectType3 data = gson.fromJson(param, JSObjectType3.class);
                        headerView.setHeadTitle(data.title);
                        break;
                    }
                    case 4: {
                        // 控制两侧的按钮
                        JSObjectType4 data = gson.fromJson(param, JSObjectType4.class);

                        Observable.just("")
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        if (data.l == NavBarType.NavBarType_Null) {
                                            headerView.setHeaderLeftHidden(true);
                                        } else {
                                            headerView.setHeaderLeftHidden(false);
                                        }

                                        switch (data.r1) {
                                            // Null
                                            case NavBarType.NavBarType_Null: {
                                                headerView.setHeaderRightHidden();
                                                break;
                                            }
                                            // 添加好友
                                            case NavBarType.NavBarType_AddFriend: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_add_friend);
                                                break;
                                            }
                                            // 返回
                                            case NavBarType.NavBarType_Back: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_arrow_left_grey);
                                                break;
                                            }
                                            // 相机
                                            case NavBarType.NavBarType_Camera: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_take_photo);
                                                break;
                                            }
                                            // 购物车
                                            case NavBarType.NavBarType_Cart: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_shopping_cart);
                                                break;
                                            }
                                            // 更多
                                            case NavBarType.NavBarType_More: {
                                                break;
                                            }
                                            // 发帖
                                            case NavBarType.NavBarType_PO: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_post_share);
                                                break;
                                            }
                                            // 查找
                                            case NavBarType.NavBarType_Search: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_search);
                                                break;
                                            }
                                            // 设置
                                            case NavBarType.NavBarType_Setting: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_setting);
                                                break;
                                            }
                                            // 分享
                                            case NavBarType.NavBarType_Share: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_title_share);
                                                break;
                                            }
                                            // 客服
                                            case NavBarType_Service: {
                                                headerView.setHeaderRightImg(R.mipmap.ic_service);
                                                break;
                                            }
                                            // 文本
                                            case NavBarType.NavBarType_Text: {
                                                headerView.setRightText(data.r1txt);
                                                break;
                                            }
                                        }
                                    }
                                });
                        break;
                    }
                    case 5: {
                        // 分享,传share model
                        JSObjectType5 data = gson.fromJson(param, JSObjectType5.class);
                        runOnUiThread(() -> {
                            Logger.d(data.toString());
                            ShareUtils.showShareBoard(mActivity, data.title, data.desc, data.url, data.icon, false);
                        });

                        break;
                    }
                    case 6: {
                        // 加载/停止加载动画
                        JSObjectType6 data = gson.fromJson(param, JSObjectType6.class);
                        if (data.data.start) {
                            showProgressDialog(data.data.message, true);
                        } else {
                            dismissProgressDialog();
                        }
                        break;
                    }
                    case 7: {
                        // 弹窗吐司
                        // TODO 后期需要区分提示类型：错误、警告、正确、积分类型等
                        JSObjectType7 data = gson.fromJson(param, JSObjectType7.class);
                        mPwToast = ToastPopuWindow.makeText(mActivity, data.data.message, AlertViewType.AlertViewType_Warning);
                        mPwToast.show();
                        break;
                    }
                    case 8:
                        // 直接弹出登录
                        JSObjectType8 jsObjectType8 = gson.fromJson(param, JSObjectType8.class);
                        Intent intent = new Intent(mActivity, LoginMobileActivity.class);
                        if (jsObjectType8.src == 3) {   // 表示是0元团引流过来的
                            intent.putExtra("src", jsObjectType8.src);
                            if (!TextUtils.isEmpty(jsObjectType8.aid))
                                intent.putExtra("activity_id", jsObjectType8.aid);
                        }
                        mActivity.startActivity(intent);
                        break;

                    case 9:
                        // 过期弹出登录
                        mActivity.startActivity(new Intent(mActivity, LoginMobileActivity.class));
                        break;

                    case 10:
                        // 被人踢弹出登录
                        mActivity.startActivity(new Intent(mActivity, LoginMobileActivity.class));
                        break;

                    case 11: {
                        // 埋点
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

                        // H5
                        JSObjectType11 data = gson.fromJson(param, JSObjectType11.class);
                        H5Activity.toThisActivity(mActivity, data.url, data.title);
                        break;
                    }
                    case 12: {
                        // 埋点
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

                        // 官网特卖(双列)
                        JSObjectType12 data = gson.fromJson(param, JSObjectType12.class);
                        FavorableSpecialActivity.toThisActivity(mActivity, "" + data.special_id);
                        break;
                    }
                    case 13: {
                        // 埋点
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

                        // 商品专题（单列）
                        JSObjectType13 data = gson.fromJson(param, JSObjectType13.class);
                        EntriesSpecialActivity.toThisActivity(mActivity, "" + data.special_id);
                        break;
                    }
                    case 14: {
                        // 埋点
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

                        // 社区专题
                        JSObjectType14 data = gson.fromJson(param, JSObjectType14.class);
                        SocialSpecialActivity.toThisActivity(mActivity, data.special_id);
                        break;
                    }
                    case 15: {
                        // 商品列表 (带筛选条件)
                        // 埋点
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

                        JSObjectType15 data      = gson.fromJson(param, JSObjectType15.class);
                        QueryBean      queryBean = new QueryBean();
                        queryBean.brand = data.brand;
                        queryBean.seller = data.seller;
                        queryBean.category = data.category;
                        queryBean.query = data.query;
                        SearchResultActivity.toThisActivity(mActivity, "搜索结果", queryBean, PageType.CATEGORY);
                        break;
                    }
                    case 16: {
                        // 埋点
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

                        //草单详情
                        JSObjectType16 data = gson.fromJson(param, JSObjectType16.class);
                        if (data != null) {
                            EasyOptDetailActivity.toThisActivity(mActivity, data.album_id);
                        }
                    }
                    break;
                    case 17: {
                        // 0元购分享,传share model, 需要登录
                        JSObjectType5 data = gson.fromJson(param, JSObjectType5.class);
                        runOnUiThread(() -> {
                            Logger.d(data.toString());
                            ShareUtils.showShareBoard(mActivity, data.alert_title, data.alert_desc, data.title, data.desc, data.url, data.icon);
                        });
                        break;
                    }
                    case 18: {
                        // 分享单张图片
                        JSObjectType2 data = gson.fromJson(param, JSObjectType2.class);
                        runOnUiThread(() -> {
                            Logger.d(data.toString());
                            callbackStr = data.callbackStr;
                            ShareUtils.showShareBoard(mActivity, data.url);
                        });
                        break;
                    }
                }

            } catch (Exception ex) {
                mPwToast = ToastPopuWindow.makeText(mActivity, ex.getMessage(), AlertViewType.AlertViewType_Error);
                mPwToast.show();
            }

            /* 回调JS方法
            final String info = param;
            H5Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:show('" + info + "')");
                }
            });
            */
        }
    }

    private void setCookie() {
        try {

            // 设置 Cookie
            String dtkCookie = "dtk=" + HaiUtils.getDeviceToken();
            String tkCookie  = "tk=" + HaiUtils.getUserToken();
            String _tkCookie = "_tk=" + HaiUtils.getUserToken();
            String uidCookie = "uid=" + HaiUtils.getUserId();

            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            if (cookieManager != null) {
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();
                cookieManager.removeAllCookie();
                cookieManager.setCookie(ServerUrl.H5_Domain, dtkCookie);
                cookieManager.setCookie(ServerUrl.H5_Domain, tkCookie);
                cookieManager.setCookie(ServerUrl.H5_Domain, _tkCookie);
                cookieManager.setCookie(ServerUrl.H5_Domain, uidCookie);

            }

            if (Build.VERSION.SDK_INT < 21) {
                CookieSyncManager.getInstance().sync();
            } else {
                CookieManager.getInstance().flush();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShareUtils.dismissShareBoard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result == null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            Logger.d("UPFILE" + result.toString());
            String path = FileUtilsTmp.getPath(this, result);
            if (TextUtils.isEmpty(path)) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            Uri uri = Uri.fromFile(new File(path));
            Logger.d("UPFILE after parser uri:" + uri.toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadMessageArr.onReceiveValue(new Uri[]{uri});
            } else {
                mUploadMessage.onReceiveValue(uri);
            }
            mUploadMessage = null;
        }

        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().stopSync();
        }
        dismissProgressDialog();
    }

}







