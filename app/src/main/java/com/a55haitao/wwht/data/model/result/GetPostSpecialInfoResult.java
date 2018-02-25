package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.CommentBean;
import com.a55haitao.wwht.data.model.entity.ShareBean;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.data.model.annotation.SpecialDetailContentType;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 社区 - 专题详情
 *
 * @author 陶声
 * @since 2016-11-10
 */

public class GetPostSpecialInfoResult {

    public String             img_cover;
    public String             name;
    public int                deleted;
    public boolean            is_liked;
    public int                reply_count;
    public ShareBean          share;
    public int                modified_time;
    public int                special_id;
    public int                like_count;
    public int                created_time;
    public List<ContentBean>  content;
    public List<CommentBean>  comments;
    public List<UserListBean> likes;

    public static class ContentBean implements MultiItemEntity {
        public DataBean data;
        public int      type;

        @Override
        public int getItemType() {
            switch (type) {
                case 0:
                    return SpecialDetailContentType.TYPE_TEXT;
                case 1:
                    return SpecialDetailContentType.TYPE_IMG;
                case 2:
                    return SpecialDetailContentType.TYPE_PRODUCT;
                default:
                    break;
            }
            return -1;
        }

        public static class DataBean {
            public String      content;
            public ImgSizeBean imgSize;
            public float       real_price;
            public String      name;
            public String      spuid;
            public float       mall_price;
            public BrandBean   brand;
            public String      img_cover;
            public float       real_price_org;
            public float       price;
            public float       mall_price_org;
            public String      uri;

            public static class ImgSizeBean {
                public int width;
                public int height;
            }

            public static class BrandBean {
                public String name_en;
                public String name_cn;
                public String description;
                public String icon;
            }
        }
    }

}
