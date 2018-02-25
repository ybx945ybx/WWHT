package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.BrandStorePagerAdapter;
import com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.myaccount.brandstore.BrandSellerFragmentFactory;
import com.a55haitao.wwht.ui.fragment.myaccount.brandstore.SellerFragment;
import com.a55haitao.wwht.utils.TraceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType.BRAND;
import static com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType.SELLER;

/**
 * 关注的品牌&商家
 * {@link com.a55haitao.wwht.ui.fragment.myaccount.brandstore.BrandFragment 品牌Fragment}
 * {@link SellerFragment 商家Fragment}
 */
public class BrandAndSellerActivity extends BaseHasFragmentActivity {
    @BindView(R.id.ib_cancel)               ImageButton mIbTitleBack;                     // 返回按钮
    @BindView(R.id.rdo_bandAndStore_band)   RadioButton mRdoBandAndStoreBand;     // 品牌
    @BindView(R.id.rdo_bandAndStore_store)  RadioButton mRdoBandAndStoreStore;   // 商家
    @BindView(R.id.rgp_bandAndStore_type)   RadioGroup  mRgpBandAndStoreType;      // RadioGroup
    @BindView(R.id.vp_bandAndStore_content) ViewPager   mVpBandAndStoreContent;   // content

    @BindString(R.string.key_user_id) String KEY_USER_ID;   // key:user_id
    @BindString(R.string.key_type)    String KEY_TYPE;         // key:user_id

    private int     mUserId;        // 用户Id
    private int     mCurrentType;   // 类型 : 品牌/商家
    private Tracker mTracker;       // GA Tracker

    public int getUserId() {
        return mUserId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_and_seller);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mCurrentType = intent.getIntExtra(KEY_TYPE, BrandSellerFragmentType.BRAND);
            mUserId = intent.getIntExtra(KEY_USER_ID, 0);
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(mCurrentType == BrandSellerFragmentType.BRAND ? "关注的品牌" : "关注的商家");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        /* bind listener */
        mVpBandAndStoreContent.setOffscreenPageLimit(0);
        mVpBandAndStoreContent.setAdapter(new BrandStorePagerAdapter(getSupportFragmentManager()));

        mRgpBandAndStoreType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rdo_bandAndStore_band:
                    mVpBandAndStoreContent.setCurrentItem(0);  // 偷懒写法
                    break;
                case R.id.rdo_bandAndStore_store:
                    mVpBandAndStoreContent.setCurrentItem(1);
                    break;
            }
        });

        // 根据不同的类型动态切换
        switch (mCurrentType) {
            case BRAND:
                mRdoBandAndStoreBand.setChecked(true);
                break;
            case SELLER:
                mRdoBandAndStoreStore.setChecked(true);
                break;
            default:
                break;
        }

        mVpBandAndStoreContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // GA
                mTracker.setScreenName(position == 0 ? "关注的品牌" : "关注的商家");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                // title中的RadioGroup切换
                switch (position) {
                    case 0:
                        mRdoBandAndStoreBand.setChecked(true); // 偷懒写法
                        break;
                    case 1:
                        mRdoBandAndStoreStore.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_cancel)
    public void onClick() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        BrandSellerFragmentFactory.clearFragments();
        super.onDestroy();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}

