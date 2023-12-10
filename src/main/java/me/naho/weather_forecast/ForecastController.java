package me.naho.weather_forecast;

import com.fasterxml.jackson.databind.JsonNode;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.StringJoiner;

// Todo
// NullPointerException: Cannot invoke "com.opencagedata.jopencage.model.JOpenCageResponse.getFirstPosition()" because "response" is null] with root cause
// java.lang.NullPointerException: Cannot invoke "com.opencagedata.jopencage.model.JOpenCageResponse.getFirstPosition()" because "response" is null



@RestController
public class ForecastController {

    Logger logger = LoggerFactory.getLogger(ForecastController.class);

    private JOpenCageLatLng getLatLng(String country, String city, String street, int housenumber)
            throws HttpClientErrorException {
        JOpenCageGeocoder geocoderApi = new JOpenCageGeocoder(System.getenv("GEOCODER_API_KEY"));
        StringJoiner query = new StringJoiner(", ");
        query.add(String.valueOf(housenumber));
        query.add(street);
        query.add(city);
        query.add(country);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(query.toString());
        JOpenCageResponse response = geocoderApi.forward(request);
        if (null == response) {
            logger.error("Geocoder API: No response");
            ErrorResponseException e = new ErrorResponseException(HttpStatus.BAD_GATEWAY);
            e.setDetail("Geocoder API: Unauthorized or Unreachable");
            throw e;
        }

        JOpenCageLatLng latLng = response.getFirstPosition();
        if (null == latLng) {
            logger.info("Geocoder API: Empty result");
        }
        return latLng;
    }

    private JsonNode getWeatherForecast(JOpenCageLatLng latLng) throws HttpClientErrorException {
        String sb = "https://api.openweathermap.org/data/2.5/forecast?" + "lat=" +
                latLng.getLat() +
                "&lon=" +
                latLng.getLng() +
                "&appid=" +
                System.getenv("WEATHER_API_KEY");
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(sb, JsonNode.class);
        } catch (HttpClientErrorException e) {
            logger.error("Weather API: " + e.getMessage());
            throw new ErrorResponseException(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping(value = "/forecast", params = {"lat", "lon"})
    @ResponseBody
    public ResponseEntity<Forecast> getForecastStatic(@RequestParam float lat, @RequestParam float lon) {
        Forecast forecast = new Forecast();
        return ResponseEntity.ok(forecast);
    }

    @GetMapping(value = "/forecast", params = {"country", "city", "street", "housenumber"})
    @ResponseBody
    public ResponseEntity<Forecast> getForecastDynamic(
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String street,
            @RequestParam int housenumber
    ) {
        JOpenCageLatLng latLng = getLatLng(country, city, street, housenumber);
        if (null == latLng) {
            return ResponseEntity.ok().build();
        }

        JsonNode json = getWeatherForecast(latLng);
        if (null == json) {
            return ResponseEntity.ok().build();
        }

        Forecast forecast = new Forecast(
                json.get("list").get(0).get("dt").asLong(),
                json.get("list").get(0).get("main").get("temp").asDouble(),
                json.get("list").get(0).get("main").get("feels_like").asDouble(),
                json.get("list").get(0).get("main").get("humidity").asInt()
        );
        return ResponseEntity.ok(forecast);
    }

}
