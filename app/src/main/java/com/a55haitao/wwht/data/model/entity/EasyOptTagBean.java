package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by drolmen on 2016/12/26.
 */

public class EasyOptTagBean implements Parcelable {


    /**
     * status : 1
     * create_dt : 1482299968
     * name : 其他
     * weight : 0
     * content :
     * image_url :
     * is_hot : 0
     * id : 1
     */

    public String name;
    public int id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public EasyOptTagBean() {
    }

    protected EasyOptTagBean(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<EasyOptTagBean> CREATOR = new Parcelable.Creator<EasyOptTagBean>() {
        @Override
        public EasyOptTagBean createFromParcel(Parcel source) {
            return new EasyOptTagBean(source);
        }

        @Override
        public EasyOptTagBean[] newArray(int size) {
            return new EasyOptTagBean[size];
        }
    };
}
