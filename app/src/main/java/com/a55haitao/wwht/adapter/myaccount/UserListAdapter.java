package com.a55haitao.wwht.adapter.myaccount;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 用户列表Adapter
 *
 * @author 陶声
 * @since 2016-10-17
 */

public class UserListAdapter extends BaseQuickAdapter<UserListBean, BaseViewHolder> {

    public UserListAdapter(List<UserListBean> data) {
        super(R.layout.item_user, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserListBean user) {
        helper.setText(R.id.tv_nickname, user.nickname) // 昵称
                .setText(R.id.tv_post_count, user.post_count + "篇笔记") // 笔记数
                .setText(R.id.tv_like_count, String.format("收获%d个赞", user.like_count)) // 赞数
                .setChecked(R.id.chk_follow_user, user.is_following); // 是否关注
        // 自己不显示关注按钮
        helper.setVisible(R.id.chk_follow_user, user.id != HaiUtils.getUserId());
        if (user.id != HaiUtils.getUserId()) {
            helper.addOnClickListener(R.id.chk_follow_user);
        }
        // 头像
        AvatarView avatar    = helper.getView(R.id.img_avatar);
        String     cornerUrl = null;
        if (user.user_title.size() != 0) {
            cornerUrl = user.user_title.get(0).getIconUrl();
        }
        avatar.loadImg(user.head_img, cornerUrl);
        // 最后一条数据隐藏Divider
        helper.setVisible(R.id.view_divider, mData.indexOf(user) != mData.size() - 1);
    }
}
