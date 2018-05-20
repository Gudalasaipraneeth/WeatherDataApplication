package com.hackerrank.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No weather data found")
public class WeatherDataNotFoundException extends Exception {
}
