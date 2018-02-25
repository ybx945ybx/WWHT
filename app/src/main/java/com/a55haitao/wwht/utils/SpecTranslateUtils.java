package com.a55haitao.wwht.utils;

/**
 * Created by Haotao_Fujie on 16/10/10.
 */

public class SpecTranslateUtils {

    public static String translateToChinese(String spec) {

        String lowerspec = spec.toLowerCase();
        String lowerCaseStr = "";
        if (lowerspec.startsWith("_")){
            lowerCaseStr = lowerspec.replace("_", "");
        }else {
            lowerCaseStr = lowerspec;
        }

        if (lowerCaseStr.equals("color")) {
            return "颜色";
        }else if (lowerCaseStr.equals("size")) {
            return "尺寸";
        }else if (lowerCaseStr.equals("length")) {
            return "长度";
        }else if (lowerCaseStr.equals("width")) {
            return "宽度";
        }else if (lowerCaseStr.equals("uk size")) {
            return "英码";
        }else if (lowerCaseStr.equals("height")) {
            return "高度";
        }else if (lowerCaseStr.equals("chest")) {
            return "胸围";
        }else if (lowerCaseStr.equals("inseam")) {
            return "裤长";
        }else if (lowerCaseStr.equals("choice_group")) {
            return "款式";
        }else if (lowerCaseStr.equals("style")) {
            return "式样";
        }else if (lowerCaseStr.equals("shade")) {
            return "阴影";
        }else if (lowerCaseStr.equals("waist")) {
            return "腰围";
        }else if (lowerCaseStr.equals("band size")) {
            return "胸围";
        }else if (lowerCaseStr.equals("cup size")) {
            return "罩杯";
        }else if (lowerCaseStr.equals("standard")) {
            return "规格";
        }else if (lowerCaseStr.equals("specialsizetype")) {
            return "特款";
        }else if (lowerCaseStr.equals("customerpackagetype")) {
            return "包装";
        }
        return spec;
    }

}
