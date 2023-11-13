package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;


/**
 * Приложение по поиску информации о погоде и интересных местах локации,
 * введённой пользователем.
 */
public class Places {
    public static final int RADIUS = 3000;
    private final ArrayList<CompletableFuture<Void>> completableFutures = new ArrayList<>();
    private final ConcurrentLinkedDeque<String> locationInfo = new ConcurrentLinkedDeque<>();
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String GRAPHHOPPER_URL_BASE =
            "https://graphhopper.com/api/1/geocode?q=%s&locale=ru&key=c82f9f59-5629-487a-9264-77d7da36b8a3";
    private static final String OPEN_TRIP_MAP_URL_BASE =
            "https://api.opentripmap.com/0.1/ru/places/radius?radius=%d&lon=%s&lat=%s&apikey=5ae2e3f221c38a28845f05b68a7044a0ad077454ab902d8b378ab8d6";
    private static final String OPEN_TRIP_MAP_DESCRIPTION_URL_BASE =
            "https://api.opentripmap.com/0.1/ru/places/xid/%s?apikey=5ae2e3f221c38a28845f05b68a7044a0ad077454ab902d8b378ab8d6";
    private static final String WEATHER_URL_BASE =
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=6e12a10e325986b1723af5fba7b83385";

    private String buildGraphhopperUrl(String place) {
        return String.format(GRAPHHOPPER_URL_BASE, place);
    }

    private String buildTripMapUrl(double lat, double lon) {
        return String.format(OPEN_TRIP_MAP_URL_BASE, RADIUS, lon, lat);
    }

    private String buildTripMapDescriptionUrl(String xid) {
        return String.format(OPEN_TRIP_MAP_DESCRIPTION_URL_BASE, xid);
    }

    private String buildWeatherUrl(double lat, double lon) {
        return String.format(WEATHER_URL_BASE, lat, lon);
    }

    /**
     * Запускает выполнение приложения.
     **/
    public void start() {
        System.out.println("Enter place name: ");
        Scanner scanner = new Scanner(System.in);
        String place = scanner.nextLine();

        CompletableFuture<Void> completableFutureWeather = new CompletableFuture<>();
        createResponse(buildGraphhopperUrl(place), completableFutureWeather, Location.class, newLocationInfo -> {
            System.out.println(newLocationInfo);
            getLocationInfo(newLocationInfo);
        });
    }


    private <T> void createResponse(String place, CompletableFuture<Void> future, Class<T> valueType, Consumer<T> lambda) {
        completableFutures.add(future);
        System.out.println("1");
        Request request = new Request.Builder()
                .url(place)
                .get()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (response) {
                    if (response.isSuccessful()) {
                        T info = objectMapper.readValue(response.body().string(), valueType);
                        lambda.accept(info);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    future.completeExceptionally(e);
                } finally {
                    System.out.println("2");
                }
            }
        });

    }

    private void getLocationInfo(Location location) {
        System.out.println("Choose number 1-" + location.hits().length + ": ");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();

        Location.Hit choosenHit = location.hits()[num];
        CompletableFuture<Void> completableFutureWeather = new CompletableFuture<>();
        createResponse(buildWeatherUrl(choosenHit.point().lat(), choosenHit.point().lng()), completableFutureWeather,
                WeatherInfo.class, (WeatherInfo info) -> locationInfo.add(info.toString()));

        CompletableFuture<Void> completableFutureWeather2 = new CompletableFuture<>();
        createResponse(buildTripMapUrl(choosenHit.point().lat(), choosenHit.point().lng()), completableFutureWeather2,
                InterestingPlaces.class, (InterestingPlaces info) -> Arrays.stream(info.features())
                        .filter(feature -> (feature.properties().wikidata() != null && !feature.properties().name().isBlank()))
                        .limit(20)
                        .forEach(feature -> {
                            CompletableFuture<Void> completableFutureWeather3 = new CompletableFuture<>();
                            createResponse(buildTripMapDescriptionUrl(feature.properties().xid()), completableFutureWeather3,
                                    Monument.class, (Monument monument) -> {
                                        if (monument.wikipediaExtracts() != null) {
                                            locationInfo.add(monument.toString());
                                        }
                                    });
                        }));

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                completableFutures.toArray(new CompletableFuture[0])
        );
        allOf.join();
        System.out.println("join " + locationInfo.size());
        for (var info : locationInfo) {
            System.out.println(info);
        }
    }

}
