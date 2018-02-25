package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 55haitao on 2016/11/5.
 */

public class AddressItemBean implements Parcelable {
    public String province;
    public String city;
    public String dist;
    public String idt_number;
    public String upload_idt_img;
    public int is_default;
    public String phone;
    public String street;
    public int id;
    public String name;

    //收货地址详情需要的字段
    public int uid;
    public String identCardBackImage;
    public String identCardFrontImage;

    public AddressItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.dist);
        dest.writeString(this.idt_number);
        dest.writeString(this.upload_idt_img);
        dest.writeInt(this.is_default);
        dest.writeString(this.phone);
        dest.writeString(this.street);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.uid);
        dest.writeString(this.identCardBackImage);
        dest.writeString(this.identCardFrontImage);
    }

    protected AddressItemBean(Parcel in) {
        this.province = in.readString();
        this.city = in.readString();
        this.dist = in.readString();
        this.idt_number = in.readString();
        this.upload_idt_img = in.readString();
        this.is_default = in.readInt();
        this.phone = in.readString();
        this.street = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.uid = in.readInt();
        this.identCardBackImage = in.readString();
        this.identCardFrontImage = in.readString();
    }

    public static final Parcelable.Creator<AddressItemBean> CREATOR = new Parcelable.Creator<AddressItemBean>() {
        @Override
        public AddressItemBean createFromParcel(Parcel source) {
            return new AddressItemBean(source);
        }

        @Override
        public AddressItemBean[] newArray(int size) {
            return new AddressItemBean[size];
        }
    };
}
