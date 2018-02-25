package com.a55haitao.wwht.data.interfaces;

/**
 * Created by a55 on 2017/11/1.
 */

public interface FileNameGenerator {

    /** Generates unique file name for image defined by URI */
    String generate(String imageUri);
}
