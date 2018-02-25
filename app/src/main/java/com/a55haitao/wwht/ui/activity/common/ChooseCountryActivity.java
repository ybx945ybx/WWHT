package com.a55haitao.wwht.ui.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.common.ChooseCountryAdapter;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseCountryActivity extends BaseNoFragmentActivity {
    @BindView(R.id.lv_country_list) ListView          mLvCountryList;    // 国家列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_country);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        mLvCountryList.setAdapter(new ChooseCountryAdapter(this));
    }

    /**
     * 跳转到本页面
     *
     * @param activity    activity
     * @param requestCode 请求码
     */
    public static void actionStart(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ChooseCountryActivity.class), requestCode);
    }
}
