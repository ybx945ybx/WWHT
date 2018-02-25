package com.a55haitao.wwht.ui.layout;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.CategoryPageBean;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.ui.activity.discover.AllBrandOrSellerActivity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by drolmen on 2016/12/28.
 */

public class ImageSiteCell extends LinearLayout implements View.OnClickListener {

    TextView mTitleTxt;

    private ImageView[] mImageView;

    private ImageView mSeeAllImgView;
    private ArrayList<CategoryPageBean.HotSiteBean> mDatas;
    private int mType;

    public ImageSiteCell(Context context) {
        super(context);
    }

    public ImageSiteCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleTxt = ButterKnife.findById(this, R.id.titleTxtView);

        //初始化View 的引用
        mImageView = new ImageView[8];
        GridLayout layout = ButterKnife.findById(this, R.id.girdLayout);
        int count = layout.getChildCount();
        View temp = null;
        for (int i = 0, k = 0; i < count; i++) {
            temp = layout.getChildAt(i);
            if (temp instanceof ImageView) {
                temp.setOnClickListener(this);          //设置监听事件
                temp.setTag(R.id.tag_for_img, k);
                if (k < 8) {
                    mImageView[k] = (ImageView) temp;
                    k++;
                } else {
                    mSeeAllImgView = (ImageView) temp;
                }
            }
        }

    }

    public void setData(ArrayList<CategoryPageBean.HotSiteBean> list, @PageType int type) {

        if (HaiUtils.getSize(list) > 8) {
            mDatas = new ArrayList<>(list.subList(0, 9));
        } else {
            mDatas = new ArrayList<>(list);
        }

        mType = type;

        updateLayout();
    }

    private void updateLayout() {

        if (mType == PageType.BRAND) {
            mTitleTxt.setText("人 气 品 牌");
            mSeeAllImgView.setBackgroundResource(R.mipmap.ic_brands_s_a);
        } else {
            mTitleTxt.setText("精 选 商 家");
            mSeeAllImgView.setBackgroundResource(R.mipmap.ic_seller_s_a);
        }

        for (int i = 0; i < mImageView.length; i++) {
            if (i < mDatas.size()) {
                mImageView[i].setVisibility(VISIBLE);
                UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(), mDatas.get(i).logo2,
                        UpaiPictureLevel.TRIBBLE, R.id.u_pai_yun_null_holder_tag, mImageView[i]);
            } else {
                mImageView[i].setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (mDatas == null) {
            return;
        }

        int position = (int) v.getTag(R.id.tag_for_img);

        if (position < 8) {      //跳往商家、品牌主页
            CategoryPageBean.HotSiteBean bean = mDatas.get(position);

            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_BrandOrSiteDetail, bean.name, getContext(), TraceUtils.PageCode_Category, mType == PageType.BRAND ? TraceUtils.PositionCode_Brand + "" : TraceUtils.PositionCode_Store + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_BrandOrSiteDetail, TraceUtils.PageCode_Category, mType == PageType.BRAND ? TraceUtils.PositionCode_Brand : TraceUtils.PositionCode_Store, bean.name);

            SiteActivity.toThisActivity(ActivityCollector.getTopActivity(),
                    bean.name, mType, bean.logo1, bean.logo3, bean.img_cover);
        } else {                 //跳往全部商家、品牌列表

            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_AllBrandOrSite, "", getContext(), TraceUtils.PageCode_Category, mType == PageType.BRAND ? TraceUtils.PositionCode_AllBrand + "" : TraceUtils.PositionCode_AllStore + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_AllBrandOrSite, TraceUtils.PageCode_Category, mType == PageType.BRAND ? TraceUtils.PositionCode_AllBrand : TraceUtils.PositionCode_AllStore, "");

            AllBrandOrSellerActivity.toThisActivity(ActivityCollector.getTopActivity(), mType);
        }
    }
}
