package com.hackerrank.weather.service;

import com.google.common.collect.Lists;
import com.hackerrank.weather.exception.DuplicateWeatherDataException;
import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    public List<Weather> getAllWeatherDataForGivenLatitudeAndLongitude(Float latitude, Float longitude) {

       return weatherRepository.findWeatherDataByLatitudeAndLongitutde(latitude, longitude);

    }
}
