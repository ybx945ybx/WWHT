package com.a55haitao.wwht.adapter.social;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.data.model.result.GetFollowingActionListResult;
import com.a55haitao.wwht.ui.activity.social.TagDetailActivity;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

/**
 * 社区 - 关注 - Adapter
 *
 * @author 陶声
 * @since 2016-11-11
 */

public class FollowingActionAdapter extends BaseQuickAdapter<GetFollowingActionListResult.ActionListBean, BaseViewHolder> {
    private Fragment       mFragment;
    private Activity       mActivity;
    private int            mPicWidth;   // 图片尺寸
    private LayoutInflater mInflater;

    public FollowingActionAdapter(List<GetFollowingActionListResult.ActionListBean> data, Fragment fragment) {
        super(R.layout.item_post, data);
        mFragment = fragment;
        mActivity = mFragment.getActivity();
        init();
    }

    private void init() {
        mPicWidth = DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 20);
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetFollowingActionListResult.ActionListBean item) {
        // 用户名
        helper.setText(R.id.tv_nickname, item.following_info.nickname)
                .addOnClickListener(R.id.tv_nickname)
                .addOnClickListener(R.id.img_avatar);
        // 头像
        AvatarView avatar    = helper.getView(R.id.img_avatar);
        String     cornerUrl = null;
        if (item.following_info.user_title.size() != 0) {
            cornerUrl = item.following_info.user_title.get(0).getIconUrl();
        }
        avatar.loadImg(item.following_info.head_img, cornerUrl);

        // 点赞状态
        SparkButton sbLike = helper.getView(R.id.sb_like);
        sbLike.setChecked(item.data.post.is_liked);

        // one_word
        helper.setVisible(R.id.tv_title, !TextUtils.isEmpty(item.data.post.one_word));
        if (!TextUtils.isEmpty(item.data.post.one_word)) {
            helper.setText(R.id.tv_title, item.data.post.one_word);
        }
        // 文本
        helper.setVisible(R.id.tv_desc, !TextUtils.isEmpty(item.data.post.content));
        if (!TextUtils.isEmpty(item.data.post.content)) {
            helper.setText(R.id.tv_desc, item.data.post.content);
        }

        // 图片数
        int imgCount = item.data.post.images.size();
        helper.setVisible(R.id.tv_img_count, imgCount > 1);
        if (imgCount > 1) {
            helper.setText(R.id.tv_img_count, String.format("%d张", imgCount));
        }

        helper.setChecked(R.id.chk_follow_user, item.data.post.is_following) // 关注状态
                .setText(R.id.tv_desc, item.data.post.content) // 文本
                .setText(R.id.tv_comment_count, String.format("评论 %d", item.data.post.reply_count)) // 评论
                .setText(R.id.tv_like_count, String.format("赞 %d", item.data.post.like_count)) // 点赞
                .addOnClickListener(R.id.tv_nickname)
                .addOnClickListener(R.id.img_avatar)
                .addOnClickListener(R.id.chk_follow_user)
                .addOnClickListener(R.id.ll_tag_container)
                .addOnClickListener(R.id.sb_like)
                .addOnClickListener(R.id.ib_comment)
                .addOnClickListener(R.id.ib_share);
        // 填充tagList
        setTagList(helper, item.data.post.tag_list);

        // 图片
        ImageView              img       = helper.getView(R.id.img_pic);
        int                    picHeight = (int) (mPicWidth / item.data.post.images.get(0).wh_rate);
        ViewGroup.LayoutParams lp        = img.getLayoutParams();
        lp.width = mPicWidth;
        lp.height = picHeight;
        img.setLayoutParams(lp);

        // 笔记图片
        Glide.with(mFragment)
                .load(UpyUrlManager.getUrl(item.data.post.image_url, mPicWidth))
                .placeholder(R.mipmap.ic_default_rect)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.img_pic));

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
