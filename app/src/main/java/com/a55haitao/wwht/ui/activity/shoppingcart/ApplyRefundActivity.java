package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.OrderChangeEvent;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ImagesBean;
import com.a55haitao.wwht.data.model.entity.RefundItemsBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.CanRefundProductListResult;
import com.a55haitao.wwht.data.model.result.RefundCommitResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MyLinearLayout;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadManager;
import com.upyun.library.listener.SignatureListener;
import com.upyun.library.utils.UpYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
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
 * 申请退款
 * Created by a55 on 2017/6/7.
 */

public class ApplyRefundActivity extends BaseNoFragmentActivity {

    @BindView(R.id.headview)               DynamicHeaderView headerView;
    @BindView(R.id.scrollView)             ScrollView        scrollView;
    @BindView(R.id.rlyt_go_to_select)      RelativeLayout    rlytGoSelectProduct;
    @BindView(R.id.tv_goods_num)           HaiTextView       tvProductNum;
    @BindView(R.id.rycv_selected_products) RecyclerView      rycvProduct;
    @BindView(R.id.llyt_et_desc)           MyLinearLayout    llytEtDesc;
    @BindView(R.id.et_desc)                EditText          etDesc;
    @BindView(R.id.tv_desc_num)            HaiTextView       tvDescNum;
    @BindView(R.id.rycv_imgs)              RecyclerView      rycvImg;
    @BindView(R.id.iv_add_img)             ImageView         ivAddImg;
    @BindView(R.id.tv_add_img_tips)        HaiTextView       tvAddImgTips;
    @BindView(R.id.et_name)                EditText          etName;
    @BindView(R.id.et_phone)               EditText          etPhone;
    @BindView(R.id.tv_commit)              HaiTextView       tvCommit;

    @BindString(R.string.key_order_id)  String KEY_ORDER_ID;
    @BindString(R.string.key_refund_no) String KEY_REFUND_NO;

    private RVBaseAdapter<PhotoInfo>                                                mImgAdapter;
    private RVBaseAdapter<CanRefundProductListResult.StorelistBean.ProductlistBean> mProductAdapter;
    private ArrayList<CanRefundProductListResult.StorelistBean.ProductlistBean>     mProducts;
    private ArrayList<PhotoInfo>                                                    mLocalImgPaths;
    private ArrayList<ImagesBean>    mUpYunImgs;

    private String[]                                                                mBase64Strings;

    private FunctionConfig mFunctionConfig;
    private static final int    REQUEST_SELECT_PRODUCT = 100;
    private static final int    REQUEST_ADD_IMG        = 101;
    private static final String KEY                    = "MJrxn7x0fP5c54QFfb0GlFiJ9pY=";
    private static final String SPACE                  = "st-prod";
    private static final String savePath               = "/post/{year}/{mon}/{day}/{random32}";

    private String                     order_id;
    private CanRefundProductListResult canRefundProductListResult;
    private ArrayList<RefundItemsBean> refundItems;
    private String                     refundItemsJson;
    private String                     voucher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refund);
        ButterKnife.bind(this);

        initVars();
        initViews();

    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initVars() {
        mLocalImgPaths = new ArrayList<>();
        mProducts = new ArrayList<>();
        mUpYunImgs = new ArrayList<>();
        mBase64Strings = new String[4];
        canRefundProductListResult = new CanRefundProductListResult();

        // 联系方式
        UserBean userBean = UserRepository.getInstance().getUserInfo();
        if (userBean != null) {
            etName.setText(userBean.getNickname());
            etPhone.setText(userBean.getMobile());
        }

        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra(KEY_ORDER_ID);
        }

    }

    private void initViews() {
        headerView.setHeaderRightHidden();

        // 受理的商品select_refund_product_item
        rycvProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mProductAdapter = new RVBaseAdapter<CanRefundProductListResult.StorelistBean.ProductlistBean>(this, mProducts, R.layout.select_refund_product_item) {
            @Override
            public void bindView(RVBaseHolder holder, CanRefundProductListResult.StorelistBean.ProductlistBean productBaseBean) {
                UPaiYunLoadManager.loadImage(mActivity, productBaseBean.coverImg_url, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.getView(R.id.iv_product));
            }
        };
        rycvProduct.setAdapter(mProductAdapter);

        // 商品描述
        llytEtDesc.setEditeText(etDesc);
        llytEtDesc.setParentScrollview(scrollView);
        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvDescNum.setText(s.length() + "/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 上传的图片
        rycvImg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImgAdapter = new RVBaseAdapter<PhotoInfo>(this, mLocalImgPaths, R.layout.select_img_item) {
            @Override
            public void bindView(RVBaseHolder holder, PhotoInfo photoInfo) {
                Glide.with(mContext)
                        .load(photoInfo.getPhotoPath())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into((ImageView) holder.getView(R.id.iv_img));
                holder.getView(R.id.iv_delete).setOnClickListener(v -> {
                    mLocalImgPaths.remove(holder.getLayoutPosition());
                    mImgAdapter.changeDatas(mLocalImgPaths);

                    if (HaiUtils.getSize(mLocalImgPaths) > 0) {
                        rycvImg.setVisibility(View.VISIBLE);
                        tvAddImgTips.setVisibility(View.GONE);
                        if (HaiUtils.getSize(mLocalImgPaths) == 4){
                            ivAddImg.setVisibility(View.GONE);
                        }else {
                            ivAddImg.setVisibility(View.VISIBLE);
                        }

                    } else {
                        mImgAdapter.changeDatas(mLocalImgPaths);
                        rycvImg.setVisibility(View.GONE);
                        tvAddImgTips.setVisibility(View.VISIBLE);
                        ivAddImg.setVisibility(View.VISIBLE);

                    }
                });
            }
        };
        rycvImg.setAdapter(mImgAdapter);

    }

    /**
     * 去选择受理商品
     */
    @OnClick(R.id.rlyt_go_to_select)
    public void selectProducts() {
        // 埋点
//        TraceUtils.addAnalysAct(TraceUtils.PageCode_CanRefundProducts, TraceUtils.PageCode_Refund, TraceUtils.PositionCode_SelectProduct, "");

        Intent intent = new Intent(this, SelectRefundProductActivity.class);
        intent.putExtra(KEY_ORDER_ID, order_id);
        intent.putExtra("products", new Gson().toJson(canRefundProductListResult));
        startActivityForResult(intent, REQUEST_SELECT_PRODUCT);
    }

    /**
     * 上传描述图片
     */
    @OnClick(R.id.iv_add_img)
    public void addImg() {
        openGallery();
    }

    /**
     * 提交申请
     */
    @OnClick(R.id.tv_commit)
    public void commit() {

        if (mProducts.size() == 0) {
            Toast.makeText(mActivity, "请选择受理商品", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(etDesc.getText().toString().trim())) {
            Toast.makeText(mActivity, "请填写问题描述", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (mLocalImgPaths.size() == 0) {
//            Toast.makeText(mActivity, "请上传描述图片", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etPhone.getText())) {
            Toast.makeText(mActivity, "请填写您的联系方式", Toast.LENGTH_SHORT).show();
            return;
        }

        // 埋点
//        TraceUtils.addAnalysAct(TraceUtils.PageCode_RefundCommitResult, TraceUtils.PageCode_Refund, TraceUtils.PositionCode_SubmitAfterSale, "");

        requestPrepare();

    }

    /**
     * 压缩操作
     */
    private void compress(ArrayList<PhotoInfo> images) {
        Observable.from(images)
                .map(photoInfo -> TextUtils.isEmpty(photoInfo.getCropPhotoPath()) ? photoInfo.getPhotoPath() : photoInfo.getCropPhotoPath())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<File>>() {
                    @Override
                    public Observable<File> call(String fileName) {
                        File file        = new File(fileName);
                        long fileContent = file.length();
                        if (fileContent > 200 * 1024) {
                            return Luban.get(ApplyRefundActivity.this)
                                    .load(file)
                                    .putGear(Luban.THIRD_GEAR)
                                    .asObservable();
                        } else {
                            return Observable.just(file);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    ArrayList<PhotoInfo> datas = new ArrayList<PhotoInfo>();

                    @Override
                    public void onStart() {
//                        showProgressDialog("正在提交", false);
                    }

                    @Override
                    public void onCompleted() {
//                        dismissProgressDialog();
                        requestUpyunUpload(datas);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(File s) {
                        PhotoInfo photoInfo = new PhotoInfo();
                        photoInfo.setPhotoPath(s.getAbsolutePath());
                        datas.add(photoInfo);
                    }
                });
    }

    /**
     * 又拍云上传图片
     *
     * @param mLocalImgPaths 本地图片
     */
    private void requestUpyunUpload(ArrayList<PhotoInfo> mLocalImgPaths) {
//        showProgressDialog("提交中", true);
        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, SPACE);
        paramsMap.put(Params.SAVE_KEY, savePath);

        final int[] count = {0};

                mUpYunImgs.clear();

        ImagesBean imgUrls[] = new ImagesBean[mLocalImgPaths.size()];
        for (int i = 0, len = mLocalImgPaths.size(); i < len; i++) {
            File file   = new File(mLocalImgPaths.get(i).getPhotoPath());
            int  finalI = i;
            UploadManager.getInstance()
                    .formUpload(file, paramsMap, signatureListener,
                            (isSuccess, result) -> {
                                Logger.t(TAG).d(result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.optInt("code") == 200) { // 成功
                                        ImagesBean imagesBean = new ImagesBean(ServerUrl.UPYUN_BASE + jsonObject.optString("url"),
                                                jsonObject.optInt("image-width") * 1.0f / jsonObject.optInt("image-height"));
                                        imgUrls[finalI] = imagesBean;
                                        //查询
                                        count[0]++;
                                        if (count[0] == imgUrls.length) {
                                            mUpYunImgs.addAll(Arrays.asList(imgUrls));

                                            voucher = "";
                                            for (int j = 0; j < imgUrls.length; j++){
                                                if (j == 0){
                                                    voucher = voucher + imgUrls[j].url;
                                                }else {
                                                    voucher = voucher + "," + imgUrls[j].url;

                                                }
                                            }
                                            requestRefund();
                                        }
                                    } else {
                                        dismissProgressDialog();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dismissProgressDialog();
                                    ToastUtils.showToast("网络请求失败，请检查您的网络设置");
                                }
                            },
                            (bytesWrite, contentLength) -> {
                            });
        }
    }

    SignatureListener signatureListener = raw -> UpYunUtils.md5(raw + KEY);


    /**
     * 提交退款请求
     */
    private void requestRefund() {

        OrderRepository.getInstance()
                .applyForRefund(order_id, etName.getText().toString(), etPhone.getText().toString()
                        , etDesc.getText().toString(), refundItemsJson, voucher)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<RefundCommitResult>() {
                    @Override
                    public void onSuccess(RefundCommitResult refundCommitResult) {
                        EventBus.getDefault().post(new OrderChangeEvent());
                        //  需要返回退款单号
                        Intent intent = new Intent(ApplyRefundActivity.this, RefundCommitResultActivity.class);
                        intent.putExtra(KEY_ORDER_ID, order_id);
                        intent.putExtra(KEY_REFUND_NO, refundCommitResult.refund_no);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private void requestPrepare() {
        //受理商品集合
        refundItems = new ArrayList<>();
        for (CanRefundProductListResult.StorelistBean.ProductlistBean productlistBean : mProducts) {
            RefundItemsBean refundItemsBean = new RefundItemsBean();
            refundItemsBean.count = productlistBean.count;
            refundItemsBean.skuid = productlistBean.selected_sku.skuid;
            refundItemsBean.spuid = productlistBean.selected_sku.spuid;
            refundItems.add(refundItemsBean);
        }

        refundItemsJson = new Gson().toJson(refundItems);

        showProgressDialog("正在提交", true);
        if (mLocalImgPaths.size() == 0){
            // 直接提交
            voucher = "";
            requestRefund();
        }else {
            // 凭证图片压缩上传upy
            compress(mLocalImgPaths);
        }
        // 描述图片base64处理
//        for (int i = 0; i < mLocalImgPaths.size(); i++) {
        //            int position = i;
        //            Observable.just(mLocalImgPaths.get(i).getPhotoPath())
        //                    .map(fileName -> {
        //                        FileUtils.normalsizeFile(fileName, -1);  //旋转图片，默认压缩度
        //                        return fileName;
        //                    })
        //                    .subscribeOn(Schedulers.io())
        //                    .flatMap(new Func1<String, Observable<File>>() {
        //                        @Override
        //                        public Observable<File> call(String fileName) {
        //                            return Luban.get(ApplyRefundActivity.this)
        //                                    .load(new File(fileName))
        //                                    .putGear(Luban.THIRD_GEAR)
        //                                    .asObservable();
        //                        }
        //                    })
        //                    .subscribeOn(Schedulers.io())
        //                    .map(file -> Base64Utils.fileToString(file))
        //                    .observeOn(AndroidSchedulers.mainThread())
        //                    .subscribe(new Subscriber<String>() {
        //                        @Override
        //                        public void onStart() {
        //                            showProgressDialog(StringConstans.TOAST_DEAL_FILE, false);
        //                        }
        //
        //                        @Override
        //                        public void onCompleted() {
        //                            dismissProgressDialog();
        //                        }
        //
        //                        @Override
        //                        public void onError(Throwable e) {
        //                            dismissProgressDialog();
        //                        }
        //
        //                        @Override
        //                        public void onNext(String base64String) {
        //                            mBase64Strings[position] = base64String;
        //                        }
        //                    });
        //        }

    }

    /**
     * 打开相册
     */
    private void openGallery() {
        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setSelected(mLocalImgPaths)
                .setMutiSelectMaxSize(4)
                .setEnablePreview(false)
                .build();
        GalleryFinal.openGalleryMuti(REQUEST_ADD_IMG, mFunctionConfig, mOnHanlderResultCallback);
    }

    /**
     * 图片选择回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
            if (mLocalImgPaths.size() > 0) {
                mLocalImgPaths.clear();
            }
            if (HaiUtils.getSize(resultList) > 0) {
                mLocalImgPaths.addAll(resultList);
                mImgAdapter.changeDatas(mLocalImgPaths);
                rycvImg.setVisibility(View.VISIBLE);
                tvAddImgTips.setVisibility(View.GONE);
                if (HaiUtils.getSize(resultList) == 4){
                    ivAddImg.setVisibility(View.GONE);
                }else {
                    ivAddImg.setVisibility(View.VISIBLE);
                }

            } else {
                mImgAdapter.changeDatas(mLocalImgPaths);
                rycvImg.setVisibility(View.GONE);
                tvAddImgTips.setVisibility(View.VISIBLE);
                ivAddImg.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PRODUCT) {
                if (data != null) {
                    // 选择受理商品后接收处理数据并更新界面
                    String products = data.getStringExtra("products");
                    canRefundProductListResult = new Gson().fromJson(products, CanRefundProductListResult.class);
                    if (mProducts.size() > 0) {
                        mProducts.clear();
                    }
                    for (int i = 0; i < HaiUtils.getSize(canRefundProductListResult.storelist); i++) {
                        for (int j = 0; j < HaiUtils.getSize(canRefundProductListResult.storelist.get(i).productlist); j++) {
                            if (canRefundProductListResult.storelist.get(i).productlist.get(j).isSelected) {
                                mProducts.add(canRefundProductListResult.storelist.get(i).productlist.get(j));
                            }
                        }

                    }
                    mProductAdapter.changeDatas(mProducts);
                    if (HaiUtils.getSize(mProducts) > 0) {
                        rycvProduct.setVisibility(View.VISIBLE);
                        tvProductNum.setText("已选" + mProducts.size() + "件");

                    } else {
                        rycvProduct.setVisibility(View.GONE);
                        tvProductNum.setText("请选择");

                    }
                }
            }
        }
    }
}
