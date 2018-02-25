package com.a55haitao.wwht.data.interfaces;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 董猛 on 16/7/22.
 */
public class OrderPayModel implements Parcelable {

    public static final String PAY_TYPE_ALI = "alipay";

    public static final String PAY_TYPE_WX = "wechat";

    /**
     * 交易名称
     */
    private String tradeName;

    /**
     * 交易内容
     */
    private String tradeBody;

    /**
     * 交易金额
     */
    private String tradeAmount;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 流水号
     */
    private String trandNo;

    private String payType;


    public OrderPayModel(String tradeName, String tradeBody) {
        //        this.tradeName = tradeName;
        //        this.tradeBody = tradeBody;
        // TODO: 2017/5/2 此处暂时写死
        this.tradeName = "55海淘-官网直购";
        this.tradeBody = "55海淘-官网直购";
    }

    public OrderPayModel() {

    }

    public String getTrandNo() {
        return trandNo;
    }

    public void setTrandNo(String trandNo) {
        this.trandNo = trandNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeName() {
        return tradeName;
    }

    public String getTradeBody() {
        return tradeBody;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tradeName);
        dest.writeString(this.tradeBody);
        dest.writeString(this.tradeAmount);
        dest.writeString(this.orderId);
        dest.writeString(this.trandNo);
        dest.writeString(this.payType);
    }

    protected OrderPayModel(Parcel in) {
        this.tradeName = in.readString();
        this.tradeBody = in.readString();
        this.tradeAmount = in.readString();
        this.orderId = in.readString();
        this.trandNo = in.readString();
        this.payType = in.readString();
    }

    public static final Creator<OrderPayModel> CREATOR = new Creator<OrderPayModel>() {
        @Override
        public OrderPayModel createFromParcel(Parcel source) {
            return new OrderPayModel(source);
        }

        @Override
        public OrderPayModel[] newArray(int size) {
            return new OrderPayModel[size];
        }
    };
}
