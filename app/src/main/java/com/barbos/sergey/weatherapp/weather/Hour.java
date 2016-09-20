package com.barbos.sergey.weatherapp.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sergey on 15.09.2016.
 */
public class Hour implements Parcelable{
    public long getTime() {
        return mTime;
    }

    public String getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date date = new Date(mTime * 1000);


        return formatter.format(date);
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getTemperature() {

        if (mTimezone.startsWith("Europe")) {
            tmpTemperature = (mTemperature - 32.0)/1.8;
        }
        return (int) Math.round(tmpTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    private long mTime;
    private String mSummary;
    private String mIcon;
    private double mTemperature;
    private String mTimezone;
    private double tmpTemperature;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mTime);
        parcel.writeString(mSummary);
        parcel.writeString(mIcon);
        parcel.writeDouble(mTemperature);
        parcel.writeString(mTimezone);
    }

    public Hour(Parcel parcel) {
        mTime = parcel.readLong();
        mSummary = parcel.readString();
        mIcon = parcel.readString();
        mTemperature = parcel.readDouble();
        mTimezone = parcel.readString();
    }

    public Hour(){}

    public final static Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel parcel) {
            return new Hour(parcel);
        }

        @Override
        public Hour[] newArray(int i) {
            return new Hour[i];
        }
    };
}
