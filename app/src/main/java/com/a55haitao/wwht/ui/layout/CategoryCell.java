package com.a55haitao.wwht.ui.layout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.CategoryBean;
import com.a55haitao.wwht.ui.activity.discover.AllCategoryActivity;
import com.a55haitao.wwht.ui.view.LoadMoreFooterView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drolmen on 2016/12/28.
 */

public class CategoryCell extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.titleTxtView) TextView mTitleTxt;

    @BindView(R.id.categorySAImgView) ImageView mSeeAllImgView;

    private ImageWithTxtVerticalCell[] mCells;

    private ArrayList<CategoryBean> mDatas;

    public CategoryCell(Context context) {
        super(context);
    }

    public CategoryCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
        mSeeAllImgView.setOnClickListener(this);

        int[] ids = new int[]{R.id.layout_0,
                R.id.layout_1,
                R.id.layout_2,
                R.id.layout_3,
                R.id.layout_4,
                R.id.layout_5,
                R.id.layout_6,
                R.id.layout_7};

        int count = ids.length;
        mCells = new ImageWithTxtVerticalCell[count];
        //初始化View 的引用
        for (int i = 0; i < count; i++) {
            mCells[i] = ButterKnife.findById(this, ids[i]);
        }

    }

    public void setData(ArrayList<CategoryBean> list) {

        if (HaiUtils.getSize(list) > 8) {
            mDatas = new ArrayList<>(list.subList(0, 9));
        } else {
            if (HaiUtils.getSize(list) > 0) {
                mDatas = new ArrayList<>(list);
            } else {
                mDatas = new ArrayList<>();
            }
        }

        updateLayout();
    }

    private void updateLayout() {

        mTitleTxt.setText("热 门 分 类");

        int length = mCells.length;
        int count  = mDatas.size();
        for (int i = 0; i < length; i++) {
            if (i < count) {
                mCells[i].setVisibility(VISIBLE);
                mCells[i].setData(mDatas.get(i));
            } else {
                mCells[i].setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Context context = getContext();
        // 埋点
        //        TraceUtils.addClick(TraceUtils.PageCode_AllCategory, "", context, TraceUtils.PageCode_Category, TraceUtils.PositionCode_AllCategory + "", "");
        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_AllCategory, TraceUtils.PageCode_Category, TraceUtils.PositionCode_AllCategory, "");

        context.startActivity(new Intent(context, AllCategoryActivity.class));
    }
}
