package com.a55haitao.wwht.ui.activity.easyopt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.EasyOptAction;
import com.a55haitao.wwht.data.event.EasyOptCreateCountChangeEvent;
import com.a55haitao.wwht.data.event.EasyoptListChangeEvent;
import com.a55haitao.wwht.data.model.annotation.EasyoptPublicVisible;
import com.a55haitao.wwht.data.model.entity.CommCountsBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.utils.EasyoptNameLengthFilter;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class EasyoptEditActivity extends EasyoptBaseActivity {

    private int         mEsayoptId;         // 草单Id
    private String      mEasyoptName;       // 草单名
    private String      mEasyoptDesc;       // 草单描述
    private AlertDialog mDlgDeleteEasyoptList;
    private Tracker     mTracker; // GA Tracker

    @Override
    protected int getLayout() {
        return R.layout.activity_easyopt_edit;
    }


    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mEsayoptId = intent.getIntExtra("easyopt_id", 0);
            mEasyoptLogoUrl = intent.getStringExtra("easyopt_logo");
            mEasyoptName = intent.getStringExtra("easyopt_name");
            mEasyoptDesc = intent.getStringExtra("easyopt_desc");
            // 设置草单是否公开可见
            int easyoptVisible = intent.getIntExtra("easyopt_visible", EasyoptPublicVisible.PUBLIC_VISIBLE);
            mSbEasyoptVisible.setChecked(easyoptVisible == EasyoptPublicVisible.PUBLIC_VISIBLE);
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("草单_编辑");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // 草单图片
        if (!TextUtils.isEmpty(mEasyoptLogoUrl)) {
            Glide.with(mActivity)
                    .load(mEasyoptLogoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImgEasyoptLogo);
        } else {
            mImgEasyoptLogo.setImageResource(R.mipmap.ic_easyopt_logo_default);
        }
        // 草单名
        mEtEasypostName.setText(mEasyoptName);
        mEtEasypostName.setSelection(mEasyoptName.length());
        // 草单描述
        mEtEasypostDesc.setText(mEasyoptDesc);
        // 草单名输入控制
        InputFilter[] filters = {new EasyoptNameLengthFilter(30)};
        mEtEasypostName.setFilters(filters);
    }

    /**
     * 修改草单
     */
    @OnClick(R.id.tv_submit)
    public void clickSubmit() {
        String easyoptName = mEtEasypostName.getText().toString().trim(); // 草单名称
        String easyoptDesc = mEtEasypostDesc.getText().toString().trim(); // 草单描述

        // 输入验证
        if (!checkInput(easyoptName, easyoptDesc))
            return;

        showProgressDialog();
        if (TextUtils.isEmpty(mLocalEasyoptLogoUrl)) { // 未上传新图片
            requestCreateEasyopt(easyoptName, easyoptDesc, mEasyoptLogoUrl);
        } else { // 上传新图片
            requestUpyunUpload(easyoptName, easyoptDesc);
        }
    }

    /**
     * 修改草单请求
     *
     * @param easyoptName 草单名称
     * @param easyoptDesc 草单描述
     * @param upyunImgUrl 又拍云图片地址
     */
    @Override
    protected void requestCreateEasyopt(String easyoptName, String easyoptDesc, String upyunImgUrl) {
        // 草单是否公开
        int easyoptVisible = mSbEasyoptVisible.isChecked() ? EasyoptPublicVisible.PUBLIC_VISIBLE : EasyoptPublicVisible.PUBLIC_INVISIBLE;

        SnsRepository.getInstance()
                .easyoptUpdate(easyoptName, easyoptDesc, upyunImgUrl, mEsayoptId, easyoptVisible)
                .compose(bindToLifecycle())
                .subscribe(new DefaultSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        ToastUtils.showToast(mActivity, "修改成功");
                        // 发送草单改变事件
                        EventBus.getDefault().post(new EasyoptListChangeEvent());
                        EventBus.getDefault().post(new EasyOptAction(EasyOptAction.UPDATE));
                        //跳往新界面
                        EasyOptDetailActivity.toThisActivity(mActivity, mEsayoptId);
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 跳转到本页面
     *
     * @param context     context
     * @param easyoptId   草单Id
     * @param easyoptLogo 草单Logo
     * @param easyoptName 草单名
     * @param easyoptDesc 草单描述
     */
    public static void toThisActivity(Context context, int easyoptId, String easyoptLogo, String easyoptName, String easyoptDesc, @EasyoptPublicVisible int visible) {
        Intent intent = new Intent(context, EasyoptEditActivity.class);
        intent.putExtra("easyopt_id", easyoptId);
        intent.putExtra("easyopt_logo", easyoptLogo);
        intent.putExtra("easyopt_name", easyoptName);
        intent.putExtra("easyopt_desc", easyoptDesc);
        intent.putExtra("easyopt_visible", visible);
        context.startActivity(intent);
    }

    /**
     * 删除草单
     */
    @OnClick(R.id.tv_delete_easyopt)
    public void clickDeleteEasyopt() {
        if (mDlgDeleteEasyoptList == null) {
            mDlgDeleteEasyoptList = new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                    .setMessage("是否删除你的草单?")
                    .setPositiveButton("确定", (dialog, which) -> {
                        requestDeleteEasyoptList();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {

                    }).create();
        }
        mDlgDeleteEasyoptList.show();
    }

    /**
     * 删除草单网络请求
     */
    private void requestDeleteEasyoptList() {
        SnsRepository.getInstance()
                .easyoptDelete(mEsayoptId)
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object obj) {
                        ToastUtils.showToast(mActivity, "删除成功");
                        // 发送草单改变事件
                        EventBus.getDefault().post(new EasyOptAction(EasyOptAction.DEL));
                        EventBus.getDefault().post(new EasyoptListChangeEvent());

                        int easyoptCount = HaiUtils.getEasyoptCount() - 1;
                        // 更新本地数据
                        Realm.getDefaultInstance()
                                .executeTransactionAsync(realm -> {
                                    RealmResults<UserBean> users = realm.where(UserBean.class).findAll();
                                    if (users.size() > 0) {
                                        UserBean       user       = users.get(0);
                                        CommCountsBean commCounts = user.getCommCounts();
                                        if (commCounts != null) {
                                            commCounts.setEasyopt_count(easyoptCount);
                                        }
                                    }
                                });

                        // 发送草单改变事件
                        EventBus.getDefault().post(new EasyOptCreateCountChangeEvent(easyoptCount));
                        EventBus.getDefault().post(new EasyoptListChangeEvent());

                        finish();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
