package com.hackerrank.weather.service;

import com.hackerrank.weather.model.Weather;

public interface WeatherService   {

    void eraseAllWeatherData();

    Weather create(Weather weather);
}
