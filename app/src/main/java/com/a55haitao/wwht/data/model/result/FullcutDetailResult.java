package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.ProductSimpleBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/7/12.
 */

public class FullcutDetailResult {
    public FullcutManage                       f_manage;
    public ArrayList<ShoppingCartBean.CutData> f_cutlist;
    public ArrayList<SelectData>               select;
    public SelectProds                         select_prods;
    public Pageinfo                            pageinfo;

    public class Pageinfo {
        public int allpage;
        public int page;
    }

    public class FullcutManage {
        public String code;
        public long   end_time;
        public int    id;
        public int    ltype;
        public String note_msg;
        public String promotion_msg;
        public String rtype;
        public long   start_time;
        public String title;
        public long   update_time;

    }

    public class SelectData {
        public String brand;
        public String fid;
        public int    id;
        public String seller;

    }

    public class SelectProds {
        public int                          count;
        public ArrayList<ProductSimpleBean> prods;
    }
}
