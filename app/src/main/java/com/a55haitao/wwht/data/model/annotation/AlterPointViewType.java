package com.a55haitao.wwht.data.model.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Comment;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Follow;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Like;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_None;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Pray;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_SelectPost;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_SendPost;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_Share;
import static com.a55haitao.wwht.data.model.annotation.AlterPointViewType.AlterPointViewType_UploadAvatar;

/**
 * Created by Haotao_Fujie on 2016/11/14.
 */
@IntDef({AlterPointViewType_None, AlterPointViewType_Pray, AlterPointViewType_Like, AlterPointViewType_Follow, AlterPointViewType_Comment, AlterPointViewType_Share, AlterPointViewType_SendPost, AlterPointViewType_SelectPost, AlterPointViewType_UploadAvatar})
@Retention(RetentionPolicy.SOURCE)
public @interface AlterPointViewType {

    // 积分
    int AlterPointViewType_None = 0;         // 没有图
    int AlterPointViewType_Pray = 1;         // 点赞商品，加入心愿单
    int AlterPointViewType_Like = 2;         // 点赞
    int AlterPointViewType_Follow = 3;       // 关注
    int AlterPointViewType_Comment = 4;      // 评论
    int AlterPointViewType_Share = 5;        // 分享
    int AlterPointViewType_SendPost = 6;     // 发帖
    int AlterPointViewType_SelectPost = 7;   // 笔记加精
    int AlterPointViewType_UploadAvatar = 8; // 更新头像
}
