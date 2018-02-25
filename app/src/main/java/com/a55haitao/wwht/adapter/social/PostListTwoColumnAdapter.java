package com.a55haitao.wwht.adapter.social;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

/**
 * 社区 - 精选 - 晒物列表
 *
 * @author 陶声
 * @since 2016-10-27
 */

public class PostListTwoColumnAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {

    private Activity       mActivity;
    private RequestManager glideWith;
    private int            mPicWidth;       // 图片宽度

    public PostListTwoColumnAdapter(List<PostBean> data, Activity activity) {
        super(R.layout.item_post_two_column, data);
        mActivity = activity;
        glideWith = Glide.with(mActivity);
        init();
    }

    /**
     * 初始化头像&图片高度
     */
    private void init() {
        mPicWidth = (DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 40)) / 2;
    }

    /**
     * 计算图片高度
     */
    private int calcImgHeight(float whRate) {
        return (int) (mPicWidth / whRate);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean post) {
        // 点赞状态
        ((SparkButton) helper.getView(R.id.sb_like)).setChecked(post.is_liked);
        // 头像
        AvatarView avatar    = helper.getView(R.id.img_avatar);
        String     cornerUrl = null;
        if (post.owner.user_title.size() != 0) {
            cornerUrl = post.owner.user_title.get(0).getIconUrl();
        }
        avatar.loadImg(post.owner.head_img, cornerUrl);

        ImageView              img = helper.getView(R.id.img_pic);
        ViewGroup.LayoutParams lp  = img.getLayoutParams();
        lp.width = mPicWidth;
        lp.height = calcImgHeight(post.images.get(0).wh_rate);
        img.setLayoutParams(lp);

        Logger.t(TAG).d("width = " + lp.width + " height = " + lp.height + " wh_rate = " + post.images.get(0).wh_rate);

        // 图片
        glideWith.load(UpyUrlManager.getUrl(post.image_url, mPicWidth))
                .placeholder(R.mipmap.ic_default_square_tiny)
                .transform(new GlideRoundTransform(mContext, 8))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into(img);
        // one_word
        helper.setVisible(R.id.tv_title, !TextUtils.isEmpty(post.one_word));
        if (!TextUtils.isEmpty(post.one_word)) {
            helper.setText(R.id.tv_title, post.one_word);
        }
        // 文本
        helper.setVisible(R.id.tv_desc, !TextUtils.isEmpty(post.content));
        if (!TextUtils.isEmpty(post.content)) {
            helper.setText(R.id.tv_desc, post.content);
        }
        helper.setText(R.id.tv_nickname, post.owner.nickname) // 昵称
                .setText(R.id.tv_like_count, String.valueOf(post.like_count)) // 点赞数
                .addOnClickListener(R.id.tv_nickname)
                .addOnClickListener(R.id.img_avatar)
                .addOnClickListener(R.id.sb_like);

    }
}
