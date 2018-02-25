package com.a55haitao.wwht.adapter.social;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.EasyoptCommentBean;
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
public class EasyoptCommentListAdapter extends BaseQuickAdapter<EasyoptCommentBean, BaseViewHolder> {

    public EasyoptCommentListAdapter(List<EasyoptCommentBean> data, int avatarSize) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EasyoptCommentBean data) {
        helper.setText(R.id.tv_nickname, data.owner.getNickname()) // 昵称
                .setText(R.id.tv_time, HaiTimeUtils.showPostTime(data.created_time)) // 发布时间
                .setText(R.id.tv_like_count, data.like_count > 0 ? String.valueOf(data.like_count) : "") // 点赞数
                .setText(R.id.tv_desc, data.parent == null ? data.content : String.format("回复%s：%s", data.parent.owner.getNickname(), data.content)) // 评论内容
                .addOnClickListener(R.id.sb_like);

        if (data.owner.getId() != HaiUtils.getUserId()) {
            helper.addOnClickListener(R.id.img_avatar)
                    .addOnClickListener(R.id.tv_nickname);
        }
        // 点赞状态
        SparkButton sb = helper.getView(R.id.sb_like);
        sb.setChecked(data.islike == 1);
        // 头像
        AvatarView avatar    = helper.getView(R.id.img_avatar);
        String     cornerUrl = null;
        if (data.owner.getUserTitleList().size() != 0) {
            cornerUrl = data.owner.getUserTitleList().get(0).getIconUrl();
        }
        avatar.loadImg(data.owner.getHeadImg(), cornerUrl);

        // 最后一条数据隐藏Divider
        helper.setVisible(R.id.view_divider, mData.indexOf(data) != mData.size() - 1);
    }
}
