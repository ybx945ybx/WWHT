package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.CategoryBean;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 董猛 on 16/6/23.
 */
public class HotCatrgoryAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<CategoryBean> datas;

    private Context context ;

    private int mNumCloumns ;

    public HotCatrgoryAdapter(Context c, List<CategoryBean> datas, int colums) {

        this.datas = new ArrayList<>() ;
        this.datas.addAll(datas) ;

        this.context = c ;

        inflater = LayoutInflater.from(c);

        mNumCloumns = colums ;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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

        CategoryBean bean = datas.get(position);
        convertView = inflater.inflate(R.layout.item_for_category, parent, false);
        ViewHolder holder = new ViewHolder(convertView) ;
        convertView.setTag(holder);

        //数据的处理
        if (!TextUtils.isEmpty(bean.image)){
            UPaiYunLoadManager.loadImage(context, bean.image, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, holder.categoryDetialImg);
        }

        holder.categoryDetialNameTxt.setText(bean.name);
        if (position == getCount()){
            holder.rightDividerView.setVisibility(View.INVISIBLE);
        } else {
            holder.rightDividerView.setVisibility(View.VISIBLE);
        }

        //判断是否是最后一行
        if (mNumCloumns != 0 && position + mNumCloumns >= getCount()){  //最后一行,隐藏分割线
            holder.bottomDividerView.setVisibility(View.INVISIBLE);
        }else {
            holder.bottomDividerView.setVisibility(View.VISIBLE);
        }
//        else if ( getItemViewType(position) == 2){
//            convertView = inflater.inflate(R.layout.customed_see_all_layout,parent,false) ;
//            TextView nameTxt = (TextView) convertView.findViewById(R.id.nameTxt);
//            nameTxt.setText("CATEGORY");
//
//            TextView bottomTxt = (TextView) convertView.findViewById(R.id.bottomTxt);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                bottomTxt.setLetterSpacing(0.4f);
//                nameTxt.setLetterSpacing(0);
//            }
//        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.categoryDetialImg)
        ImageView categoryDetialImg;
        @BindView(R.id.categoryDetialNameTxt)
        TextView categoryDetialNameTxt;
        @BindView(R.id.rightDividerView)
        View rightDividerView;
        @BindView(R.id.bottomDividerView)
        View bottomDividerView ;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
