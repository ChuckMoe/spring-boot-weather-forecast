package me.naho.weather_forecast;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Random;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Forecast {
    @Getter
    private final long time;  // unix timestamp utc
    private final double temp;
    private final double feels_like;
    @Getter
    private final int humidity;

    Forecast() {
        Random rand = new Random();
        this.time = Instant.now().plusSeconds(86400).getEpochSecond();  // now + 1 day
        this.temp = rand.nextFloat(-30, 50);
        this.feels_like = this.temp + rand.nextFloat(-3, 4);
        this.humidity = rand.nextInt(0, 100);
    }

    Forecast(long time, double temp, double feelsLike, int humidity) {
        this.time = time;
        this.temp = temp;
        this.feels_like = feelsLike;
        this.humidity = humidity;
    }

    public float getTemp() {
        int i = (int) (this.temp * 100);
        return (float) i / 100;
    }

    public float getFeels_like() {
        int i = (int) (this.feels_like * 100);
        return (float) i / 100;
    }
}
