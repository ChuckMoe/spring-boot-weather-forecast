package me.naho.weather_forecast;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForecastController.class)
@WithMockUser(username = "user", password = "password")
class ForecastControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldBeBadRequest() throws Exception {
        mockMvc.perform(get("/forecast?lat=1"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/forecast?lon=1"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/forecast?city=Berlin&street=abc1&housenumber=1"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/forecast?country=DE&city=Berlin&street=abc1&housenumber=a"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getForecastStatic() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/forecast?lat=1&lon=2"))
                .andExpect(status().isOk())
                .andReturn();
        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Forecast.class);
    }

    @Test
    void getForecastDynamic() throws Exception {
        String uri = "/forecast?country=DE&city=Berlin&street=Platz%20der%20Republic&housenumber=1";
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();
        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Forecast.class);
    }
}