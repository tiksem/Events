package com.khevents.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by CM on 7/7/2015.
 */
public class Comment implements Parcelable {
    public long userId;
    public String text;
    public int date;
    @JsonIgnore
    public String userName;
    @JsonIgnore
    public String avatar;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.text);
        dest.writeInt(this.date);
        dest.writeString(this.userName);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.userId = in.readLong();
        this.text = in.readString();
        this.date = in.readInt();
        this.userName = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
