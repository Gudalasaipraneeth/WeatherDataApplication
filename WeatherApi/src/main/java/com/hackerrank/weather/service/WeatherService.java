package com.hackerrank.weather.service;

import com.hackerrank.weather.dto.WeatherStats;
import com.hackerrank.weather.exception.DuplicateWeatherDataException;
import com.hackerrank.weather.exception.WeatherDataNotFoundException;
import com.hackerrank.weather.model.Weather;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface WeatherService   {

    void eraseAllWeatherData();

    void eraseWeatherDataForGivenDateRangeAndLocation(Date startDate, Date endDate, Float latitude, Float longitude) throws ParseException;

    Weather create(Weather weather) throws DuplicateWeatherDataException;

    List<Weather> getAllWeatherData();

    List<Weather> getAllWeatherDataForGivenLatitudeAndLongitude(Float  latitude, Float longitudde)
            throws WeatherDataNotFoundException;

    List<WeatherStats> getAllWeatherDataForGivenDateRange(Date startDate, Date endDate);
}
