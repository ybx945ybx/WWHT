package com.a55haitao.wwht.ui.activity.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.data.model.entity.AddressItemBean;
import com.a55haitao.wwht.data.model.entity.AddressListBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.PassPortRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 地址列表
 */
public class AddressListActivity extends BaseNoFragmentActivity {

    @BindView(R.id.shippingRecyclerView) RecyclerView       mRecyclerView;
    @BindView(R.id.addAddressBtn)        Button             mAddAddressBtn;
    @BindView(R.id.msv)                  MultipleStatusView msView;

    private boolean                        isPicker;
    private RVBaseAdapter<AddressItemBean> mAdapter;
    private Tracker                        mTracker;    // GA Tracker
    private int                            addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("地址管理");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if (getIntent() != null) {
            isPicker = getIntent().getBooleanExtra(Boolean.class.getName(), false);
            addressId = getIntent().getIntExtra("addressId", -1);
        }

        mAddAddressBtn.setOnClickListener(v -> startActivity(new Intent(AddressListActivity.this, AddressCreateActivity.class)));

        msView.setOnRetryClickListener(v -> requestList());
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestList();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void requestList() {
        msView.showLoading();

        PassPortRepository.getInstance()
                .addressList()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<AddressListBean>() {
                    @Override
                    public void onSuccess(AddressListBean addressListBean) {
                        if (mAdapter == null) {
                            mAdapter = new RVBaseAdapter<AddressItemBean>(mActivity, addressListBean.address_list, R.layout.item_ship_info_layout) {
                                @Override
                                public void bindView(RVBaseHolder holder, AddressItemBean item) {
                                    holder.setText(R.id.nameTxt, item.name);
                                    holder.setText(R.id.phoneTxt, item.phone);
                                    holder.setText(R.id.addressTxt, item.province + item.city + item.dist + item.street);
                                    holder.getView(R.id.defaultTxt).setVisibility(item.is_default == 1 ? View.VISIBLE : View.GONE);

                                    holder.getView(R.id.arrowRightImg).setTag(item.id);
                                    holder.itemView.setTag(item);
                                }
                            };
                            mAdapter.setOnClickListener(R.id.arrowRightImg, v -> {
                                Intent intent = new Intent(mActivity, AddressCreateActivity.class);
                                intent.putExtra(Integer.class.getName(), (int) v.getTag());
                                startActivity(intent);
                            });
                            mAdapter.setOnItemClickListener(v -> {
                                if (!isPicker) {
                                    return;
                                }
                                Intent intent = new Intent();
                                intent.putExtra(AddressItemBean.class.getName(), (AddressItemBean) v.getTag());
                                setResult(RESULT_OK, intent);
                                finish();
                            });
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.changeDatas(addressListBean.address_list);
                        }
                        msView.showContent();
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if(isPicker) {
            Intent intent = new Intent();
            if (mAdapter != null && HaiUtils.getSize(mAdapter.getmDatas()) > 0) {
                if (addressId == -1) {
                    boolean hasdefault = false;
                    for (int i = 0; i < HaiUtils.getSize(mAdapter.getmDatas()); i++) {
                        if (mAdapter.getmDatas().get(i).is_default == 1) {
                            hasdefault = true;
                            intent.putExtra(AddressItemBean.class.getName(), mAdapter.getmDatas().get(i));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    if (!hasdefault){
                        finish();
                    }
                } else {
                    boolean hasVisible = false;
                    for (int i = 0; i < HaiUtils.getSize(mAdapter.getmDatas()); i++) {
                        if (mAdapter.getmDatas().get(i).id == addressId) {
                            hasVisible = true;
                        }
                    }
                    intent.putExtra("addressVisible", hasVisible);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            } else {
                intent.putExtra("addressVisible", false);
                setResult(RESULT_CANCELED, intent);
                finish();
            }

        }else {
            finish();
        }
    }

}
