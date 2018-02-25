package com.a55haitao.wwht.ui.activity.easyopt;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadManager;
import com.upyun.library.listener.SignatureListener;
import com.upyun.library.utils.UpYunUtils;

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

public abstract class EasyoptBaseActivity extends BaseNoFragmentActivity {

    protected static final int    REQUEST_CODE_GALLERY = 1001;
    protected static final String KEY                  = "MJrxn7x0fP5c54QFfb0GlFiJ9pY=";
    protected static final String SPACE                = "st-prod";
    protected static final String savePath             = "/post/{year}/{mon}/{day}/{random32}";

    @BindView(R.id.img_easyopt_logo)   ImageView      mImgEasyoptLogo;    // 草单封面图
    @BindView(R.id.rl_img_cover)       RelativeLayout mRlImgCover;        // 更换草单封面
    @BindView(R.id.et_easypost_name)   EditText       mEtEasypostName;    // 草单名称
    @BindView(R.id.et_easypost_desc)   EditText       mEtEasypostDesc;    // 草单描述
    @BindView(R.id.sb_easyopt_visible) SwitchButton   mSbEasyoptVisible;  // 草单是否公开

    protected String mLocalEasyoptLogoUrl;  // 草单Logo本地地址
    protected String mEasyoptLogoUrl;       // 草单Logo
    protected String mSpuId;                // 商品spuId
    protected Dialog mDlg;
    private FunctionConfig mFunctionConfig;

    /**
     * 子类布局
     *
     * @return layout
     */
    protected abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);

        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(false)
                .build();

    }

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initVariables();

    /**
     * 返回
     */
    @OnClick(R.id.ib_back)
    public void clickBack() {
        finish();
    }

    /**
     * 输入验证
     *
     * @param easyoptName 草单名称
     * @param easyoptDesc 草单描述
     * @return 是否通过输入验证
     */
    protected boolean checkInput(String easyoptName, String easyoptDesc) {
        if (TextUtils.isEmpty(easyoptName)) {
            ToastUtils.showToast(mActivity, getString(R.string.toast_activity_easyoptcreate_easyopt_name_not_empty));
        } else if (TextUtils.isEmpty(easyoptDesc)) {
            ToastUtils.showToast(mActivity, getString(R.string.toast_activity_easyoptcreate_easyopt_desc_not_empty));
        } else {
            return true;
        }
        return false;
    }

    /**
     * 更换草单封面
     */
    @OnClick(R.id.rl_img_cover)
    public void clickRlImgCover() {
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mFunctionConfig, mOnHanlderResultCallback);
    }

    /**
     * 图片选择回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
            if (resultList != null) {
                mLocalEasyoptLogoUrl = TextUtils.isEmpty(resultList.get(0).getCropPhotoPath()) ? resultList.get(0).getPhotoPath() : resultList.get(0).getCropPhotoPath();
                Glide.with(mActivity)
                        .load(mLocalEasyoptLogoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(mImgEasyoptLogo);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };

    /**
     * 又拍云上传图片
     *
     * @param easyoptName 草单名称
     * @param easyoptDesc 草单描述
     */
    protected void requestUpyunUpload(String easyoptName, String easyoptDesc) {

        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, SPACE);
        paramsMap.put(Params.SAVE_KEY, savePath);

        UploadManager.getInstance()
                .formUpload(new File(mLocalEasyoptLogoUrl),
                        paramsMap,
                        signatureListener,
                        (isSuccess, result) -> {
                            Logger.d(result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.optInt("code") == 200) {
                                    // 又拍云图片url
                                    String upyunImgUrl = ServerUrl.UPYUN_BASE + jsonObject.optString("url");
                                    Logger.d(upyunImgUrl);
                                    // 创建草单请求
                                    requestCreateEasyopt(easyoptName, easyoptDesc, upyunImgUrl);
                                } else {
                                    dismissProgressDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        (bytesWrite, contentLength) -> {
                        });
    }

    /**
     * 创建草单请求
     *
     * @param easyoptName 草单名
     * @param easyoptDesc 草单内容
     * @param upyunImgUrl 草单图
     */
    protected abstract void requestCreateEasyopt(String easyoptName, String easyoptDesc, String upyunImgUrl);

    // 又拍云签名
    SignatureListener signatureListener = raw -> UpYunUtils.md5(raw + KEY);

    /**
     * 弹出草单是否公开提示对话框
     */
    @OnClick(R.id.ib_easyopt_visible)
    public void showEasyoptVisibleDilaog() {
        if (mDlg == null) {
            mDlg = new Dialog(mActivity);
            mDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View dlgView = LayoutInflater.from(mActivity).inflate(R.layout.dlg_easyopt_public_visible, null);
            mDlg.setContentView(dlgView);
        }
        mDlg.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDlg != null) {
            mDlg.dismiss();
        }
    }
}
