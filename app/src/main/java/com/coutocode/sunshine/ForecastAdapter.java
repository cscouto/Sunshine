package com.coutocode.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.coutocode.sunshine.utilities.SunshineDateUtils;
import com.coutocode.sunshine.utilities.SunshineWeatherUtils;
import static com.coutocode.sunshine.utilities.Constants.*;

/**
 * Created by docouto on 1/16/18.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{
    //contants
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    //vars
    private final Context mContext;
    final private ForecastAdapterOnClickHandler mClickHandler;
    public interface ForecastAdapterOnClickHandler {
        void onClick(long date);
    }
    private boolean mUseTodayLayout;
    private Cursor mCursor;

    //constructor
    public ForecastAdapter(@NonNull Context context, ForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    //class functions
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.forecast_list_item;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.forecast_list_item;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new ForecastAdapterViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        int weatherId = mCursor.getInt(INDEX_WEATHER_CONDITION_ID);
        int weatherImageId;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                weatherImageId = SunshineWeatherUtils
                        .getLargeArtResourceIdForWeatherCondition(weatherId);
                break;
            case VIEW_TYPE_FUTURE_DAY:
                weatherImageId = SunshineWeatherUtils
                        .getSmallArtResourceIdForWeatherCondition(weatherId);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        forecastAdapterViewHolder.iconView.setImageResource(weatherImageId);
        long dateInMillis = mCursor.getLong(INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        forecastAdapterViewHolder.dateView.setText(dateString);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        String descriptionA11y = mContext.getString(R.string.a11y_forecast, description);
        forecastAdapterViewHolder.descriptionView.setText(description);
        forecastAdapterViewHolder.descriptionView.setContentDescription(descriptionA11y);
        double highInCelsius = mCursor.getDouble(INDEX_WEATHER_MAX_TEMP);
        String highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius);
        String highA11y = mContext.getString(R.string.a11y_high_temp, highString);
        forecastAdapterViewHolder.highTempView.setText(highString);
        forecastAdapterViewHolder.highTempView.setContentDescription(highA11y);
        double lowInCelsius = mCursor.getDouble(INDEX_WEATHER_MIN_TEMP);
        String lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius);
        String lowA11y = mContext.getString(R.string.a11y_low_temp, lowString);
        forecastAdapterViewHolder.lowTempView.setText(lowString);
        forecastAdapterViewHolder.lowTempView.setContentDescription(lowA11y);
    }
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }
    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;
        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;
        ForecastAdapterViewHolder(View view) {
            super(view);
            iconView = view.findViewById(R.id.weather_icon);
            dateView = view.findViewById(R.id.date);
            descriptionView = view.findViewById(R.id.weather_description);
            highTempView = view.findViewById(R.id.high_temperature);
            lowTempView = view.findViewById(R.id.low_temperature);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(INDEX_WEATHER_DATE);
            mClickHandler.onClick(dateInMillis);
        }
    }
}
