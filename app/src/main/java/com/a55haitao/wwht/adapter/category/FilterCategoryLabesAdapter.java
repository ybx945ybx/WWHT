package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.graphics.Color;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * 筛选界面分类内标签适配器
 * Created by a55 on 2017/4/13.
 */

public class FilterCategoryLabesAdapter extends BaseQuickAdapter<SearchResultBean.LabelsBean, BaseViewHolder> {
    private Context mContext;
    private OnLabeSelecteListener onLabeSelecteListener;
//    private String parentLabel;

    public FilterCategoryLabesAdapter(Context mContext, ArrayList<SearchResultBean.LabelsBean> data) {
        super(R.layout.filter_category_labe_item, data);
        this.mContext = mContext;
//        this.parentLabel = parentLabel;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResultBean.LabelsBean item) {
        helper.setText(R.id.tv_category, item.label)
        .setVisible(R.id.iv_selected, item.isChecked)
        .setTextColor(R.id.tv_category, item.isChecked ? Color.parseColor("#333333") : Color.parseColor("#666666"));

        helper.itemView.setSelected(item.isChecked);

        helper.itemView.setOnClickListener(v -> {
            if(onLabeSelecteListener != null){
                int position = helper.getLayoutPosition();

                onLabeSelecteListener.onLabeSelect(position, !item.isChecked);
            }
        });
    }

    public interface OnLabeSelecteListener{
        void onLabeSelect(int position, boolean isChecked);
    }

    public void setOnLabeSelecteListener(OnLabeSelecteListener onLabeSelecteListener){
        this.onLabeSelecteListener = onLabeSelecteListener;
    }
}
