package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView分隔线
 *
 * @author 陶声
 * @since 2016-09-22
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS           = new int[]{
            android.R.attr.listDivider
    };
    public static final  int   HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final  int   VERTICAL_LIST   = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;
    private int      mWidth;
    private int      mHeight;
    private int      mDividerPadding;
    private int      mOrientation;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * 新增：支持自定义dividerDrawable
     *
     * @param context
     * @param orientation
     * @param dividerDrawable
     */
    public DividerItemDecoration(Context context, int orientation, Drawable dividerDrawable) {
        mDivider = dividerDrawable;
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    /**
     * 设置Divider的padding
     *
     * @param dividerPadding Divider的padding
     */
    public void setDividerPadding(int dividerPadding) {
        mDividerPadding = dividerPadding;
    }


    /**
     * 新增：支持手动为无高宽的drawable制定宽度
     *
     * @param width
     */
    public void setWidth(int width) {
        this.mWidth = width;
    }

    /**
     * 新增：支持手动为无高宽的drawable制定高度
     *
     * @param height
     */
    public void setHeight(int height) {
        this.mHeight = height;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left       = parent.getPaddingLeft() + mDividerPadding;
        final int right      = parent.getWidth() - parent.getPaddingRight() - mDividerPadding;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + getDividerHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top        = parent.getPaddingTop() + mDividerPadding;
        final int bottom     = parent.getHeight() - parent.getPaddingBottom() - mDividerPadding;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left + getDividerWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, getDividerHeight());
        } else {
            outRect.set(0, 0, getDividerWidth(), 0);
        }
    }

    private int getDividerWidth() {
        return mWidth > 0 ? mWidth : mDivider.getIntrinsicWidth();
    }

    private int getDividerHeight() {
        return mHeight > 0 ? mHeight : mDivider.getIntrinsicHeight();
    }
}