package com.coutocode.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coutocode.sunshine.utilities.SunshineDateUtils;
import com.coutocode.sunshine.utilities.SunshineWeatherUtils;

/**
 * Created by docouto on 1/16/18.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{

    final private ForecastAdapterOnClickHandler mCLickHandler;
    private final Context mContext;
    private Cursor mCursor;

    ForecastAdapter(ForecastAdapterOnClickHandler mCLickHandler, Context mContext){
        this.mCLickHandler = mCLickHandler;
        this.mContext = mContext;
    }

    public interface ForecastAdapterOnClickHandler {
        void onClick(long date);
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_list_item,parent, false);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        int weatherImageId;
        weatherImageId = SunshineWeatherUtils
                .getSmallArtResourceIdForWeatherCondition(weatherId);
        holder.iconView.setImageResource(weatherImageId);
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        holder.dateView.setText(dateString);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        String descriptionA11y = mContext.getString(R.string.a11y_forecast, description);
        holder.descriptionView.setText(description);
        holder.descriptionView.setContentDescription(descriptionA11y);
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        String highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius);
        String highA11y = mContext.getString(R.string.a11y_high_temp, highString);
        holder.highTempView.setText(highString);
        holder.highTempView.setContentDescription(highA11y);
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
        String lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius);
        String lowA11y = mContext.getString(R.string.a11y_low_temp, lowString);
        holder.lowTempView.setText(lowString);
        holder.lowTempView.setContentDescription(lowA11y);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;
        final ImageView iconView;

        ForecastAdapterViewHolder(View view) {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mCLickHandler.onClick(dateInMillis);
        }
    }
}
