package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.utils.DeviceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by drolmen on 2016/12/7.
 */

public class SiteNagivationLayout extends RelativeLayout {

    @BindView(R.id.bgImg)    ImageView   mBgImg;
    @BindView(R.id.backImg)  ImageView   mBackImg;
    @BindView(R.id.shareImg) ImageView   mShareImg;
    @BindView(R.id.titleTxt) HaiTextView mTitleTxt;
    @BindView(R.id.starTxt)  HaiTextView mStarTxt;
    @BindView(R.id.rightTxt) HaiTextView mRightTxt;

    private boolean hasSetBackground = false;

    private NagivationHandlerIml     mHandler;
    private NagivationRightHandleIml mRightHandler;

    public SiteNagivationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMainHandler(NagivationHandlerIml handler) {
        mHandler = handler;
    }

    public void setRightHandler(NagivationRightHandleIml rightHandler) {
        mRightHandler = rightHandler;
    }

    public void setStarStatus(boolean isStar, String str) {
        if (isStar) {
            mStarTxt.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTransWhite));
        } else {
            mStarTxt.setBackgroundResource(R.drawable.shape_radius_px_4_white);
        }
        mStarTxt.setText(str);
        mStarTxt.setVisibility(VISIBLE);
    }

    public void setTitleTxt(String str) {
        mTitleTxt.setText(str);
    }

    public void showBackground(Bitmap b) {
        hasSetBackground = true;
        if (b != null) {
            mBgImg.setImageBitmap(b);
        }
        mTitleTxt.setVisibility(VISIBLE);
        mBgImg.setAlpha(1f);
    }

    public void hideBackground() {
        mBgImg.setAlpha(0f);
        mTitleTxt.setVisibility(INVISIBLE);
    }

    public boolean hasBackground() {
        return hasSetBackground;
    }

    public int getNagivationHeight() {
        return (int) DeviceUtils.dipToPx(46);
    }

    public void statusSwitchForEO(boolean editActive) {
        if (editActive) {
            mRightTxt.setVisibility(VISIBLE);
            mStarTxt.setVisibility(GONE);
            mShareImg.setVisibility(GONE);
        } else {
            mRightTxt.setVisibility(GONE);
            mStarTxt.setVisibility(VISIBLE);
            mShareImg.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.backImg, R.id.shareImg, R.id.starTxt})
    public void onClick(View view) {
        if (mHandler == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.backImg:
                mHandler.onBackClick();
                break;
            case R.id.shareImg:
                mHandler.onShareClick();
                break;
            case R.id.starTxt:
                mHandler.onStarClick();
                break;
        }
    }

    @OnClick({R.id.rightTxt})
    public void onRightClick(View view) {
        if (mRightHandler != null) {
            mRightHandler.rightTxtClick();
        }
    }

    public interface NagivationHandlerIml {
        void onShareClick();

        void onStarClick();

        void onBackClick();
    }

    public interface NagivationRightHandleIml {
        void rightTxtClick();
    }
}
