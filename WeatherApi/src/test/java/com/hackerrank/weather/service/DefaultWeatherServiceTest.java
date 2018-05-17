package com.hackerrank.weather.service;

import com.hackerrank.weather.repository.WeatherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DefaultWeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private DefaultWeatherService defaultWeatherService;

    @Test
    public void shouldEraseAllWeatherData() {

        defaultWeatherService.eraseAllWeatherData();

        verify(weatherRepository, times(1)).deleteAll();
        verifyNoMoreInteractions(weatherRepository);
    }
}