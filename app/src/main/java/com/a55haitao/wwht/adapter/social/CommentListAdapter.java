package com.a55haitao.wwht.adapter.social;

import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.CommentBean;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

/**
 * 评论列表Adapter
 *
 * @author 陶声
 * @since 2016-10-31
 */
public class CommentListAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    public CommentListAdapter(List<CommentBean> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean data) {
        helper.setText(R.id.tv_nickname, data.user_info.nickname.trim()) // 昵称
                .setText(R.id.tv_time, HaiTimeUtils.showPostTime(data.create_dt)) // 发布时间
                .setText(R.id.tv_like_count, data.like_count > 0 ? String.valueOf(data.like_count) : "") // 点赞数
                .setText(R.id.tv_desc, data.at_user_info == null ? data.content : String.format("回复%s：%s", data.at_user_info.nickname, data.content)) // 评论内容
                .addOnClickListener(R.id.sb_like);

        if (data.user_info.id != HaiUtils.getUserId()) {
            helper.addOnClickListener(R.id.img_avatar)
                    .addOnClickListener(R.id.tv_nickname);
        }

        SparkButton sb = helper.getView(R.id.sb_like);
        sb.setChecked(data.is_liked);
        // 头像
        AvatarView avatar    = helper.getView(R.id.img_avatar);
        String     cornerUrl = null;
        if (data.user_info.user_title.size() != 0) {
            cornerUrl = data.user_info.user_title.get(0).getIconUrl();
        }
        avatar.loadImg(data.user_info.head_img, cornerUrl);

        // 最后一条数据隐藏Divider
        helper.setVisible(R.id.view_divider, mData.indexOf(data) != mData.size() - 1);
    }
}
