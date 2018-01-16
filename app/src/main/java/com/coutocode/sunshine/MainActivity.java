package com.coutocode.sunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.coutocode.sunshine.utilities.SunshineWeatherUtils;

public class MainActivity extends AppCompatActivity {

    TextView mWeathertextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeathertextView = findViewById(R.id.tv_weather_data);

        String[] weatherForecast = {"test1", "test2"};

        for (String weather: weatherForecast){
            mWeathertextView.append(weather + "\n\n\n");
        }
    }
}
