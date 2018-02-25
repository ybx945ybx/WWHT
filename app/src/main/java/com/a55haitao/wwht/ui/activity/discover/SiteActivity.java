package com.a55haitao.wwht.ui.activity.discover;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.BrandStoreFollowEvent;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.SellerBean;
import com.a55haitao.wwht.data.model.entity.ShareModel;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.fragment.discover.RelateSiteFragment;
import com.a55haitao.wwht.ui.fragment.discover.TwiceProductFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.SiteHeaderLayout;
import com.a55haitao.wwht.ui.view.SiteNagivationLayout;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.magicwindow.mlink.annotation.MLinkRouter;

/**
 * 商家、品牌主页
 */
@MLinkRouter(keys = {"brandAndSellerPage"})
public class SiteActivity extends BaseHasFragmentActivity implements SiteNagivationLayout.NagivationHandlerIml {

    @BindView(R.id.fixLayout)            RadioGroup           mFixLayout;
    @BindView(R.id.msView)               MultipleStatusView   msView;
    @BindView(R.id.siteHeaderLayout)     SiteHeaderLayout     mHeaderLayout;
    @BindView(R.id.appBarLayout)         AppBarLayout         mAppBarLayout;
    @BindView(R.id.floatingHeaderLayout) SiteNagivationLayout mNagLayout;

    private BaseFragment[] mFragments;
    private int            mCriticalOffset;
    private int            mSiteType;
    private String         mSiteName;
    private boolean        isStar;
    private ShareModel     mShare;
    private Tracker        mTracker;        // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        ButterKnife.bind(this);
        ButterKnife.bind(mHeaderLayout);

        msView.setOnRetryClickListener(v -> requestInfo());

        initVars();
        initFragment();

        initTitle();        //初始化NagivationLayout
        initFixLayout();
        initAppBarLayout();

        requestInfo();
    }

    private void initFragment() {
        mFragments = new BaseFragment[2];
        mFragments[0] = TwiceProductFragment.newInstance(getIntent().getExtras());
        mFragments[1] = RelateSiteFragment.newInstance(mSiteType, mSiteName);
    }

    private void initVars() {
        Intent intent = getIntent();
        String mwType = intent.getStringExtra("type");
        if (TextUtils.isEmpty(mwType)) {
            mSiteType = intent.getIntExtra("type", -1);
        } else {
            mSiteType = Integer.valueOf(mwType);
        }
        mSiteName = intent.getStringExtra("name");
        String str = mSiteType == PageType.BRAND ? "相关品牌" : "相关商家";
        HaiUtils.setText(mFixLayout, R.id.relateBrandRadiobtn, str);

        //设置drawable大小
        Paint paint = new Paint();
        paint.setTextSize(DeviceUtils.getDensity() * 14);

        for (int i = 0; i < mFixLayout.getChildCount(); i++) {
            RadioButton rb       = (RadioButton) mFixLayout.getChildAt(i);
            Drawable    drawable = ContextCompat.getDrawable(this, R.drawable.sl_switch_for_site);
            drawable.setBounds(0, 0, ((int) paint.measureText(rb.getText().toString())), ((int) (DeviceUtils.getDensity() * 2)));
            ((RadioButton) mFixLayout.getChildAt(i)).setCompoundDrawables(null, null, null, drawable);
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(mSiteType == PageType.BRAND ? "品牌主页" : "商家主页");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // GA 事件
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("电商运营")
                .setAction(mSiteType == PageType.BRAND ? "人气品牌 Click" : "精选商家 Click")
                .setLabel(mSiteName)
                .build());

    }

    private void initAppBarLayout() {
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset + mCriticalOffset <= 0) {
                if (!mNagLayout.hasBackground()) {
                    Bitmap bitMap = mHeaderLayout.getBitMap(mNagLayout.getNagivationHeight());
                    mNagLayout.showBackground(bitMap);
                } else {
                    mNagLayout.showBackground(null);
                }
            } else {
                mNagLayout.hideBackground();
            }
        });
    }

    private void initFixLayout() {
        mFixLayout.setOnCheckedChangeListener((radioGroup, i) -> {
            int newPosition = -1;
            if (i == R.id.productRadioBtn) {
                newPosition = 0;
            } else if (i == R.id.relateBrandRadiobtn) {
                newPosition = 1;
            }
            switchFragment(newPosition);
        });
        //已经在xml中设置第一个RadioButton checked,这里不再设置
        //也是为了避免一个BUG，在此处设置checked状态，会导致上面回调方法执行两次
        switchFragment(0);
    }

    public void initTitle() {
        //计算悬浮窗高度
        //计算偏移量临界值
        mCriticalOffset = mHeaderLayout.getImageHeight() - mNagLayout.getNagivationHeight();
        mNagLayout.setMainHandler(this);
        mNagLayout.setTitleTxt(mSiteName);
    }

    /**
     * 请求商家、品牌信息
     */
    private void requestInfo() {
        msView.showLoading();
        ProductRepository.getInstance()
                .getBrandSellerInfo(mSiteType, mSiteName)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<SellerBean.SiteDescBaseBean>() {
                    @Override
                    public void onSuccess(SellerBean.SiteDescBaseBean sellerDescBaseBean) {
                        if (sellerDescBaseBean != null) {
                            msView.showContent();
                            mShare = sellerDescBaseBean.share;
                            isStar = sellerDescBaseBean.is_followed;
                            mNagLayout.setStarStatus(sellerDescBaseBean.is_followed, sellerDescBaseBean.is_followed ? "已关注" : "关注");

                            //数据处理
                            if (TextUtils.isEmpty(sellerDescBaseBean.img_cover)) {
                                sellerDescBaseBean.img_cover = getIntent().getStringExtra("img");
                            }
                            if (TextUtils.isEmpty(sellerDescBaseBean.logo1)) {
                                sellerDescBaseBean.logo1 = getIntent().getStringExtra("logo1");
                            }
                            if (TextUtils.isEmpty(sellerDescBaseBean.logo3)) {
                                sellerDescBaseBean.logo3 = getIntent().getStringExtra("logo3");
                            }
                            mHeaderLayout.init(SiteActivity.this, sellerDescBaseBean, mSiteType);
                            TwiceProductFragment fragment = (TwiceProductFragment) mFragments[0];
                            fragment.setReposityInfo(mSiteName, mSiteType, sellerDescBaseBean.query);
                            fragment.setmTag(getActivityTAG());
                            fragment.requestData();
                        } else {
                            msView.showEmpty();
                        }
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }
                });

    }

    private void switchFragment(int newPosition) {
        if (newPosition == -1) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        int oldPosition = 1 - newPosition;

        if (!mFragments[newPosition].isAdded()) {
            ft.add(R.id.framLayout, mFragments[newPosition]);
        } else {
            ft.show(mFragments[newPosition]);
        }

        if (mFragments[oldPosition].isAdded()) {
            ft.hide(mFragments[oldPosition]);
        }

        ft.commit();
    }

    @Override
    public void onShareClick() {
        if (mShare != null) {
            ShareUtils.showShareBoard(this, mShare.title, mShare.desc, mShare.url, mShare.icon, false);
        }
    }

    @Override
    public void onStarClick() {

        if (HaiUtils.needLogin(this)) {
            return;
        }

        ProductRepository.getInstance()
                .followBrandSeller(mSiteType, mSiteName)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean commonDataBean) {
                        boolean newStatus = !isStar;
                        if (newStatus)
                            ToastUtils.showToast(mActivity, "关注成功");
                        mNagLayout.setStarStatus(newStatus, newStatus ? "已关注" : "关注");
                        isStar = newStatus;
                        // 关注品牌/商家 事件
                        EventBus.getDefault().post(new BrandStoreFollowEvent());
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissProgressDialog();
    }

    public static void toThisActivity(Activity activity, String siteName, int pageType) {
        Intent intent = new Intent(activity, SiteActivity.class);
        intent.putExtra("name", siteName);
        intent.putExtra("type", pageType);
        activity.startActivity(intent);
    }

    public static void toThisActivity(Activity activity, String siteName, int pageType, String logo1, String logo3, String imgCover) {
        Intent intent = new Intent(activity, SiteActivity.class);
        intent.putExtra("name", siteName);
        intent.putExtra("type", pageType);
        intent.putExtra("logo1", logo1);
        intent.putExtra("logo3", logo3);
        intent.putExtra("img", imgCover);
        activity.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "name=" + mSiteName;
    }
}
