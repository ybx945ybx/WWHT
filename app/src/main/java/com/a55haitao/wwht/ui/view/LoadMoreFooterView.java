package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.a55haitao.wwht.R;

/**
 * Created by 55haitao on 2016/10/28.
 */

public class LoadMoreFooterView extends FrameLayout {

    private View mLoadMoreView ;
    private View mLoadFinishView ;

    public LoadMoreFooterView(Context context) {
        super(context);
        initView(context,null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attributeSet){

        LayoutInflater.from(context).inflate(R.layout.footer_for_twice_prod_layout, this);
        mLoadFinishView = getChildAt(0);

        LayoutInflater.from(context).inflate(R.layout.layout_listview_load_more, this);
        mLoadMoreView = getChildAt(1) ;

        mLoadMoreView.setVisibility(INVISIBLE);
    }

    public void setLoadStatus(boolean hasMore){
        mLoadMoreView.setVisibility(hasMore ? View.VISIBLE : View.GONE);
        mLoadFinishView.setVisibility(hasMore ? View.GONE : View.VISIBLE);
    }
}
