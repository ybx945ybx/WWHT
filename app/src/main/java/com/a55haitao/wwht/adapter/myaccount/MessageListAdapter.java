package com.a55haitao.wwht.adapter.myaccount;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.MessageBean;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.a55haitao.wwht.utils.glide.GlideCircleTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 消息 - Adapter
 *
 * @author 陶声
 * @since 2016-10-18
 */

public class MessageListAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    private Fragment mFragment;
    private Activity mActivity;
    private int      mAvatarSize;   // 头像尺寸
    private int      mPicSize;      // 图片尺寸

    public MessageListAdapter(List<MessageBean> data, Fragment fragment) {
        super(R.layout.item_message, data);
        mFragment = fragment;
        mActivity = mFragment.getActivity();
        init();
    }

    /**
     * 计算图片尺寸
     */
    private void init() {
        mAvatarSize = mActivity.getResources().getDimensionPixelSize(R.dimen.avatar_medium);
        mPicSize = mActivity.getResources().getDimensionPixelSize(R.dimen.message_post_size);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        helper.setText(R.id.tv_nickname, item.nickname)
                .setText(R.id.tv_time, HaiTimeUtils.showPostTime(item.create_dt))
                .setText(R.id.tv_desc, item.title);
        // 头像
        Glide.with(mFragment)
                .load(UpyUrlManager.getUrl(item.head_img, mAvatarSize))
                .placeholder(R.mipmap.ic_avatar_default_small)
                .transform(new GlideCircleTransform(mContext))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.img_avatar));

        // 晒物图
        if (!TextUtils.isEmpty(item.image_url)) {
            helper.setVisible(R.id.img_pic, true);
            Glide.with(mFragment)
                    .load(UpyUrlManager.getUrl(item.image_url, mPicSize))
                    .placeholder(R.mipmap.ic_default_square_tiny)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into((ImageView) helper.getView(R.id.img_pic));
        } else {
            helper.setVisible(R.id.img_pic, false);
        }
        // 最后一条数据隐藏Divider
        helper.setVisible(R.id.view_divider, mData.indexOf(item) != mData.size() - 1);
    }
}
