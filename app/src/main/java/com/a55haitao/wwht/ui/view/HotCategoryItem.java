package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.HotCatrgoryAdapter;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.CategoryBean;
import com.a55haitao.wwht.ui.activity.discover.SearchResultActivity;
import com.a55haitao.wwht.utils.TraceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 董猛 on 16/6/23.
 */
public class HotCategoryItem extends LinearLayout {

    @BindView(R.id.categoryNameTxt)
    TextView categoryNameTxt;
    @BindView(R.id.categoryGridView)
    GridView categoryGridView;

    public HotCategoryItem(Context context) {
        super(context);
        initView(context, null);

    }

    public HotCategoryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_view_hot_category, this);
        ButterKnife.bind(this);

        categoryGridView.setNumColumns(2);
    }

    public void setDataAndName(final List<CategoryBean> list, String name) {
        categoryNameTxt.setText(name);

        HotCatrgoryAdapter adapter = new HotCatrgoryAdapter(getContext(), list, 2);
        categoryGridView.setAdapter(adapter);
        categoryGridView.setOnItemClickListener((parent, view, position, id) -> {
            CategoryBean bean = list.get(position);

            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_Product, bean.name, getContext(), TraceUtils.PageCode_AllCategory, TraceUtils.PositionCode_HotCategory + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_Product, TraceUtils.PageCode_AllCategory, TraceUtils.PositionCode_HotCategory, bean.name);

            SearchResultActivity.toThisActivity(ActivityCollector.getTopActivity(), bean.name, bean.query, PageType.CATEGORY);
        });
    }

    public void setTitleTxtVisibility(int flags) {
        categoryNameTxt.setVisibility(flags);
    }

    public void setTitleColor(int color) {
        this.categoryNameTxt.setBackgroundColor(color);
    }

    public void setTitleGravity(int flags) {
        this.categoryNameTxt.setGravity(flags);
    }

    public void setTitlePadding(int l) {
        categoryNameTxt.setPadding(l, categoryNameTxt.getPaddingTop(), categoryNameTxt.getPaddingRight(), categoryNameTxt.getPaddingBottom());
    }

}
