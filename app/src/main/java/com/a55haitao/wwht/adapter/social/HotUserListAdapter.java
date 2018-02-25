package com.a55haitao.wwht.adapter.social;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.GetHotUserListResult;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 添加好友 - 推荐用户
 *
 * @author 陶声
 * @since 2016-11-01
 */

public class HotUserListAdapter extends BaseQuickAdapter<GetHotUserListResult.UsersBean, BaseViewHolder> {
    private int[] mImgIds = {R.id.img_pic1, R.id.img_pic2, R.id.img_pic3, R.id.img_pic4};
    private       int mPicSize;     // 笔记图片尺寸

    public HotUserListAdapter(List<GetHotUserListResult.UsersBean> data, int picWidth, int avatarSize) {
        super(R.layout.item_hot_user, data);
        mPicSize = picWidth;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetHotUserListResult.UsersBean user) {
        helper.setVisible(R.id.chk_follow_user, user.id != HaiUtils.getUserId());
        helper.setText(R.id.tv_nickname, user.nickname)
                .setText(R.id.tv_post_count, String.format("%d篇笔记", user.post_count))
                .setText(R.id.tv_like_count, String.format("收获%d个赞", user.like_count));

        if (user.id != HaiUtils.getUserId()) {
            helper.setChecked(R.id.chk_follow_user, user.is_following)
                    .addOnClickListener(R.id.chk_follow_user);
        }
        // 头像
        AvatarView avatar    = helper.getView(R.id.img_avatar);
        String     cornerUrl = null;
        if (user.user_title.size() != 0) {
            cornerUrl = user.user_title.get(0).getIconUrl();
        }
        avatar.loadImg(user.head_img, cornerUrl);

        // 晒物图片
        LinearLayout llPostContainer = helper.getView(R.id.ll_post_container);
        if (user.posts_images == null) {
            llPostContainer.setVisibility(ImageView.GONE);
        } else {
            llPostContainer.setVisibility(ImageView.VISIBLE);

            // 实际长度
            int len = user.posts_images.size();
            // 隐藏空图片
            for (int i = 0; i < 4; i++) {
                helper.setVisible(mImgIds[i], i < len);
            }
            // 加载图片
            for (int i = 0; i < len; i++) {
                ImageView              img = helper.getView(mImgIds[i]);
                ViewGroup.LayoutParams lp  = img.getLayoutParams();
                lp.width = mPicSize;
                lp.height = mPicSize;
                img.setLayoutParams(lp);

                Glide.with(mContext)
                        .load(UpyUrlManager.getUrl(user.posts_images.get(i), mPicSize))
                        .placeholder(R.mipmap.ic_default_square_tiny)
                        .centerCrop()
                        .transform(new GlideRoundTransform(mContext, 8))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into(img);
            }
        }
    }
}
