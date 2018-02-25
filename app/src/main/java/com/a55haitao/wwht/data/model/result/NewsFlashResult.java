package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.NewsFlashBean;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/3/21.
 */

public class NewsFlashResult {
    public ArrayList<NewsFlashBean> letters; //获取草单列表时使用

    public int page;
    public int allpage;
    public int count;
}
