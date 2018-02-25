package com.a55haitao.wwht.ui.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.TabBannerBean;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.view.HaiImageView;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by a55 on 2017/8/1.
 */

public class PhotoPreviewActivity extends BaseActivity {

    private ViewPager viewPager;

    private List<TabBannerBean> mBanners;
    private int                 defaultPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        initVars();
        initViews();
        initEvent();
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            String banners = intent.getStringExtra("banners");
            mBanners = new Gson().fromJson(banners, new TypeToken<List<TabBannerBean>>() {
            }.getType());
            defaultPos = intent.getIntExtra("position", 0);
        }
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        PhotoAdapter madapter = new PhotoAdapter(this, mBanners);
        viewPager.setAdapter(madapter);
        viewPager.setCurrentItem(defaultPos);
    }

    private void initEvent() {

    }

    class PhotoAdapter extends PagerAdapter {
        private List<TabBannerBean> mData;
        private Context             mContext;

        public PhotoAdapter(Context mContext, List<TabBannerBean> mData) {
            this.mContext = mContext;
            this.mData = mData;
        }

        public void setData(List<TabBannerBean> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            final HaiImageView imageView = new HaiImageView(viewGroup.getContext());

            UPaiYunLoadManager.loadImage(mActivity, mData.get(position).image, UpaiPictureLevel.SINGLE, R.mipmap.ic_default_square_large, imageView, false);

            try {
                viewGroup.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            return imageView;
        }

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
    }
}
