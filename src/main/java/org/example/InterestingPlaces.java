package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InterestingPlaces(
        @JsonProperty("features") Feature[] features
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Feature(
            @JsonProperty("geometry") Geometry geometry,
            @JsonProperty("properties") Properties properties,
            @JsonProperty("type") String type,
            @JsonProperty("id") String id
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Geometry(
            @JsonProperty("type") String type,
            @JsonProperty("coordinates") double[] coordinates
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Properties(
            @JsonProperty("xid") String xid,
            @JsonProperty("name") String name,
            @JsonProperty("dist") double dist,
            @JsonProperty("rate") int rate,
            @JsonProperty("osm") String osm,
            @JsonProperty("wikidata") String wikidata,
            @JsonProperty("kinds") String kinds
    ) {}

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("InterestingPlaces:\n");
        for (Feature feature : features) {
            result.append(feature.properties.wikidata).append("  xid: ").append(feature.properties().xid()).append(", name: ").append(feature.properties().name()).append("\n");
        }
        return result.toString();
    }

}