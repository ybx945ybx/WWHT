package com.a55haitao.wwht.utils;

import com.a55haitao.wwht.data.model.entity.UserBean;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by a55 on 2017/5/22.
 */

public class BugLyUtils {

    public static void setUserId(UserBean userBean) {
        if (userBean == null) {
            CrashReport.setUserId("");
        } else {
            CrashReport.setUserId(String.valueOf(userBean.getId()));
        }
    }

}
