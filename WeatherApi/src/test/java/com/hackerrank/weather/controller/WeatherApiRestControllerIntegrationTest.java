package com.hackerrank.weather.controller;

import com.hackerrank.weather.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Application.class})
@AutoConfigureMockMvc
public class WeatherApiRestControllerIntegrationTest {

    private static final String WEATHERS_ENDPOINT = ""; //"/v1/api/weathers";
    private static final String ERASE_ENDPOINT = WEATHERS_ENDPOINT + "/erase";

    @Autowired
    private MockMvc mvc;


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


}
