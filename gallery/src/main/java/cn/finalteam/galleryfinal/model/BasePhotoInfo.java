package cn.finalteam.galleryfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * class description here
 *
 * @author 陶声
 * @since 2017-04-26
 */

public class BasePhotoInfo implements Parcelable {
    protected BasePhotoInfo(Parcel in) {
    }

    public static final Creator<BasePhotoInfo> CREATOR = new Creator<BasePhotoInfo>() {
        @Override
        public BasePhotoInfo createFromParcel(Parcel in) {
            return new BasePhotoInfo(in);
        }

        @Override
        public BasePhotoInfo[] newArray(int size) {
            return new BasePhotoInfo[size];
        }
    };

    public BasePhotoInfo() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
