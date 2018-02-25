package com.a55haitao.wwht.data.interfaces;

import com.a55haitao.wwht.data.model.entity.SearchResultBean;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by 55haitao on 2016/11/2.
 */

public interface SearchCcontact {

    interface SearchResultModel {
        /**
         * 执行搜索
         */
        Observable<SearchResultBean> doSearch(int currentPage);

        //        Observable<ResponseBody> getRecommend();

        Observable<ResponseBody> likeProduct();
    }

    public static interface SearchResultView {
        /**
         * 初始化搜索框
         */
        void initTitle();

        /**
         * 更新排序、筛选栏
         *
         * @param booleens
         */
        void updateBar(boolean... booleens);

        /**
         * 初始化筛选条件显示栏
         */
        void initTagLayout();

        /**
         * 更新筛选条件显示栏
         */
        //        void updateTagLayout() ;

        /**
         * 更新筛选条件显示栏
         */
        void updateTagLayout(int position);

        /**
         * 更新筛选条件显示栏显示栏
         */
        void updateConditionLayout();

        /**
         * 更新主界面，切换搜索结果和推荐
         */
        void updateMainLayout();
    }
}
