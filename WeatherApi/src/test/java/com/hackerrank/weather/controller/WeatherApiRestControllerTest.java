package com.hackerrank.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.weather.service.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@RunWith(MockitoJUnitRunner.class)
public class WeatherApiRestControllerTest {

    private MockMvc mockMvc;

    private static final String WEATHERS_ENDPOINT = ""; //"/v1/api/weathers";
    private static final String ERASE_ENDPOINT = WEATHERS_ENDPOINT + "/erase";

    @InjectMocks
    private WeatherApiRestController weatherApiRestController;

    @Mock
    private WeatherService weatherService;

    @Before
    public void testSetup() {

        JacksonTester.initFields(this, new ObjectMapper());

        MockitoAnnotations.initMocks(WeatherApiRestController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherApiRestController).dispatchOptions(true).build();

    }

    @Test
    public void shouldEraseAllWeatherData() throws Exception {

        MockHttpServletResponse mvcResponse = mockMvc.perform(
                delete(ERASE_ENDPOINT))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(mvcResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(mvcResponse.getContentAsString()).isEmpty();

        verify(weatherService, times(1)).eraseAllWeatherData();
        verifyNoMoreInteractions(weatherService);
    }
}