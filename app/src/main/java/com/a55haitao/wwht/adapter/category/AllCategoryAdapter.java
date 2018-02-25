package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.AllCategoryResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 董猛 on 16/6/27.
 */
public class AllCategoryAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private List<AllCategoryResult> datas;

    public AllCategoryAdapter(Context context, List<AllCategoryResult> datas) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AllCategoryResult dataBean = datas.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_simple_text_view, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.catogoryTotalName.setText(dataBean.name);

        //根据状态切换背景
        if (dataBean.isChecked) {
            viewHolder.catogoryTotalName.setTextColor(ContextCompat.getColor(context, R.color.colorGray26241F));
            viewHolder.rightIndictor.setVisibility(View.INVISIBLE);
            viewHolder.leftIndictor.setVisibility(View.VISIBLE);
        } else {
            viewHolder.catogoryTotalName.setTextColor(ContextCompat.getColor(context, R.color.colorGray666666));
            viewHolder.rightIndictor.setVisibility(View.VISIBLE);
            viewHolder.leftIndictor.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.catogoryTotalNameTxt)
        TextView catogoryTotalName;
        @BindView(R.id.leftIndictor)
        View leftIndictor;
        @BindView(R.id.rightIndictor)
        View rightIndictor;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
