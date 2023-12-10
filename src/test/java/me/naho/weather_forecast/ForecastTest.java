package me.naho.weather_forecast;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ForecastTest {

    @Test
    void shouldTruncateFloats() {
        Forecast forecast = new Forecast(123456, 12.3456, 12.3456, 50);
        assertThat(forecast.getTemp()).isEqualTo(12.34F);
        assertThat(forecast.getFeels_like()).isEqualTo(12.34F);
    }

}
