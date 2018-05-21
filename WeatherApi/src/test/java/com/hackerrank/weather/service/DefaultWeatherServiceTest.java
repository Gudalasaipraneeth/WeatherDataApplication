package com.hackerrank.weather.service;

import com.google.common.collect.Lists;
import com.hackerrank.weather.dto.WeatherStats;
import com.hackerrank.weather.exception.DuplicateWeatherDataException;
import com.hackerrank.weather.exception.WeatherDataNotFoundException;
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
        given(weatherRepository.findAll()).willReturn(Collections.singletonList(expectedWeatherDO));

        List<Weather> actualWeatherDO = defaultWeatherService.getAllWeatherData();

        verify(weatherRepository, times(1)).findAll();
        verifyNoMoreInteractions(weatherRepository);

        assertThat(actualWeatherDO).isEqualTo(Collections.singletonList(expectedWeatherDO));
    }

    @Test
    public void shouldGetAllWeatherDataForGivenLatAndLong() throws ParseException, WeatherDataNotFoundException {

        Weather expectedWeatherDO = createWeatherDO();
        Float latitude = expectedWeatherDO.getLocation().getLatitude();
        Float longitude = expectedWeatherDO.getLocation().getLongitude();

        given(weatherRepository.findWeatherDataByLatitudeAndLongitutde(latitude, longitude))
                .willReturn(Collections.singletonList(expectedWeatherDO));


        List<Weather> actualWeatherDO = defaultWeatherService.getAllWeatherDataForGivenLatitudeAndLongitude(
                latitude, longitude
        );

        verify(weatherRepository, times(1))
                .findWeatherDataByLatitudeAndLongitutde(latitude, longitude);

        verifyNoMoreInteractions(weatherRepository);

        assertThat(actualWeatherDO).isEqualTo(Collections.singletonList(expectedWeatherDO));
    }

    @Test
    public void shouldGetAllWeatherDataForGivenDateRange() throws ParseException, WeatherDataNotFoundException {

        Weather expectedWeatherDO = createWeatherDO();
        Date startDate = simpleDateFormat.parse("2011-08-11");
        Date endDate = simpleDateFormat.parse("2011-08-12");

        given(weatherRepository.findWeatherDataForGivenDateRange(startDate, endDate))
                .willReturn(Collections.singletonList(expectedWeatherDO));


        List<WeatherStats> actualWeatherStatsList = defaultWeatherService.getAllWeatherDataForGivenDateRange(
                startDate, endDate
        );
        verify(weatherRepository, times(1))
                .findWeatherDataForGivenDateRange(startDate, endDate);

        verifyNoMoreInteractions(weatherRepository);

        assertThat(actualWeatherStatsList.size()).isEqualTo(1);
        assertThat(actualWeatherStatsList.get(0).getLocation().getCityName()).isEqualTo("wvg");
        assertThat(actualWeatherStatsList.get(0).getLocation().getStateName()).isEqualTo("lower saxony");
        assertThat(actualWeatherStatsList.get(0).getLocation().getLatitude()).isEqualTo(10);
        assertThat(actualWeatherStatsList.get(0).getLocation().getLongitude()).isEqualTo(10);
        assertThat(actualWeatherStatsList.get(0).getTemperatureSummaryStatistics()).isNotNull();
        assertThat(actualWeatherStatsList.get(0).getTemperatureStats().getMax()).isEqualTo(13);
        assertThat(actualWeatherStatsList.get(0).getTemperatureStats().getMin()).isEqualTo(12);
    }

    @Test
    public void shouldCreateSummaryStatistics() throws ParseException {

        Weather weatherDO = createWeatherDO();
        Weather weatherDO2 = createWeatherDO();
        Weather weatherDO3 = createWeatherDOForCity("dusseldorf");
        List<Weather> weatherList = Lists.newArrayList(weatherDO, weatherDO2, weatherDO3);

        List<WeatherStats> weatherStats = defaultWeatherService.getWeatherStats(weatherList);

        assertThat(weatherStats).isNotEmpty();

        weatherList.sort((weatherData1, weatherData2) -> (weatherData1
                .getLocation()
                .getCityName()
                .compareTo(weatherData2.getLocation().getCityName())));
        assertThat(weatherStats.size()).isEqualTo(2);
        assertThat(weatherStats.get(0).getLocation().getCityName()).isEqualTo("dusseldorf");
        assertThat(weatherStats.get(0).getTemperatureStats().getMin()).isEqualTo(12.0f);
        assertThat(weatherStats.get(0).getTemperatureStats().getMax()).isEqualTo(14.0f);
    }


    @Test
    public void shouldCreateSummaryStatisticsWithFailureMessageWhenNoDataIsPresent() throws ParseException {

        List<Weather> weatherList = Collections.emptyList();

        List<WeatherStats> weatherStats = defaultWeatherService.getWeatherStats(weatherList);

        assertThat(weatherStats).isNotEmpty();

        assertThat(weatherStats.size()).isEqualTo(1);
        assertThat(weatherStats.get(0).getFailureMessage()).isEqualTo("There is no weather data in the given date range");
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

    private Weather createWeatherDOForCity(String cityName) throws ParseException {
        Weather weatherDO = new Weather();
        weatherDO.setDateRecorded(simpleDateFormat.parse("2018-12-12"));
        weatherDO.setTemperature("12, 13, 14");
        weatherDO.setLocation(new Location(cityName, "lower saxony", 10f, 10f));
        weatherDO.setId(12L);
        return weatherDO;
    }
}