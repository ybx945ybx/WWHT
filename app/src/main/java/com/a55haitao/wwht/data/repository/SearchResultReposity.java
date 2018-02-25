package com.a55haitao.wwht.data.repository;

import android.text.TextUtils;

import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.Sort;
import com.a55haitao.wwht.data.model.annotation.UseSynType;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.data.model.entity.SearchResultBean;
import com.a55haitao.wwht.data.model.entity.SearchSpecialBean;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.ProductService;
import com.a55haitao.wwht.utils.HaiParamPrepare;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;

/**
 * Created by 55haitao on 2016/11/2.
 * 处理筛选数据
 */

public class SearchResultReposity extends BaseRepository {

    private
    @PageType int mPageType;        //标识类型，0搜索 ，1分类，2品牌，3商家 4专题

    private boolean mIsQuery;               //页面类别 true 为搜索，false为主页

    private String mTopic;                  //搜索

    private String mTitle;                  //主页

    private QueryBean                                mQueryBean;                  //基础Query参数
    private SearchSpecialBean.SearchSpecialQueryBean mSpecialQueryBean;           //搜索专题基础Query参数

    int mCurrentSort;                      //排序

    public String priceFilter_s;
    public String priceFilter_e;

    private String defaultCategory;
    private String defaultBrand;
    private String defaultSeller;

    private ArrayList<SearchResultBean.GroupBean> mGroups;            //筛选数据
    private ArrayList<SearchResultBean.LabelsBean> selectedlist = new ArrayList<>();
    private int[] mFilterPositions;                                   //选中的筛选条件位置 -1显示相应模块-2不显示  大于等于0为选中的相应位置

    String[] letterArry = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z", "#"};

    // 搜索关键字
    public static SearchResultReposity newInstance(String topic) {
        SearchResultReposity repisity = new SearchResultReposity();
        repisity.mIsQuery = true;
        repisity.mTopic = topic;
        repisity.mCurrentSort = -1;
        repisity.mFilterPositions = new int[]{-1, -1, -1, -1};
        return repisity;
    }

    // 品牌 商家
    public static SearchResultReposity newInstance(String title, QueryBean bean, int pageType) {
        SearchResultReposity repisity = new SearchResultReposity();
        repisity.mIsQuery = false;
        repisity.mTitle = title;
        repisity.mQueryBean = bean;
        repisity.mCurrentSort = -1;
        repisity.mPageType = pageType;
        repisity.mFilterPositions = new int[]{-1, pageType == PageType.BRAND ? -2 : -1, pageType == PageType.SELLER ? -2 : -1, -1};
        return repisity;
    }

    //  搜索专题
    public static SearchResultReposity newInstance(String title, int pageType, SearchSpecialBean.SearchSpecialQueryBean bean) {
        SearchResultReposity repisity = new SearchResultReposity();
        repisity.mIsQuery = false;
        repisity.mTitle = title;
        repisity.mSpecialQueryBean = bean;
        repisity.mCurrentSort = -1;
        repisity.mPageType = pageType;
        repisity.mFilterPositions = new int[]{-1, HaiUtils.getSize(bean.brands) > 0 ? -2 : -1, HaiUtils.getSize(bean.sellers) > 0 ? -2 : -1, -1};
        return repisity;
    }

    // 满减活动
    public static SearchResultReposity newInstance(QueryBean bean, int pageType, boolean allBrand) {
        SearchResultReposity repisity = new SearchResultReposity();
        repisity.mIsQuery = false;
        repisity.mQueryBean = bean;
        repisity.mCurrentSort = -1;
        repisity.mPageType = pageType;
        repisity.mFilterPositions = new int[]{-1, allBrand ? -1 : -2, -2, -1};
        return repisity;
    }

    public Observable<SearchResultBean> doSearch(int currentPage, int synType) {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        //创建基础查询参数
        treeMap.put("_mt", ApiUrl.MT_PRODUCT_SEARCH);
        treeMap.put("page", currentPage == -1 ? 1 : currentPage + 1);
        treeMap.put("count", 20);
        if (synType == UseSynType.SYN_USE) {
            treeMap.put("usesyn", 1);
        }
        if (mIsQuery) {
            treeMap.put("query", mTopic);
        } else {
            if (mPageType == PageType.SPECIAL) {
                getSpecialQueryParam(mSpecialQueryBean, treeMap);
            } else {
                HaiUtils.getQueryParam(mQueryBean, treeMap);
                //                getQueryParam(mQueryBean, treeMap);
            }

        }
        //排序参数
        creatSortParam(treeMap);
        //筛选参数
        getFilterParam(treeMap, false);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, treeMap);
        return transform(RetrofitFactory.createService(ProductService.class).productSearch(treeMap));
    }

    // 搜索动作上报
    public Observable<Object> searchLogServer(int currentPage, int resNum) {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        //创建基础查询参数
        treeMap.put("_mt", "55haitao_sns.ProductAPI/searchlogserver");
        treeMap.put("page", currentPage == -1 ? 1 : currentPage + 1);
        treeMap.put("resNum", resNum);
        if (mIsQuery) {
            treeMap.put("query", mTopic);
        } else {
            if (mPageType == PageType.SPECIAL) {
                getSpecialQueryParam(mSpecialQueryBean, treeMap);
            } else {
                HaiUtils.getQueryParam(mQueryBean, treeMap);
            }

        }
        //排序参数
        getSortParam(treeMap);
        //筛选参数
        getFilterParam(treeMap, true);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, treeMap);
        return transform(RetrofitFactory.createService(ProductService.class).searchLogServer(treeMap));
    }

    private void getSortParam(TreeMap<String, Object> paramMap) {
        switch (mCurrentSort) {
            case -1:
            case Sort.HOT:     //doNothing , hot is default
                paramMap.put("sort", "0");
                break;
            case Sort.SALE:
                paramMap.put("sort", "3");
                break;
            case Sort.PRICE_ASCEND:
                paramMap.put("sortPrice", "1");
                break;
            case Sort.PRICE_DESCEND:
                paramMap.put("sortPrice", "2");
                break;
            default:
                break;
        }
    }

    private void getFilterParam(TreeMap<String, Object> map, boolean logServe) {
        if (mGroups == null || mGroups.size() == 0) {
            return;
        }
        if (mFilterPositions[0] >= 0) {
            map.put("category", getSelectedParam(0));
        }
        if (mFilterPositions[1] >= 0) {
            map.put("brand", getSelectedParam(1));
        }
        if (mFilterPositions[2] >= 0) {
            map.put("seller", getSelectedParam(2));
        }
        if (mFilterPositions[3] >= 0) {
            if (logServe) {
                if (!TextUtils.isEmpty(priceFilter_s) || !TextUtils.isEmpty(priceFilter_e))
                    map.put("priceRange", TextUtils.isEmpty(priceFilter_s) ? "" : priceFilter_s + "-" + (TextUtils.isEmpty(priceFilter_e) ? "" : priceFilter_e));
            } else {
                if (!TextUtils.isEmpty(priceFilter_s)) {
                    map.put("priceFilter_s", priceFilter_s);
                }
                if (!TextUtils.isEmpty(priceFilter_e)) {
                    map.put("priceFilter_e", priceFilter_e);
                }
            }
        }
    }

    // 获取被选中的筛选项,对参数进行拼接
    public String getSelectedParam(int position) {
        String param = "";

        if (mGroups.get(position).labels != null) {
            for (int i = 0; i < mGroups.get(position).labels.size(); i++) {
                if (HaiUtils.getSize(mGroups.get(position).labels.get(i).sub_labels) > 0) {
                    for (int j = 0; j < mGroups.get(position).labels.get(i).sub_labels.size(); j++) {
                        if (mGroups.get(position).labels.get(i).sub_labels.get(j).isChecked) {
                            if (position == 0) {                                //  分类
                                String paramSting = "";
                                // 生成paramSting
                                if (mGroups.get(position).labels.get(i).noSecond) {    // 其他类的item 只有一层
                                    paramSting = mGroups.get(position).labels.get(i).label;
                                } else {                                              //  非其他类的item 至少有两层
                                    if (TextUtils.isEmpty(mGroups.get(position).labels.get(i).parentLabel)) {  // 直接就是1 2 层  没包装
                                        if (mGroups.get(position).labels.get(i).sub_labels.get(j).noThrid) {  //  全部
                                            paramSting = mGroups.get(position).labels.get(i).label;
                                        } else {
                                            paramSting = mGroups.get(position).labels.get(i).label + ">" + mGroups.get(position).labels.get(i).sub_labels.get(j).label;
                                        }
                                    } else {     //  属于有3层的  但是里面有没有三层的，是包装过的
                                        if (mGroups.get(position).labels.get(i).sub_labels.get(j).noThrid) {// 没有三层  是包装的数据
                                            paramSting = mGroups.get(position).labels.get(i).parentLabel + ">" + mGroups.get(position).labels.get(i).label;
                                        } else {   //  有三层
                                            paramSting = mGroups.get(position).labels.get(i).parentLabel + ">" + mGroups.get(position).labels.get(i).label + ">" + mGroups.get(position).labels.get(i).sub_labels.get(j).label;
                                        }
                                    }
                                }

                                if (TextUtils.isEmpty(param)) {
                                    param = paramSting;
                                } else {
                                    param = param + "@@" + paramSting;
                                }

                                // 如果有默认参数的拼接默认筛选参数（商家／品牌／专题）
                                if (!TextUtils.isEmpty(defaultCategory)) {
                                    param = defaultCategory + "@@" + param;
                                }

                            } else if (position == 1 || position == 2) {      //  品牌 商家
                                if (TextUtils.isEmpty(param)) {
                                    param = mGroups.get(position).labels.get(i).sub_labels.get(j).label;
                                } else {
                                    param = param + "@@" + mGroups.get(position).labels.get(i).sub_labels.get(j).label;
                                }

                                // 如果有默认参数的拼接默认筛选参数（商家／品牌／专题）
                                if (position == 1) {
                                    if (!TextUtils.isEmpty(defaultBrand)) {
                                        param = defaultBrand + "@@" + param;
                                    }
                                } else {
                                    if (!TextUtils.isEmpty(defaultSeller)) {
                                        param = defaultSeller + "@@" + param;
                                    }
                                }

                            } else {
                                return param;
                            }
                        }
                    }
                }
            }
        }

        return param;
    }

    /**
     * 拼接排序参数
     *
     * @param paramMap
     */
    private void creatSortParam(TreeMap<String, Object> paramMap) {
        switch (mCurrentSort) {
            case Sort.HOT:     //doNothing , hot is default
                break;
            case Sort.SALE:
                paramMap.put("sortDiscount", "0");
                break;
            case Sort.PRICE_ASCEND:
                paramMap.put("sortPrice", "0");
                break;
            case Sort.PRICE_DESCEND:
                paramMap.put("sortPrice", "1");
                break;
            default:
                break;
        }
    }

    /**
     * 设置排排序条件
     *
     * @param currentSort
     */
    public void setCurrentSort(@Sort int currentSort) {
        mCurrentSort = currentSort;
    }

    public int getCurrentSort() {
        return mCurrentSort;
    }

    public int getmPageType() {
        return mPageType;
    }

    /**
     * 设置筛选中数据position数据
     *
     * @param filterPositions
     */
    public void setFilterPositions(int[] filterPositions) {
        if (mFilterPositions == null || filterPositions == null)
            return;

        for (int i = 0; i < 4; i++) {
            if (mFilterPositions[i] != -2) {
                mFilterPositions[i] = filterPositions[i];
            }
        }
    }

    public void removeFilterPosition(int position) {
        mFilterPositions[position] = -1;
    }

    public void setGroups(ArrayList<SearchResultBean.GroupBean> groups) {
        if (groups == null) {
            mGroups = null;
            return;
        }

        //  对分类数据进行处理
        ArrayList<SearchResultBean.ParentLabelBean> labesTemp = new ArrayList<>();
        SearchResultBean.GroupBean                  groupBean = new SearchResultBean.GroupBean();
        groupBean.property = "category";
        if (groups.get(0) != null) {   //  处理分类数据
            if (groups.get(0).labels != null && groups.get(0).labels.size() > 0) {  //  有标题
                for (int i = 0; i < groups.get(0).labels.size(); i++) {         // 遍历一级元素
                    if (groups.get(0).labels.get(i).sub_labels != null
                            && groups.get(0).labels.get(i).sub_labels.size() > 0) {  // 拿到一级元素 查看里面是否有二级元素，有二级元素就去遍历

                        boolean has = false;        // 是否有三级
                        for (int j = 0; j < groups.get(0).labels.get(i).sub_labels.size(); j++) {   // 遍历二级元素
                            if (groups.get(0).labels.get(i).sub_labels.get(j).sub_labels != null
                                    && groups.get(0).labels.get(i).sub_labels.get(j).sub_labels.size() > 0) {// 拿到二级元素 查看里面是否有三级元素
                                // 有三级元素
                                has = true;
                                break;
                            }
                        }

                        if (has) {
                            for (int j = 0; j < groups.get(0).labels.get(i).sub_labels.size(); j++) {
                                //  如果其中有一个有三级元素 另一个没有三级元素  则没有三级元素的那个补齐第三级  名字是全部XX

                                SearchResultBean.ParentLabelBean parentLabelBean1 = new SearchResultBean.ParentLabelBean();
                                parentLabelBean1.parentLabel = groups.get(0).labels.get(i).label;

                                if (groups.get(0).labels.get(i).sub_labels.get(j).sub_labels != null
                                        && groups.get(0).labels.get(i).sub_labels.get(j).sub_labels.size() > 0) {// 有三级元素
                                    parentLabelBean1.label = groups.get(0).labels.get(i).sub_labels.get(j).label;
                                    parentLabelBean1.sub_labels = groups.get(0).labels.get(i).sub_labels.get(j).sub_labels;
                                    parentLabelBean1.sub_labels.add(0, creatLabel(parentLabelBean1.label));
                                    labesTemp.add(parentLabelBean1);
                                } else {                                                                          //  没三级元素的补齐
                                    parentLabelBean1.label = groups.get(0).labels.get(i).sub_labels.get(j).label;
                                    SearchResultBean.LabelsBean labelsBean = new SearchResultBean.LabelsBean();
                                    labelsBean.label = "全部" + parentLabelBean1.label;
                                    labelsBean.noThrid = true;
                                    ArrayList<SearchResultBean.LabelsBean> labelsBeenlist = new ArrayList<>();
                                    labelsBeenlist.add(labelsBean);
                                    parentLabelBean1.sub_labels = labelsBeenlist;
                                    labesTemp.add(parentLabelBean1);
                                }

                            }

                        } else {
                            // 添加全部
                            SearchResultBean.ParentLabelBean parentLabelBean = groups.get(0).labels.get(i);
                            parentLabelBean.sub_labels.add(0, creatLabel(parentLabelBean.label));
                            labesTemp.add(parentLabelBean);
                        }

                    } else {
                        //  其他分类,补全二级 全部其他   其他不要

                        if (!"其他".equals(groups.get(0).labels.get(i).label)) {
                            SearchResultBean.ParentLabelBean parentLabelBean = new SearchResultBean.ParentLabelBean();
                            parentLabelBean.label = groups.get(0).labels.get(i).label;
                            parentLabelBean.noSecond = true;
                            SearchResultBean.LabelsBean labelsBean = new SearchResultBean.LabelsBean();
                            labelsBean.label = parentLabelBean.label;
                            ArrayList<SearchResultBean.LabelsBean> labelsBeenlist = new ArrayList<>();
                            labelsBeenlist.add(labelsBean);
                            parentLabelBean.sub_labels = labelsBeenlist;
                            labesTemp.add(parentLabelBean);
                        }
                    }
                }
            }
            groupBean.labels = labesTemp;
        }
        groups.remove(0);
        groups.add(0, groupBean);
        if (HaiUtils.getSize(groupBean.labels) == 0) {   // 没有分类数据 则不显示该筛选板块
            mFilterPositions[0] = -2;
        }

        if (groups.get(1) != null) {    // 品牌
            groups.set(1, initBrandOrSellerData(groups.get(1).labels, "brand", null));
        }

        if (groups.get(2) != null) {    // 商家
            groups.set(2, initBrandOrSellerData(groups.get(2).labels, "sellerName", null));
        }

        // 价格
        SearchResultBean.GroupBean priceGroup = new SearchResultBean.GroupBean();
        priceGroup.property = "price";
        groups.add(priceGroup);


        mGroups = groups;
    }

    // 包装商家和品牌数据
    public SearchResultBean.GroupBean initBrandOrSellerData(ArrayList<SearchResultBean.ParentLabelBean> labels, String property
            , ArrayList<SearchResultBean.LabelsBean> labelsSeleted) {

        ArrayList<SearchResultBean.ParentLabelBean> brandOrSeller = new ArrayList<>();
        SearchResultBean.GroupBean                  groupBean     = new SearchResultBean.GroupBean();

        if (labels != null) {
            for (int i = 0; i < letterArry.length; i++) {
                SearchResultBean.ParentLabelBean parentLabelBean = new SearchResultBean.ParentLabelBean();
                parentLabelBean.label = letterArry[i];
                ArrayList<SearchResultBean.LabelsBean> labelsBeanArrayList = new ArrayList<>();
                for (int j = 0; j < labels.size(); j++) {
                    String label = labels.get(j).label;
                    if (i == 26) {
                        if (!checkLetter(label.substring(0, 1))) {
                            SearchResultBean.LabelsBean labelsBean = new SearchResultBean.LabelsBean();
                            labelsBean.label = label;
                            labelsBean.document_count = labels.get(j).document_count;
                            if (labelsSeleted != null) {
                                for (int k = 0; k < labelsSeleted.size(); k++) {
                                    if (property.equals(typeToProperty(labelsSeleted.get(k).type)) && label.equals(labelsSeleted.get(k).label)) {
                                        labelsBean.isChecked = true;
                                    }
                                }
                            }
                            labelsBeanArrayList.add(labelsBean);
                        }
                    } else {
                        String key = letterArry[i];
                        if (label.toUpperCase().startsWith(key)) {
                            SearchResultBean.LabelsBean labelsBean = new SearchResultBean.LabelsBean();
                            labelsBean.label = label;
                            labelsBean.document_count = labels.get(j).document_count;
                            if (labelsSeleted != null) {
                                for (int k = 0; k < labelsSeleted.size(); k++) {
                                    if (property.equals(typeToProperty(labelsSeleted.get(k).type)) && label.equals(labelsSeleted.get(k).label)) {
                                        labelsBean.isChecked = true;
                                    }
                                }
                            }
                            labelsBeanArrayList.add(labelsBean);
                        }
                    }
                }
                parentLabelBean.sub_labels = labelsBeanArrayList;
                brandOrSeller.add(parentLabelBean);

            }
        }
        groupBean.property = property;
        groupBean.labels = brandOrSeller;

        return groupBean;
    }

    private String typeToProperty(int type) {
        switch (type) {
            case 1:
                return "category";
            case 2:
                return "brand";
            case 3:
                return "sellerName";
            case 4:
                return "price";
        }
        return "";
    }

    //  列表页分类标签改变后商家 品牌 相应改变  但要保留选中状态
    public void changeGroup(ArrayList<SearchResultBean.GroupBean> groups, ArrayList<SearchResultBean.LabelsBean> labels) {
        // 分类
        if (mGroups.get(0) != null) {
            mGroups.get(0).isChecked = false;
            if (HaiUtils.getSize(mGroups.get(0).labels) > 0) {
                for (int i = 0; i < mGroups.get(0).labels.size(); i++) {
                    if (mGroups.get(0).labels.get(i).isChecked) {
                        mGroups.get(0).labels.get(i).isChecked = false;
                        if (HaiUtils.getSize(mGroups.get(0).labels.get(i).sub_labels) > 0) {
                            for (int j = 0; j < mGroups.get(0).labels.get(i).sub_labels.size(); j++) {
                                if (mGroups.get(0).labels.get(i).sub_labels.get(j).isChecked) {
                                    mGroups.get(0).labels.get(i).sub_labels.get(j).isChecked = false;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (groups.get(1) != null) {    // 品牌
            mGroups.set(1, initBrandOrSellerData(groups.get(1).labels, "brand", labels));
        }

        if (groups.get(2) != null) {    // 商家
            mGroups.set(2, initBrandOrSellerData(groups.get(2).labels, "sellerName", labels));
        }

    }

    //  筛选完后返回的结果
    public void upDataGroup(ArrayList<SearchResultBean.GroupBean> groups) {
        mGroups = groups;
    }

    // 获取被选中的筛选项，用于显示搜索结果页顶部已选中标签栏数据源
    public ArrayList<SearchResultBean.LabelsBean> getSelected(ArrayList<SearchResultBean.GroupBean> groups) {
        if (selectedlist != null) {
            selectedlist.clear();
        }

        for (int k = 0; k < 3; k++) {
            if (mGroups.get(k).labels != null) {
                for (int i = 0; i < mGroups.get(k).labels.size(); i++) {
                    if (HaiUtils.getSize(mGroups.get(k).labels.get(i).sub_labels) > 0) {
                        for (int j = 0; j < mGroups.get(k).labels.get(i).sub_labels.size(); j++) {
                            if (mGroups.get(k).labels.get(i).sub_labels.get(j).isChecked) {
                                SearchResultBean.LabelsBean labelsBean = new SearchResultBean.LabelsBean();
                                labelsBean.type = k + 1;
                                if (k == 0) {
                                    //                                    labelsBean.label = TextUtils.isEmpty(mGroups.get(k).labels.get(i).parentLabel)
                                    //                                            ? mGroups.get(k).labels.get(i).label + "/" + mGroups.get(k).labels.get(i).sub_labels.get(j).label
                                    //                                            : mGroups.get(k).labels.get(i).parentLabel + "/" + mGroups.get(k).labels.get(i).label + "/" + mGroups.get(k).labels.get(i).sub_labels.get(j).label;
                                    labelsBean.label = mGroups.get(k).labels.get(i).sub_labels.get(j).label;
                                } else {
                                    labelsBean.label = mGroups.get(k).labels.get(i).sub_labels.get(j).label;
                                }
                                int[] pos = new int[]{i, j};
                                labelsBean.pos = pos;

                                selectedlist.add(labelsBean);
                            }
                        }
                    }
                }
            }
        }

        // 价格
        if (mGroups.get(3).isChecked) {
            SearchResultBean.LabelsBean labelsBean = new SearchResultBean.LabelsBean();
            labelsBean.type = 4;

            String price_s = "";
            String price_e = "";

            if (TextUtils.isEmpty(priceFilter_s) || TextUtils.isEmpty(priceFilter_e)) {
                price_s = TextUtils.isEmpty(priceFilter_s) ? priceFilter_e : priceFilter_s;
                price_e = TextUtils.isEmpty(priceFilter_s) ? "以下" : "以上";
            } else {
                price_s = priceFilter_s + "-";
                price_e = priceFilter_e;
            }

            labelsBean.label = "¥" + price_s + price_e;
            selectedlist.add(labelsBean);
        }

        return selectedlist;
    }

    // 创建一个全部开头的标签
    private SearchResultBean.LabelsBean creatLabel(String label) {
        SearchResultBean.LabelsBean labelsBean = new SearchResultBean.LabelsBean();
        labelsBean.label = "全部" + label;
        labelsBean.noThrid = true;
        return labelsBean;
    }

    //  点击删除已选择标签后更新相应数据被选择状态,并判断是否还有其他选中的
    public boolean upData(int position, int[] pos) {
        int     selectedNum = 0;
        boolean updataed    = false;

        if (position == 3) { // 价格
            mGroups.get(position).isChecked = false;
            priceFilter_s = "";
            priceFilter_e = "";
            return false;
        }

        if (mGroups.get(position).labels != null) {
            for (int i = 0; i < mGroups.get(position).labels.size(); i++) {
                if (HaiUtils.getSize(mGroups.get(position).labels.get(i).sub_labels) > 0) {
                    for (int j = 0; j < mGroups.get(position).labels.get(i).sub_labels.size(); j++) {
                        if (mGroups.get(position).labels.get(i).sub_labels.get(j).isChecked) {
                            if (i == pos[0] && j == pos[1]) {
                                mGroups.get(position).labels.get(i).sub_labels.get(j).isChecked = false;
                                updataed = true;
                            } else {
                                selectedNum++;
                                if (selectedNum > 0 && updataed) {
                                    return true;
                                }

                            }
                        }
                    }
                }
            }
        }
        if (selectedNum == 0) {
            mGroups.get(position).isChecked = false;
        }
        return selectedNum > 0 ? true : false;
    }

    public boolean isQuery() {
        return mIsQuery;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTopic() {
        return mTopic;
    }

    public ArrayList<SearchResultBean.GroupBean> getGroups() {
        return mGroups;
    }

    public String getGroupsJson() {
        SearchResultBean searchResultBean = new SearchResultBean();
        searchResultBean.group = mGroups;
        return new Gson().toJson(searchResultBean);
    }

    public int[] getFilterPositions() {
        return mFilterPositions;
    }

    public void clearFilterPositions() {
        for (int i = 0; i < mFilterPositions.length; i++) {
            mFilterPositions[i] = -1;
        }
    }

    /**
     * 验证26字母
     *
     * @param letter
     * @return
     */
    private boolean checkLetter(String letter) {
        String regex = "^[A-Za-z]+$";
        return check(letter, regex);
    }

    private boolean check(String str, String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    boolean flag;

    // 获取筛选默认条件(搜索专题)
    private void getSpecialQueryParam(SearchSpecialBean.SearchSpecialQueryBean queryBean, TreeMap<String, Object> paramMap) {
        if (queryBean == null) {
            return;
        }
        if (queryBean.categories != null && queryBean.categories.length > 0) {
            String category = "";
            for (int i = 0; i < queryBean.categories.length; i++) {
                if (TextUtils.isEmpty(category)) {
                    category = queryBean.categories[i];
                } else {
                    category = category + "@@" + queryBean.categories[i];
                }
            }
            defaultCategory = category;
            paramMap.put("category", category);
        }

        if (queryBean.brands != null && queryBean.brands.length > 0) {
            String brand = "";
            for (int i = 0; i < queryBean.brands.length; i++) {
                if (TextUtils.isEmpty(brand)) {
                    brand = queryBean.brands[i];
                } else {
                    brand = brand + "@@" + queryBean.brands[i];
                }
            }
            defaultBrand = brand;
            paramMap.put("brand", brand);
        }

        if (queryBean.sellers != null && queryBean.sellers.length > 0) {
            String seller = "";
            for (int i = 0; i < queryBean.sellers.length; i++) {
                if (TextUtils.isEmpty(seller)) {
                    seller = queryBean.sellers[i];
                } else {
                    seller = seller + "@@" + queryBean.sellers[i];
                }
            }
            defaultSeller = seller;
            paramMap.put("seller", seller);
        }
    }

    // 获取筛选默认条件(商家／ 品牌)
    private void getQueryParam(QueryBean queryBean, TreeMap<String, Object> paramMap) {
        if (queryBean == null) {
            return;
        }
        if (queryBean.query != null) {
            paramMap.put("query", queryBean.query);
        }
        if (queryBean.category != null) {
            defaultCategory = queryBean.category;
            paramMap.put("category", queryBean.category);
        }
        if (queryBean.brand != null) {
            defaultBrand = queryBean.brand;
            paramMap.put("brand", queryBean.brand);
        }
        if (queryBean.seller != null) {
            defaultSeller = queryBean.seller;
            paramMap.put("seller", queryBean.seller);
        }
    }


    /**
     * 二次搜索,更换搜索关键词
     *
     * @param topic
     */
    public void setTopic(String topic) {
        mTopic = topic;
    }

    public String getTopic(String topic) {
        return mTopic;
    }

    //  获取最高价和最低价   如果最高价低于最低价  则取反    反之亦然
    public String getPriceFilter_s() {
        if (TextUtils.isEmpty(priceFilter_s)) {
            return priceFilter_s;
        } else {

            if (TextUtils.isEmpty(priceFilter_s)) {
                return priceFilter_s;
            } else {
                if (Integer.parseInt(priceFilter_s) < Integer.parseInt(priceFilter_e)) {
                    return priceFilter_s;
                } else {
                    return priceFilter_e;
                }
            }
        }
    }

    public String getPriceFilter_e() {
        if (TextUtils.isEmpty(priceFilter_e)) {
            return priceFilter_e;
        } else {

            if (TextUtils.isEmpty(priceFilter_s)) {
                return priceFilter_e;
            } else {
                if (Integer.parseInt(priceFilter_s) < Integer.parseInt(priceFilter_e)) {
                    return priceFilter_e;
                } else {
                    return priceFilter_s;
                }
            }
        }
    }

}
