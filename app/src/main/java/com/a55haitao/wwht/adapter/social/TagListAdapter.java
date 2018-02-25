package com.a55haitao.wwht.adapter.social;

import com.a55haitao.wwht.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 搜索标签列表 Adapter
 *
 * @author 陶声
 * @since 2016-11-17
 */

public class TagListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TagListAdapter(List<String> data) {
        super(R.layout.item_tag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_tag_name, item);
    }
}
