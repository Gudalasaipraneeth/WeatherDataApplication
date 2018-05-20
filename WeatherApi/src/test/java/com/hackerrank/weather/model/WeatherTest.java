package com.hackerrank.weather.model;


import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class WeatherTest {

    @Test
    public void shouldRetrieveFloatVaulesFromCommaSeparatedString() {

        Weather weatherDO = new Weather();
        weatherDO.setTemperature("12,  13");

        Float[] temperatureArray = weatherDO.getTemperatureArray();

        assertThat(temperatureArray).isNotEmpty();
        assertThat(temperatureArray.length).isEqualTo(2);
        assertThat(temperatureArray[0]).isEqualTo(12f);
        assertThat(temperatureArray[1]).isEqualTo(13f);

    }
}