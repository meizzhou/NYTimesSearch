package com.example.sarahz.nytimessearch.modals;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sarahz on 2/2/17.
 */

public class Filter implements Parcelable {
    String dateValue;

    public String getDateValue() {
        return dateValue;
    }

    public String getSortOrderValue() {
        return sortOrderValue;
    }

    public boolean isArts() {
        return isArts;
    }

    public boolean isFashionAndStyle() {
        return isFashionAndStyle;
    }

    public boolean isSports() {
        return isSports;
    }

    String sortOrderValue;
    boolean isArts;
    boolean isFashionAndStyle;
    boolean isSports;

    public Filter() {
        dateValue = null;
        sortOrderValue = null;
        isArts = false;
        isFashionAndStyle = false;
        isSports = false;
    }

    public Filter(String dateValue, String sortOrderValue, boolean isArts, boolean isFashionAndStyle, boolean isSports) {
        this.dateValue = dateValue;
        this.sortOrderValue = sortOrderValue;
        this.isArts = isArts;
        this.isFashionAndStyle = isFashionAndStyle;
        this.isSports = isSports;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dateValue);
        dest.writeString(this.sortOrderValue);
        dest.writeByte(this.isArts ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFashionAndStyle ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSports ? (byte) 1 : (byte) 0);
    }

    protected Filter(Parcel in) {
        this.dateValue = in.readString();
        this.sortOrderValue = in.readString();
        this.isArts = in.readByte() != 0;
        this.isFashionAndStyle = in.readByte() != 0;
        this.isSports = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
