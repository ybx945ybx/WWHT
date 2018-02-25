package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.LVBaseAdaptrer;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.utils.HaiUtils;

import java.util.ArrayList;

/**
 * 筛选界面品牌商家适配器
 * Created by a55 on 2017/4/17.
 */

public class FilterBrandsOrSellerAdapter extends SectionedBaseAdapter {
    private ArrayList<SearchResultBean.ParentLabelBean> datas;
    private Context                                     mContext;
    private LayoutInflater                              inflater;

    public FilterBrandsOrSellerAdapter(ArrayList<SearchResultBean.ParentLabelBean> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    public ArrayList<SearchResultBean.ParentLabelBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<SearchResultBean.ParentLabelBean> datas) {
        this.datas = datas;
    }

    // 是否有被选中的
    public boolean hasSelected() {
        if (getCount() == 0)
            return false;

        for (int i = 0; i < getSectionCount(); i++) {
            for (int j = 0; j < getDatas().get(i).sub_labels.size(); j++) {
                if (getDatas().get(i).sub_labels.get(j).isChecked) {
                    return true;
                }
            }
        }
        return false;
    }

    // 清除所有被选中的labels
    public void clearAllSelected(){
        if (HaiUtils.getSize(datas) > 0) {
            for (int i = 0; i < datas.size(); i++) {
                if (HaiUtils.getSize(datas.get(i).sub_labels) > 0) {
                    datas.get(i).isChecked = false;
                    for (int j = 0; j < datas.get(i).sub_labels.size(); j++) {
                        if (datas.get(i).sub_labels.get(j).isChecked) {
                            datas.get(i).sub_labels.get(j).isChecked = false;
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        long result = 0;
        result += section;
        for (int i = 0; i < section; i++) {
            result += getCountForSection(i);
        }
        result += position;
        return result;
    }

    @Override
    public int getSectionCount() {
        return HaiUtils.getSize(datas);
    }

    @Override
    public int getCountForSection(int section) {
        return HaiUtils.getSize(datas.get(section).sub_labels);
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        SearchResultBean.LabelsBean item = datas.get(section).sub_labels.get(position);

        LVBaseAdaptrer.LVHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sort_brand_or_seller_item, parent, false);
            holder = new LVBaseAdaptrer.LVHolder(convertView);
            holder.getView(R.id.rightIndictor).setVisibility(View.INVISIBLE);

            convertView.setTag(holder);
        } else {
            holder = (LVBaseAdaptrer.LVHolder) convertView.getTag();
        }

        holder.getItemView().setTag(R.id.tag_for_img, item);


        TextView txtView = (TextView) holder.getView(R.id.catogoryTotalNameTxt);
        txtView.setText(item.label);

        TextView tvCount = (TextView) holder.getView(R.id.tv_count);
        tvCount.setText("(" + item.document_count + ")");

        if (item.isChecked) {
            txtView.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray26241F));
            tvCount.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray26241F));
            holder.getView(R.id.rightImg).setVisibility(View.VISIBLE);
        } else {
            txtView.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
            tvCount.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
            holder.getView(R.id.rightImg).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_filter_brand_seller_header, parent, false);
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.height = 0;
            convertView.setLayoutParams(layoutParams);
        }

        ((TextView) convertView.findViewById(R.id.headerTxt)).setText(datas.get(section).label);

        return convertView;
    }

}
