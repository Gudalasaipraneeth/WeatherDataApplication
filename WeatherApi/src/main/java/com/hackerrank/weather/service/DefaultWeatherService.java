package com.hackerrank.weather.service;

import com.google.common.collect.Lists;
import com.hackerrank.weather.dto.WeatherStats;
import com.hackerrank.weather.exception.DuplicateWeatherDataException;
import com.hackerrank.weather.exception.WeatherDataNotFoundException;
import com.hackerrank.weather.model.Constants;
import com.hackerrank.weather.model.Location;
import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultWeatherService implements WeatherService {

    private WeatherRepository weatherRepository;

    @Autowired
    public DefaultWeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


    @Override
    public Weather create(Weather weather) throws DuplicateWeatherDataException {

        if (weatherRepository.findOne(weather.getId()) != null)
            throw new DuplicateWeatherDataException();
        return weatherRepository.save(weather);
    }

    @Override
    @Transactional
    public void eraseAllWeatherData() {
        weatherRepository.deleteAll();
    }

    @Override
    @Transactional
    public void eraseWeatherDataForGivenDateRangeAndLocation(Date startDate, Date endDate,
                                                             Float latitude, Float longitude) {

        weatherRepository.deleteByDateRangeForGivenLocation(startDate, endDate, latitude, longitude);
    }


    @Override
    public List<Weather> getAllWeatherData() {
        Iterable<Weather> all = weatherRepository.findAll();
        return Lists.newArrayList(all);

    }

    @Override
    public List<Weather> getAllWeatherDataForGivenLatitudeAndLongitude(Float latitude, Float longitude) throws WeatherDataNotFoundException {

        List<Weather> weatherDataByLatitudeAndLongitutde = weatherRepository.findWeatherDataByLatitudeAndLongitutde(latitude, longitude);

        if (weatherDataByLatitudeAndLongitutde.isEmpty())
            throw new WeatherDataNotFoundException();

        return weatherDataByLatitudeAndLongitutde;
    }

    @Override
    public List<WeatherStats> getAllWeatherDataForGivenDateRange(Date startDate, Date endDate) {
        List<Weather> weatherDataForGivenDateRange = weatherRepository.findWeatherDataForGivenDateRange(startDate, endDate);
        return getWeatherStats(weatherDataForGivenDateRange);
    }

    public List<WeatherStats> getWeatherStats(List<Weather> weatherDataList) {

        Map<Location, WeatherStats> locationWiseTemperatureStats = new HashMap<>();

        addEmptyWeatherStatsInMapIfThereIsNoWeatherData(weatherDataList, locationWiseTemperatureStats);

        addWeatherDataIntoHMap(weatherDataList, locationWiseTemperatureStats);

        return locationWiseTemperatureStats.entrySet()
                .stream()
                .map(Map.Entry::getValue).collect(Collectors.toList());

    }

    private void addWeatherDataIntoHMap(List<Weather> weatherDataList, Map<Location, WeatherStats> locationWiseTemperatureStats) {
        weatherDataList.forEach(weather -> {

            Location location = weather.getLocation();
            WeatherStats weatherStats;
            WeatherStats weatherStatsInHashmap = locationWiseTemperatureStats.get(location);
            if (weatherStatsInHashmap != null) {
                weatherStats = weatherStatsInHashmap;
            } else {
                weatherStats = new WeatherStats(location, Either.left(new DoubleSummaryStatistics()));
            }

            Float[] temperatureArray = weather.getTemperature_values_in_float();


            if (temperatureArray != null && temperatureArray.length > 0) {
                WeatherStats finalWeatherStats = weatherStats;
                Arrays.stream(temperatureArray)
                        .forEach(temperature -> finalWeatherStats
                                .getTemperatureSummaryStatistics()
                                .getLeft()
                                .accept(temperature));
            }

            locationWiseTemperatureStats.put(location, weatherStats);

        });
    }

    private void addEmptyWeatherStatsInMapIfThereIsNoWeatherData(List<Weather> weatherDataList, Map<Location, WeatherStats> locationWiseTemperatureStats) {
        if (weatherDataList.isEmpty()) {
            Location invalidLocation = new Location("NA", "NA", 0F, 0F);
            WeatherStats emptyWeatherStats = new WeatherStats(invalidLocation, Either.right(Constants.NO_DATA_FOR_GIVEN_DATE_RANGE));
            locationWiseTemperatureStats.put(invalidLocation, emptyWeatherStats);
        }
    }
}
