package com.coutocode.sunshine;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                String mimeType = "text/plain";
                String title = "#Sunshine";

                Intent shareIntent =   ShareCompat.IntentBuilder.from(this)
                        .setChooserTitle(title)
                        .setType(mimeType)
                        .setText(mTextWeather.getText().toString())
                        .getIntent();
                if (shareIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(shareIntent);
                }
                return true;
            case R.id.action_settings:
                Intent intentSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(intentSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
