package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.TrackInfoResult;
import com.a55haitao.wwht.utils.HaiUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by 董猛 on 16/9/8.
 */
public class TraceAdapter extends BaseAdapter {

    private ArrayList<TrackInfoResult.PackageListBean.TrackInfoBean> mDatas;
    
    private Context mContext;

    public TraceAdapter(Context context, ArrayList<TrackInfoResult.PackageListBean.TrackInfoBean> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return HaiUtils.getSize(mDatas);
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position) ;
        TrackInfoResult.PackageListBean.TrackInfoBean item = mDatas.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_for_trace_layout, parent, false);
        }
        ImageView imageView = ButterKnife.findById(convertView,R.id.statusImg);

        if (viewType == 0){
            imageView.setImageResource(R.mipmap.ic_trace_end_orange) ;
        }else if (viewType == 1){
            imageView.setImageResource(R.mipmap.ic_trace_middle) ;
        }else {
            imageView.setImageResource(R.mipmap.ic_trace_end) ;
        }

        HaiUtils.setText(convertView,R.id.statusContentTxt,item.content);
        HaiUtils.setText(convertView,R.id.statusDataTxt,item.date);
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0 ;
        }else if (position == getCount() - 1){
            return 2 ;
        }else {
            return 1 ;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }
}
