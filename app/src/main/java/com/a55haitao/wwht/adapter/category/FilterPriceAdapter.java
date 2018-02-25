package com.a55haitao.wwht.adapter.category;

import android.content.Context;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by a55 on 2017/4/11.
 */

public class FilterPriceAdapter extends BaseQuickAdapter<SearchResultBean.ParentLabelBean, BaseViewHolder> {
    private Context mContext;

    public FilterPriceAdapter(Context context, List<SearchResultBean.ParentLabelBean> data) {
        super(R.layout.filter_price_list_item, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResultBean.ParentLabelBean item) {
        helper.setText(R.id.tv_price, item.label);
    }
}
