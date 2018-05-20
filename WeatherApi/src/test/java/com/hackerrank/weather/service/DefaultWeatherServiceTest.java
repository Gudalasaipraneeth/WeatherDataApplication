package com.hackerrank.weather.service;

import com.hackerrank.weather.exception.DuplicateWeatherDataException;
import com.hackerrank.weather.model.Location;
import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultWeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private DefaultWeatherService defaultWeatherService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");

    @Test
    public void shouldAddAWeatherData() throws DuplicateWeatherDataException, ParseException {

        Weather weatherDO = createWeatherDO();

        given(weatherRepository.findOne(weatherDO.getId())).willReturn(null);

        defaultWeatherService.create(weatherDO);

        verify(weatherRepository, times(1)).findOne(weatherDO.getId());
        verify(weatherRepository, times(1)).save(weatherDO);
        verifyNoMoreInteractions(weatherRepository);
    }


    @Test
    public void shouldThrowDuplicateWeatherDataWhenAddingPrexistingWeatherData() throws ParseException {

        Weather weatherDO = createWeatherDO();

        given(weatherRepository.findOne(weatherDO.getId())).willReturn(weatherDO);

        try {
            defaultWeatherService.create(weatherDO);

        } catch (DuplicateWeatherDataException e) {

            verify(weatherRepository, times(1)).findOne(weatherDO.getId());
            verify(weatherRepository, never()).save(weatherDO);
            verifyNoMoreInteractions(weatherRepository);
        }
    }

    @Test
    public void shouldGetAllWeatherData() throws DuplicateWeatherDataException, ParseException {

        Weather expectedWeatherDO = createWeatherDO();
        given(weatherRepository.findAll()).willReturn(Collections.singleton(expectedWeatherDO));

        List<Weather> actualWeatherDO = defaultWeatherService.getAllWeatherData();

        verify(weatherRepository, times(1)).findAll();
        verifyNoMoreInteractions(weatherRepository);

        assertThat(actualWeatherDO).isEqualTo(Collections.singletonList(expectedWeatherDO));
    }

    @Test
    public void shouldEraseAllWeatherData() {

        defaultWeatherService.eraseAllWeatherData();

        verify(weatherRepository, times(1)).deleteAll();
        verifyNoMoreInteractions(weatherRepository);
    }

    @Test
    public void shouldEraseWeatherDataForAGivenDateAndGeoCordinateRagne() throws ParseException {

        Date startDate = simpleDateFormat.parse("2011-08-11");
        Date endDate = simpleDateFormat.parse("2011-08-12");
        Float latitude = 10f, longitude = 10f;

        defaultWeatherService.eraseWeatherDataForGivenDateRangeAndLocation(startDate, endDate, latitude, longitude);

        verify( weatherRepository, times(1)).deleteByDateRangeForGivenLocation(startDate, endDate, latitude, longitude);

        verifyNoMoreInteractions(weatherRepository);
    }

    private Weather createWeatherDO() throws ParseException {
        Weather weatherDO = new Weather();
        weatherDO.setDateRecorded(simpleDateFormat.parse("2018-12-12"));
        weatherDO.setTemperature("12, 13");
        weatherDO.setLocation(new Location("wvg", "lower saxony", 10f, 10f));
        weatherDO.setId(12L);
        return weatherDO;
    }

}