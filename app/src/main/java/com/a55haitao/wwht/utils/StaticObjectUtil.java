package com.a55haitao.wwht.utils;

import java.util.HashMap;

/**
 * Created by Haotao_Fujie on 2016/11/4.
 */

public class StaticObjectUtil {
    private static HashMap<String,String> sizes;
    // private static HashMap<String,String> squareFlags;
    // private static HashMap<String,String> waveFlags;

    static {
        // 尺码
        sizes = new HashMap<String,String>();
        sizes.put("78" , "men_belts");
        sizes.put("94" , "men_belts");
        sizes.put("137" , "men_bottom");
        sizes.put("138" , "men_up");
        sizes.put("138" , "men_up");
        sizes.put("139" , "men_up");
        sizes.put("140" , "men_up");
        sizes.put("141" , "men_up");
        sizes.put("142" , "men_bottom");
        sizes.put("143" , "men_up");
        sizes.put("144" , "men_up");
        sizes.put("145" , "men_up");
        sizes.put("146" , "men_up");
        sizes.put("147" , "men_up");
        sizes.put("149" , "men_up");
        sizes.put("150" , "men_bottom");
        sizes.put("151" , "men_up");
        sizes.put("152" , "men_up");
        sizes.put("153" , "men_up");
        sizes.put("211" , "children");
        sizes.put("212" , "children");
        sizes.put("213" , "children_shoes");
        sizes.put("214" , "children");
        sizes.put("215" , "children");
        sizes.put("216" , "children");
        sizes.put("217" , "children");
        sizes.put("254" , "women_shoes");
        sizes.put("255" , "women_shoes");
        sizes.put("256" , "women_shoes");
        sizes.put("257" , "women_shoes");
        sizes.put("258" , "women_shoes");
        sizes.put("259" , "women_shoes");
        sizes.put("260" , "women_shoes");
        sizes.put("261" , "women_shoes");
        sizes.put("262" , "women_shoes");
        sizes.put("264" , "men_shoes");
        sizes.put("265" , "men_shoes");
        sizes.put("266" , "men_shoes");
        sizes.put("267" , "men_shoes");
        sizes.put("268" , "men_shoes");
        sizes.put("269" , "men_shoes");
        sizes.put("270" , "men_shoes");
        sizes.put("272" , "children_shoes");
        sizes.put("273" , "children_shoes");
        sizes.put("274" , "children_shoes");
        sizes.put("276" , "women_up");
        sizes.put("277" , "women_up");
        sizes.put("278" , "women_bottom");
        sizes.put("279" , "women_up");
        sizes.put("280" , "women_bottom");
        sizes.put("281" , "women_up");
        sizes.put("282" , "women_up");
        sizes.put("283" , "women_up");
        sizes.put("284" , "women_up");
        sizes.put("285" , "women_up");
        sizes.put("286" , "women_up");
        sizes.put("287" , "women_bottom");
        sizes.put("288" , "women_up");
        sizes.put("289" , "women_bottom");
        sizes.put("290" , "women_bottom");
        sizes.put("291" , "women_bottom");
        sizes.put("292" , "women_up");
        sizes.put("293" , "women_up");
        sizes.put("294" , "women_up");
        sizes.put("295" , "women_up");
        sizes.put("296" , "women_up");
        sizes.put("297" , "women_up");
    }

    public static String sizeForKey(String key) {
        String value = sizes.get(key);
        return value == null? "" : value;
    }
}
