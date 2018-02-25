package com.a55haitao.wwht.data.model.entity;

/**
 * Created by a55 on 2017/6/14.
 */

public class AssuranceBean {
    public int rtype;         // 0 false   1  true
    public int id;
    public String title;

    public boolean getRtype() {
        return rtype == 0 ? false : true;
    }
}
