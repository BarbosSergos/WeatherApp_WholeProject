package com.barbos.sergey.weatherapp.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barbos.sergey.weatherapp.R;
import com.barbos.sergey.weatherapp.weather.Current;
import com.barbos.sergey.weatherapp.weather.Forecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Forecast mForecast;

    //Инициализация всех элементов экрана
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.degreeLabel) ImageView mDegreeLabel;
    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.imageLabel) ImageView mImageLabel;
    @InjectView(R.id.humiditiLabel) TextView mHumidityLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipLabel) TextView mPrecipLabel;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.refreshButton) ImageView mRefreshButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        final double letitude = 55.75222;
        final double longitude = 37.61556;


        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast(letitude, longitude);
            }
        });

        getForecast(letitude, longitude);

        Log.d(TAG, "Main UI is running");
    }

    private void getForecast(double letitude, double longitude) {
        String apiKey = "0754214d1efe06c4a8fb1f5bb7df1104";

        String forecastURL = "https://api.forecast.io/forecast/"+apiKey+"/"+letitude+","+longitude;

        if (isNetworkAvailable()) {

            //Show progress bar
            toggleRefresh();

            //Lets create a http client
            OkHttpClient client = new OkHttpClient();

            //Build here a Request object and after do a call with Call class.

            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);


            //Initialize asynchronical call and get the responce from forecast.io
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        if (response.isSuccessful()) {
                            //Store responce result in JSON format into String variable
                            String jsonResponce = response.body().string();
                            mForecast = parseForecastDetails(jsonResponce);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                            Log.v(TAG, jsonResponce);
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught:" + e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught:" + e);
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Network is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {

        Current current = mForecast.getCurrent();

        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity()+"%");
        mImageLabel.setImageResource(current.getIconId());
        mLocationLabel.setText(current.getTimeZone());
        mPrecipValue.setText(current.getPrecipChance()+"%");
        mSummaryLabel.setText(current.getSummary());
        mTemperatureLabel.setText(current.getTemperature()+"");
    }

    private void alertUserAboutError() {
        AlertDialogFragment alert = new AlertDialogFragment();
        alert.show(getFragmentManager(), "error_dialog");
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentJsonDate(jsonData));
        return forecast;
    }

    private Current getCurrentJsonDate(String jsonResponce) throws JSONException {
        //Get the first JSON object from responce file
        JSONObject forecast = new JSONObject(jsonResponce);

        Current current = new Current();
        //Set the value of key "timezone" to current object
        current.setTimeZone(forecast.getString("timezone"));
        //Review another JSON objects inside forecast
        JSONObject currently = forecast.getJSONObject("currently");



        current.setHumidity(currently.getDouble("humidity"));
        current.setIcon(currently.getString("icon"));
        current.setTime(currently.getLong("time"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));

        return current;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

         boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }
}
