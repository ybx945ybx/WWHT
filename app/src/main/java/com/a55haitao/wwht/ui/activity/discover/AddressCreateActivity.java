package com.a55haitao.wwht.ui.activity.discover;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.entity.AddressItemBean;
import com.a55haitao.wwht.data.model.entity.CommonSuccessResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.net.PullParser;
import com.a55haitao.wwht.data.repository.PassPortRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.Base64Utils;
import com.a55haitao.wwht.utils.CheckHelper;
import com.a55haitao.wwht.utils.FileUtils;
import com.a55haitao.wwht.utils.NameLengthFilter;
import com.a55haitao.wwht.utils.ToastUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kyleduo.switchbutton.SwitchButton;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 新建收货地址
 */
public class AddressCreateActivity extends BaseNoFragmentActivity implements GalleryFinal.OnHanlderResultCallback {

    private final int GALLERY_A_REQUEST_CODE = 66;        //RequestCode
    private final int GALLERY_B_REQUEST_CODE = 67;        //RequestCode

    @BindView(R.id.isDefaultBtn)  SwitchButton       mIsDefaultBtn;
    @BindView(R.id.msv)           MultipleStatusView mSv;
    @BindView(R.id.nameEdt)       EditText           mNameEdt;
    @BindView(R.id.phoneEdt)      EditText           mPhoneEdt;
    @BindView(R.id.cardEdt)       EditText           mCardEdt;
    @BindView(R.id.areaEdt)       EditText           mAreaEdt;
    @BindView(R.id.addressEdt)    EditText           mAddressEdt;
    @BindView(R.id.title)         DynamicHeaderView  mHeaderView;
    @BindView(R.id.aImg)          ImageView          mAImg;
    @BindView(R.id.bImg)          ImageView          mBImg;
    @BindView(R.id.aWaterMarkImg) ImageView          mAWaterMarkImg;
    @BindView(R.id.bWaterMarkImg) ImageView          mBWaterMarkImg;
    @BindView(R.id.delBtn)        Button             mDelBtn;

    @BindColor(R.color.colorGray666666) int mColorGrey666;

    private String[] mUris; //用于存储拍照的照片

    private String[] mBase64Strings;

    private int currentPositon;

    //用于xml解析城市,已经保存数据
    PullParser pullParser;

    private OptionsPickerView pickerView;

    private AddressItemBean mAddressItemBean;

    //收货地址id,默认为-1
    private int             id;
    private ToastPopuWindow mPwToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_create);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra(Integer.class.getName(), -1);
        mHeaderView.setHeadClickListener(() -> {
            if (exam()) {
                AddressCreateActivity.this.request();
            }
        });

        mSv.setOnRetryClickListener(v -> loadData());

        if (id == -1) { //新增收货地址
            mAddressItemBean = new AddressItemBean();
        } else {  //修改收货地址
            mDelBtn.setVisibility(View.VISIBLE);
            loadData();
            mHeaderView.setHeadTitle("修改收货地址");
            mAImg.setImageResource(R.mipmap.ic_card_loading);
            mBImg.setImageResource(R.mipmap.ic_card_loading);
        }

        //初始化
        mUris = new String[2];
        mBase64Strings = new String[2];
        // 姓名不超过15个汉字（30个英文字符）
        mNameEdt.setFilters(new InputFilter[]{new NameLengthFilter(mActivity, 30)});

    }

    private void loadData() {
        mSv.showLoading();
        PassPortRepository.getInstance()
                .addressDetail(id)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<AddressItemBean>() {
                    @Override
                    public void onSuccess(AddressItemBean result) {
                        mAddressItemBean = result;
                        mIsDefaultBtn.setChecked(result.is_default == 1);

                        mNameEdt.setText(result.name);
                        mPhoneEdt.setText(result.phone);
                        mCardEdt.setText(result.idt_number);

                        mAreaEdt.setText(result.province + result.city + result.dist);

                        mAddressEdt.setText(result.street);

                        Glide.with(mActivity)
                                .load(result.identCardFrontImage)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        mAWaterMarkImg.setVisibility(View.VISIBLE);
                                        mAImg.setImageBitmap(resource);
                                    }
                                });
                        Glide.with(mActivity)
                                .load(result.identCardBackImage)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        mBWaterMarkImg.setVisibility(View.VISIBLE);
                                        mBImg.setImageBitmap(resource);
                                    }
                                });

                        mSv.showContent();
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }
                });
    }

    @OnClick({R.id.aImg, R.id.bImg, R.id.areaEdt, R.id.delBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aImg:
                currentPositon = 0;
                openGallery(GALLERY_A_REQUEST_CODE);
                break;
            case R.id.bImg:
                currentPositon = 1;
                openGallery(GALLERY_B_REQUEST_CODE);
                break;
            case R.id.areaEdt:
                //隐藏输入法
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                showPickerView();
                break;
            case R.id.delBtn:
                AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                        .setMessage("确认删除该地址信息")
                        .setPositiveButton("确定", (dialog, which) -> {
                            PassPortRepository.getInstance()
                                    .deleteAddress(id)
                                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                                    .subscribe(new DefaultSubscriber<CommonSuccessResult>() {
                                        @Override
                                        public void onSuccess(CommonSuccessResult result) {
                                            if (TextUtils.equals("1", result.success)) {
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFinish() {

                                        }
                                    });
                        })
                        .setNegativeButton("取消", null).create();
                alertDialog.show();
                break;
        }
    }

    /**
     * 添加或修改收货地址
     * 若id = -1 ,为添加,调用添加接口
     * 否则,为修改调用修改接口
     */
    private void request() {
        showProgressDialog(StringConstans.TOAST_SAVE, true);

        PassPortRepository.getInstance()
                .addOrUpdateAddress(id, mPhoneEdt.getText().toString(), mNameEdt.getText().toString(), mCardEdt.getText().toString(),
                        mAddressItemBean.province, mAddressItemBean.city, mAddressItemBean.dist, mAddressEdt.getText().toString(),
                        mBase64Strings[0], mBase64Strings[1], mIsDefaultBtn.isChecked())
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<AddressItemBean>() {
                    @Override
                    public void onSuccess(AddressItemBean addressItemBean) {
                        ToastUtils.showToast(AddressCreateActivity.this, id == -1 ? "添加成功" : "修改成功");
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
        String photoPath = TextUtils.isEmpty(resultList.get(0).getCropPhotoPath()) ? resultList.get(0).getPhotoPath() : resultList.get(0).getCropPhotoPath();
        //加载缩略图
        if (reqeustCode == GALLERY_A_REQUEST_CODE) {
            mAWaterMarkImg.setVisibility(View.INVISIBLE);
            Glide.with(this).load(photoPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mAImg);
            mUris[0] = photoPath;
        } else if (reqeustCode == GALLERY_B_REQUEST_CODE) {
            mBWaterMarkImg.setVisibility(View.INVISIBLE);
            Glide.with(this).load(photoPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mBImg);
            mUris[1] = photoPath;
        }

        //使用RX处理文件
        Observable.just(photoPath)
                .map(fileName -> {
                    FileUtils.normalsizeFile(fileName, -1);  //旋转图片，默认压缩度
                    return fileName;
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<File>>() {
                    @Override
                    public Observable<File> call(String fileName) {
                        return Luban.get(AddressCreateActivity.this)
                                .load(new File(fileName))
                                .putGear(Luban.THIRD_GEAR)
                                .asObservable();
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(file -> Base64Utils.fileToString(file))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onStart() {
                        showProgressDialog(StringConstans.TOAST_DEAL_FILE, false);
                    }

                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(String base64String) {
                        mBase64Strings[currentPositon] = base64String;
                    }
                });

    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
    }

    private void openGallery(int position) {
        FunctionConfig
                sFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableRotate(true)
                .setEnableCrop(true)
                .setAspectX(3)
                .setAspectY(2)
                .build();

        GalleryFinal.openGallerySingle(position, sFunctionConfig, this);
    }

    /**
     * 显示地址选择器
     */
    private void showPickerView() {

        if (pickerView == null) {
            pullParser = new PullParser();
            try {
                pullParser.parseFromJson(this);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            pickerView = new OptionsPickerView.Builder(mActivity, (options1, options2, options3, v) -> {
                mAddressItemBean.province = pullParser.getPrinceList().get(options1);
                mAddressItemBean.city = pullParser.getCityList().get(options1).get(options2);
                // 没有区一级的用市填补
                mAddressItemBean.dist = options3 >= 0 ? pullParser.getDistrictList().get(options1).get(options2).get(options3) : mAddressItemBean.city;
                mAreaEdt.setText(mAddressItemBean.province + mAddressItemBean.city + mAddressItemBean.dist);
            })
                    .setTitleText("选择城市")
                    .setCyclic(false, false, false)
                    .setSelectOptions(0, 0, 0)
                    .setSubmitText("确定")
                    .setCancelText("取消")
                    .setSubmitColor(mColorGrey666)
                    .setCancelColor(mColorGrey666)
                    .setSubCalSize(17)
                    .setContentTextSize(20)
                    .build();
            pickerView.setPicker(pullParser.getPrinceList(), pullParser.getCityList(), pullParser.getDistrictList());
        }
        pickerView.show();
    }

    /**
     * 检查用户信息是由有空,以及手机号/身份证号 合法性,空.可合法返回false
     * 否则返回true
     *
     * @return
     */
    private boolean exam() {
        if (CheckHelper.isEdtNull(mNameEdt)) {
            mPwToast = ToastPopuWindow.makeText(this, "请输入姓名", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }
        if (CheckHelper.hasDigit(mNameEdt)) {
            mPwToast = ToastPopuWindow.makeText(this, "请输入正确的姓名", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }
        if (CheckHelper.isEdtNull(mPhoneEdt)) {
            mPwToast = ToastPopuWindow.makeText(this, "请输入手机号", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }
        if (CheckHelper.isEdtNull(mCardEdt)) {
            mPwToast = ToastPopuWindow.makeText(this, "请输入身份证号", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }
        if (CheckHelper.isEdtNull(mAddressEdt)) {
            mPwToast = ToastPopuWindow.makeText(this, "请输入地址", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }
        if (CheckHelper.isEdtNull(mAreaEdt)) {
            mPwToast = ToastPopuWindow.makeText(this, "请输入地区", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }

        if (!CheckHelper.isIDNumber(mCardEdt)) {
            mPwToast = ToastPopuWindow.makeText(this, "请输入合法身份证号码", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }
        if (id == -1 && (mUris[0] == null || mUris[1] == null)) {
            mPwToast = ToastPopuWindow.makeText(this, "请上传身份证", AlertViewType.AlertViewType_Warning);
            mPwToast.show();
            return false;
        }

        return true;
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
