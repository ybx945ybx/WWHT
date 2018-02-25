package com.a55haitao.wwht.data.model.jsobject;

/**
 * Created by Haotao_Fujie on 2016/11/22.
 */

/**
 * Type 6
 * { type:6, data:{start:1, message:}}
 */
public class JSObjectType6 extends BaseJSObject {


    public DataObject data;

    public class DataObject {
        public boolean start;
        public String message;
    }
}