package com.hackerrank.weather.dto;

import com.hackerrank.weather.model.Location;
import io.vavr.control.Either;
import org.junit.Test;

import java.util.DoubleSummaryStatistics;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherStatsTest {

    @Test
    public void shouldReturnDoubleSummaryStatsWhenItIsNotNullInEitherType() {

        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        statistics.accept(0f);
        statistics.accept(10f);
        WeatherStats weatherStats = new WeatherStats(new Location("Asgard", "Sate of Asgard", 0f, 0f),
                Either.left(statistics));

        assertThat(weatherStats.getTemperatureStats()).isNotNull();
        assertThat(weatherStats.getTemperatureStats().getMin()).isEqualTo(0);
        assertThat(weatherStats.getTemperatureStats().getMax()).isEqualTo(10f);

    }

    @Test
    public void shouldReturnFailureMessageWhenItIsNotNullInEitherType() {

        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        statistics.accept(0f);
        statistics.accept(10f);
        String someFailureMessage = "Faielure has happened!!!";
        WeatherStats weatherStats = new WeatherStats(new Location("Asgard", "Sate of Asgard", 0f, 0f),
                Either.right(someFailureMessage));

        assertThat(weatherStats.getFailureMessage()).isNotNull();
        assertThat(weatherStats.getFailureMessage()).isEqualTo(someFailureMessage);
    }
}