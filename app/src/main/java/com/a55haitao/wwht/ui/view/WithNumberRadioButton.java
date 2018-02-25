package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by 董猛 on 16/9/13.
 */
public class WithNumberRadioButton extends AppCompatRadioButton {

    private Paint mTxtPaint;                   //用于绘制文本的画笔
    private Paint mCirclePaint;                //用于绘制圆的画笔

    private int mTag;                          //显示的数字

    private final static int sRadius = 7;      //默认圆半径
    private final static int sTextSize = 10;   //默认数字文本尺寸
    private final static int sXOffset = 8;     //圆心相对于 中心 在 X 轴的偏移量
    private final static int sYOffset = 2;     //圆心相对于 顶部 在 Y 轴的偏移量

    public WithNumberRadioButton(Context context) {
        super(context);
        initView();
    }

    public WithNumberRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mTxtPaint = new Paint();
        int textSize =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sTextSize, getResources().getDisplayMetrics());
        mTxtPaint.setTextSize(textSize);
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);    //抗锯齿
        mCirclePaint.setColor(Color.parseColor("#F23524"));
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setNumber(int number) {

        mTag = number > 99 ? 99 : number;

        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTag <= 0) {
            return;
        }

        Rect rect = new Rect();
        String s = String.valueOf(mTag);
        mTxtPaint.getTextBounds(s, 0, s.length(), rect);

//        int recWid = rect.width();
//        int recHeight = rect.height();
//        float radius = mTag >= 10 ?
//                (float) Math.sqrt(recWid*recWid + recHeight*recHeight)*3/4
//                :
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,sRadius,getResources().getDisplayMetrics())
//                ;
        float radius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                sRadius,
                getResources().getDisplayMetrics());

        int width = getWidth();
        float o_x = width / 2 + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sXOffset, getResources().getDisplayMetrics());
        float o_y = radius + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sYOffset, getResources().getDisplayMetrics());

        if (mCirclePaint != null) {
            canvas.drawCircle(
                    o_x,
                    o_y,
                    radius,
                    mCirclePaint);
        }

        if (mTxtPaint != null) {

            canvas.drawText(
                    s,
                    o_x - rect.width() / 2 - 2,
                    o_y + rect.height() / 2,
                    mTxtPaint);
        }

    }
}
