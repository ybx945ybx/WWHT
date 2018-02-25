package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by Haotao_Fujie on 16/10/12.
 */

public class SimilarBrandBean extends PageBean {

    public ArrayList<SimilarBrands> brands;

    public class SimilarBrands {
        public BrandBaseDesModel brand_info;
        public ArrayList<ProductBaseBean> products;
    }

    public class BrandBaseDesModel {

        public String desc;
        public String namecn;
        public String nameen;
        public String country;
        public boolean is_followed;
    }

}
