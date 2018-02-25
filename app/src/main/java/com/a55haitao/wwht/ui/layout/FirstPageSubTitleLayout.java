package com.a55haitao.wwht.ui.layout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.ui.activity.easyopt.RecommendEasyOptActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.TraceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by drolmen on 2016/12/30.
 */

public class FirstPageSubTitleLayout extends RelativeLayout {
    @BindView(R.id.tilteDivider) View mTilteDivider;
    @BindView(R.id.titleTxt) HaiTextView mTitleTxt;
    @BindView(R.id.moreTxt) ImageView mMoreTxt;
    @BindView(R.id.bottomLine) View mBottomLine;

    public FirstPageSubTitleLayout(Context context) {
        super(context);
    }

    public FirstPageSubTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setTitleTxt(String str) {
        mTitleTxt.setText(str);
        mTilteDivider.setVisibility(GONE);
        mBottomLine.setVisibility(INVISIBLE);
    }

    @OnClick(R.id.moreTxt)
    public void onClick(View v) {
        Context context = getContext();
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_Album, "", context, TraceUtils.PageCode_Category, TraceUtils.PositionCode_AlbumSelectedList + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_Album, TraceUtils.PageCode_Category, TraceUtils.PositionCode_AlbumSelectedList, "");

        context.startActivity(new Intent(context, RecommendEasyOptActivity.class));
    }
}
