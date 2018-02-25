package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.ui.view.FullyGridLayoutManager;
import com.a55haitao.wwht.utils.HaiUtils;

import java.util.ArrayList;

/**
 * 筛选界面分类适配器
 * Created by a55 on 2017/4/11.
 */

public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.FilterCategoryViewHolder> {

    private Context                                     mContext;
    private ArrayList<SearchResultBean.ParentLabelBean> data;
    private OnLabelClickListener                        onLabelClickListener;
    private ArrayList<int[]>                            selectedPosList;                //   所有选中的label集合

    public FilterCategoryAdapter(Context mContext, ArrayList<SearchResultBean.ParentLabelBean> data) {
        this.data = data;
        this.mContext = mContext;
        //        initSelectedPosList(data);
    }

    //  所有被选中的labes
    public ArrayList<int[]> getSelectedPosList() {
        selectedPosList = new ArrayList<>();
        if (HaiUtils.getSize(data) > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isChecked && HaiUtils.getSize(data.get(i).sub_labels) > 0) {
                    for (int j = 0; j < data.get(i).sub_labels.size(); j++) {
                        if (data.get(i).sub_labels.get(j).isChecked) {
                            int[] selectedPos = new int[]{i, j};
                            selectedPosList.add(selectedPos);
                        }

                    }
                }
            }
        }
        return selectedPosList;
    }

    // 判断分类是否有被选中
    public boolean hasLabels() {
        if (HaiUtils.getSize(data) > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isChecked) {
                    return true;
                }
            }
        }
        return false;
    }

    //  清空选中的labes
    public void clearAllSelecte() {
        if (HaiUtils.getSize(data) > 0) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).isExpanded = false;
                if (data.get(i).isChecked && HaiUtils.getSize(data.get(i).sub_labels) > 0) {
                    data.get(i).isChecked = false;
                    for (int j = 0; j < data.get(i).sub_labels.size(); j++) {
                        if (data.get(i).sub_labels.get(j).isChecked) {
                            data.get(i).sub_labels.get(j).isChecked = false;
                        }

                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public ArrayList<SearchResultBean.ParentLabelBean> getData() {
        return data;
    }

    public void setData(ArrayList<SearchResultBean.ParentLabelBean> data) {
        this.data = data;
    }

    @Override
    public FilterCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterCategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.filter_category_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FilterCategoryViewHolder holder, int position) {

        SearchResultBean.ParentLabelBean item = data.get(position);

        holder.viewLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        // 是展开还是收起状态
        if (item.isExpanded) {
            holder.rycvLabes.setVisibility(View.VISIBLE);
            holder.ivArrow.setImageResource(R.mipmap.ic_product_up);
        } else {
            holder.rycvLabes.setVisibility(View.GONE);
            holder.ivArrow.setImageResource(R.mipmap.ic_product_down);
        }

        //  父标签名
        holder.tvCategory.setText(item.label);

        // 被选中标签的展示
        if (item.isChecked) {
            String selected = "";
            for (int i = 0; i < item.sub_labels.size(); i++) {
                if (item.sub_labels.get(i).isChecked) {
                    if (TextUtils.isEmpty(selected)) {
                        selected = item.sub_labels.get(i).label;
                        //                        selected = TextUtils.isEmpty(item.parentLabel) ? item.label + "/" + item.sub_labels.get(i).label : item.parentLabel + "/" + item.label + "/" + item.sub_labels.get(i).label;
                    } else {
                        selected = selected + "," + item.sub_labels.get(i).label;
                        //                        selected = selected + "," + (TextUtils.isEmpty(item.parentLabel) ? item.label + "/" + item.sub_labels.get(i).label : item.parentLabel + "/" + item.label + "/" + item.sub_labels.get(i).label);
                    }
                }
            }
            holder.tvCategorySelected.setText(selected);
        }
        holder.tvCategorySelected.setVisibility(item.isChecked ? View.VISIBLE : View.GONE);

        // 子类标签列表
        holder.rycvLabes.setLayoutManager(new FullyGridLayoutManager(mContext, 2));
        FilterCategoryLabesAdapter adapter = new FilterCategoryLabesAdapter(mContext, item.sub_labels);
        adapter.setOnLabeSelecteListener((pos, isChecked) -> {

            item.sub_labels.get(pos).setChecked(isChecked);

            //  本分类内是否有选中项
            for (int i = 0; i < item.sub_labels.size(); i++) {
                if (item.sub_labels.get(i).isChecked) {
                    item.isChecked = true;
                    break;
                } else {
                    item.isChecked = false;
                }
            }
            notifyDataSetChanged();

            // 选了分类后接口传递出去获取数据刷新品牌和商家，价格的筛选数据
            if (onLabelClickListener != null) {
                onLabelClickListener.onLabelClick();
            }
        });
        holder.rycvLabes.setAdapter(adapter);

        // 点击父标签后展开或收起子标签列表
        holder.rlytCategory.setOnClickListener(v -> {
            if (holder.rycvLabes.getVisibility() == View.VISIBLE) {
                holder.rycvLabes.setVisibility(View.GONE);
                holder.ivArrow.setImageResource(R.mipmap.ic_product_down);
                item.isExpanded = false;
            } else {
                holder.rycvLabes.setVisibility(View.VISIBLE);
                holder.ivArrow.setImageResource(R.mipmap.ic_product_up);
                item.isExpanded = true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface OnLabelClickListener {
        void onLabelClick();
    }

    public void setOnLabelClickListener(OnLabelClickListener onLabelClickListener) {
        this.onLabelClickListener = onLabelClickListener;
    }

    class FilterCategoryViewHolder extends RecyclerView.ViewHolder {
        View           viewLine;
        RelativeLayout rlytCategory;
        TextView       tvCategory;
        TextView       tvCategorySelected;
        ImageView      ivArrow;
        RecyclerView   rycvLabes;

        public FilterCategoryViewHolder(View itemView) {
            super(itemView);
            viewLine = itemView.findViewById(R.id.viewline);
            rlytCategory = (RelativeLayout) itemView.findViewById(R.id.rlytCategory);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvCategorySelected = (TextView) itemView.findViewById(R.id.tv_category_selected);
            ivArrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            rycvLabes = (RecyclerView) itemView.findViewById(R.id.rycv_labes);
        }
    }

}
