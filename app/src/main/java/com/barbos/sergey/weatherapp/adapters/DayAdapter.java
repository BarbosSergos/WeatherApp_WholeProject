package com.barbos.sergey.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.barbos.sergey.weatherapp.R;
import com.barbos.sergey.weatherapp.weather.Day;

/**
 * Created by Sergey on 18.09.2016.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int i) {
        return mDays[i];
    }

    @Override
    public long getItemId(int i) {
        return 0; //Мы не будем использовать этот метод.
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.dayLabel = (TextView) view.findViewById(R.id.dayNameLabel);
            holder.imageIconView = (ImageView) view.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) view.findViewById(R.id.temperatureIcon);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Day day = mDays[position];

        holder.temperatureLabel.setText(day.getTemperatureMax()+"");
        holder.imageIconView.setImageResource(day.getIconId());

        if (position == 0) {
            holder.dayLabel.setText("Today");
        } else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        return view;
    }

    public static class ViewHolder{
        ImageView imageIconView; //public by default
        TextView dayLabel;
        TextView temperatureLabel;
    }
}
