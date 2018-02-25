package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.DailyProductBean;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/9/7.
 */

public class DailyProductResult {
    public ArrayList<DailyProductBean> products;
    public int                         count;
    public PagenationBean              pagenation;

    public class PagenationBean {
        public int page;
        public int count;
        public int allpage;
    }
}
