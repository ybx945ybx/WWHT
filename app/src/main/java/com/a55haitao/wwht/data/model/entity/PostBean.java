package com.a55haitao.wwht.data.model.entity;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.data.model.annotation.PostFragmentLayoutType;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子实体类
 *
 * @author 陶声
 * @since 2016-11-07
 */

public class PostBean implements MultiItemEntity {
    public int                   create_dt;
    public String                one_word;
    public int                   region_id;
    public boolean               is_liked;
    public ShareBean             share;
    public String                location_desc;
    public String                content;
    public int                   post_id;
    public int                   reply_count;
    public String                image_url;
    public int                   like_count;
    public OwnerBean             owner;
    public boolean               is_following;
    public ArrayList<ImagesBean> images;
    public ArrayList<TagBean>    tag_list;


    @Override
    @PostFragmentLayoutType
    public int getItemType() {
        return HaiApplication.layoutType;
        //        return PostFragmentLayoutType.DOUBLE;
    }

    public static class OwnerBean {
        public String          username;
        public int             following_count;
        public int             post_count;
        public String          ucenter_token;
        public int             id;
        public int             membership_point;
        public int             is_operation;
        public int             sex;
        public String          nickname;
        public int             like_count;
        public boolean         is_following;
        public String          location;
        public String          signature;
        public int             follower_count;
        public String          head_img;
        public String          email;
        public List<UserTitle> user_title;
    }
}
