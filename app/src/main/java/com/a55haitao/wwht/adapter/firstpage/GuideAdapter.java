package com.a55haitao.wwht.adapter.firstpage;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 引导页ViewPager Adapter
 *
 * @author 陶声
 * @date 2016-08-08
 */
public class GuideAdapter extends PagerAdapter {

    private List<ImageView> mImgList;

    public GuideAdapter(List<ImageView> imgList) {
        mImgList = imgList;
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView img = mImgList.get(position);
        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}