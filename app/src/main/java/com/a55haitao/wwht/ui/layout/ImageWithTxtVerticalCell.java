package com.a55haitao.wwht.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.CategoryBean;
import com.a55haitao.wwht.ui.activity.discover.SearchResultActivity;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drolmen on 2016/12/28.
 */

public class ImageWithTxtVerticalCell extends LinearLayout implements View.OnClickListener {


    @BindView(R.id.bigCoverImg) ImageView mCoverImg;

    @BindView(R.id.categoryNameTxt) TextView mNameTxt;

    private CategoryBean mBean;

    public ImageWithTxtVerticalCell(Context context) {
        super(context);
    }

    public ImageWithTxtVerticalCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        super.setOnClickListener(this);
    }

    public void setData(CategoryBean bean) {
        mBean = bean;
        mNameTxt.setText(mBean.name);
        UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(), bean.image, UpaiPictureLevel.TRIBBLE, R.mipmap.ic_default_square_small, mCoverImg);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        throw new RuntimeException("不允许在外部设置点击事件");
    }

    @Override
    public void onClick(View v) {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_Product, mBean.name, getContext(), TraceUtils.PageCode_Category, TraceUtils.PositionCode_HotCategory + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_Product, TraceUtils.PageCode_Category, TraceUtils.PositionCode_HotCategory, mBean.name);

        SearchResultActivity.toThisActivity(ActivityCollector.getTopActivity(), mBean.name, mBean.query, PageType.CATEGORY);
    }
}
