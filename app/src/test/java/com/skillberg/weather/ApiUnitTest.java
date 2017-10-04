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

package com.skillberg.weather;

import com.skillberg.weather.api.ApiFactory;
import com.skillberg.weather.api.ApiInterface;
import com.skillberg.weather.api.model.Condition;
import com.skillberg.weather.api.model.LocationData;
import com.skillberg.weather.api.model.WeatherData;
import com.skillberg.weather.api.model.forecast.DailyForecastContainer;
import com.skillberg.weather.api.model.forecast.HourlyForecast;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

/**
 * Юнит-тест для API
 */
public class ApiUnitTest {

    private ApiInterface api;

    @Before
    public void setUp() {
        api = ApiFactory.createApi();
    }

    /**
     * Тестируем получение прогноза по IP адресу
     */
    @Test
    public void testForecastByIp() {
        try {
            // Выполняем запрос синхронно
            Response<WeatherData> response = api.getForecast(Constants.API_KEY, Constants.AUTO_IP_QUERY, 5).execute();

            // Проверяем, что запрос выполнился успешно
            Assert.assertTrue("Request was not successful", response.isSuccessful());

            WeatherData weatherData = response.body();

            // Body может быть null, поэтому проверяем
            Assert.assertNotNull("Empty response body", weatherData);

            LocationData locationData = weatherData.getLocation();

            // Проверяем, есть ли геолокация
            Assert.assertNotNull("No location data", locationData);

            // Проверяем, есть ли страна и город
            Assert.assertNotNull("No country", locationData.getCountry());
            Assert.assertNotNull("No name", locationData.getName());

            // Проверяем прогноз на текущее время
            HourlyForecast currentData = weatherData.getCurrentData();

            Assert.assertNotNull("No current data", currentData);

            // Проверяем "состояние" погоды на текущее время
            Condition condition = currentData.getCondition();

            Assert.assertNotNull("No condition", condition);

            Assert.assertNotNull("No condition text", condition.getText());
            Assert.assertNotNull("No condition url", condition.getIconUrl());

            // Проверяем, что все дни и часы на месте
            List<DailyForecastContainer> forecasts = weatherData.getForecast().getForecasts();
            Assert.assertNotNull("No forecasts", forecasts);

            Assert.assertEquals("Day count does not match", 5, forecasts.size());

            // В каждом дне проверяем, что все часы на месте
            for (DailyForecastContainer forecast : forecasts) {
                List<HourlyForecast> hourlyForecasts = forecast.getHourlyForecasts();
                Assert.assertEquals("Hour count does not match", 24, hourlyForecasts.size());
            }
        } catch (IOException e) {
            Assert.fail("Failed to get data: " + e.getMessage());
        }
    }

}
