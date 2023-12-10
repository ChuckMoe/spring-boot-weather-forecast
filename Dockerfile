FROM openjdk:21
LABEL authors="Moritz Hannemann"
ENV GEOCODER_API_KEY=""
ENV WEATHER_API_KEY=""
COPY build/libs/weather_forecast-1.0.0.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]