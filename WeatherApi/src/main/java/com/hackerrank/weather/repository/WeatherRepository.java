package com.hackerrank.weather.repository;

import com.hackerrank.weather.model.Weather;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {

    @Modifying
    @Query("delete FROM Weather w " +
            "where " +
            "   w.dateRecorded BETWEEN ?1 AND ?2  " +
            "   AND " +
            "   (w.location.latitude     = ?3) AND" +
            "   (w.location.longitude    = ?4)")
    void deleteByDateRangeForGivenLocation(final Date startDate, final Date endDate, final float latitude, final float longitude);


    @Query("select w FROM Weather w " +
            "where " +
            "   (w.location.latitude     = ?1) AND" +
            "   (w.location.longitude    = ?2)")
    List<Weather> findWeatherDataByLatitudeAndLongitutde(final float latitude, final float longitude);

}
