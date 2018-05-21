package com.hackerrank.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.weather.Application;
import com.hackerrank.weather.model.Location;
import com.hackerrank.weather.model.Weather;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Application.class})
@AutoConfigureMockMvc
public class WeatherApiRestControllerIntegrationTest {

    private static final String WEATHERS_ENDPOINT = "/weather";
    private static final String ERASE_ENDPOINT = "/erase";
    private static final String TEMPERATURES_ENDPOINT = "/temperature";

    @Autowired
    private MockMvc mvc;

    private static final ObjectMapper mapper = new ObjectMapper();
    private JacksonTester<Weather> weatherDOJacksonTester;
    private JacksonTester<List<Weather>> weatherDOListJacksonTester;


    @Before
    public void setup() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldCreateWeatherDataResourceForValidWeatherDataObject() throws Exception {

        Weather expectedWeather = createWeatherDO();

        String weatherDataAsString = mapper.writeValueAsString(expectedWeather);

        MockHttpServletResponse mvcResponse = mvc.perform(
                post(WEATHERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(weatherDataAsString))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(mvcResponse.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(mvcResponse.getContentAsString()).isEqualTo(
                weatherDOJacksonTester.write(expectedWeather).getJson()
        );
    }

    @Test
    public void shouldGetAllWeatherData()
            throws Exception {
        MockHttpServletResponse response = mvc.perform(get(WEATHERS_ENDPOINT))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotEmpty();
    }

    @Test
    public void shouldGetAllWeatherDataForGivenLatAndLongitude() throws Exception {

        MockHttpServletResponse getFilterWeatherDataResponse = mvc.perform(
                get(WEATHERS_ENDPOINT)
                        .param("lat", "10")
                        .param("lon", "10"))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(getFilterWeatherDataResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getFilterWeatherDataResponse.getContentAsString()).isNotEmpty();
    }

    @Test
    public void shouldGetAllWeatherDataForGivenDateRange() throws Exception {

        MockHttpServletResponse getFilterWeatherDataResponse = mvc.perform(
                get(WEATHERS_ENDPOINT)
                        .param("start", "2011-01-01")
                        .param("end", "2020-01-11"))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(getFilterWeatherDataResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getFilterWeatherDataResponse.getContentAsString()).isNotEmpty();
    }

    @Test
    public void shouldReturn404WhenNoWeatherDataForGivenLatAndLongitudeIsFound() throws Exception {

        MockHttpServletResponse getFilterWeatherDataResponse = mvc.perform(
                get(WEATHERS_ENDPOINT)
                        .param("lat", "999")
                        .param("lon", "10000"))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(getFilterWeatherDataResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(getFilterWeatherDataResponse.getContentAsString()).isEmpty();
    }

    @Test
    public void shouldEraseAllWeatherData()
            throws Exception {
        MockHttpServletResponse response = mvc.perform(delete(ERASE_ENDPOINT))

                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void shouldEraseWeatherDataForGivenDateRangeAndLocation() throws Exception {
        String startDateInString = "2018-02-11", endDateInString = "2018-02-12";

        MockHttpServletResponse mvcResponse = mvc.perform(
                delete(ERASE_ENDPOINT)
                        .param("start", startDateInString)
                        .param("end", endDateInString)
                        .param("lat", "10")
                        .param("lon", "10"))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(mvcResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(mvcResponse.getContentAsString()).isEmpty();
    }

    @Test
    public void shouldReturn400WhenInvalidDateFormatsAreGivenForErasingWeatherData() throws Exception {

        String invalid_start_date = "INVALID_START_DATE", invalid_end_date = "invalid_end_date";

        MockHttpServletResponse mvcResponse = mvc.perform(
                delete(ERASE_ENDPOINT)
                        .param("start", invalid_start_date)
                        .param("end", invalid_end_date)
                        .param("lat", "10")
                        .param("lon", "10"))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(mvcResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResponse.getContentAsString()).isEmpty();
    }


    private Weather createWeatherDO() {
        Weather expectedWeather = new Weather();
        expectedWeather.setId(2L);
        expectedWeather.setDateRecorded(new Date());
        Location location = new Location("wolfsburg", "lower saxony", 10f, 10f);
        expectedWeather.setLocation(location);
        expectedWeather.setTemperature("11, 12");
        return expectedWeather;
    }

    private Weather createWeatherDOAsInDataSqlFile() {
        Weather expectedWeather = new Weather();
        expectedWeather.setId(1L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
            expectedWeather.setDateRecorded(simpleDateFormat.parse("2018-11-11"));
        } catch (ParseException e) {
            return null;
        }
        Location location = new Location("wolfsburg", "lower saxony", 10f, 10f);
        expectedWeather.setLocation(location);
        expectedWeather.setTemperature("12,10");
        return expectedWeather;
    }
}
