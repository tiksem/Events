package com.azazai.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Icon implements Parcelable {
    public int mediaId;
    public String tag;

    public String getUrl() {
        return "http://azazai.com/icon/" + mediaId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mediaId);
        dest.writeString(this.tag);
    }

    public Icon() {
    }

    protected Icon(Parcel in) {
        this.mediaId = in.readInt();
        this.tag = in.readString();
    }

    public static final Parcelable.Creator<Icon> CREATOR = new Parcelable.Creator<Icon>() {
        @Override
        public Icon createFromParcel(Parcel source) {
            return new Icon(source);
        }

        @Override
        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };
}
