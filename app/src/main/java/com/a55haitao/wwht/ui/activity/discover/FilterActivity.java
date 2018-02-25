package com.a55haitao.wwht.ui.activity.discover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.FilterBrandsOrSellerAdapter;
import com.a55haitao.wwht.adapter.category.FilterCategoryAdapter;
import com.a55haitao.wwht.adapter.category.FilterPriceAdapter;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.CondicationItemView;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.EasyRecyclerViewSidebar;
import com.a55haitao.wwht.ui.view.PinnedHeaderListView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 筛选
 */
public class FilterActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)                      DynamicHeaderView                     mHeaderView;                   //  title
    @BindView(R.id.leftRootLayout)             LinearLayout                          mLeftRootLayout;               //  左侧子父布局
    @BindView(R.id.rycv_category)              RecyclerView                          rycvCategory;                  //  分类筛选部分
    @BindView(R.id.rlyt_brand)                 RelativeLayout                        rlytBrand;                     //  品牌筛选部分
    @BindView(R.id.sortItemListView_brand)     PinnedHeaderListView                  mbrandListView;                //  显示品牌的ListView
    @BindView(R.id.section_sidebar_brand)      EasyRecyclerViewSidebar               slidebarBrand;                 //  侧边栏
    @BindView(R.id.section_floating_tv_brand)  TextView                              slideFloatingTxtBrand;         //  悬浮提示框
    @BindView(R.id.rlyt_seller)                RelativeLayout                        rlytSeller;                    //  商家筛选部分
    @BindView(R.id.sortItemListView_seller)    PinnedHeaderListView                  mSellerListView;               //  显示商家的ListView
    @BindView(R.id.section_sidebar_seller)     EasyRecyclerViewSidebar               slidebarSeller;                //  侧边栏
    @BindView(R.id.section_floating_tv_seller) TextView                              slideFloatingTxtSeller;        //  悬浮提示框
    @BindView(R.id.scrollView_price)           ScrollView                            scrollViewPrie;                //  价格筛选部分
    @BindView(R.id.rycv_price)                 RecyclerView                          rycvPrice;                     //  价格筛选部分列表
    @BindView(R.id.etv_price_from)             EditText                              etvPriceFrom;                  //  价格筛选部分自定义起价
    @BindView(R.id.etv_price_to)               EditText                              etvPriceTo;                    //  价格筛选部分自定义最高价
    @BindView(R.id.ensureTxt)                  TextView                              mEnsureTxt;                    //  确定
    private                                    CondicationItemView[]                 mItemViews;                    //  左侧View集合
    private                                    FilterCategoryAdapter                 categoryAdapter;               //  分类适配器
    private                                    FilterBrandsOrSellerAdapter           brandAdapter;                  //  品牌适配器
    private                                    FilterBrandsOrSellerAdapter           sellerAdapter;                 //  商家适配器
    private                                    FilterPriceAdapter                    priceAdapter;                  //  价格适配器
    private                                    ArrayList<SearchResultBean.GroupBean> mDatas;                        //  数据
    private                                    int[]                                 mInts;                         //  位置集合    -2不显示该板块-1显示该板块为没有选择项状态0是有选中项状态
    private                                    int                                   mCurrentPosiition;             //  左侧当前选中位置 0~3
    private                                    InputMethodManager                    imm;
    public                                     String                                priceFilter_s;
    public                                     String                                priceFilter_e;
    private                                    Tracker                               mTracker;                      // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("筛选");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        initIntent();
        initLefter();
        initRight();
        initContent();
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String           data             = intent.getStringExtra("datas");
            SearchResultBean searchResultBean = new Gson().fromJson(data, SearchResultBean.class);
            mDatas = searchResultBean.group;

            mInts = intent.getIntArrayExtra("ints");

            priceFilter_s = intent.getStringExtra("priceFilter_s");
            priceFilter_e = intent.getStringExtra("priceFilter_e");
        }
    }

    /**
     * 初始化左侧视图
     */
    private void initLefter() {
        mItemViews = new CondicationItemView[4];
        for (int i = 0; i < 4; i++) {
            CondicationItemView itemView = (CondicationItemView) mLeftRootLayout.getChildAt(i * 2);
            if (mInts[i] == -2) {
                itemView.setVisibility(View.GONE);
                mLeftRootLayout.getChildAt(i * 2 + 1).setVisibility(View.GONE);
            }
            itemView.setDate(mDatas.get(i));

            mItemViews[i] = itemView;
        }
    }

    /**
     * 初始化右侧ListView 以及需要的Adapter
     */
    private void initRight() {
        //1.分类的适配器
        if (mInts[0] != -2) {
            rycvCategory.setLayoutManager(new LinearLayoutManager(this));
            categoryAdapter = new FilterCategoryAdapter(this, mDatas.get(0).labels);
            categoryAdapter.setOnLabelClickListener(() -> {
                boolean hasSelected = categoryAdapter.hasLabels();
                mItemViews[0].setItemCheckStatus(hasSelected);
                mInts[0] = hasSelected ? 0 : -1;
            });
            rycvCategory.setAdapter(categoryAdapter);
        }

        //2.品牌的适配器
        if (mInts[1] != -2) {
            ArrayList<EasyRecyclerViewSidebar.EasySection> list = new ArrayList<>();
            for (SearchResultBean.ParentLabelBean bean : mDatas.get(1).labels) {
                if (bean.label != null)
                    list.add(new EasyRecyclerViewSidebar.EasySection(bean.label));
            }
            slidebarBrand.setSections(list);

            brandAdapter = new FilterBrandsOrSellerAdapter(mDatas.get(1).labels, this);
            mbrandListView.setAdapter(brandAdapter);
            // 侧边栏
            slidebarBrand.setFloatView(slideFloatingTxtBrand);
            slidebarBrand.setOnTouchSectionListener((sectionIndex, letterSection) -> {
                slideFloatingTxtBrand.setText(letterSection.letter);

                //计算正确的位置
                int itemId = (int) brandAdapter.getItemId(sectionIndex, 0);
                mbrandListView.setSelection(itemId);
            });

            mbrandListView.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                    boolean newStatus;
                    newStatus = !brandAdapter.getDatas().get(section).sub_labels.get(position).isChecked;
                    //1.修改右侧数据，并更新ListView
                    brandAdapter.getDatas().get(section).sub_labels.get(position).isChecked = newStatus;
                    brandAdapter.notifyDataSetChanged();

                    //2.修改左侧数据，并记录选中位置
                    mItemViews[mCurrentPosiition].setItemCheckStatus(brandAdapter.hasSelected());
                    mInts[mCurrentPosiition] = brandAdapter.hasSelected() ? position : -1;

                }

                @Override
                public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

                }
            });
        }

        //3.商家的适配器
        if (mInts[2] != -2) {
            ArrayList<EasyRecyclerViewSidebar.EasySection> list = new ArrayList<>();
            for (SearchResultBean.ParentLabelBean bean : mDatas.get(2).labels) {
                if (bean.label != null)
                    list.add(new EasyRecyclerViewSidebar.EasySection(bean.label));
            }
            slidebarSeller.setSections(list);

            sellerAdapter = new FilterBrandsOrSellerAdapter(mDatas.get(2).labels, this);
            mSellerListView.setAdapter(sellerAdapter);
            // 侧边栏
            slidebarSeller.setFloatView(slideFloatingTxtSeller);
            slidebarSeller.setOnTouchSectionListener((sectionIndex, letterSection) -> {
                slideFloatingTxtSeller.setText(letterSection.letter);

                //计算正确的位置
                int itemId = (int) sellerAdapter.getItemId(sectionIndex, 0);
                mSellerListView.setSelection(itemId);
            });

            mSellerListView.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                    boolean newStatus;
                    newStatus = !sellerAdapter.getDatas().get(section).sub_labels.get(position).isChecked;
                    //1.修改右侧数据，并更新ListView
                    sellerAdapter.getDatas().get(section).sub_labels.get(position).isChecked = newStatus;
                    sellerAdapter.notifyDataSetChanged();

                    //2.修改左侧数据，并记录选中位置
                    mItemViews[mCurrentPosiition].setItemCheckStatus(sellerAdapter.hasSelected());
                    mInts[mCurrentPosiition] = sellerAdapter.hasSelected() ? position : -1;

                }

                @Override
                public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

                }
            });
        }

        // 4价格的适配器
        if (mInts[3] != -2) {
            //            priceAdapter = new FilterPriceAdapter(this, mDatas.get(2).labels);
            //            rycvPrice.setLayoutManager(new FullyLinearLayoutManager(this));
            //            rycvPrice.setAdapter(priceAdapter);

            if (!TextUtils.isEmpty(priceFilter_s)) {

                etvPriceFrom.setText(priceFilter_s);
            }

            if (!TextUtils.isEmpty(priceFilter_e)) {

                etvPriceTo.setText(priceFilter_e);
            }

            etvPriceFrom.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        mItemViews[3].setItemCheckStatus(true);
                        mInts[3] = 0;
                    } else if (TextUtils.isEmpty(etvPriceTo.getText())) {
                        mItemViews[3].setItemCheckStatus(false);
                        mInts[3] = -1;
                    } else {
                        mItemViews[3].setItemCheckStatus(true);
                        mInts[3] = 0;
                    }

                    priceFilter_s = s.toString();
                }
            });
            etvPriceTo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        mItemViews[3].setItemCheckStatus(true);
                        mInts[3] = 0;
                    } else if (TextUtils.isEmpty(etvPriceFrom.getText())) {
                        mItemViews[3].setItemCheckStatus(false);
                        mInts[3] = -1;
                    } else {
                        mItemViews[3].setItemCheckStatus(true);
                        mInts[3] = 0;
                    }
                    priceFilter_e = s.toString();
                }
            });
        }

    }

    /**
     * 初始化content
     */
    private void initContent() {

        for (int i = 0; i < 4; i++) {
            if (mInts[i] >= 0) {
                mItemViews[i].setItemCheckStatus(true);
                //                changeCheckedStatus(i, true);
            }
        }

        //  判断右侧显示哪个板块   第一个有选中内容的板块，都没有的话默认是第一个
        int posotion = 0;
        boolean hasSelect = false;
        for (int i = 0; i < 4; i++) {
            if (mInts[i] >= 0) {
                posotion = i;
                hasSelect = true;
                break;
            }
        }
        if (!hasSelect) {
            for (int i = 0; i < 4; i++) {
                if (mInts[i] == -1) {
                    posotion = i;
                    break;
                }
            }
        }
        setItemViewSelectStatus(posotion);
    }

    public static void toThisActivityForResult(Activity activity, int[] ints, String jsondatas, int requestCode, String priceFilter_s, String priceFilter_e) {
        Intent intent = new Intent(activity, FilterActivity.class);
        intent.putExtra("ints", ints);
        intent.putExtra("datas", jsondatas);
        intent.putExtra("priceFilter_s", priceFilter_s);
        intent.putExtra("priceFilter_e", priceFilter_e);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void toThisActivityForResult(BaseFragment fragment, int[] ints, String jsondatas, int requestCode, String priceFilter_s, String priceFilter_e) {
        Intent intent = new Intent(fragment.getContext(), FilterActivity.class);
        intent.putExtra("ints", ints);
        intent.putExtra("datas", jsondatas);
        intent.putExtra("priceFilter_s", priceFilter_s);
        intent.putExtra("priceFilter_e", priceFilter_e);
        fragment.startActivityForResult(intent, requestCode);
    }

    @OnClick({R.id.item01, R.id.item02, R.id.item03, R.id.item04, R.id.ensureTxt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item01:
                hideSoftInput();
                setItemViewSelectStatus(0);
                break;
            case R.id.item02:
                hideSoftInput();
                setItemViewSelectStatus(1);
                break;
            case R.id.item03:
                hideSoftInput();
                setItemViewSelectStatus(2);
                break;
            case R.id.item04:
                setItemViewSelectStatus(3);
                break;
            case R.id.ensureTxt:
                Intent intent = new Intent();

                SearchResultBean searchResultBean = new SearchResultBean();
                searchResultBean.group = mDatas;
                String data = new Gson().toJson(searchResultBean);

                intent.putExtra("data", data);
                intent.putExtra("ints", mInts);
                intent.putExtra("priceFilter_s", getPriceFilter_s());
                intent.putExtra("priceFilter_e", getPriceFilter_e());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @OnClick(R.id.headRightTxt)
    public void clearAll(View view) {
        hideSoftInput();
        for (int i = 0; i < 4; i++) {
            changeCheckedStatus(i);
            if (mInts[i] != -2) {
                mInts[i] = -1;
            }
        }
    }

    /**
     * 改变适配器中，指定position位置checked 值
     * 刷新右侧列表样式
     */
    private void changeCheckedStatus(int position) {
        // 0是分类 1是品牌 2是商家 3是价格
        if (mInts[position] < 0)
            return;

        // 重置右侧列表
        if (position == 0) {
            //重置右侧 分类poosition位置数据
            categoryAdapter.clearAllSelecte();
        } else if (position == 1) {
            //重置右侧 品牌position位置数据
            if (brandAdapter != null) {
                brandAdapter.clearAllSelected();
            }
        } else if (position == 2) {
            //重置右侧 商家position位置数据
            if (sellerAdapter != null) {
                sellerAdapter.clearAllSelected();
            }
        } else {
            //  价格部分
            etvPriceFrom.setText("");
            etvPriceFrom.setHint("最低价");
            etvPriceTo.setText("");
            etvPriceTo.setHint("最高价");
        }

        //重置左侧 position位置数据
        mItemViews[position].setItemCheckStatus(false);
    }

    // 点击左侧item后改变右侧显示状态
    private void setItemViewSelectStatus(int position) {
        mCurrentPosiition = position;
        for (int i = 0; i < mItemViews.length; i++) {
            mItemViews[i].setItemSelectStauts(i == position);
        }

        if (position == 0) {            // 分类
            rycvCategory.setVisibility(View.VISIBLE);
            rlytBrand.setVisibility(View.GONE);
            rlytSeller.setVisibility(View.GONE);
            scrollViewPrie.setVisibility(View.GONE);
        } else if (position == 3) {     // 价格
            scrollViewPrie.setVisibility(View.VISIBLE);
            rycvCategory.setVisibility(View.GONE);
            rlytBrand.setVisibility(View.GONE);
            rlytSeller.setVisibility(View.GONE);
        } else if (position == 1) {       // 品牌
            rycvCategory.setVisibility(View.GONE);
            scrollViewPrie.setVisibility(View.GONE);
            rlytBrand.setVisibility(View.VISIBLE);
            rlytSeller.setVisibility(View.GONE);
        } else {                         // 商家
            rycvCategory.setVisibility(View.GONE);
            scrollViewPrie.setVisibility(View.GONE);
            rlytBrand.setVisibility(View.GONE);
            rlytSeller.setVisibility(View.VISIBLE);
        }
    }

    //收起软键盘
    private void hideSoftInput() {
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(etvPriceFrom.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etvPriceTo.getWindowToken(), 0);
        }
    }

    //  获取最高价和最低价   如果最高价低于最低价  则取反    反之亦然
    private String getPriceFilter_s() {
        if (TextUtils.isEmpty(priceFilter_s)) {
            return priceFilter_s;
        } else {
            if (TextUtils.isEmpty(priceFilter_e)) {
                return String.valueOf(Integer.parseInt(priceFilter_s));
            } else {
                if (Integer.parseInt(priceFilter_s) < Integer.parseInt(priceFilter_e)) {
                    return String.valueOf(Integer.parseInt(priceFilter_s));
                } else {
                    return String.valueOf(Integer.parseInt(priceFilter_e));
                }
            }
        }
    }

    private String getPriceFilter_e() {
        if (TextUtils.isEmpty(priceFilter_e)) {
            return priceFilter_e;
        } else {
            if (TextUtils.isEmpty(priceFilter_s)) {
                return String.valueOf(Integer.parseInt(priceFilter_e));
            } else {
                if (Integer.parseInt(priceFilter_s) < Integer.parseInt(priceFilter_e)) {
                    return String.valueOf(Integer.parseInt(priceFilter_e));
                } else {
                    return String.valueOf(Integer.parseInt(priceFilter_s));
                }
            }
        }
    }

}
