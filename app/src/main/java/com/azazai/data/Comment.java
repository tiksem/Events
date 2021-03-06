package com.azazai.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by CM on 7/7/2015.
 */
public class Comment implements Parcelable, VkUserHolderEntity {
    public long id;
    public long userId;
    public String text;
    public long eventId = -1;
    public int date;
    @JsonIgnore
    public String userName;
    @JsonIgnore
    public String avatar;

    public Comment() {
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUsernameAndAvatar(String userName, String avatar) {
        this.userName = userName;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.userId);
        dest.writeString(this.text);
        dest.writeLong(this.eventId);
        dest.writeInt(this.date);
        dest.writeString(this.userName);
        dest.writeString(this.avatar);
    }

    protected Comment(Parcel in) {
        this.id = in.readLong();
        this.userId = in.readLong();
        this.text = in.readString();
        this.eventId = in.readLong();
        this.date = in.readInt();
        this.userName = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
