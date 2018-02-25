package com.a55haitao.wwht.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by 董猛 on 2016/10/13.
 */

public class RVBaseHolder extends RecyclerView.ViewHolder{

    private HashMap<Integer, View> mViewMaps;

    public RVBaseHolder(View itemView) {
        super(itemView);
        mViewMaps = new HashMap<>();
    }

    /**
     * 代替findViewById
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int id){
        View view = mViewMaps.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViewMaps.put(id, view);
        }
        return (T) view;
    }

    public void setText(int id,String txt){
        ((TextView) getView(id)).setText(txt);
    }

    public View setCheckStatus(int id, boolean b) {
        View view = getView(id);
        ((Checkable) view).setChecked(b);
        return view;
    }

    public void setVisibility(int visibility, int... ids) {
        for (int id : ids) {
            getView(id).setVisibility(visibility);
        }
    }
}
