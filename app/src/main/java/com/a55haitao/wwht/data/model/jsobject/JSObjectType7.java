package com.a55haitao.wwht.data.model.jsobject;

/**
 * Created by Haotao_Fujie on 2016/11/22.
 */

/**
 * Type 7
 * { type:7, data:{type:1, message:}}
 */
public class JSObjectType7 extends BaseJSObject {

    public DataObject data;

    public class DataObject {
        public int type;
        public String message;
    }
}