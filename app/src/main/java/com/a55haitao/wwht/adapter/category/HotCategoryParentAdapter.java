package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.AllCategoryResult;
import com.a55haitao.wwht.ui.view.HotCategoryItem;

import java.util.ArrayList;

/**
 * Created by 董猛 on 16/6/27.
 */
public class HotCategoryParentAdapter extends SectionedBaseAdapter {

    private Context context ;

    private ArrayList<AllCategoryResult> datas;

    public HotCategoryParentAdapter(Context context, ArrayList<AllCategoryResult> datas) {
        this.context = context;
        this.datas = new ArrayList<>();
        this.datas.addAll(datas) ;
    }

    //---------------------我是分界线------------------------

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return datas == null ? 0 : datas.size() ;
    }

    @Override
    public int getCountForSection(int section) {
        return 1;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        AllCategoryResult dataBean = datas.get(section);
        HotCategoryItem itemView = null ;

        if(convertView == null){
            itemView = new HotCategoryItem(context) ;
            itemView.setTitleTxtVisibility(View.GONE);
        } else {
            itemView = (HotCategoryItem) convertView;
        }

        itemView.setDataAndName(dataBean.sub,dataBean.name);

        return itemView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = ((LayoutInflater)(parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.item_view_hot_category_header,null) ;
        }
        TextView textView = (TextView) convertView.findViewById(R.id.headerTxt);
        textView.setText(datas.get(section).name);

        return convertView;
    }

}
