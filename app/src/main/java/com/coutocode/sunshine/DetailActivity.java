package com.coutocode.sunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView mTextWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTextWeather = (TextView)findViewById(R.id.tv_display_weather);

        Intent intent = getIntent();
        String info = intent.getStringExtra("weatherData");

        mTextWeather.setText(info);

    }
}
