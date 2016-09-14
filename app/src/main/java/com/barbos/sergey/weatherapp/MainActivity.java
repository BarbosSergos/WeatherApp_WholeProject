package com.barbos.sergey.weatherapp;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private CurrentWeather mCurrentWeather;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        String apiKey = "0754214d1efe06c4a8fb1f5bb7df1104";
        double letitude = 37.8267;
        double longitude = -122.423;
        String forecastURL = "https://api.forecast.io/forecast/"+apiKey+"/"+letitude+","+longitude;

        if (isNetworkAvailable()) {
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

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            //Store responce result in JSON format into String variable
                            String jsonResponce = response.body().string();
                            mCurrentWeather = getCurrentJsonDate(jsonResponce);
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

        Log.d(TAG, "Main UI is running");
    }

    private void alertUserAboutError() {
        AlertDialogFragment alert = new AlertDialogFragment();
        alert.show(getFragmentManager(), "error_dialog");
    }

    private CurrentWeather getCurrentJsonDate(String jsonResponce) throws JSONException {
        //Get the first JSON object from responce file
        JSONObject forecast = new JSONObject(jsonResponce);

        CurrentWeather currentWeather = new CurrentWeather();
        //Set the value of key "timezone" to currentWeather object
        currentWeather.setTimeZone(forecast.getString("timezone"));
        //Review another JSON objects inside forecast
        JSONObject currently = forecast.getJSONObject("currently");
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));

        return currentWeather;
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
