package com.a55haitao.wwht.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by a55 on 2017/3/15.
 * 优惠抢购画廊效果
 */

public class PagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    boolean mNeedsRedraw = false;

    public PagerContainer(Context context) {
        super(context);
        init();
    }

    public PagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setClipChildren(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        try {
            mPager = (ViewPager) getChildAt(0);
            mPager.addOnPageChangeListener(this);
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }

    public ViewPager getViewPager() {
        return mPager;
    }

    private Point mCenter       = new Point();
    private Point mInitialTouch = new Point();


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenter.x = w / 2;
        mCenter.y = h / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialTouch.x = (int) ev.getX();
                mInitialTouch.y = (int) ev.getY();
            default:
                float deltaX = mCenter.x - mInitialTouch.x;
                float deltaY = mCenter.y - mInitialTouch.y;
                ev.offsetLocation(deltaX, deltaY);
                break;
        }


        return mPager.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        final int scrollX = mPager.getScrollX();
        final int childCount = mPager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = mPager.getChildAt(i);
            final ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
            if (lp.isDecor) continue;
            final float transformPos = (float) (child.getLeft() - scrollX) / child.getWidth();
            transformPage(child, transformPos);
        }

        if (mNeedsRedraw) invalidate();
        //        if (mNeedsRedraw)
        //            invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //        mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
        //        invalidate();
    }

    private static float MIN_SCALE = 0.9f;
    private static float MIN_ALPHA = 0.8f;
    private static float MARGIN = 0;

    public void transformPage(View view, float position) {

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(MIN_ALPHA);
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            ViewGroup page = (ViewGroup) view;
            ViewGroup card = (ViewGroup) page.getChildAt(0);
            card.getChildAt(0).setAlpha(0.1f);


        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            ViewGroup page = (ViewGroup) view;
            ViewGroup card = (ViewGroup) page.getChildAt(0);
            if (hasAlpha) {
                float realAphla = getFloat(1 - Math.abs(position * 1.0f), 0.0f, 1.0f);
                if (realAphla < 0.1f) {
                    card.getChildAt(0).setAlpha(0.1f);
                } else
                    card.getChildAt(0).setAlpha(realAphla);
            } else {
                card.getChildAt(0).setAlpha(MIN_ALPHA);
            }
            view.setTranslationX(position * MARGIN);

            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            view.clearAnimation();


        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(MIN_ALPHA);
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            ViewGroup page = (ViewGroup) view;
            ViewGroup card = (ViewGroup) page.getChildAt(0);
            card.getChildAt(0).setAlpha(0.1f);
        }

    }

    public static float getFloat(float value, float minValue, float maxValue) {
        return Math.min(maxValue, Math.max(minValue, value));
    }

    public void setMinScale(float f) {
        MIN_SCALE = f;
    }

    public void setMargin(float f) {
        MARGIN = f;
    }

    public void setMinAlpha(float f) {
        MIN_ALPHA = f;
    }

    private boolean hasAlpha = true;

    public void setHasAlpha(boolean hasAlpha) {
        this.hasAlpha = hasAlpha;
    }
}

