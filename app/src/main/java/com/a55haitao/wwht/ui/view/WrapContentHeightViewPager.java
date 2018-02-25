package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Haotao_Fujie on 16/10/25.
 */

public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int childSize = getChildCount();
//        int maxHeight = 0;
//        for (int i = 0; i < childSize; i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            if (child.getMeasuredHeight() > maxHeight) {
//                maxHeight = child.getMeasuredHeight();
//            }
//        }
//
//        if (maxHeight > 0) {
//            setMeasuredDimension(getMeasuredWidth(), maxHeight);
//        }



        int viewHeight = 0;
        View childView = getChildAt(getCurrentItem());
        if(childView != null) {
            childView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            viewHeight = childView.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
