package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class WeatherApiRestController {

    private WeatherService weatherService;

    @Autowired
    public WeatherApiRestController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping(value = "/weather", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Weather createWeather(@Valid @RequestBody Weather weatherData) {
        return weatherService.create(weatherData);
    }

    @DeleteMapping("/erase")
    @ResponseStatus(HttpStatus.OK)
    public void eraseAllWeatherInformation() {
        weatherService.eraseAllWeatherData();
    }
}
