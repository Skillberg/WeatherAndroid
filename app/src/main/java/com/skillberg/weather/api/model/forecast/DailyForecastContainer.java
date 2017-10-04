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

package com.skillberg.weather.api.model.forecast;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Контейнер для прогноза на день
 */
public class DailyForecastContainer {

    @SerializedName("date_epoch")
    private long dateTs;

    @Nullable
    private transient Date date;

    @SerializedName("day")
    private DailyForecast dailyForecast;

    @SerializedName("hour")
    private List<HourlyForecast> hourlyForecasts;


    public synchronized Date getDate() {
        if (date == null) {
            if (dateTs == 0) {
                date = new Date();
            } else {
                date = new Date(dateTs * 1000);
            }
        }

        return date;
    }

    public DailyForecast getDailyForecast() {
        return dailyForecast;
    }

    public List<HourlyForecast> getHourlyForecasts() {
        return hourlyForecasts;
    }
}
