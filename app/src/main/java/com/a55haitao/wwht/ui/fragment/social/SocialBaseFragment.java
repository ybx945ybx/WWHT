package com.a55haitao.wwht.ui.fragment.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.ui.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 社区 BaseFragment
 * {@link SocialPostFragment 精选Fragment}
 * {@link FollowFragment 关注Fragment}
 *
 * @author 陶声
 * @since 2016-10-12
 */

public abstract class SocialBaseFragment extends BaseFragment {

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayout(), null, false);
        mUnbinder = ButterKnife.bind(this, v);
        initVariables();
        initViews(v, savedInstanceState);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * 获得布局
     */
    protected abstract int getLayout();

    /**
     * 初始化变量
     */
    public abstract void initVariables();

    /**
     * 初始化布局
     */
    protected abstract void initViews(View v, Bundle savedInstanceState);

    /**
     * 加载数据
     */
    public abstract void loadData();
}
