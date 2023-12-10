# Dependencies
- Java 21
- Gradle 8.4

# Instructions
## Build the 
```
gradle build
```

## Build the Container
I am using podman instead of docker, but you can exchange it without problem. 

```
podman build -t spring-weather-forecast:latest .
```

## Starting - Only one of both
### Directly
```
java -DGEOCODER_API_KEY="YOUR_API_KEY" \
    -DWEATHER_API_KEY="YOUR_API_KEY" \
    -jar build/libs/weather_forecast-1.0.0.jar
```

### Container
```
podman run -it --rm \
    -e GEOCODER_API_KEY=YOUR_API_KEY_HERE \
    -e WEATHER_API_KEY=YOUR_API_KEY_HERE \
    -p 8080:8080 \
    spring-weather-forecast:latest
```
