package com.a55haitao.wwht.ui.activity.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.APIErrorEvent;
import com.a55haitao.wwht.data.event.CouponCashEvent;
import com.a55haitao.wwht.data.model.entity.ConvertCouponBean;
import com.a55haitao.wwht.data.model.entity.CouponBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.discover.CouponFragment;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.ExchangeCouponLayout;
import com.a55haitao.wwht.utils.HaiUtils;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 优惠券列表页
 */
public class CouponListActivity extends BaseHasFragmentActivity {

    @BindView(R.id.headView)             DynamicHeaderView mHeadView;
    @BindView(R.id.tabLayout)            TabLayout         mTabLayout;
    @BindView(R.id.viewPager)            ViewPager         mViewPager;

    private CouponFragment[] mFragments;
    private AlertDialog      mExchangeCouponDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mFragments = new CouponFragment[3];
        for (int i = 0; i < 3; i++) {
            mFragments[i] = CouponFragment.newInstance(i + 1);
        }

        mViewPager.setAdapter(new CouponPageAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mHeadView.setHeadClickListener(() -> {
            if (mExchangeCouponDialog == null) {
                creatAlertDialog();
            }
            mExchangeCouponDialog.show();
        });

    }

    private void creatAlertDialog() {
        ExchangeCouponLayout layout = (ExchangeCouponLayout) LayoutInflater.from(this).inflate(R.layout.exchange_code_for_coupon_layout, null);

        layout.setInterface(code -> {

            if (HaiUtils.needLogin(mActivity)) return;

            if (TextUtils.isEmpty(code)) {
                layout.setError(true, "兑换码不能为空, 请输入");
                return;
            }

            SnsRepository.getInstance()
                    .exchangeCopuon(code)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<ConvertCouponBean>() {
                        @Override
                        public void onSuccess(ConvertCouponBean convertCouponBean) {
                            if (convertCouponBean.couponq_list.size() > 0) {
                                layout.setError(false, null);
                                CouponBean couponBean = convertCouponBean.couponq_list.get(0);
                                layout.setExchangeSuccess(couponBean.getTitle(), couponBean.getSubtitle(), couponBean.getDuration());
                                EventBus.getDefault().post(new CouponCashEvent());
                            } else {
                                layout.setError(true, "兑换码错误，请重新输入");
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    });

        });

        mExchangeCouponDialog = new AlertDialog.Builder(this).setView(layout).create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    @Subscribe
    public void onApiErrorEvent(APIErrorEvent event) {
        ExchangeCouponLayout layout = (ExchangeCouponLayout) mExchangeCouponDialog.findViewById(R.id.centerView);
        layout.setError(true, event.msg);
    }

    public class CouponPageAdapter extends FragmentPagerAdapter {

        public CouponPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "未使用";
            } else if (position == 1) {
                return "已过期";
            } else if (position == 2) {
                return "已使用";
            } else {
                return super.getPageTitle(position);
            }
        }
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     */
    public static void toThisActivity(Context context) {
        context.startActivity(new Intent(context, CouponListActivity.class));
    }
}
