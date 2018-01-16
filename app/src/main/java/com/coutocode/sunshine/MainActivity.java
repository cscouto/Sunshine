package com.coutocode.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coutocode.sunshine.data.SunshinePreferences;
import com.coutocode.sunshine.utilities.NetworkUtils;
import com.coutocode.sunshine.utilities.OpenWeatherJsonUtils;
import com.coutocode.sunshine.utilities.SunshineWeatherUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    TextView mWeatherTextView;
    TextView mErrorTextView;
    ProgressBar mWeatherProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);
        mErrorTextView = (TextView) findViewById(R.id.tv_error_message);
        mWeatherProgressBar = (ProgressBar) findViewById(R.id.pb_weather);

        loadWeatherData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                mWeatherTextView.setText("");
                loadWeatherData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    class FetchWeatherTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWeatherProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... urls) {
            if (urls.length == 0) {
                return null;
            }

            String location = urls[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mWeatherProgressBar.setVisibility(View.INVISIBLE);

            if (weatherData != null) {
                showWeatherDataView();
                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                for (String weatherString : weatherData) {
                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }
            }else{
                showErrorMessage();
            }
        }
    }

    void loadWeatherData(){
        showWeatherDataView();
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    void showWeatherDataView(){
        mErrorTextView.setVisibility(View.INVISIBLE);
        mWeatherTextView.setVisibility(View.VISIBLE);
    }
    void showErrorMessage(){
        mErrorTextView.setVisibility(View.VISIBLE);
        mWeatherTextView.setVisibility(View.INVISIBLE);
    }
}
