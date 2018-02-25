package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_AddFriend;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Back;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Camera;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Cart;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_More;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Null;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_PO;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Search;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Service;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Setting;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Share;
import static com.a55haitao.wwht.data.model.annotation.NavBarType.NavBarType_Text;

/**
 * Created by Haotao_Fujie on 2016/11/22.
 */

@IntDef({NavBarType_Null,NavBarType_AddFriend,NavBarType_Back,NavBarType_Camera,NavBarType_Cart,NavBarType_More,NavBarType_PO,NavBarType_Search,NavBarType_Setting,NavBarType_Share,NavBarType_Service,NavBarType_Text})
@Retention(RetentionPolicy.SOURCE)
public @interface NavBarType {
    // 没有按钮（null）
    int NavBarType_Null = 0;
    // 添加好友
    int NavBarType_AddFriend = 4;
    // 返回
    int NavBarType_Back = 2;
    // 相机
    int NavBarType_Camera = 7;
    // 购物车
    int NavBarType_Cart = 3;
    // 更多
    int NavBarType_More = 6;
    // 发帖
    int NavBarType_PO = 10;
    // 查找
    int NavBarType_Search = 5;
    // 设置
    int NavBarType_Setting = 9;
    // 分享
    int NavBarType_Share = 8;
    // 客服
    int NavBarType_Service = 11;
    // 文本
    int NavBarType_Text = 1;
}
