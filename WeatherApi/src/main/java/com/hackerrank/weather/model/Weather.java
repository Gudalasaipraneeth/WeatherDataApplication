package com.hackerrank.weather.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(
        name = "weather",
        uniqueConstraints={ @UniqueConstraint(
                columnNames = { "id" }
        )
        }
)
public class Weather {

    @Id
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Daterecorded not be null!")
    private Date dateRecorded;

    @Column(nullable = false)
    @NotNull(message = "Location can not be null!")
    @Embedded
    private Location location;

    @Column(nullable = false)
    @NotNull(message = "Temperate can not be null!")
    private Float[] temperature;

    public Weather() {
    }

    public Weather(Long id, Date dateRecorded, Location location, Float[] temperature) {
        this.id = id;
        this.dateRecorded = dateRecorded;
        this.location = location;
        this.temperature = temperature;
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

    public Float[] getTemperature() {
        return temperature;
    }

    public void setTemperature(Float[] temperature) {
        this.temperature = temperature;
    }
}
