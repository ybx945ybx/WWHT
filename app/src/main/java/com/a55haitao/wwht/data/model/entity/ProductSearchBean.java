package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by Haotao_Fujie on 16/10/26.
 */

public class ProductSearchBean extends PageBean{
    public ArrayList<ProductGroup> group;
    public ArrayList<ProductBaseBean> products;

    public class ProductGroup {
        public String property;
        public ArrayList<ProductGroupBase> labels;

    }

    public class ProductGroupBase {

        public String label;
        public ArrayList<ProductGroupBase> sub_labels;
    }
}
