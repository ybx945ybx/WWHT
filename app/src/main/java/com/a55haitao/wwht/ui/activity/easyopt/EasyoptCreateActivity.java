package com.a55haitao.wwht.ui.activity.easyopt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.EasyOptCreateCountChangeEvent;
import com.a55haitao.wwht.data.event.EasyoptListChangeEvent;
import com.a55haitao.wwht.data.model.annotation.EasyoptPublicVisible;
import com.a55haitao.wwht.data.model.entity.CommCountsBean;
import com.a55haitao.wwht.data.model.entity.PriorityModel;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.utils.EasyoptNameLengthFilter;
import com.a55haitao.wwht.utils.FileUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.Md5FileNameGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class EasyoptCreateActivity extends EasyoptBaseActivity {

    private Tracker mTracker;   // GA Tracker

    @Override
    protected int getLayout() {
        return R.layout.activity_easyopt_create;
    }

    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            mEasyoptLogoUrl = intent.getStringExtra("easyopt_logo");
            mSpuId = intent.getStringExtra("spu_id");
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("草单_创建");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(mEasyoptLogoUrl)) {
            Glide.with(mActivity)
                    .load(mEasyoptLogoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImgEasyoptLogo);
        } else {
            mImgEasyoptLogo.setImageResource(R.mipmap.ic_easyopt_logo_default);
        }
        // 草单名输入控制
        InputFilter[] filters = {new EasyoptNameLengthFilter(30)};
        mEtEasypostName.setFilters(filters);
    }

    /**
     * 创建草单
     */
    @OnClick(R.id.tv_submit)
    public void clickSubmit() {
        String easyoptName = mEtEasypostName.getText().toString().trim(); // 草单名称
        String easyoptDesc = mEtEasypostDesc.getText().toString().trim(); // 草单描述

        // 输入验证
        if (!checkInput(easyoptName, easyoptDesc))
            return;

        showProgressDialog();
        if (TextUtils.isEmpty(mLocalEasyoptLogoUrl)) { // 未上传本地图片
            if (TextUtils.isEmpty(mEasyoptLogoUrl)) { // 不是商品详情过来的,没有封面了补充默认图做封面
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_easyopt_logo_default);
                mLocalEasyoptLogoUrl = FileUtils.saveBitmap(mActivity, bitmap, new Md5FileNameGenerator().generate("easy"));
                requestUpyunUpload(easyoptName, easyoptDesc);
            }else {
                requestCreateEasyopt(easyoptName, easyoptDesc, mEasyoptLogoUrl);
            }
        } else { // 上传新图片
            requestUpyunUpload(easyoptName, easyoptDesc);
        }

    }

    /**
     * 创建草单请求
     *
     * @param easyoptName 草单名称
     * @param easyoptDesc 草单描述
     * @param upyunImgUrl 又拍云图片地址
     */
    @Override
    protected void requestCreateEasyopt(String easyoptName, String easyoptDesc, String upyunImgUrl) {
        String selectIds = "";
        if (!TextUtils.isEmpty(mSpuId)) {
            ArrayList<PriorityModel> list = new ArrayList<>(1);
            list.add(new PriorityModel(1, mSpuId));
            selectIds = new Gson().toJson(list);
        }

        // 草单是否公开
        int easyoptVisible = mSbEasyoptVisible.isChecked() ? EasyoptPublicVisible.PUBLIC_VISIBLE : EasyoptPublicVisible.PUBLIC_INVISIBLE;

        SnsRepository.getInstance()
                .easyoptUpdate(easyoptName, easyoptDesc, upyunImgUrl, 0, easyoptVisible, selectIds)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        ToastUtils.showToast(mActivity, "草单创建成功");

                        int easyoptCount = HaiUtils.getEasyoptCount() + 1;
                        // 更新本地数据
                        Realm.getDefaultInstance()
                                .executeTransactionAsync(realm -> {
                                    RealmResults<UserBean> users = realm.where(UserBean.class).findAll();
                                    if (users.size() > 0) {
                                        UserBean       user       = users.get(0);
                                        CommCountsBean commCounts = user.getCommCounts();
                                        if (commCounts != null) {
                                            commCounts.setEasyopt_count(easyoptCount);
                                            Logger.d(user.getCommCounts().getEasyopt_count());

                                        }
                                    }
                                });

                        // 发送草单改变事件
                        EventBus.getDefault().post(new EasyoptListChangeEvent());
                        EventBus.getDefault().post(new EasyOptCreateCountChangeEvent(easyoptCount));

                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 新建草单
     *
     * @param context     context
     * @param easyoptLogo 草单Logo
     * @param spuId       商品spuId
     */
    public static void toThisActivity(Context context, String easyoptLogo, String spuId) {
        Intent intent = new Intent(context, EasyoptCreateActivity.class);
        intent.putExtra("easyopt_logo", easyoptLogo);
        intent.putExtra("spu_id", spuId);
        context.startActivity(intent);
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
