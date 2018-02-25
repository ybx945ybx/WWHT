package com.a55haitao.wwht.data.model.result;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/6/14.
 */

public class RelateSpecialsResult {
    public ArrayList<SpecialsBean> specials;
    public ArrayList<PostsBean> posts;

    public class SpecialsBean{
        public String image;
        public String uri;

    }

    public class PostsBean{
        public String content;
        public int id;
        public ArrayList<ImageUrlBean> image_url;

    }

    public class ImageUrlBean{
        public float  wh_rate;
        public String url;

    }
}
