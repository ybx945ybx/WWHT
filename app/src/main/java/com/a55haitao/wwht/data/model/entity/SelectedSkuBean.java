package com.a55haitao.wwht.data.model.entity;

import java.util.List;

/**
 * 规格信息
 *
 * @author 陶声
 * @since 2017-04-18
 */

public class SelectedSkuBean {
    public String          skuid;
    public String          spuid;
    public double          mallPriceOrg;
    public double          mallPrice;
    public int             inStock;
    public String          styleId;
    public double          realPrice;
    public double          realPriceOrg;
    public int             stock;
    public List<SkuValues> skuValues;

    public static class SkuValues {
        public String name;
        public String value;
    }
}
