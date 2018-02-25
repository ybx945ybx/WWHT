package com.a55haitao.wwht.data.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Haotao_Fujie on 2016/11/17.
 */

public class EnforceUpdateBean extends RealmObject{
    public String project_name;   // 项目名称

    public String change_desc;    // 升级描述
    public String platform;       // 平台

    public String now_ver_desc;   // 当前版本描述,v2.0以后用作最新版本的versionCode使用

    @PrimaryKey
    public String now_ver_num;    // 当前版本号

    public String low_ver_desc;   // 最低版本号(versionCode）,这个值一定可以转化为整形
    public String low_ver_num;    // 最低版本号（versionName）

    public String download_link;    // 下载地址

}
