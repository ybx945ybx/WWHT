package com.a55haitao.wwht.ui.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.model.entity.EasyOptBean;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.easyopt.EasyoptEditActivity;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by drolmen on 2017/1/9.
 */

public class SimpleIntroducePopupWindow extends PopupWindow {

    @BindView(R.id.coverImg)      HaiImageView mCoverImg;
    @BindView(R.id.bgImg)         ImageView    mBgImg;
    @BindView(R.id.nameTxt)       HaiTextView  mNameTxt;
    @BindView(R.id.descTxt)       HaiTextView  mDescTxt;
    @BindView(R.id.editTxt)       HaiTextView  mEditTxt;
    @BindView(R.id.contentLayout) LinearLayout mLinearLayout;

    private EasyOptBean mEasyOptBean;

    public SimpleIntroducePopupWindow(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.easy_opt_detail_for_popu_window, null), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        ButterKnife.bind(this, getContentView());
    }

    public void setData(EasyOptBean bean) {
        mEasyOptBean = bean;
        UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(), bean.img_cover, UpaiPictureLevel.TWICE, R.mipmap.ic_default_square_small, mCoverImg, 8);
        mNameTxt.setText(bean.name);
        mDescTxt.setText(TextUtils.isEmpty(bean.content) ? "该草单还木有编写描述哦~" : bean.content);

        if (HaiUtils.isLogin() && bean.easyopt_id == UserRepository.getInstance().getUserInfo().getId()) {
            mEditTxt.setVisibility(View.VISIBLE);
        } else {
            mEditTxt.setVisibility(View.INVISIBLE);
        }
    }

    public void setBlurBackground(Bitmap bitmap) {
        //        mLinearLayout.setBackground(new BitmapDrawable(bitmap));
        mBgImg.setImageBitmap(bitmap);
    }

    @OnClick(R.id.closeImg)
    public void onCloseClick() {
        dismiss();
    }

    @OnClick(R.id.editTxt)
    public void onEditClick() {
        if (mEasyOptBean == null)
            return;
        EasyoptEditActivity.toThisActivity(ActivityCollector.getTopActivity(), mEasyOptBean.easyopt_id, mEasyOptBean.img_cover, mEasyOptBean.name, mEasyOptBean.content, mEasyOptBean.is_visible);
    }
}
