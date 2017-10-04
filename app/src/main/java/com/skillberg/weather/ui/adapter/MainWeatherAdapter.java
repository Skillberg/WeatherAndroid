/*
 * Copyright 2017 Skillberg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skillberg.weather.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skillberg.weather.R;
import com.skillberg.weather.api.model.WeatherData;
import com.skillberg.weather.api.model.forecast.DailyForecast;
import com.skillberg.weather.api.model.forecast.DailyForecastContainer;
import com.skillberg.weather.api.model.forecast.HourlyForecast;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Адаптер для RecyclerView на главном экране
 */
public class MainWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final SimpleDateFormat DAY_SDF = new SimpleDateFormat("EEE, MMMM d", Locale.getDefault());

    private static final int VIEW_TYPE_NOW = 1;
    private static final int VIEW_TYPE_DAILY = 2;

    private WeatherData weatherData;

    public synchronized void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    /**
     * В этом методе создается View и соответствующий ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_NOW) {
            View view = layoutInflater.inflate(R.layout.view_item_current, parent, false);

            return new CurrentViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.view_item_day, parent, false);

            return new DayViewHolder(view);
        }
    }

    /**
     * В этом методе мы привязываем данные к View
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        Context context = holder.itemView.getContext();

        if (viewType == VIEW_TYPE_NOW) {
            CurrentViewHolder currentViewHolder = (CurrentViewHolder) holder;

            HourlyForecast currentData = weatherData.getCurrentData();

            String temperature = context.getString(R.string.temperature_format, (int) currentData.getTemperature());
            String feelsLike = context.getString(R.string.temperature_feels_like_format, (int) currentData.getTemperatureFeelsLike());

            currentViewHolder.temperatureTv.setText(temperature);
            currentViewHolder.feelsLikeTv.setText(feelsLike);
            currentViewHolder.conditionTv.setText(currentData.getCondition().getText());

            Picasso.with(context).load(currentData.getCondition().getIconUrl()).into(currentViewHolder.conditionIv);
        } else {
            DayViewHolder dayViewHolder = (DayViewHolder) holder;

            // Вычитаем из позиции единицу, т.к. из-за первой строки индекс будет смещен на единицу
            DailyForecastContainer dailyForecastContainer = weatherData.getForecast().getForecasts().get(position - 1);
            DailyForecast dailyForecast = dailyForecastContainer.getDailyForecast();

            dayViewHolder.dateTv.setText(DAY_SDF.format(dailyForecastContainer.getDate()));
            String temperature = context.getString(R.string.temperature_format, (int) dailyForecast.getAverageTemperature());
            dayViewHolder.temperatureTv.setText(temperature);

            Picasso.with(context).load(dailyForecast.getCondition().getIconUrl()).into(dayViewHolder.conditionIv);
        }
    }

    /**
     * Получаем количество элементов в списке
     */
    @Override
    public int getItemCount() {
        if (weatherData == null) {
            return 0;
        } else {
            // Добавляем единицу, потому что первая строчка — сегодняшний прогноз
            return weatherData.getForecast().getForecasts().size() + 1;
        }
    }

    /**
     * Получаем тип View
     * Первая строка — сегодняшний прогноз
     * Остальные — прогноз на следующие дни
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_NOW;

            default:
                return VIEW_TYPE_DAILY;
        }
    }

    /**
     * View holder для текущего прогноза
     */
    class CurrentViewHolder extends RecyclerView.ViewHolder {

        private final TextView conditionTv;
        private final ImageView conditionIv;
        private final TextView feelsLikeTv;
        private final TextView temperatureTv;


        public CurrentViewHolder(View itemView) {
            super(itemView);

            conditionTv = itemView.findViewById(R.id.condition_tv);
            conditionIv = itemView.findViewById(R.id.condition_iv);
            feelsLikeTv = itemView.findViewById(R.id.feels_like_tv);
            temperatureTv = itemView.findViewById(R.id.temperature_tv);

            int color = ContextCompat.getColor(conditionIv.getContext(), R.color.colorTextCurrentWeather);
            conditionIv.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * View Holder для строчки с днем
     */
    class DayViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateTv;
        private final ImageView conditionIv;
        private final TextView temperatureTv;

        public DayViewHolder(View itemView) {
            super(itemView);

            dateTv = itemView.findViewById(R.id.date_tv);
            conditionIv = itemView.findViewById(R.id.condition_iv);
            temperatureTv = itemView.findViewById(R.id.temperature_tv);
        }

    }

}
