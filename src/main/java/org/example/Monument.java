package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Monument(
        @JsonProperty("xid") String xid,
        @JsonProperty("name") String name,
        @JsonProperty("address") Address address,
        @JsonProperty("rate") String rate,
        @JsonProperty("osm") String osm,
        @JsonProperty("wikidata") String wikidata,
        @JsonProperty("kinds") String kinds,
        @JsonProperty("sources") Sources sources,
        @JsonProperty("otm") String otm,
        @JsonProperty("wikipedia") String wikipedia,
        @JsonProperty("image") String image,
        @JsonProperty("preview") Preview preview,
        @JsonProperty("wikipedia_extracts") WikipediaExtracts wikipediaExtracts,
        @JsonProperty("point") Point point
) {

    @Override
    public String toString() {
        return name + " " + address + " " + wikipediaExtracts;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Address(
            @JsonProperty("city") String city,
            @JsonProperty("state") String state,
            @JsonProperty("county") String county,
            @JsonProperty("suburb") String suburb,
            @JsonProperty("country") String country,
            @JsonProperty("address29") String address29,
            @JsonProperty("pedestrian") String pedestrian,
            @JsonProperty("country_code") String countryCode,
            @JsonProperty("neighbourhood") String neighbourhood
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Sources(
            @JsonProperty("geometry") String geometry,
            @JsonProperty("attributes") String[] attributes
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Preview(
            @JsonProperty("source") String source,
            @JsonProperty("height") int height,
            @JsonProperty("width") int width
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WikipediaExtracts(
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("html") String html
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Point(
            @JsonProperty("lon") double lon,
            @JsonProperty("lat") double lat
    ) {}
}

