package com.hackerrank.weather.repository;

import com.google.common.collect.Lists;
import com.hackerrank.weather.model.Location;
import com.hackerrank.weather.model.Weather;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan(basePackages = {"com.hackerrank.weather"})
@ContextConfiguration(classes = {WeatherRepository.class})
@AutoConfigurationPackage
public class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");

    @Test
    public void shouldGetAllWeatherDataFromRepository() {

        ArrayList<Weather> weathers = Lists.newArrayList(weatherRepository.findAll());

        assertThat(weathers).isNotEmpty();
        assertThat(weathers.size()).isGreaterThan(0);
    }

    @Test
    public void shouldEraseAllWeatherDataFromRepository() {
        weatherRepository.deleteAll();
        Iterable<Weather> all = weatherRepository.findAll();
        all.forEach(weather -> fail());
    }

    @Test
    public void shouldEraseWeatherDataForAGivenDateRangeAndLocation() throws ParseException {

        Date startDate = simpleDateFormat.parse("2011-08-11");
        Date endDate = simpleDateFormat.parse("2011-08-12");

        Float latitude = 11F, longitude = 12F;

        weatherRepository.deleteByDateRangeForGivenLocation(startDate, endDate, latitude, longitude);

        Iterable<Weather> all = weatherRepository.findAll();
        all.forEach(weather -> {

            Location currentLocation = weather.getLocation();

            if ((weather.getDateRecorded() == startDate) ||
                    (weather.getDateRecorded() == endDate)) {
                fail();
            }

            if (Objects.equals(currentLocation.getLatitude(), latitude) &&
                    Objects.equals(currentLocation.getLongitude(), longitude)) {
                fail();
            }
        });
    }
}