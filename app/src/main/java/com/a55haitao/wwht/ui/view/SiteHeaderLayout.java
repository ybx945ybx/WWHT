package com.a55haitao.wwht.ui.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CheckableImageButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.SellerBean;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by drolmen on 2016/12/7.
 */

/**
 * 该类与{@link com.a55haitao.wwht.R.layout#site_header_layout}结合使用
 */
public class SiteHeaderLayout extends RelativeLayout {

    @BindView(R.id.nameTxt)       HaiTextView          mNameTxt;
    @BindView(R.id.bigCoverImg)   ImageView            mBigCoverImg;
    @BindView(R.id.smallCoverImg) ImageView            mSmallCoverImg;
    @BindView(R.id.smallCoverTex) HaiTextView          mSmallCoverTex;
    @BindView(R.id.logoImg)       ImageView            mLogoImg;
    @BindView(R.id.descripeTxt)   HaiTextView          mDescripeTxt;
    @BindView(R.id.countryTxt)    HaiTextView          mCountryTxt;
    @BindView(R.id.arrowImgBtn)   CheckableImageButton mArrowImgBtn;

    public SiteHeaderLayout(Context context) {
        super(context);
    }

    public SiteHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Note:此方法只可以调用一次
     */
    public void init(Activity activity, SellerBean.SellerDescBaseBean bean, int type) {

        if (TextUtils.isEmpty(bean.img_cover)) {
            int[] mipmaps = new int[]{
                    R.mipmap.img_home_cover_1,
                    R.mipmap.img_home_cover_2,
                    R.mipmap.img_home_cover_3,
                    R.mipmap.img_home_cover_4,
                    R.mipmap.img_home_cover_5
            };
            int radom = (int) (Math.random() * 4);
            Glide.with(activity).load(mipmaps[radom]).diskCacheStrategy(UPaiYunLoadManager.sDiskCacheStrategy).into(mBigCoverImg);
        } else {
            UPaiYunLoadManager.loadImage(activity, bean.img_cover, UpaiPictureLevel.SINGLE, R.id.u_pai_yun_null_holder_tag, mBigCoverImg);
        }
        if (TextUtils.isEmpty(bean.logo3)) {

        } else {
            UPaiYunLoadManager.loadImage(activity, bean.logo3, UpaiPictureLevel.SINGLE, R.id.u_pai_yun_null_holder_tag, mLogoImg);
        }
        if (TextUtils.isEmpty(bean.logo1)) {
            mSmallCoverTex.setVisibility(View.VISIBLE);
            mSmallCoverImg.setVisibility(View.GONE);

            String logoName = "";
            if (!TextUtils.isEmpty(bean.nameen)) {
                logoName = bean.nameen;
            } else if (!TextUtils.isEmpty(bean.namecn)) {
                logoName = bean.namecn;
            } else {
                logoName = bean.name;
            }

            if(!TextUtils.isEmpty(logoName)) {
                HaiUtils.setBrandOrSellerImg(mSmallCoverTex, logoName, 1);
            }

        } else {
            UPaiYunLoadManager.loadImage(activity, bean.logo1, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, mSmallCoverImg);
        }

        if (type == PageType.BRAND) {
            String name = null;
            if (!TextUtils.isEmpty(bean.nameen) && !TextUtils.isEmpty(bean.namecn)) {
                if (bean.nameen.equals(bean.namecn)) {
                    name = bean.nameen;
                } else {
                    name = bean.nameen + "/" + bean.namecn;
                }
            } else if (!TextUtils.isEmpty(bean.nameen)) {
                name = bean.nameen;
            } else if (!TextUtils.isEmpty(bean.namecn)) {
                name = bean.namecn;
            }
            mNameTxt.setText(name);
        } else if (type == PageType.SELLER) {
            mNameTxt.setText(bean.nameen);
        }

        mCountryTxt.setText(bean.country);

        if (TextUtils.isEmpty(bean.desc)) {
            mDescripeTxt.setHeight(0);
            mArrowImgBtn.setVisibility(GONE);
        } else {
            mDescripeTxt.setText(bean.desc);
        }
    }

    /**
     * 获取{@link #mBigCoverImg}中显示的部分的从底部开始、高度为height的Bitmap
     */
    public Bitmap getBitMap(int height) {

        Bitmap result = null;

        Drawable drawable = mBigCoverImg.getDrawable();

        if (drawable instanceof GlideBitmapDrawable) {
            GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable) drawable;
            if (bitmapDrawable != null) {
                Rect bounds = bitmapDrawable.getBounds();
                result = Bitmap.createBitmap(bitmapDrawable.getBitmap(), 0, bounds.height() - height, bounds.width(), height);
            }
        }

        return result;
    }

    public int getImageHeight() {
        return (int) DeviceUtils.dipToPx(200);
    }

    @OnClick(R.id.arrowImgBtn)
    public void onArrowClick(View view) {

        boolean newStatus = !mArrowImgBtn.isChecked();
        mArrowImgBtn.setChecked(newStatus);

        int beginRotationValue = 0;
        int afterRoationValue  = 0;
        if (newStatus) {     //true 箭头向上
            beginRotationValue = 0;
            afterRoationValue = 180;
            mDescripeTxt.setMaxLines(100);
        } else {             //false 箭头向下
            beginRotationValue = 180;
            afterRoationValue = 360;
            mDescripeTxt.setMaxLines(2);
        }

        ObjectAnimator rotation = ObjectAnimator.ofFloat(mArrowImgBtn, "rotation", beginRotationValue, afterRoationValue);
        rotation.setDuration(300);  //设置旋转时间
        rotation.start();
    }

}
