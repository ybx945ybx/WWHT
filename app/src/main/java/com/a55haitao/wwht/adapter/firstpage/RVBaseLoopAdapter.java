package com.a55haitao.wwht.adapter.firstpage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;

import java.util.ArrayList;

/**
 * Created by 董猛 on 2016/10/13.
 */

public abstract class RVBaseLoopAdapter<T> extends RVBaseAdapter<T> {

    public RVBaseLoopAdapter(Context context, ArrayList<T> arrayList, int resId) {
        super(context, arrayList, resId);
    }

    @Override
    public void onBindViewHolder(RVBaseHolder holder, int position) {
        bindView(holder,mDatas.get(position%super.getItemCount()));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() == 0 ? 0 : Integer.MAX_VALUE;
    }

    public int getItemRealCount(){
        return super.getItemCount();
    }
}
