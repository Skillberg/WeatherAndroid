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

import com.google.gson.annotations.SerializedName;
import com.skillberg.weather.api.model.Condition;

/**
 * Прогноз на день
 */
public class DailyForecast {

    @SerializedName("mintemp_c")
    private float minTemperature;

    @SerializedName("maxtemp_c")
    private float maxTemperature;

    @SerializedName("avgtemp_c")
    private float averageTemperature;

    private Condition condition;


    public float getMinTemperature() {
        return minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getAverageTemperature() {
        return averageTemperature;
    }

    public Condition getCondition() {
        return condition;
    }
}
