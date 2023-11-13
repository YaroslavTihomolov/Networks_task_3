package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherInfo(
        @JsonProperty("coord") Coord coord,
        @JsonProperty("weather") List<Weather> weather,
        @JsonProperty("base") String base,
        @JsonProperty("main") Main main,
        @JsonProperty("visibility") int visibility,
        @JsonProperty("wind") Wind wind,
        @JsonProperty("clouds") Clouds clouds,
        @JsonProperty("dt") long dt,
        @JsonProperty("sys") Sys sys,
        @JsonProperty("timezone") int timezone,
        @JsonProperty("id") int id,
        @JsonProperty("name") String name,
        @JsonProperty("cod") int cod
) implements JsonDTO {

    public record Coord(@JsonProperty("lon") double lon,
                        @JsonProperty("lat") double lat) {
    }

    public record Weather(@JsonProperty("id") int id,
                          @JsonProperty("main") String main,
                          @JsonProperty("description") String description,
                          @JsonProperty("icon") String icon) {
    }

    public record Main(@JsonProperty("temp") double temp,
                       @JsonProperty("feels_like") double feels_like,
                       @JsonProperty("temp_min") double temp_min,
                       @JsonProperty("temp_max") double temp_max,
                       @JsonProperty("pressure") int pressure,
                       @JsonProperty("humidity") int humidity,
                       @JsonProperty("sea_level") int sea_level,
                       @JsonProperty("grnd_level") int grnd_level) {
    }

    public record Wind(@JsonProperty("speed") double speed,
                       @JsonProperty("deg") int deg,
                       @JsonProperty("gust") double gust) {
    }

    public record Clouds(@JsonProperty("all") int all) {
    }

    public record Sys(@JsonProperty("type") int type,
                      @JsonProperty("id") int id,
                      @JsonProperty("country") String country,
                      @JsonProperty("sunrise") long sunrise,
                      @JsonProperty("sunset") long sunset) {
    }

    @Override
    public String toString() {
        return String.format(
                   """
                   temp_min = %f
                   temp_max = %f
                   feels_like = %f
                   wind_speed = %f
                   description = %S      
                   """, main.temp_min, main.temp_max, main.feels_like, wind().speed, weather.get(0).description);
    }
}

