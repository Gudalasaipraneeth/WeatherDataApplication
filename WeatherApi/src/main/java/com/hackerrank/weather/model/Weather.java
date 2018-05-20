package com.hackerrank.weather.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(
        name = "weather",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"id"}
        )
        }
)
public class Weather {

    @Id
    private Long id;

    @Column(name = "date_recorded", nullable = false)
    @NotNull(message = "Daterecorded not be null!")
    private Date dateRecorded;

    @Column(nullable = false)
    @NotNull(message = "Location can not be null!")
    @Embedded
    private Location location;

    @NotNull(message = "Temperate can not be null!")
    @Column(name = "temperature_values", nullable = false)
    private String temperature_values;

    private Float[] temperature_values_in_float;

    public Weather() {
    }

    public Weather(Long id, Date dateRecorded, Location location, String temperature_values) {
        this.id = id;
        this.dateRecorded = dateRecorded;
        this.location = location;
        this.temperature_values = temperature_values;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTemperature() {
        return temperature_values;
    }

    public void setTemperature(String temperature_values) {
        this.temperature_values = temperature_values;
        this.temperature_values_in_float = getTemperatureArray(this.temperature_values);
    }

    public Float[] getTemperatureArray(String temperature_values) {

        String[] temperatures = temperature_values.split(",");

        return Arrays.stream(temperatures).map(Float::valueOf).toArray(Float[]::new);
    }

    public void setTemperature_values_in_float(Float[] temperature_values_in_float) {
        this.temperature_values_in_float = temperature_values_in_float;
    }
    public Float[] getTemperature_values_in_float() {
        return temperature_values_in_float;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (id != null ? !id.equals(weather.id) : weather.id != null) return false;
        if (dateRecorded != null ? !dateRecorded.equals(weather.dateRecorded) : weather.dateRecorded != null)
            return false;
        if (location != null ? !location.equals(weather.location) : weather.location != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Objects.equals(temperature_values, weather.temperature_values);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateRecorded != null ? dateRecorded.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + temperature_values.hashCode();
        return result;
    }
}
