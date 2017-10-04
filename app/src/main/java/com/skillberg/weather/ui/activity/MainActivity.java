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

package com.skillberg.weather.ui.activity;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.skillberg.weather.Constants;
import com.skillberg.weather.R;
import com.skillberg.weather.api.ApiFactory;
import com.skillberg.weather.api.ApiInterface;
import com.skillberg.weather.api.model.WeatherData;
import com.skillberg.weather.ui.adapter.MainWeatherAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainWeatherAdapter mainWeatherAdapter;

    private LocationManager locationManager;

    private final ApiInterface api = ApiFactory.createApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Находим RecyclerView
        RecyclerView recyclerView = findViewById(R.id.weather_rv);

        // Создаем LayoutManager — он будет управлять отображением данных в RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Создаем разделители для списка
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setLayoutManager(layoutManager);

        // Создаем и устанавливаем адаптер
        mainWeatherAdapter = new MainWeatherAdapter();

        recyclerView.setAdapter(mainWeatherAdapter);

        // Грузим данные
        loadData(Constants.AUTO_IP_QUERY);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupLocationManager();
    }

    @Override
    protected void onStop() {
        locationManager.removeUpdates(locationListener);

        super.onStop();
    }

    /**
     * Сетапим LocationManager
     */
    @SuppressLint("MissingPermission")
    private void setupLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            String provider = locationManager.getBestProvider(new Criteria(), true);

            if (provider != null) {
                Log.i(TAG, "Provider: " + provider);

                locationManager.requestLocationUpdates(provider,
                        10000,
                        10,
                        locationListener);
            }
        }
    }


    /**
     * Загружаем данные
     */
    private void loadData(String query) {
        api.getForecast(Constants.API_KEY, query, 10).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {

                    // Если запрос выполнен успешно — передаем данные в адаптер
                    mainWeatherAdapter.setWeatherData(response.body());

                    // Уведомляем адаптер о том, что данные изменились
                    mainWeatherAdapter.notifyDataSetChanged();

                } else {
                    // Запрос не удался (ошибка HTTP)
                    Log.e(TAG, "Failed to load data: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                // Запрос не удался (ошибка сети)
                Log.e(TAG, "Failed to load forecast: " + t.getMessage());
            }
        });
    }

    /**
     * Слушатель для получения геоданных
     */
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "Location: " + location.getLatitude() + "x" + location.getLongitude());

            Toast.makeText(MainActivity.this,
                    location.getLatitude() + "x" + location.getLongitude(),
                    Toast.LENGTH_LONG).show();

            // Загружаем погоду по геолокации
            loadData(location.getLatitude() + "," + location.getLongitude());

            // Отписываемся от событий
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
