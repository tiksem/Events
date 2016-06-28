package com.azazai.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by CM on 6/21/2015.
 */
public class Event implements Parcelable {
    public long id;
    public String name;
    public String description;
    public long userId;
    public String address;
    public int peopleNumber;
    public int subscribersCount;
    public int date;
    public boolean isPrivate;
    @JsonIgnore
    public boolean isSubscribed;

    @JsonIgnore
    public boolean isCanceled;

    public Event() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeLong(this.userId);
        dest.writeString(this.address);
        dest.writeInt(this.peopleNumber);
        dest.writeInt(this.subscribersCount);
        dest.writeInt(this.date);
        dest.writeByte(this.isPrivate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSubscribed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCanceled ? (byte) 1 : (byte) 0);
    }

    protected Event(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.userId = in.readLong();
        this.address = in.readString();
        this.peopleNumber = in.readInt();
        this.subscribersCount = in.readInt();
        this.date = in.readInt();
        this.isPrivate = in.readByte() != 0;
        this.isSubscribed = in.readByte() != 0;
        this.isCanceled = in.readByte() != 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
