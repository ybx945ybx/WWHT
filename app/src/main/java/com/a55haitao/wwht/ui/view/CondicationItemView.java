package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 董猛 on 16/7/12.
 */
public class CondicationItemView extends RelativeLayout {

    @BindView(R.id.catogoryTotalNameTxt)
    TextView mLabelTxt;
    @BindView(R.id.rightImg)
    ImageView rightImg;
    @BindView(R.id.rightIndictor)
    View rightIndictor;

    private enum CondicationType{
        Status_1,Status_2
    }

    private CondicationType type ;

    private SearchResultBean.GroupBean groupBean ;

    private SearchResultBean.LabelsBean labelsBean ;

    public CondicationItemView(Context context) {
        super(context);
        init(context, null);
    }

    public CondicationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setDate(SearchResultBean.LabelsBean labelsBean){
        type = CondicationType.Status_2 ;
        this.labelsBean = labelsBean ;

        mLabelTxt.setText(labelsBean.label);
        setItemCheckStatus(labelsBean.isChecked);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
    }

    public void setDate(SearchResultBean.GroupBean groupBean){
        type = CondicationType.Status_1 ;
        this.groupBean = groupBean ;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mLabelTxt.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        setItemSelectStauts(groupBean.isSelect);
        setItemCheckStatus(groupBean.isChecked);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

        switch (groupBean.property) {
            case "category":
                mLabelTxt.setText("分类");
                break;
            case "brand":
                mLabelTxt.setText("品牌");
                break;
            case "sellerName":
                mLabelTxt.setText("商家");
                break;
            case "price":
                mLabelTxt.setText("价格");
                break;

        }

    }

    /**
     * 点击选中
     */
    public void setItemSelectStauts(boolean itemSelectStauts){
        this.groupBean.isSelect = itemSelectStauts ;
        if (itemSelectStauts){
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            rightIndictor.setVisibility(View.INVISIBLE);
        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrayF3F3F9));
            rightIndictor.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 子分类中被选中
     */
    public void setItemCheckStatus(boolean itemCheckStatus){
        if (type ==  CondicationType.Status_1){
            this.groupBean.isChecked = itemCheckStatus ;
        }else if (type == CondicationType.Status_2){
            this.labelsBean.isChecked = itemCheckStatus ;
        }
        if (itemCheckStatus){
            mLabelTxt.setTextColor(ContextCompat.getColor(getContext(),R.color.colorGray26241F));
            rightImg.setVisibility(VISIBLE);
        } else {
            mLabelTxt.setTextColor(ContextCompat.getColor(getContext(),R.color.colorGray666666));
            rightImg.setVisibility(INVISIBLE);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.sort_simple_text_view, this);
        ButterKnife.bind(this) ;
        setClickable(true);
        TypedArray t = getResources().obtainAttributes(attrs, R.styleable.CondicationItemView);
        String string = t.getString(R.styleable.CondicationItemView_txt);
        t.recycle();
        mLabelTxt.setText(string);
    }

    public String getLabel(){
        return mLabelTxt.getText().toString();
    }

}
