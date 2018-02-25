package com.a55haitao.wwht.ui.activity.myaccount.account;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadManager;
import com.upyun.library.listener.SignatureListener;
import com.upyun.library.utils.UpYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 注册后引导用户填写昵称 头像
 * Created by a55 on 2017/5/23.
 */

public class RegisterSetUserInfoActivity extends BaseNoFragmentActivity {

    @BindView(R.id.iv_head)     ImageView      ivHead;
    @BindView(R.id.iv_change)   ImageView      ivChange;
    @BindView(R.id.et_nickname) EditText       etNickName;
    @BindView(R.id.btn_commit)  Button         btnCommit;

    private static final int    REQUEST_CODE_GALLERY = 100;
    private static final String KEY                  = "MJrxn7x0fP5c54QFfb0GlFiJ9pY=";
    private static final String SPACE                = "st-prod";
    private static final String savePath             = "/avatar/{year}/{mon}/{day}/{random32}";

    private String         mImagePath;
    private FunctionConfig mFunctionConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_set_user_info);
        ButterKnife.bind(this);

        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(false)
                .build();

        initViews();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    private void initViews() {
        etNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    btnCommit.setEnabled(false);
                } else {
                    btnCommit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.ib_back, R.id.rlyt_skip})
    public void onSkipOrCancle() {
        onBackPressed();
    }

    @OnClick(R.id.btn_commit)
    public void onCommit() {
        showProgressDialog("保存中", true);
        if (TextUtils.isEmpty(mImagePath)) {
            requestSaveUserInfo();
        } else {
            compress();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 上传头像
     */
    @OnClick(R.id.iv_head)
    public void changeAvatar() {
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mFunctionConfig, mOnHanlderResultCallback);
    }

    /**
     * 图片选择回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
            if (resultList != null) {
                mImagePath = TextUtils.isEmpty(resultList.get(0).getCropPhotoPath()) ? resultList.get(0).getPhotoPath() : resultList.get(0).getCropPhotoPath();
                Glide.with(mActivity)
                        .load(mImagePath)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .transform(new GlideCircleTransform(mActivity))
                        .into(ivHead);
                ivChange.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };


    /**
     * 压缩图片后上传又拍云
     */
    private void compress() {
        File file        = new File(mImagePath);
        long fileContent = file.length();
        if (fileContent > 200 * 1024) {
            Luban.get(RegisterSetUserInfoActivity.this)
                    .load(file)
                    .putGear(Luban.THIRD_GEAR)
                    .asObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<File>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(File file) {
                            mImagePath = file.getAbsolutePath();
                            requestUpyunUpload();
                        }
                    });
        } else {
            requestUpyunUpload();
        }
    }

    /**
     * E
     * 又拍云上传图片
     */
    private void requestUpyunUpload() {

        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, SPACE);
        paramsMap.put(Params.SAVE_KEY, savePath);

        UploadManager.getInstance().formUpload(new File(mImagePath),
                paramsMap,
                signatureListener,
                (isSuccess, result) -> {
                    Logger.t(TAG).d(result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optInt("code") == 200) {
                            mImagePath = ServerUrl.UPYUN_BASE + jsonObject.optString("url");
                            RegisterSetUserInfoActivity.this.requestSaveUserInfo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (bytesWrite, contentLength) -> {
                });

    }

    SignatureListener signatureListener = raw -> UpYunUtils.md5(raw + KEY);

    /**
     * 保存个人信息
     */
    private void requestSaveUserInfo() {
        UserRepository.getInstance()
                .updateProfile(etNickName.getText().toString(), !TextUtils.isEmpty(mImagePath) ? mImagePath : null)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean result) {
                        EventBus.getDefault().post(new LoginStateChangeEvent());
                        ToastUtils.showToast(mActivity, "保存成功");
                        new Handler().postDelayed(() -> onBackPressed(), 300);

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

}
