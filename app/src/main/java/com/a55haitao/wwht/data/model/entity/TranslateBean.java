package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by Haotao_Fujie on 16/10/12.
 */

public class TranslateBean {

    public ArrayList<ArrayList<String>> tranText;

    public String getTranslatedTxt() {
        String ret = "";
        for (ArrayList<String> s : tranText) {
            for (String s1 : s) {
                ret += s1 + "\r\n";
            }
        }
        return ret;
    }

}
