package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import cn.finalteam.galleryfinal.model.BasePhotoInfo;

public class ImagesBean extends BasePhotoInfo implements Parcelable {
    public String url;
    public float  wh_rate;

    public ImagesBean() {
    }

    public ImagesBean(String url, float wh_rate) {
        this.url = url;
        this.wh_rate = wh_rate;
    }

    protected ImagesBean(Parcel in) {
        url = in.readString();
        wh_rate = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(url);
        dest.writeFloat(wh_rate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
        @Override
        public ImagesBean createFromParcel(Parcel in) {
            return new ImagesBean(in);
        }

        @Override
        public ImagesBean[] newArray(int size) {
            return new ImagesBean[size];
        }
    };

    @Override
    public String toString() {
        return "ImagesBean{" +
                "small_icon='" + url + '\'' +
                ", wh_rate=" + wh_rate +
                '}';
    }
}