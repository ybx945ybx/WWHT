package com.a55haitao.wwht.data.model.jsobject;

/**
 * Created by Haotao_Fujie on 2016/11/22.
 */

/**
 * Type 1
 * { type:1, data: {type:0,name:Nike,imgCover:可空,logo:可空}}
 */
public class JSObjectType1 extends BaseJSObject {
    public DataObject data;

    public class DataObject {
        public int type;
        public String name;
        public String imgCover;
        public String logo;
    }
}