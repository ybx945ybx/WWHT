package com.a55haitao.wwht.ui.fragment.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.TranslateBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductTxtImageFragment extends BaseFragment {
    @BindView(R.id.productDescTxt)  TextView     productDescTxt;
    @BindView(R.id.translationBtn)  TextView     translationBtn;
    @BindView(R.id.productImageLin) LinearLayout productImageLin;

    private String originalProductDesc;
    private String translatedProductDesc;
    private int    pageId;

    private ArrayList<String> coverImgList;


    public static ProductTxtImageFragment newInstance(String originalProductDesc, int pageId, ArrayList<String> coverImgList) {
        ProductTxtImageFragment fragment = new ProductTxtImageFragment();
        Bundle                  bundle   = new Bundle();
        bundle.putInt("pageId", pageId);
        bundle.putString("originalProductDesc", originalProductDesc);
        bundle.putStringArrayList("coverImgList", coverImgList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageId = getArguments().getInt("pageId");
        originalProductDesc = getArguments().getString("originalProductDesc");
        coverImgList = getArguments().getStringArrayList("coverImgList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_txt_image, container, false);
        ButterKnife.bind(this, view);
        renderUI();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void renderUI() {
        // 文
        if (TextUtils.isEmpty(originalProductDesc) || TextUtils.getTrimmedLength(originalProductDesc) == 0) {
            productDescTxt.setVisibility(View.GONE);
            translationBtn.setVisibility(View.GONE);
        } else {
            translationBtn.setTag(false);
            productDescTxt.setText(originalProductDesc);
            translationBtn.setOnClickListener(v -> {

                boolean translated = (boolean) translationBtn.getTag();
                if (translated) {
                    productDescTxt.setText(originalProductDesc);
                    translationBtn.setTag(false);
                    translationBtn.setText("一键翻译");
                    return;
                } else {
                    if (!TextUtils.isEmpty(translatedProductDesc)) {
                        productDescTxt.setText(translatedProductDesc);
                        translationBtn.setTag(true);
                        translationBtn.setText("查看原文");
                        return;
                    }
                }

                showProgressDialog("正在翻译...");
                ProductRepository.getInstance()
                        .translateTxt(originalProductDesc, pageId)
                        .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                        .subscribe(new DefaultSubscriber<TranslateBean>() {
                            @Override
                            public void onSuccess(TranslateBean translateBean) {
                                translatedProductDesc = translateBean.getTranslatedTxt();
                                productDescTxt.setText(translatedProductDesc);
                                translationBtn.setTag(true);
                                translationBtn.setText("查看原文");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
            });
        }

        // 图
        for (String url : coverImgList) {
            ImageView                 imageView = new ImageView(mActivity);
            LinearLayout.LayoutParams params    = new LinearLayout.LayoutParams(DeviceUtils.getScreenWidth(), DeviceUtils.getScreenWidth());
            params.topMargin = DisplayUtils.dp2px(mActivity, 10);
            params.bottomMargin = DisplayUtils.dp2px(mActivity, 10);

            imageView.setLayoutParams(params);
            productImageLin.addView(imageView);
            UPaiYunLoadManager.loadImage(mActivity, url, UpaiPictureLevel.SINGLE, R.mipmap.ic_default_square_large, imageView);
        }
    }
}
