package com.hackerrank.weather.service;

import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultWeatherService implements WeatherService {

    private WeatherRepository weatherRepository;

    @Autowired
    public DefaultWeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Override
    public void eraseAllWeatherData() {
        weatherRepository.deleteAll();
    }
}
