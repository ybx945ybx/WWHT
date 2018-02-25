package com.a55haitao.wwht.data.model.entity;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/6/14.
 */

public class OrderLogsBean {
    public int                                  infoType;           // 0:pay-order/1:submit-order
    public String                               serialNumber;       // 流水号,会变
    public String                               source;             // 预留字段
    public String                               orderid;
    public int                                  paystatus;          // 0 未付款,1付款失败，2付款成功
    public int                                  payType;            // 0:微信；1：支付宝；2：佣金
    public long                                 paytimestamp;
    public long                                 ordertimestamp;
    public ArrayList<String> skulist;

}
