package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.varunest.sparkbutton.SparkButton;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 晒物详情 点赞按钮
 *
 * @author 陶声
 * @since 2016-08-29
 */
public class PostLikeButton extends LinearLayout {

    @BindView(R.id.sb_like)       SparkButton  mSbLike;
    @BindView(R.id.tv_like_desc)  TextView     mTvLikeDesc;
    @BindView(R.id.tv_like_count) TextView     mTvLikeCount;
    @BindView(R.id.container)     LinearLayout mContainer;

    @BindColor(R.color.spark_button_secondary_color) int COLOR_RED;
    @BindColor(R.color.colorGray666666)              int COLOR_GREY;

    private boolean                checked;
    private int                    likeCount;
    private PostLikeButtonListener mListener;

    public PostLikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.layout_post_like_button, this);
        ButterKnife.bind(this, v);

        mSbLike.setChecked(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener != null)
            mListener.onLike(checked);
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        if (likeCount > 0) {
            mTvLikeCount.setVisibility(View.VISIBLE);
            mTvLikeCount.setText(String.valueOf(likeCount));
            mTvLikeDesc.setText("赞");

        } else {
            mTvLikeDesc.setText("献上第一个赞");
            mTvLikeCount.setVisibility(View.GONE);
        }
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked)
            mSbLike.playAnimation();
        mSbLike.setChecked(checked);

        mTvLikeCount.setTextColor(checked ? COLOR_RED : COLOR_GREY);
        mTvLikeDesc.setTextColor(checked ? COLOR_RED : COLOR_GREY);

        mContainer.setBackgroundResource(checked ?
                R.drawable.shape_post_detail_like_button_checked : R.drawable.shape_post_detail_like_button_normal);
    }

    public void setCheckedNoAnim(boolean checked) {
        this.checked = checked;
        mSbLike.setChecked(checked);

        mTvLikeCount.setTextColor(checked ? COLOR_RED : COLOR_GREY);
        mTvLikeDesc.setTextColor(checked ? COLOR_RED : COLOR_GREY);

        mContainer.setBackgroundResource(checked ?
                R.drawable.shape_post_detail_like_button_checked : R.drawable.shape_post_detail_like_button_normal);
    }

    public void setLikeListener(PostLikeButtonListener listener) {
        mListener = listener;
    }

    public interface PostLikeButtonListener {
        void onLike(boolean isChecked);
    }
}
