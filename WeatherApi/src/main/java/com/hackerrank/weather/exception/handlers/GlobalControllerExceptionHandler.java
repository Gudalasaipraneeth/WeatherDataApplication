package com.hackerrank.weather.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.ParseException;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Date is not valid yyyy-mm-dd format")  // 409
    @ExceptionHandler(ParseException.class)
    public void handleParseException() { }
}