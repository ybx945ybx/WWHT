package com.a55haitao.wwht.adapter.firstpage;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.TabBannerBean;
import com.a55haitao.wwht.ui.activity.product.PhotoPreviewActivity;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 商详页Banner
 * Created by 董猛 on 2016/10/12.
 */

public class BannerAdapter extends PagerAdapter {

    private List<TabBannerBean> mBanners;

    private HashMap<Integer, ImageView> mCacheViews;

    private Activity mActivity;

    private ImageView.ScaleType scaleType;

    private View.OnClickListener mOnClickListener;

    private View      hintView;
    public  TextView  slideText;
    public  ImageView arrowImage;

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public void setImgListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public BannerAdapter(Activity activity, List<TabBannerBean> tabDatas) {
        mActivity = activity;
        mBanners = tabDatas;
        mCacheViews = new HashMap<>();
        hintView = LayoutInflater.from(activity).inflate(R.layout.more_view, null);
        slideText = (TextView) hintView.findViewById(R.id.tv);
        arrowImage = (ImageView) hintView.findViewById(R.id.iv);

    }

    public void setmBanners(ArrayList<TabBannerBean> mBanners) {
        this.mBanners = mBanners;
        notifyDataSetChanged();
    }

    public int getDataCount() {
        return HaiUtils.getSize(mBanners);
    }

    @Override
    public int getCount() {
        return mBanners == null ? 0 : mBanners.size() + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position < HaiUtils.getSize(mBanners)) {
            ImageView     imageView     = mCacheViews.get(position);
            TabBannerBean tabBannerBean = mBanners.get(position);
            if (imageView == null) {

                imageView = new ImageView(mActivity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceUtils.getScreenWidth(), DeviceUtils.getScreenWidth());
                imageView.setLayoutParams(params);

                if (mOnClickListener != null) {
                    imageView.setOnClickListener(mOnClickListener);
                }
                if (scaleType != null) {
                    imageView.setScaleType(scaleType);
                }
                imageView.setTag(R.id.tag_for_img, tabBannerBean.uri);
                imageView.setOnClickListener(v -> {
                    Intent intent  = new Intent(mActivity, PhotoPreviewActivity.class);
                    String banners = new Gson().toJson(mBanners);
                    intent.putExtra("banners", banners);
                    intent.putExtra("position", position);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.spec_window_fade_in, R.anim.exit_fixed);

                });
                mCacheViews.put(position, imageView);
            }
            UPaiYunLoadManager.loadImage(mActivity, tabBannerBean.image, UpaiPictureLevel.SINGLE, R.mipmap.ic_default_square_large, imageView, false);
            container.addView(imageView);
            return imageView;

        } else {
            container.addView(hintView);
            return hintView;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
