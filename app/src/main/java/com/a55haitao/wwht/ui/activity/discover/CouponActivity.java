package com.a55haitao.wwht.ui.activity.discover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.CouponListAdapter;
import com.a55haitao.wwht.data.model.annotation.CouponStatus;
import com.a55haitao.wwht.data.model.entity.CouponBean;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 优惠券使用页
 */
public class CouponActivity extends BaseNoFragmentActivity {
    @BindView(R.id.headView)             DynamicHeaderView  headView;
    @BindView(R.id.msView)               MultipleStatusView mSv;
    @BindView(R.id.rv_contnet)           RecyclerView       mRvContent;

    private ArrayList<CouponBean> mCouponList;
    private Tracker               mTracker;                 // GA Tracker
    private CouponListAdapter     mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);

        initVars();
        initViews();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void initVars() {
        mCouponList = getIntent().getParcelableArrayListExtra(CouponBean.class.getName());

        if (mCouponList != null) {
            headView.setHeaderRightHidden();
        } else {
            mCouponList = new ArrayList<>();
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("优惠券列表");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRvContent.setLayoutManager(layoutManager);
        // adapter
        mAdapter = new CouponListAdapter(mCouponList);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItem(position).getStatus() == CouponStatus.UNUSED)
                    finishSelectCopuon(mAdapter.getData().get(position).getCode());
            }
        });

        if (mCouponList.size() != 0) {
            setView(mCouponList);
        }

    }

    private void setView(ArrayList<CouponBean> couponList) {

        if (couponList == null || couponList.size() == 0) {
            mSv.showEmpty();
        } else {
            mSv.showContent();
            mAdapter.setNewData(couponList);
        }

    }

    @OnClick({R.id.tv_do_not_use_coupon})
    public void clickDoNotUseCoupon() {
        finishSelectCopuon("");
    }

    /**
     * 结束优惠券选择
     *
     * @param code 优惠券码
     */
    private void finishSelectCopuon(String code) {
        Intent intent = new Intent();
        intent.putExtra("code", TextUtils.isEmpty(code) ? "" : code);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void toThisActivityWithResult(Activity activity, ArrayList<CouponBean> datas, String couponCode, int requestCode) {
        Intent intent = new Intent(activity, CouponActivity.class);
        if (datas != null && !TextUtils.isEmpty(couponCode)) {
            for (CouponBean bean : datas) {
                bean.setSelect(couponCode.equals(bean.getCode()));
            }
        }
        intent.putParcelableArrayListExtra(CouponBean.class.getName(), datas);
        activity.startActivityForResult(intent, requestCode);
    }
}
