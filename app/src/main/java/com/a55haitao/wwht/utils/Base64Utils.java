package com.a55haitao.wwht.utils;

import android.util.Base64;

import java.io.File;
import java.io.IOException;

import cn.finalteam.toolsfinal.io.FileUtils;

/**
 * Created by 董猛 on 16/7/17.
 */
public class Base64Utils {

    public static String fileToString(File file){

        String result = null ;

        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            result = Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result ;
    }

}
