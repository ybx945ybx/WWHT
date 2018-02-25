package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Haotao_Fujie on 16/10/10.
 */

public class QueryBean implements Parcelable {

    public String query;
    public String brand;
    public String seller;
    public String category;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.query);
        dest.writeString(this.brand);
        dest.writeString(this.seller);
        dest.writeString(this.category);
    }

    public QueryBean() {
    }

    protected QueryBean(Parcel in) {
        this.query = in.readString();
        this.brand = in.readString();
        this.seller = in.readString();
        this.category = in.readString();
    }

    public static final Parcelable.Creator<QueryBean> CREATOR = new Parcelable.Creator<QueryBean>() {
        @Override
        public QueryBean createFromParcel(Parcel source) {
            return new QueryBean(source);
        }

        @Override
        public QueryBean[] newArray(int size) {
            return new QueryBean[size];
        }
    };
}
