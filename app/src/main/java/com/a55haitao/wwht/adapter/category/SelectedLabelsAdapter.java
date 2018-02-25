package com.a55haitao.wwht.adapter.category;

import android.content.Context;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by a55 on 2017/4/17.
 */

public class SelectedLabelsAdapter extends BaseQuickAdapter<SearchResultBean.LabelsBean, BaseViewHolder> {
    private Context mContext;

    public SelectedLabelsAdapter(Context mContext, List<SearchResultBean.LabelsBean> data) {
        super(R.layout.simple_condiction_txt_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResultBean.LabelsBean item) {
        helper.setText(R.id.tv_label, item.label + " \u00D7");
    }
}
