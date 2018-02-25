package com.a55haitao.wwht.data.model.entity;

import android.databinding.BaseObservable;

/**
 * Created by 董猛 on 2016/10/11.
 */

public class TabProductBean extends BaseObservable {

    /**
     * 非服务器返回数据，用于标识HotFragment 热卖单品和新品推荐两个接口返回数据
     */
    private int position ;
    public String coverImgUrl;
    public String name;
    public String spuid;
    public String image;
    public boolean is_star;
    public int star_count;
    public double mallPrice;
    public double realPrice;
    public String brand;
    public String desc;
    public SellerBean.SellerDescBaseBean sellerInfo ;

    /**
     * @return 0 ， 1
     * position 0 热卖单品
     * position 1 新品推荐
     * position 2 今日推荐
     */
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
