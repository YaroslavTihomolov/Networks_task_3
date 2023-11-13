package org.example;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public record Location(Hit[] hits, String locale) implements JsonDTO {
    private static int index = 1;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Hit(Point point, double[] extent, String name, String country, String countrycode, String state,
                      String postcode, long osm_id, String osm_type, String osm_key, String osm_value) {
        @Override
        public String toString() {
            return new String(String.format(
                    "%d. "
                    + "\"name\": \"%s\", "
                    + "\"country\": \"%s\", "
                    + "\"state\": \"%s\", "
                    + "\"osm_key\": \"%s\", "
                    + "\"osm_value\": \"%s\""
                    , index++, name, country, state, osm_key, osm_value).getBytes(StandardCharsets.UTF_8));
        }
    }

    public record Point(double lat, double lng) {
        @Override
        public String toString() {
            return String.format("{"
                    + "    \"lat\": %.7f,%n"
                    + "    \"lng\": %.7f%n"
                    + "}", lat, lng);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hits.length; i++) {
            sb.append(hits[i]);
            if (i < hits.length - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        index = 1;
        String s = new String(sb.toString().getBytes(StandardCharsets.UTF_8));
        return new String(s);
    }
}

