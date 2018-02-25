package com.a55haitao.wwht.adapter.social;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.PostFragmentLayoutType;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.ui.activity.social.TagDetailActivity;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

/**
 * 社区 - 精选 - 晒物列表
 *
 * @author 陶声
 * @since 2016-10-27
 */

public class SocialPostListAdapter extends BaseMultiItemQuickAdapter<PostBean, BaseViewHolder> {

    private Fragment       mFragment;
    private Activity       mActivity;
    private RequestManager glideWith;
    private boolean hasUserInfoHeader = true;
    private int            mPicWidth;
    private int            mPicWidth2;
    private LayoutInflater mInflater;

    public SocialPostListAdapter(List<PostBean> data, Fragment fragment) {
        super(data);
        mFragment = fragment;
        mActivity = fragment.getActivity();
        glideWith = Glide.with(mFragment);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        // 计算图片宽度
        mPicWidth = DisplayUtils.getScreenWidth(mActivity) - 2 * DisplayUtils.dp2px(mActivity, 10);
        mPicWidth2 = (DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 40)) / 2;

        // inflater
        mInflater = LayoutInflater.from(mActivity);
        addItemType(PostFragmentLayoutType.SINGLE, R.layout.item_post);
        addItemType(PostFragmentLayoutType.DOUBLE, R.layout.item_post_two_column);
    }

    /**
     * 计算图片高度
     */
    private int calcImgHeight(float whRate) {
        return (int) (mPicWidth2 / whRate);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean post) {
        // 点赞状态
        ((SparkButton) helper.getView(R.id.sb_like)).setChecked(post.is_liked);

        switch (helper.getItemViewType()) {
            case PostFragmentLayoutType.SINGLE:
                if (!hasUserInfoHeader) {
                    helper.getView(R.id.ll_user).setVisibility(View.GONE);
                } else {
                    // 昵称
                    helper.setText(R.id.tv_nickname, post.owner.nickname);
                    // 头像
                    AvatarView avatar    = helper.getView(R.id.img_avatar);
                    String     cornerUrl = null;
                    if (post.owner.user_title.size() != 0) {
                        cornerUrl = post.owner.user_title.get(0).getIconUrl();
                    }
                    avatar.loadImg(post.owner.head_img, cornerUrl);
                }

                // 标题
                boolean titleVisible = !TextUtils.isEmpty(post.one_word);
                helper.setVisible(R.id.tv_title, titleVisible);
                if (titleVisible) {
                    helper.setText(R.id.tv_title, post.one_word);
                }

                // 图片数
                int imgCount = post.images.size();
                helper.setVisible(R.id.tv_img_count, imgCount > 1);
                if (imgCount > 1) {
                    helper.setText(R.id.tv_img_count, String.format("%d张", imgCount));
                }

                // 关注状态
                if (HaiUtils.getUserId() == post.owner.id) { // 自己隐藏
                    helper.setVisible(R.id.chk_follow_user, false);
                } else {
                    helper.setVisible(R.id.chk_follow_user, true)
                            .setChecked(R.id.chk_follow_user, post.is_following)
                            .addOnClickListener(R.id.chk_follow_user);
                }

                helper.setText(R.id.tv_desc, post.content) // 文本
                        .setText(R.id.tv_comment_count, String.format("评论 %d", post.reply_count)) // 评论
                        .setText(R.id.tv_like_count, String.format("赞 %d", post.like_count)); // 点赞

                ImageView img = helper.getView(R.id.img_pic);

                int picHeight = (int) (mPicWidth / post.images.get(0).wh_rate);

                ViewGroup.LayoutParams lp = img.getLayoutParams();
                lp.width = mPicWidth;
                lp.height = picHeight;
                img.setLayoutParams(lp);

                // 图片
                glideWith.load(UpyUrlManager.getUrl(post.image_url, mPicWidth))
                        .placeholder(R.mipmap.ic_default_square_big)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into(img);

                setTagList(helper, post.tag_list);

                // 点击事件
                helper.addOnClickListener(R.id.tv_nickname)
                        .addOnClickListener(R.id.img_avatar)
                        .addOnClickListener(R.id.ll_tag_container)
                        .addOnClickListener(R.id.sb_like)
                        .addOnClickListener(R.id.ib_comment)
                        .addOnClickListener(R.id.ib_share);
                break;
            case PostFragmentLayoutType.DOUBLE:
                // 头像
                AvatarView avatar = helper.getView(R.id.img_avatar);
                String cornerUrl = null;
                if (post.owner.user_title.size() != 0) {
                    cornerUrl = post.owner.user_title.get(0).getIconUrl();
                }
                avatar.loadImg(post.owner.head_img, cornerUrl);

                ImageView img2 = helper.getView(R.id.img_pic);
                ViewGroup.LayoutParams lp2 = img2.getLayoutParams();
                lp2.width = mPicWidth2;
                lp2.height = calcImgHeight(post.images.get(0).wh_rate);
                img2.setLayoutParams(lp2);

                // 图片
                glideWith.load(UpyUrlManager.getUrl(post.image_url, mPicWidth))
                        .placeholder(R.mipmap.ic_default_square_tiny)
                        .transform(new GlideRoundTransform(mContext, 8))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into(img2);
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
                break;
        }
    }

    /**
     * 填充tagList
     *
     * @param helper
     * @param tagList
     */
    private void setTagList(BaseViewHolder helper, List<TagBean> tagList) {
        LinearLayout llTagContainer = helper.getView(R.id.ll_tag_container);
        llTagContainer.removeAllViews();

        for (final TagBean tag : tagList) {
            TextView tvTag = (TextView) mInflater.inflate(tag.is_hot == 1 ? R.layout.tag_hot : R.layout.tag_normal, null);

            //            tvTag.setText(tag.is_hot == 1 ? String.format("# %s", tag.name) : tag.name);

            String tagName = tag.name;
            if (tagName.length() > 15) {
                tagName = tagName.substring(0, 15) + "...";
            }
            if (tag.is_hot == 1) {
                tagName = "# " + tagName;
            }
            tvTag.setText(tagName);

            tvTag.setOnClickListener(v -> {
                Intent intent = new Intent(mActivity, TagDetailActivity.class);   // 跳转到tag晒物页
                intent.putExtra("tag_id", tag.id);
                intent.putExtra("tag_name", tag.name);
                intent.putExtra("is_hot", tag.is_hot);
                mActivity.startActivity(intent);

            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.margin_medium);
            tvTag.setLayoutParams(layoutParams);
            llTagContainer.addView(tvTag);
        }

    }
}
