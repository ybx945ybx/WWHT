package com.a55haitao.wwht.ui.fragment.firstpage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
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
import com.a55haitao.wwht.data.model.jsobject.JSObjectType4;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType5;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType6;
import com.a55haitao.wwht.data.model.jsobject.JSObjectType7;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.discover.CouponListActivity;
import com.a55haitao.wwht.ui.activity.discover.SearchResultActivity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.easyopt.EasyOptDetailActivity;
import com.a55haitao.wwht.ui.activity.firstpage.EntriesSpecialActivity;
import com.a55haitao.wwht.ui.activity.firstpage.FavorableSpecialActivity;
import com.a55haitao.wwht.ui.activity.myaccount.account.LoginMobileActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.activity.social.SocialSpecialActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by a55 on 2017/3/23.
 */

public class FirstPageH5Fragment extends BaseFragment {
    private static final String URL      = "file:///android_asset/test.html";
    private static final String AppModel = "AppModel";
    private static final String ERR_URL  = "file:///android_asset/error.html";
    private WebView       webView;
    //    private DynamicHeaderView headerView;
    private ProgressBar     progressBar;
    private String          url;
    private ToastPopuWindow mPwToast;

    public static FirstPageH5Fragment newInstance(String url) {
        FirstPageH5Fragment fragment = new FirstPageH5Fragment();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
//        url = "http://zhigou.55haitao.com/h5/activities?id=4";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_h5, container, false);
        // Progress Bar
        progressBar = (ProgressBar) view.findViewById(R.id.h5ProgressBar);

        webView = (WebView) view.findViewById(R.id.webView);
        // 加载网页
        if (url == null || TextUtils.isEmpty(url)) {
            // 加载失败的页面           small_icon = ERR_URL;
        }
        webView.loadUrl(url);

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

        // 设置 Cookie
        String dtkCookie = "dtk=" + HaiUtils.getDeviceToken();
        String tkCookie  = "tk=" + HaiUtils.getUserToken();
        String uidCookie = "uid=" + HaiUtils.getUserId();

        CookieSyncManager.createInstance(getActivity());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, dtkCookie);
        cookieManager.setCookie(url, tkCookie);
        cookieManager.setCookie(url, uidCookie);
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

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
                //                headerView.setHeadTitle(title);
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
                super.onProgressChanged(view, newProgress);
            }
        };
        webView.setWebChromeClient(wvcc);

        // 设置 WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("wvjbscheme://")) {
                    return false;
                }

                // 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                //                Log.d(tag, " small_icon:" + url);
                view.loadUrl(url);
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
            }

        });

        return view;
    }


    public class JSHook<T> {

        private Gson gson;

        public JSHook(Gson gson) {
            this.gson = gson;
        }

        @JavascriptInterface
        public void javaMethod(String p) {
            Log.d("JAVAMETHOD", "JSHook.JavaMethod() called! + " + p);
        }

        @JavascriptInterface
        public void showAndroid() {
            final String info = "来自手机内的内容！！！";
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:show('" + info + "')");
                }
            });
        }

        @JavascriptInterface
        public void postMessage(String param) {


            if (gson == null) {
                return;
            }

            try {
                BaseJSObject jsobj = gson.fromJson(param, BaseJSObject.class);
                switch (jsobj.type) {
                    case 0: {
                        // 统计事件并记下当前的activity
//                        TraceUtils.addFirstAnalysAct(mActivity, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url);

                        // 商详主页
                        JSObjectType0 data = gson.fromJson(param, JSObjectType0.class);
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
                        //                        SearchResultActivity.toThisActivity(mActivity, data.data.name, queryBean, pageType);
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
                        //                        JSObjectType3 data = gson.fromJson(param, JSObjectType3.class);
                        //                        headerView.setHeadTitle(data.title);
                        break;
                    }
                    case 4: {
                        // 控制两侧的按钮
                        JSObjectType4 data = gson.fromJson(param, JSObjectType4.class);

                        //                        Observable.just("").observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        //                            @Override
                        //                            public void call(String s) {
                        //                                if (data.l == NavBarType.NavBarType_Null) {
                        //                                    headerView.setHeaderLeftHidden(true);
                        //                                } else {
                        //                                    headerView.setHeaderLeftHidden(false);
                        //                                }
                        //
                        //                                switch (data.r1) {
                        //                                    // Null
                        //                                    case NavBarType.NavBarType_Null: {
                        //                                        headerView.setHeaderRightHidden();
                        //                                        break;
                        //                                    }
                        //                                    // 添加好友
                        //                                    case NavBarType.NavBarType_AddFriend: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_add_friend);
                        //                                        break;
                        //                                    }
                        //                                    // 返回
                        //                                    case NavBarType.NavBarType_Back: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_arrow_left_grey);
                        //                                        break;
                        //                                    }
                        //                                    // 相机
                        //                                    case NavBarType.NavBarType_Camera: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_take_photo);
                        //                                        break;
                        //                                    }
                        //                                    // 购物车
                        //                                    case NavBarType.NavBarType_Cart: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_shopping_cart);
                        //                                        break;
                        //                                    }
                        //                                    // 更多
                        //                                    case NavBarType.NavBarType_More: {
                        //                                        break;
                        //                                    }
                        //                                    // 发帖
                        //                                    case NavBarType.NavBarType_PO: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_post_share);
                        //                                        break;
                        //                                    }
                        //                                    // 查找
                        //                                    case NavBarType.NavBarType_Search: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_search);
                        //                                        break;
                        //                                    }
                        //                                    // 设置
                        //                                    case NavBarType.NavBarType_Setting: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_setting);
                        //                                        break;
                        //                                    }
                        //                                    // 分享
                        //                                    case NavBarType.NavBarType_Share: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_title_share);
                        //                                        break;
                        //                                    }
                        //                                    // 客服
                        //                                    case NavBarType_Service: {
                        //                                        headerView.setHeaderRightImg(R.mipmap.ic_service);
                        //                                        break;
                        //                                    }
                        //                                    // 文本
                        //                                    case NavBarType.NavBarType_Text: {
                        //                                        headerView.setRightText(data.r1txt);
                        //                                        break;
                        //                                    }
                        //                                }
                        //                            }
                        //                        });
                        break;
                    }
                    case 5: {
                        // 分享,传share model
                        JSObjectType5 data = gson.fromJson(param, JSObjectType5.class);
                        getActivity().runOnUiThread(() -> {
                            /*if (data.amount == 0) {
                                ShareUtils.showShareBoard(mActivity, data.title, data.desc, data.small_icon, data.icon, data.canEarnMemberShipPoint);
                            } else {
                                ShareUtils.showShareBoard(mActivity, data.title, data.desc, data.small_icon, data.icon, (int) (Math.round((double) data.amount / 100.0)), data.canEarnMemberShipPoint);
                            }*/
                            ShareUtils.showShareBoard(mActivity, data.title, data.desc, data.url, data.icon, false);
                        });

                        // 分享成功后是否Toast
                        // data.canEarnMemberShipPoint

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
                        mActivity.startActivity(new Intent(mActivity, LoginMobileActivity.class));
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
                        // 统计事件并记下当前的activity
//                        TraceUtils.addFirstAnalysAct(mActivity, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url);

                        // H5
                        JSObjectType11 data = gson.fromJson(param, JSObjectType11.class);
                        H5Activity.toThisActivity(mActivity, data.url, data.title);
                        break;
                    }
                    case 12: {
                        // 统计事件并记下当前的activity
//                        TraceUtils.addFirstAnalysAct(mActivity, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url);

                        // 官网特卖(双列)
                        JSObjectType12 data = gson.fromJson(param, JSObjectType12.class);
                        FavorableSpecialActivity.toThisActivity(mActivity, "" + data.special_id);
                        break;
                    }
                    case 13: {
                        // 统计事件并记下当前的activity
//                        TraceUtils.addFirstAnalysAct(mActivity, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url);

                        // 商品专题（单列）
                        JSObjectType13 data = gson.fromJson(param, JSObjectType13.class);
                        EntriesSpecialActivity.toThisActivity(mActivity, "" + data.special_id);
                        break;
                    }
                    case 14: {
                        // 统计事件并记下当前的activity
//                        TraceUtils.addFirstAnalysAct(mActivity, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url);

                        // 社区专题
                        JSObjectType14 data = gson.fromJson(param, JSObjectType14.class);
                        SocialSpecialActivity.toThisActivity(mActivity, data.special_id);
                        break;
                    }
                    case 15: {
                        // 商品列表 (带筛选条件)
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
                        // 统计事件并记下当前的activity
//                        TraceUtils.addFirstAnalysAct(mActivity, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url, TraceUtils.Tab_Activity, TraceUtils.PS_H5 + url);

                        //草单详情
                        JSObjectType16 data = gson.fromJson(param, JSObjectType16.class);
                        if (data != null) {
                            EasyOptDetailActivity.toThisActivity(mActivity, data.album_id);
                        }
                    }
                    break;
                    case 17: {
                        // 0元购分享,传share model
                        JSObjectType5 data = gson.fromJson(param, JSObjectType5.class);
                        getActivity().runOnUiThread(() -> {
                            Logger.d(data.toString());
                            ShareUtils.showShareBoard(mActivity, data.alert_title, data.alert_desc, data.title, data.desc, data.url, data.icon);
                        });
                        break;
                    }
                }

            } catch (Exception ex) {
                mPwToast = ToastPopuWindow.makeText(mActivity, ex.getMessage(), AlertViewType.AlertViewType_Error);
                mPwToast.show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        dismissProgressDialog();
    }
}
