package com.a55haitao.wwht.adapter.myaccount;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.MessageBean;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 个人中心 - 通知列表Adapter
 *
 * @author 陶声
 * @since 2016-10-18
 */

public class NotificationListAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public NotificationListAdapter(List<MessageBean> data) {
        super(R.layout.item_notification, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        helper.setText(R.id.tv_time, HaiTimeUtils.showPostTime(item.create_dt))
                .setText(R.id.tv_desc, item.title);
        // 最后一条数据隐藏Divider
        helper.setVisible(R.id.view_divider, mData.indexOf(item) != mData.size() - 1);
    }

}
