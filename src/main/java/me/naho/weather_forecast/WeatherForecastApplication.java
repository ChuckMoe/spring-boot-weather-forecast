package me.naho.weather_forecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@SpringBootApplication
public class WeatherForecastApplication {

    public static void main(String[] args) {
        if (System.getenv("GEOCODER_API_KEY") == null || System.getenv("WEATHER_API_KEY") == null) {
            throw new AuthenticationCredentialsNotFoundException("Environment variables GEOCODER_API_KEY and " +
                    "WEATHER_API_KEY must be set");
        }
        SpringApplication.run(WeatherForecastApplication.class, args);
    }

}
