package com.hackerrank.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Duplicate weather data")
public class DuplicateWeatherDataException extends Exception {
}
