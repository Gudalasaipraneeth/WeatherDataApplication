package com.hackerrank.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hackerrank.weather.model.Location;
import io.vavr.control.Either;

import javax.validation.constraints.NotNull;
import java.util.DoubleSummaryStatistics;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherStats {

    @NotNull(message = "Location cannot be null")
    private Location location;

    @JsonIgnore
    private Either<DoubleSummaryStatistics, String> temperatureSummaryStatistics;

    private DoubleSummaryStatistics temperatureStats;

    private String failureMessage;

    public WeatherStats() {
    }

    public WeatherStats(Location location,
                        Either<DoubleSummaryStatistics, String> temperatureSummaryStatistics) {
        this.location = location;
        this.temperatureSummaryStatistics = temperatureSummaryStatistics;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Either<DoubleSummaryStatistics, String> getTemperatureSummaryStatistics() {
        return temperatureSummaryStatistics;
    }

    public String getFailureMessage() {
        if (temperatureSummaryStatistics.isRight())
            return temperatureSummaryStatistics.get();
        else
            return null;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = temperatureSummaryStatistics.get();
    }

    //Checks whether data is present, only then returns the data.
    public DoubleSummaryStatistics getTemperatureStats() {
        if (temperatureSummaryStatistics.isLeft())
            return temperatureSummaryStatistics.getLeft();
        else
            return null;
    }

    public void setTemperatureStats(DoubleSummaryStatistics temperatureStats) {
        this.temperatureStats = temperatureStats;
    }
}
