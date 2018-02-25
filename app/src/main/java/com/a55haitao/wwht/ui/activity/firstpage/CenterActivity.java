package com.a55haitao.wwht.ui.activity.firstpage;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.a55haitao.wwht.BuildConfig;
import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.EnforceUpdateReciever;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;
import com.a55haitao.wwht.data.event.SwitchTabEvent;
import com.a55haitao.wwht.data.event.UserInfoChangeEvent;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.PostFragmentLayoutType;
import com.a55haitao.wwht.data.model.entity.EnforceUpdateBean;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.DeviceRepository;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.discover.CouponListActivity;
import com.a55haitao.wwht.ui.activity.discover.SearchResultActivity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.easyopt.EasyOptDetailActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.activity.social.PostDetailActivity;
import com.a55haitao.wwht.ui.activity.social.SocialSpecialActivity;
import com.a55haitao.wwht.ui.activity.social.TagDetailActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.fragment.discover.CenterCategoryFragment;
import com.a55haitao.wwht.ui.fragment.firstpage.CenterFirstPageFragment;
import com.a55haitao.wwht.ui.fragment.myaccount.CenterMyAccountFragment;
import com.a55haitao.wwht.ui.fragment.shoppingcart.CenterShoppingCartFragment;
import com.a55haitao.wwht.ui.fragment.social.CenterSocialFragment;
import com.a55haitao.wwht.ui.view.WithNumberRadioButton;
import com.a55haitao.wwht.utils.BugLyUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.magicwindow.mlink.annotation.MLinkDefaultRouter;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import cn.xiaoneng.uiapi.Ntalker;
import tom.ybxtracelibrary.YbxTrace;

/**
 * App主页
 */
@MLinkDefaultRouter
@MLinkRouter(keys = {"mallHomeKey"})
public class CenterActivity extends BaseHasFragmentActivity {
    @BindView(R.id.radioGroup) RadioGroup mRadioGroup;         // 底部RadioGroup

    private ArrayList<BaseFragment> mFragments;                // 保存Fragment的集合

    private int oldPosition;                                   // 上次checked位置
    private int newPosition;                                   // 最新checked位置
    private long clickTime = 0;                                // 第一次点击的时间

    private EnforceUpdateReciever mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initData();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int position = intent.getIntExtra("position", -1);
        if (position != -1) {
            mRadioGroup.check(position);
        }

        // Uri 跳转
        Uri uri = intent.getData();
        if (uri != null) {
            dispatchUri(uri);
        }
    }


    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            // 闪频广告页跳过来则跳至相应页面
            String adUri = intent.getStringExtra("adUri");
            if (!TextUtils.isEmpty(adUri)) {
                HaiUriMatchUtils.matchUri(CenterActivity.this, adUri);
            }
        }

        // 小能客服／bugly初始化客服用户信息，获取权限
        UserBean userBean = UserRepository.getInstance().getUserInfo();
        NtalkerUtils.loginNtalker(userBean);
        getPermissions();

        BugLyUtils.setUserId(userBean);
    }

    private void initViews() {

        //初始化Fragment
        mFragments = new ArrayList<>(5);
        mFragments.add(new CenterFirstPageFragment());
        mFragments.add(new CenterCategoryFragment());
        mFragments.add(new CenterSocialFragment());
        CenterShoppingCartFragment centerShoppingCartFragment = new CenterShoppingCartFragment();
        centerShoppingCartFragment.setTabShoppingCart(true);
        mFragments.add(centerShoppingCartFragment);
        mFragments.add(new CenterMyAccountFragment());
        //初始化标记
        oldPosition = 0;
        newPosition = 0;
        getSupportFragmentManager().beginTransaction().add(R.id.framLayout, mFragments.get(0)).commit();
        for (int i = 0; i < mFragments.size(); i++) {
            mRadioGroup.getChildAt(i).setId(i);
        }
        mRadioGroup.check(0);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int currentId = (int) checkedId;
            if ((currentId == 3) && HaiUtils.needLogin(mActivity)) {
                group.check(newPosition);
                return;
            }

            oldPosition = newPosition;
            newPosition = checkedId;

            // 切换底部tab清除chid.
            YbxTrace.getInstance().clearChid();
            purl = mFragments.get(newPosition).getClass().getSimpleName();
            purlh = mFragments.get(newPosition).toString().substring(mFragments.get(newPosition).toString().indexOf("{") + 1, mFragments.get(newPosition).toString().indexOf("}"));

            switchFragment();
        });

    }

    private void initData() {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mReceiver = new EnforceUpdateReciever(this, String.valueOf(System.currentTimeMillis()));
        registerReceiver(mReceiver, intentFilter);
        examAppVersion();
        updatePushToken();
        getCartBadge();
    }

    /**
     * 检查版本更新
     */
    private void examAppVersion() {
        long updateExamTimestamp = (long) SPUtils.get(mActivity, "update_exam_timestamp", 0L);

        if (System.currentTimeMillis() - updateExamTimestamp > 24L * 60 * 60 * 1000) {
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

                        }
                    });
        }
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
        //        EnforceUpdateBean first = RealmUtils.getInstance().where(EnforceUpdateBean.class).findFirst();
        //        EnforceUpdateBean first = Realm.getDefaultInstance().where(EnforceUpdateBean.class).findFirst();
        //        if (first != null && first.now_ver_num.equals(bean.now_ver_num)) {     //版本号一致，不在弹窗
        //            return;
        //        } else
        if (bean.now_ver_num != null && bean.now_ver_num.equals(BuildConfig.VERSION_NAME)) {
            return;
        } else if (newVersionCode != -1 && BuildConfig.VERSION_CODE >= newVersionCode) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(CenterActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert_Self).setTitle(title).setMessage(bean.change_desc);
        builder.setPositiveButton("立刻更新", (dialog, which) -> {
            dialog.dismiss();
            dowmAPKFile(uri);

        });
        builder.setCancelable(false);
        if (BuildConfig.VERSION_CODE >= versionCode) {      //不需要强制升级
            builder.setNegativeButton("稍后再说", (dialog, which) -> {
                dialog.dismiss();
                //记录时间
                //                SPUtils.put(mActivity, "update_exam_timestamp", System.currentTimeMillis());
                //                //                Realm realm = RealmUtils.getInstance();
                //                Realm realm = Realm.getDefaultInstance();
                //                realm.beginTransaction();
                //                realm.insertOrUpdate(bean);
                //                realm.commitTransaction();

            }).setCancelable(true);
        }
        builder.create().show();
    }

    private void dowmAPKFile(Uri uri) {
        //开启下载
        DownloadManager.Request request = null;

        try {
            request = new DownloadManager.Request(uri);
            //                        loadData.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mReceiver.getFileName());
            request.setDestinationInExternalFilesDir(this, null, mReceiver.getFileName());
            request.setTitle("下载").setDescription("正在下载55海淘");
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
        } catch (IllegalArgumentException e) {      //捕捉Uri参数异常，low_ver_desc异常
        }
    }

    /**
     * 更新pushToken
     */
    private void updatePushToken() {
        final String[] pushId = {null};
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (TextUtils.isEmpty(pushId[0])) {
                    pushId[0] = JPushInterface.getRegistrationID(getApplicationContext());
                    SystemClock.sleep(500);
                }

                Logger.t(TAG).d(pushId[0]);

                runOnUiThread(() -> DeviceRepository.getInstance()
                        .updatePushToken(pushId[0])
                        .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                        .subscribe(new DefaultSubscriber<Boolean>() {
                            @Override
                            public void onSuccess(Boolean result) {
                                Logger.d("更新pushToken" + pushId[0]);
                            }

                            @Override
                            public void onFinish() {

                            }
                        }));
            }
        }.start();
    }

    /**
     * 获取购物车数量
     */
    private void getCartBadge() {
        if (!HaiUtils.isLogin()) {
            return;
        }
        ProductRepository.getInstance()
                .getShoppingCartListCnt()
                .subscribe(new DefaultSubscriber<ShoppingCartCntBean>() {
                    @Override
                    public void onSuccess(ShoppingCartCntBean shoppingCartCntBean) {
                        setShoppingCartBadgs(shoppingCartCntBean.count);
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    /**
     * 重写但不实现  解决长期处于后台后唤醒fragmeng重叠的问题
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    private void dispatchUri(Uri uri) {
        try {
            final String domain = uri.getAuthority();
            if (domain.equals("tab")) {
                // TODO
                // 点击某个Tab
                int index = Integer.valueOf(uri.getQueryParameter("index"));
            } else if (domain.equals("productdetail")) {
                // 打开某个商品
                // @"/productdetail/:spuid"
                String spuid = uri.getQueryParameter("id");
                ProductMainActivity.toThisAcivity(mActivity, spuid);

            } else if (domain.equals("postdetail")) {
                // 打开某个笔记
                // @"/postdetail/:post_id"
                int    postid = Integer.valueOf(uri.getQueryParameter("id"));
                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", postid);
                mActivity.startActivity(intent);

            } else if (domain.equals("h5")) {
                // 打开某个H5
                // @"/h5/:small_icon"
                String url   = uri.getQueryParameter("small_icon");
                String title = uri.getQueryParameter("title");
                H5Activity.toThisActivity(mActivity, url, title);

            } else if (domain.equals("productspecial")) {
                // 打开商品专题(潮流风尚)
                // @"/productspecial/:special_id"
                String spcialid = uri.getQueryParameter("id");
                EntriesSpecialActivity.toThisActivity(mActivity, spcialid);

            } else if (domain.equals("preferentialspecial")) {
                // 打开特惠专题(官网特卖)
                // @"/preferentialspecial/:special_id"
                String spcialid = uri.getQueryParameter("id");
                FavorableSpecialActivity.toThisActivity(mActivity, spcialid);

            } else if (domain.equals("postspecial")) {
                // 打开社区专题
                // @"/postspecial/:special_id"
                int postspcialid = Integer.valueOf(uri.getQueryParameter("id"));
                SocialSpecialActivity.toThisActivity(mActivity, postspcialid);

            } else if (domain.equals("sellerhome")) {
                // 商家主页
                // @"/sellerhome/:seller_name"
                String name = uri.getQueryParameter("name");
                SiteActivity.toThisActivity(mActivity, name, PageType.SELLER);

            } else if (domain.equals("brandhome")) {
                // 品牌主页
                // @"/brandhome/:brand_name"
                String name = uri.getQueryParameter("name");
                SiteActivity.toThisActivity(mActivity, name, PageType.SELLER);

            } else if (domain.equals("searchresult")) {
                // 筛选搜索结果页
                // @"/searchresult/:query/:brand/:seller/:category"
                String    query     = uri.getQueryParameter("query");
                String    brand     = uri.getQueryParameter("brand");
                String    seller    = uri.getQueryParameter("seller");
                String    category  = uri.getQueryParameter("category");
                QueryBean queryBean = new QueryBean();
                queryBean.query = query;
                queryBean.brand = brand;
                queryBean.seller = seller;
                queryBean.category = category;
                SearchResultActivity.toThisActivity(mActivity, "搜索结果", queryBean, PageType.BRAND);

            } else if (domain.equals("hottagpostlist")) {
                // 打开某个热门标签页
                // @"/hottagpostlist/:tag_id/:tag_name"
                int    tag_id   = Integer.valueOf(uri.getQueryParameter("tag_id"));
                String tag_name = uri.getQueryParameter("tag_name");
                Intent intent   = new Intent(mActivity, TagDetailActivity.class);
                intent.putExtra("tag_id", tag_id);
                intent.putExtra("tag_name", tag_name);
                intent.putExtra("is_hot", 1);
                mActivity.startActivity(intent);
            } else if (domain.equals("albumdetail")) {
                int easyOptId = Integer.valueOf(uri.getQueryParameter("album_id"));
                EasyOptDetailActivity.toThisActivity(mActivity, easyOptId);
            }else if (domain.equals("mycouponlist")) {
                CouponListActivity.toThisActivity(mActivity);
            }

        } catch (Exception ex) {
            if (BuildConfig.DEBUG) Log.d("CenterActivity", "ex:" + ex);
        }
    }

    public void switchFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mFragments.get(oldPosition));
        BaseFragment fragment = mFragments.get(newPosition);
        if (fragment.isAdded()) {
            ft.show(fragment);
            if (newPosition == 4) {
                EventBus.getDefault().post(new UserInfoChangeEvent());

            }
        } else {
            ft.add(R.id.framLayout, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Subscribe
    public void onSwitchTabEvent(SwitchTabEvent event) {

        int current = event.tabIndex;
        if (newPosition == current) return;

        // RadioGroup Selected
        mRadioGroup.check(current);

        // Tab下标
        oldPosition = newPosition;
        newPosition = current;
        // 切换Fragment
        switchFragment();
    }

    /**
     * 更新底部购物车按钮数量
     *
     * @param event
     */
    @Subscribe
    public void cartCountChange(ShoppingCartCntChangeEvent event) {
        setShoppingCartBadgs(event.shoppingCartBadgeCnt);
    }

    private void setShoppingCartBadgs(int count) {
        WithNumberRadioButton withNumberBtn = (WithNumberRadioButton) mRadioGroup.getChildAt(3);
        withNumberBtn.setNumber(count);
    }

    /**
     * 登录状态改变  更新ui
     *
     * @param event
     */
    @Subscribe
    public void onLoginStatusEvent(LoginStateChangeEvent event) {
        if (event.isLogin) {
            getCartBadge();
        } else {
            setShoppingCartBadgs(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (System.currentTimeMillis() - clickTime > 2000) {
            Toast.makeText(this, "再点一次退出55海淘", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            HaiApplication.layoutType = PostFragmentLayoutType.DOUBLE;
            finish();
        }
    }

    private void getPermissions() {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                //                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        Ntalker.getInstance().getPermissions(this, 200, permissions);

    }

    // 分享回调 在onresum中dismissProgressDialog()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissProgressDialog();
    }
}