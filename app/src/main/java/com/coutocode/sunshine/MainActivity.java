package com.coutocode.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.coutocode.sunshine.utilities.NetworkUtils;
import com.coutocode.sunshine.utilities.SunshineWeatherUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadWeatherData("");
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    class RequestWeatherTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String results = null;
            try {
                results = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            //display the data
            super.onPostExecute(s);
        }
    }

    void loadWeatherData(String location){
        URL locationWeather = NetworkUtils.buildUrl(location);
        new RequestWeatherTask().execute(locationWeather);
    }
}
