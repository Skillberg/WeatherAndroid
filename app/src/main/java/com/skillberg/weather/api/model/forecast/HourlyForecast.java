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
import com.skillberg.weather.api.model.Condition;

import java.util.Date;

/**
 * Прогноз на час
 */
public class HourlyForecast {

    @SerializedName("time_epoch")
    private long timeTs;

    @Nullable
    private transient Date dateTime;

    @SerializedName("temp_c")
    private float temperature;

    @SerializedName("feelslike_c")
    private float temperatureFeelsLike;

    private Condition condition;

    public HourlyForecast() {
    }


    public synchronized Date getDateTime() {
        if (dateTime == null) {
            if (timeTs == 0) {
                // Для текущего дня таймстампа не будет, поэтому создаем текущую дату
                dateTime = new Date();
            } else {
                dateTime = new Date(timeTs * 1000);
            }
        }

        return dateTime;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getTemperatureFeelsLike() {
        return temperatureFeelsLike;
    }

    public Condition getCondition() {
        return condition;
    }


}
