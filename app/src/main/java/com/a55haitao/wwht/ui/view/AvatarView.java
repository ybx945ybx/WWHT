package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.utils.glide.GlideCircleTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户头像
 *
 * @author 陶声
 * @since 2017-04-05
 */
public class AvatarView extends RelativeLayout {
    @BindView(R.id.img_avatar_view) ImageView mImgAvatar;
    @BindView(R.id.img_corner_view) ImageView mImgCorner;

    private static final int DEFAULT_CORNER_SIZE = 10;

    private Context mContext;
    private int     mCornerSize;
    private int     mBorderWidth;
    private int     mPlaceHolder;
    private int     mDefaultBorderColor;
    private boolean mHasDefaultBorder;

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_avatar_view, this);

        ButterKnife.bind(this);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AvatarView);

        mCornerSize = t.getDimensionPixelSize(R.styleable.AvatarView_corner_size, DEFAULT_CORNER_SIZE);
        mBorderWidth = t.getDimensionPixelSize(R.styleable.AvatarView_border_width, 0);
        mPlaceHolder = t.getResourceId(R.styleable.AvatarView_placeholder, R.mipmap.ic_avatar_default_ultra);
        mHasDefaultBorder = t.getBoolean(R.styleable.AvatarView_has_default_border, false);

        t.recycle();
    }

    public void loadImg(String avatarUrl, String cornerUrl) {
        // 角标是否可见
        boolean cornerVisible = !TextUtils.isEmpty(cornerUrl);
        mImgCorner.setVisibility(cornerVisible ? View.VISIBLE : View.GONE);
        // 头像
        if (cornerVisible && mBorderWidth > 0) {
            Glide.with(mContext)
                    .load(UpyUrlManager.getUrl(avatarUrl, getMeasuredWidth()))
                    .transform(new GlideCircleTransform(mContext, mBorderWidth, ContextCompat.getColor(mContext, R.color.colorOrange)))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(mPlaceHolder)
                    .into(mImgAvatar);
        } else if (mHasDefaultBorder) {
            Glide.with(mContext)
                    .load(UpyUrlManager.getUrl(avatarUrl, getMeasuredWidth()))
                    .transform(new GlideCircleTransform(mContext, mBorderWidth, ContextCompat.getColor(mContext, R.color.colorWhite)))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(mPlaceHolder)
                    .into(mImgAvatar);
        } else {
            Glide.with(mContext)
                    .load(UpyUrlManager.getUrl(avatarUrl, getMeasuredWidth()))
                    .transform(new GlideCircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(mPlaceHolder)
                    .into(mImgAvatar);
        }
        // 画角标
        if (cornerVisible) {
            ViewGroup.LayoutParams lp = mImgCorner.getLayoutParams();
            lp.width = mCornerSize;
            lp.height = mCornerSize;
            mImgCorner.setLayoutParams(lp);
            // 头像
            Glide.with(mContext)
                    .load(cornerUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImgCorner);
        }

    }

    public void loadImg(Integer resId) {
        mImgCorner.setVisibility(View.GONE);
        Glide.with(mContext)
                .load(R.mipmap.ic_avatar_default_large)
                .dontAnimate()
                .into(mImgAvatar);

    }

}
