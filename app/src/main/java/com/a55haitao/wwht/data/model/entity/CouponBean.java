package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.a55haitao.wwht.utils.HaiTimeUtils;

/**
 * Created by 55haitao on 2016/11/6.
 */

public class CouponBean implements Parcelable {
    private int status;
    private String code;
    private String subtitle;
    private int uid;
    private String title;
    private String value;
    private long begintime;
    private String images;
    private long endtime;
    private String type;
    private int id;
    private String show_desc ;
    private String show_title ;
    private boolean isSelect ;

    // 优惠劵计算后实际优惠价格, 用于计算佣金使用
    private boolean maxDiscount;

    public boolean isMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(boolean maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getShow_desc() {
        return show_desc;
    }

    public void setShow_desc(String show_desc) {
        this.show_desc = show_desc;
    }

    public String getShow_title() {
        return show_title;
    }

    public void setShow_title(String show_title) {
        this.show_title = show_title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getBegintime() {
        return begintime;
    }

    public void setBegintime(long begintime) {
        this.begintime = begintime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public String getDuration() {
        return HaiTimeUtils.showDate(getBegintime()) + "-" + HaiTimeUtils.showDate(getEndtime());
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CouponBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.code);
        dest.writeString(this.subtitle);
        dest.writeInt(this.uid);
        dest.writeString(this.title);
        dest.writeString(this.value);
        dest.writeLong(this.begintime);
        dest.writeString(this.images);
        dest.writeLong(this.endtime);
        dest.writeString(this.type);
        dest.writeInt(this.id);
        dest.writeString(this.show_desc);
        dest.writeString(this.show_title);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected CouponBean(Parcel in) {
        this.status = in.readInt();
        this.code = in.readString();
        this.subtitle = in.readString();
        this.uid = in.readInt();
        this.title = in.readString();
        this.value = in.readString();
        this.begintime = in.readLong();
        this.images = in.readString();
        this.endtime = in.readLong();
        this.type = in.readString();
        this.id = in.readInt();
        this.show_desc = in.readString();
        this.show_title = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<CouponBean> CREATOR = new Creator<CouponBean>() {
        @Override
        public CouponBean createFromParcel(Parcel source) {
            return new CouponBean(source);
        }

        @Override
        public CouponBean[] newArray(int size) {
            return new CouponBean[size];
        }
    };
}
