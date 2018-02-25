package com.a55haitao.wwht.ui.fragment.myaccount;

import com.a55haitao.wwht.ui.fragment.BaseFragment;

/**
 * 个人主页Fragment基类
 *
 * @author 陶声
 * @since 2016-10-12
 */

public abstract class MyHomePageBaseFragment extends BaseFragment {
   /* private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayout(), null, false);
        mUnbinder = ButterKnife.bind(this, v);
        initVars();
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

    *//**
     * 获得布局
     *//*
    protected abstract int getLayout();

    *//**
     * 初始化变量
     *//*
    public abstract void initVars();

    *//**
     * 初始化布局
     *//*
    protected abstract void initViews(View v, Bundle savedInstanceState);

    *//**
     * 加载数据
     *//*
    public abstract void loadData();*/
}
