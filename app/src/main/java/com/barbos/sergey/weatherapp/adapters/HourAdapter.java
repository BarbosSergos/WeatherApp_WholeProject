package com.barbos.sergey.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.barbos.sergey.weatherapp.R;
import com.barbos.sergey.weatherapp.weather.Hour;

/**
 * Created by Sergey on 19.09.2016.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    Context mContext;
    Hour[] mHours;

    public HourAdapter(Context context, Hour[] hours) {
        mContext = context;
        mHours = hours;
    }

    public HourAdapter(Hour[] hours){
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder hourViewHolder = new HourViewHolder(view);

        return hourViewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }


    public static class HourViewHolder extends RecyclerView.ViewHolder{

        public TextView mTimeLabel;
        public TextView mTemperatureLabel;
        public TextView mSummaryLabel;
        public ImageView mImageViewLabel;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mImageViewLabel = (ImageView) itemView.findViewById(R.id.iconImageView);
        }

        public void bindHour(Hour hour){
            mTimeLabel.setText(hour.getHour());
            mTemperatureLabel.setText(hour.getTemperature() + "");
            mSummaryLabel.setText(hour.getSummary());
            mImageViewLabel.setImageResource(hour.getIconId());
        }
    }


}
