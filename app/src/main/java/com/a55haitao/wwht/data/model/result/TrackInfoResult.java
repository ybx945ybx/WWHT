package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.ProductBaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by drolmen on 2016/11/17.
 */

public class TrackInfoResult {

    public ArrayList<PackageListBean> package_list;

    public static class PackageListBean {
        public String track_company;
        public String track_number;

        public ArrayList<TrackInfoBean>   track_info;
        public ArrayList<ProductBaseBean> product_list;

        public static class TrackInfoBean {
            public String content;
            public String date;
            public String code;
        }
    }
}
