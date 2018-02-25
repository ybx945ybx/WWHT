package com.a55haitao.wwht.ui.activity.myaccount;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.UserInfoChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.Sex;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.entity.UserTitle;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.TxtUtils;
import com.a55haitao.wwht.utils.glide.GlideCircleTransform;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.realm.RealmList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 用户信息设置页
 */
public class UserInfoSettingsActivity extends BaseNoFragmentActivity {
    @BindView(R.id.title)         DynamicHeaderView mTitle;         // 标题
    @BindView(R.id.img_avatar)    ImageView         mImgAvatar;     // 头像
    @BindView(R.id.et_nickname)   EditText          mEtNickname;    // 昵称
    @BindView(R.id.tv_sex)        TextView          mTvSex;         // 性别
    @BindView(R.id.et_location)   EditText          mEtLocation;    // 地区
    @BindView(R.id.tv_signature)  HaiTextView       mTvSignature;   // 签名
    @BindView(R.id.tv_user_title) HaiTextView       mTvUserTitle;   // 认证信息
    @BindView(R.id.ll_user_title) LinearLayout      mLlUserTitle;   // 认证信息容器

    @BindString(R.string.key_head_img)   String KEY_HEAD_IMG;
    @BindString(R.string.key_nickname)   String KEY_NICKNAME;
    @BindString(R.string.key_sex)        String KEY_SEX;
    @BindString(R.string.key_location)   String KEY_LOCATION;
    @BindString(R.string.key_signature)  String KEY_SIGNATURE;
    @BindString(R.string.key_method)     String KEY_METHOD;
    @BindString(R.string.key_ftype)      String KEY_FTYPE;
    @BindString(R.string.key_suffix)     String KEY_SUFFIX;
    @BindString(R.string.hint_signature) String HINT_SIGNATURE;

    @BindColor(R.color.colorGrayCCCCCC) int COLOR_HINT;
    @BindColor(R.color.colorGray666666) int COLOR_GREY;

    private static final int BAIDU_READ_PHONE_STATE        = 100;
    private static final int REQUEST_CODE_GALLERY          = 1001;
    private static final int REQUEST_CODE_UPDATE_SIGNATURE = 1002;

    private static final String KEY      = "MJrxn7x0fP5c54QFfb0GlFiJ9pY=";
    private static final String SPACE    = "st-prod";
    private static final String savePath = "/avatar/{year}/{mon}/{day}/{random32}";

    // 百度定位相关
    public LocationClient     mLocationClient = null;
    public BDLocationListener myListener      = new MyLocationListener();
    private String            mLocalImgPath;  // 本地图片地址
    private String            mNewHeadImg;    // 新头像
    private int               mSex;           // 性别
    private String            mSignature;
    private Tracker           mTracker;       // GA Tracker
    private ToastPopuWindow   mPwToast;
    private ArrayList<String> mPermissionList;// 权限列表
    private FunctionConfig mFunctionConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_settings);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
    }

    private void initVars() {
        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(false)
                .build();

        mPermissionList = new ArrayList<>();
        //        mPermissionList.add(Manifest.permission.READ_PHONE_STATE);
        mPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        mPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        //        mPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("个人信息设置");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 初始化UI
     */
    public void initViews(Bundle savedInstanceState) {
        mTitle.setHeadClickListener(() -> {
            if (!checkInput())
                return;

            showProgressDialog("保存中", true);
            if (!TextUtils.isEmpty(mLocalImgPath)) {
                //                requestUpyunUpload();
                compress();
            } else {
                requestSaveUserInfo();
            }
        });
        // 用户信息
        UserBean userInfo = UserRepository.getInstance().getUserInfo();
        if (userInfo != null) {
            // 头像
            Glide.with(mActivity)
                    .load(userInfo.getHeadImg())
                    .placeholder(R.mipmap.ic_avatar_default_large)
                    .transform(new GlideCircleTransform(mActivity))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(mImgAvatar);
            // 昵称
            mEtNickname.setText(userInfo.getNickname());
            // 性别
            setSexView(userInfo.getSex());
            // 地址
            mEtLocation.setText(userInfo.getLocation());
            // 禁止emoji
            mEtNickname.setFilters(new InputFilter[]{TxtUtils.EMOJI_FILTER, new InputFilter.LengthFilter(16)});
            // 设置签名文本
            setSignature(userInfo.getSignature());
            // 认证信息
            RealmList<UserTitle> userTitle = userInfo.getUserTitleList();
            if (userTitle != null && userTitle.size() != 0) {
                mLlUserTitle.setVisibility(View.VISIBLE);
                mTvUserTitle.setText(userTitle.get(0).getTitle());
            } else {
                mLlUserTitle.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置签名文本
     *
     * @param signature 用户签名
     */
    public void setSignature(String signature) {
        mSignature = signature;
        // 设置签名
        mTvSignature.setText(TextUtils.isEmpty(signature) ? HINT_SIGNATURE : signature);
        // 设置签名颜色
        mTvSignature.setTextColor(TextUtils.isEmpty(signature) ? COLOR_HINT : COLOR_GREY);
    }

    /**
     * 压缩图片后上传又拍云
     */
    private void compress() {
        File file        = new File(mLocalImgPath);
        long fileContent = file.length();
        if (fileContent > 200 * 1024) {
            Luban.get(UserInfoSettingsActivity.this)
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
                            mLocalImgPath = file.getAbsolutePath();
                            requestUpyunUpload();
                        }
                    });
        } else {
            requestUpyunUpload();
        }
    }

    /**
     * 又拍云上传图片
     */
    private void requestUpyunUpload() {

        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, SPACE);
        paramsMap.put(Params.SAVE_KEY, savePath);

        UploadManager.getInstance().formUpload(new File(mLocalImgPath),
                paramsMap,
                signatureListener,
                (isSuccess, result) -> {
                    //                    dismissProgressDialog();
                    Logger.t(TAG).d(result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optInt("code") == 200) {
                            //                            mNewHeadImg = ServerUrl.UPYUN_BASE + jsonObject.optString("small_icon");
                            mNewHeadImg = ServerUrl.UPYUN_BASE + jsonObject.optString("url");
                            requestSaveUserInfo();
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
     * 检验
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(mEtNickname.getText().toString())) {
            ToastUtils.showToast(mActivity, "昵称不能为空");
            return false;
        }
        return true;
    }

    /**
     * 保存个人信息
     */
    private void requestSaveUserInfo() {
        UserRepository.getInstance()
                .updateProfile(mEtNickname.getText().toString(),
                        mSex,
                        mEtLocation.getText().toString(),
                        mSignature,
                        !TextUtils.isEmpty(mNewHeadImg) ? mNewHeadImg : null)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean result) {

                        EventBus.getDefault().post(new UserInfoChangeEvent());
                        if (result.membership_point > 0) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, result.membership_point, AlterPointViewType.AlterPointViewType_UploadAvatar).parentView(mImgAvatar);
                        } else {
                            mPwToast = ToastPopuWindow.makeText(mActivity, "更新成功", AlertViewType.AlertViewType_OK).parentView(mImgAvatar);
                        }
                        mPwToast.show();

                        new Handler().postDelayed(() -> onBackPressed(), 1600);

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 修改头像
     */
    @OnClick(R.id.img_avatar)
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
                mLocalImgPath = TextUtils.isEmpty(resultList.get(0).getCropPhotoPath()) ? resultList.get(0).getPhotoPath() : resultList.get(0).getCropPhotoPath();
                Glide.with(mActivity)
                        .load(mLocalImgPath)
                        .transform(new GlideCircleTransform(mActivity))
                        .into(mImgAvatar);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };

    /**
     * 修改性别
     */
    @OnClick(R.id.tv_sex)
    public void changeGender() {
        ActionSheet.createBuilder(mActivity, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("男", "女")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                mSex = Sex.MAN;
                                break;
                            case 1:
                                mSex = Sex.WOMAN;
                                break;
                            default:
                                mSex = Sex.UNDEFINED;
                                break;
                        }
                        setSexView(mSex);
                    }
                }).show();
    }

    /**
     * 设置性别框文字
     */
    private void setSexView(@Sex int sex) {
        switch (sex) {
            case Sex.MAN:
                mTvSex.setText("男");
                break;
            case Sex.WOMAN:
                mTvSex.setText("女");
                break;
            case Sex.UNDEFINED:
                mTvSex.setText("");
                break;
        }
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     */
    public static void toThisActivity(Context context) {
        context.startActivity(new Intent(context, UserInfoSettingsActivity.class));
    }

    /**
     * 修改签名
     */
    @OnClick(R.id.ll_signature)
    public void clickSignature(View view) {
        Intent intent = new Intent(this, UserSignatureActivity.class);
        intent.putExtra("signature", mSignature);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_SIGNATURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_SIGNATURE && resultCode == RESULT_OK) { // 修改签名
            String signature = data.getStringExtra(KEY_SIGNATURE);
            setSignature(signature);
        }
    }

    /**
     * 获取定位
     */
    @OnClick(R.id.tv_get_position)
    public void getPosition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < mPermissionList.size(); i++) {
                if (checkSelfPermission(mPermissionList.get(i)) != PackageManager.PERMISSION_GRANTED) {
                    list.add(mPermissionList.get(i));
                }
            }

            switch (list.size()) {
                case 0:
                    startLocation();
                    break;
                case 1:
                    String[] permisses1 = {list.get(0)};
                    requestPermissions(permisses1, BAIDU_READ_PHONE_STATE);
                    break;
                case 2:
                    String[] permisses2 = {list.get(0), list.get(1)};
                    requestPermissions(permisses2, BAIDU_READ_PHONE_STATE);
                    break;
                case 3:
                    String[] permisses3 = {list.get(0), list.get(1), list.get(2)};
                    requestPermissions(permisses3, BAIDU_READ_PHONE_STATE);
                    break;
                case 4:
                    String[] permisses4 = {list.get(0), list.get(1), list.get(2), list.get(3)};
                    requestPermissions(permisses4, BAIDU_READ_PHONE_STATE);
                    break;

            }

        } else {
            startLocation();
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        ToastUtils.showToast(mActivity, "正在获取定位");
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        if (grantResults.length == permissions.length && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
            startLocation();
        } else {
            ToastUtils.showToast(mActivity, "请打开相关权限");
        }
    }

    /**
     * 定位监听
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位
                sb.append(location.getAddress().country + ", " + location.getAddress().city);    //获取地址信息
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                sb.append(location.getAddress().country + ", " + location.getAddress().city);    //获取地址信息
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                sb.append(location.getAddress().country + ", " + location.getAddress().city);    //获取地址信息
            }

            mEtLocation.post(() -> {
                String locationStr = sb.toString();
                if (TextUtils.isEmpty(locationStr)) {
                    // 获取失败
                    ToastUtils.showToast(mActivity, "网络忙，请稍后重试");
                } else {
                    // 获取成功
                    mEtLocation.setText(locationStr);
                    mEtLocation.setSelection(locationStr.length());
                }
            });
            Logger.d(location.getLocType());
            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
