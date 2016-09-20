package com.barbos.sergey.weatherapp.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sergey on 15.09.2016.
 */
public class Day implements Parcelable{
    private String mSummary;
    private long mTime;
    private String mTimezone;
    private double mTemperatureMax;
    private double tmpTempatureMax;
    private String mIcon;


    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getTemperatureMax() {
        if (mTimezone.startsWith("Europe")) {
            tmpTempatureMax = (mTemperatureMax - 32.0)/1.8;
        }
        return (int) Math.round(tmpTempatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
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

    public String getDayOfTheWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date date = new Date(getTime() * 1000);

        return formatter.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //Упаковываем переменные в "посылку" :) Делаем наш класс Parcelable, или готовым к сериализации.
        parcel.writeString(mSummary);
        parcel.writeLong(mTime);
        parcel.writeString(mTimezone);
        parcel.writeDouble(mTemperatureMax);
        parcel.writeString(mIcon);
    }

    public Day(Parcel in) {
        mSummary = in.readString();
        mTime = in.readLong();
        mTimezone = in.readString();
        mTemperatureMax = in.readDouble();
        mIcon = in.readString();
    }

    public Day(){}

    public final static Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel parcel) {
            return new Day(parcel);
        }

        @Override
        public Day[] newArray(int i) {
            return new Day[i];
        }
    };
}
