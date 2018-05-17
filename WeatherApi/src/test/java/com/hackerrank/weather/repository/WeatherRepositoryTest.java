package com.hackerrank.weather.repository;

import com.hackerrank.weather.model.Weather;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan(basePackages = {"com.hackerrank.weather"})
@ContextConfiguration(classes = {WeatherRepository.class})
@AutoConfigurationPackage
public class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    public void shouldEraseAllWeatherDataFromRepository() {
        weatherRepository.deleteAll();
        Iterable<Weather> all = weatherRepository.findAll();
        all.forEach( weather -> {
            fail();
        });
    }

}