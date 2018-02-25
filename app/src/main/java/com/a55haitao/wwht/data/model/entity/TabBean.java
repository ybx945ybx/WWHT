package com.a55haitao.wwht.data.model.entity;

import android.support.design.widget.TabLayout;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 董猛 on 2016/10/9.
 */

public class TabBean {

    public ArrayList<Tab> tabs ;

    public static class Tab{
        public String name;
        public String uri;
        public int tab_id;
    }
}
